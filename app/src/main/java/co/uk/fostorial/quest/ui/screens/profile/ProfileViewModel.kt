package co.uk.fostorial.quest.ui.screens.profile

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.uk.fostorial.quest.QuestApplication
import co.uk.fostorial.quest.data.ProfileExportManager
import co.uk.fostorial.quest.data.dto.ClassProgressDto
import co.uk.fostorial.quest.data.dto.FullQuestExportDto
import co.uk.fostorial.quest.data.dto.ProfileDto
import co.uk.fostorial.quest.data.dto.ProfileExport
import co.uk.fostorial.quest.data.dto.QuestListExportDto
import co.uk.fostorial.quest.data.model.BackgroundRegistry
import co.uk.fostorial.quest.data.model.HeroClass
import co.uk.fostorial.quest.data.model.HeroClassProgress
import co.uk.fostorial.quest.data.model.HeroClassRegistry
import co.uk.fostorial.quest.data.model.PlayerProfile
import co.uk.fostorial.quest.data.model.ProfileBackground
import co.uk.fostorial.quest.data.model.Quest
import co.uk.fostorial.quest.data.model.QuestList
import co.uk.fostorial.quest.data.model.RecurrenceType
import co.uk.fostorial.quest.data.model.Title
import co.uk.fostorial.quest.data.model.TitleRegistry
import co.uk.fostorial.quest.data.model.XPTier
import co.uk.fostorial.quest.notification.NotificationUpdateWorker
import co.uk.fostorial.quest.notification.QuestNotificationManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class ProfileUiState(
    val profile: PlayerProfile = PlayerProfile(),
    val isEditingName: Boolean = false,
    val editName: String = "",
    val showTitlesDialog: Boolean = false,
    val showBackgroundsDialog: Boolean = false,
    val showClassDialog: Boolean = false,
    val showResetConfirm: Boolean = false,
    val showImportConfirm: Boolean = false,
    val selectedTitle: Title = TitleRegistry.ALL.first(),
    val selectedBackground: ProfileBackground = BackgroundRegistry.ALL.first(),
    val selectedClass: HeroClass = HeroClassRegistry.getById("adventurer"),
    val unlockedTitles: List<Title> = listOf(TitleRegistry.ALL.first()),
    val xpPerClass: Map<String, Long> = emptyMap()
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as QuestApplication
    private val playerRepo = app.playerRepository
    private val questRepo = app.questRepository

    private val _snackbarChannel = Channel<String>(Channel.BUFFERED)
    val snackbarEvents = _snackbarChannel.receiveAsFlow()

    private val _exportShareChannel = Channel<Uri>(Channel.BUFFERED)
    val exportShareEvents = _exportShareChannel.receiveAsFlow()

    private val _isEditingName = MutableStateFlow(false)
    private val _editName = MutableStateFlow("")
    private val _showTitlesDialog = MutableStateFlow(false)
    private val _showBackgroundsDialog = MutableStateFlow(false)
    private val _showClassDialog = MutableStateFlow(false)
    private val _showResetConfirm = MutableStateFlow(false)
    private val _showImportConfirm = MutableStateFlow(false)
    private var _pendingImportUri: Uri? = null

    private val _classProgress = playerRepo.getAllClassProgress()
        .map { list -> list.associate { it.classId to it.xpEarned } }

    init {
        viewModelScope.launch {
            var prevTitles: Set<String>? = null
            var prevBackgrounds: Set<String>? = null
            combine(playerRepo.getProfile(), _classProgress) { profile, xpPerClass ->
                val p = profile ?: PlayerProfile()
                TitleRegistry.unlockedFor(p.level, xpPerClass).map { it.id }.toSet() to
                    BackgroundRegistry.unlockedFor(p, xpPerClass).map { it.id }.toSet()
            }.collect { (titles, backgrounds) ->
                if (prevTitles != null) {
                    for (id in titles - prevTitles!!) {
                        _snackbarChannel.send("Title unlocked: ${TitleRegistry.getById(id).displayName}")
                    }
                    for (id in backgrounds - prevBackgrounds!!) {
                        _snackbarChannel.send("Background unlocked: ${BackgroundRegistry.getById(id).displayName}")
                    }
                }
                prevTitles = titles
                prevBackgrounds = backgrounds
            }
        }
    }

    val uiState: StateFlow<ProfileUiState> = combine(
        combine(playerRepo.getProfile(), _isEditingName, _editName) { p, e, n -> Triple(p, e, n) },
        combine(_showTitlesDialog, _showBackgroundsDialog) { t, b -> t to b },
        combine(_showClassDialog, _showResetConfirm, _showImportConfirm) { c, r, i -> Triple(c, r, i) },
        _classProgress
    ) { (profile, editing, editName), (showTitles, showBgs), (showClass, showReset, showImport), xpPerClass ->
        val safeProfile = profile ?: PlayerProfile()
        ProfileUiState(
            profile = safeProfile,
            isEditingName = editing,
            editName = editName,
            showTitlesDialog = showTitles,
            showBackgroundsDialog = showBgs,
            showClassDialog = showClass,
            showResetConfirm = showReset,
            showImportConfirm = showImport,
            selectedTitle = TitleRegistry.getById(safeProfile.selectedTitleId),
            selectedBackground = BackgroundRegistry.getById(safeProfile.selectedBackgroundId),
            selectedClass = HeroClassRegistry.getById(safeProfile.selectedClassId),
            unlockedTitles = TitleRegistry.unlockedFor(safeProfile.level, xpPerClass),
            xpPerClass = xpPerClass
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileUiState())

    fun startEditName() {
        _editName.value = uiState.value.profile.heroName
        _isEditingName.value = true
    }

    fun setEditName(name: String) { _editName.value = name }

    fun saveHeroName() {
        viewModelScope.launch {
            val profile = playerRepo.getOrCreateProfile()
            playerRepo.updateProfile(profile.copy(heroName = _editName.value.trim().ifBlank { "Hero" }))
            _isEditingName.value = false
        }
    }

    fun cancelEditName() { _isEditingName.value = false }

    // ── Titles ────────────────────────────────────────────────────────────────

    fun openTitlesDialog() { _showTitlesDialog.value = true }
    fun closeTitlesDialog() { _showTitlesDialog.value = false }

    fun selectTitle(titleId: String) {
        viewModelScope.launch {
            val profile = playerRepo.getOrCreateProfile()
            val xpPerClass = playerRepo.getAllClassProgress()
                .map { list -> list.associate { it.classId to it.xpEarned } }
                .first()
            if (TitleRegistry.isUnlocked(titleId, profile.level, xpPerClass)) {
                playerRepo.updateProfile(profile.copy(selectedTitleId = titleId))
            }
            _showTitlesDialog.value = false
        }
    }

    // ── Class ─────────────────────────────────────────────────────────────────

    fun openClassDialog() { _showClassDialog.value = true }
    fun closeClassDialog() { _showClassDialog.value = false }

    fun selectClass(classId: String) {
        viewModelScope.launch {
            val profile = playerRepo.getOrCreateProfile()
            val xpPerClass = playerRepo.getAllClassProgress()
                .map { list -> list.associate { it.classId to it.xpEarned } }
                .first()
            if (HeroClassRegistry.isUnlocked(classId, xpPerClass)) {
                playerRepo.updateProfile(profile.copy(selectedClassId = classId))
            }
            _showClassDialog.value = false
        }
    }

    // ── Backgrounds ───────────────────────────────────────────────────────────

    fun openBackgroundsDialog() { _showBackgroundsDialog.value = true }
    fun closeBackgroundsDialog() { _showBackgroundsDialog.value = false }

    fun selectBackground(backgroundId: String) {
        viewModelScope.launch {
            val profile = playerRepo.getOrCreateProfile()
            val xpPerClass = playerRepo.getAllClassProgress()
                .map { list -> list.associate { it.classId to it.xpEarned } }
                .first()
            if (BackgroundRegistry.isUnlocked(backgroundId, profile, xpPerClass)) {
                playerRepo.updateProfile(profile.copy(selectedBackgroundId = backgroundId))
            }
            _showBackgroundsDialog.value = false
        }
    }

    // ── Reset ─────────────────────────────────────────────────────────────────

    fun promptReset() { _showResetConfirm.value = true }
    fun cancelReset() { _showResetConfirm.value = false }

    fun confirmReset() {
        viewModelScope.launch {
            playerRepo.resetProfile()
            _showResetConfirm.value = false
        }
    }

    // ── Export / Import ───────────────────────────────────────────────────────

    fun exportProfile(context: Context) {
        viewModelScope.launch {
            val profile = playerRepo.getOrCreateProfile()
            val classProgress = playerRepo.getAllClassProgressOnce()
            val lists = questRepo.getAllListsOnce()
            val questLists = lists.map { list ->
                val quests = questRepo.getQuestsForListOnce(list.id)
                QuestListExportDto(
                    shareId = list.shareId ?: java.util.UUID.randomUUID().toString(),
                    name = list.name,
                    emoji = list.emoji,
                    colorHex = list.colorHex,
                    boardBackgroundId = list.boardBackgroundId,
                    sortOrder = list.sortOrder,
                    quests = quests.map { q ->
                        FullQuestExportDto(
                            title = q.title,
                            description = q.description,
                            xpTier = q.xpTier.name,
                            isCompleted = q.isCompleted,
                            isPinned = q.isPinned,
                            recurrenceType = q.recurrenceType.name,
                            dueDateMillis = q.dueDateMillis,
                            completedAtMillis = q.completedAtMillis,
                            createdAtMillis = q.createdAtMillis,
                            sortOrder = q.sortOrder
                        )
                    }
                )
            }
            val export = ProfileExport(
                profile = ProfileDto(
                    heroName = profile.heroName,
                    level = profile.level,
                    totalXP = profile.totalXP,
                    totalQuestsCompleted = profile.totalQuestsCompleted,
                    paltryQuestsCompleted = profile.paltryQuestsCompleted,
                    smallQuestsCompleted = profile.smallQuestsCompleted,
                    mediumQuestsCompleted = profile.mediumQuestsCompleted,
                    largeQuestsCompleted = profile.largeQuestsCompleted,
                    epicQuestsCompleted = profile.epicQuestsCompleted,
                    selectedTitleId = profile.selectedTitleId,
                    selectedBackgroundId = profile.selectedBackgroundId,
                    selectedClassId = profile.selectedClassId,
                    notificationEnabled = profile.notificationEnabled,
                    notificationPersistent = profile.notificationPersistent
                ),
                classProgress = classProgress.map { ClassProgressDto(it.classId, it.xpEarned) },
                questLists = questLists
            )
            val uri = ProfileExportManager.createExportUri(context, export)
            _exportShareChannel.send(uri)
        }
    }

    fun onImportFileSelected(uri: Uri) {
        _pendingImportUri = uri
        _showImportConfirm.value = true
    }

    fun cancelImport() {
        _pendingImportUri = null
        _showImportConfirm.value = false
    }

    fun confirmImport(context: Context) {
        val uri = _pendingImportUri ?: run {
            _showImportConfirm.value = false
            return
        }
        _showImportConfirm.value = false
        _pendingImportUri = null

        viewModelScope.launch {
            val export = ProfileExportManager.readExportFromUri(context, uri)
            if (export == null) {
                _snackbarChannel.send("Failed to read export file")
                return@launch
            }

            val restoredProfile = PlayerProfile(
                heroName = export.profile.heroName,
                level = export.profile.level,
                totalXP = export.profile.totalXP,
                totalQuestsCompleted = export.profile.totalQuestsCompleted,
                paltryQuestsCompleted = export.profile.paltryQuestsCompleted,
                smallQuestsCompleted = export.profile.smallQuestsCompleted,
                mediumQuestsCompleted = export.profile.mediumQuestsCompleted,
                largeQuestsCompleted = export.profile.largeQuestsCompleted,
                epicQuestsCompleted = export.profile.epicQuestsCompleted,
                selectedTitleId = export.profile.selectedTitleId,
                selectedBackgroundId = export.profile.selectedBackgroundId,
                selectedClassId = export.profile.selectedClassId,
                notificationEnabled = export.profile.notificationEnabled,
                notificationPersistent = export.profile.notificationPersistent
            )
            val restoredProgress = export.classProgress.map {
                HeroClassProgress(classId = it.classId, xpEarned = it.xpEarned)
            }
            playerRepo.restoreProfile(restoredProfile, restoredProgress)

            for (listDto in export.questLists) {
                val existing = questRepo.getListByShareId(listDto.shareId)
                val listId: Long = if (existing != null) {
                    questRepo.updateList(
                        existing.copy(
                            name = listDto.name,
                            emoji = listDto.emoji,
                            colorHex = listDto.colorHex,
                            boardBackgroundId = listDto.boardBackgroundId,
                            sortOrder = listDto.sortOrder
                        )
                    )
                    existing.id
                } else {
                    questRepo.insertList(
                        QuestList(
                            name = listDto.name,
                            emoji = listDto.emoji,
                            colorHex = listDto.colorHex,
                            shareId = listDto.shareId,
                            boardBackgroundId = listDto.boardBackgroundId,
                            sortOrder = listDto.sortOrder
                        )
                    )
                }
                questRepo.deleteAllQuestsInList(listId)
                listDto.quests.forEach { q ->
                    questRepo.insertQuest(
                        Quest(
                            listId = listId,
                            title = q.title,
                            description = q.description,
                            xpTier = runCatching { XPTier.valueOf(q.xpTier) }.getOrDefault(XPTier.SMALL),
                            isCompleted = q.isCompleted,
                            isPinned = q.isPinned,
                            recurrenceType = runCatching { RecurrenceType.valueOf(q.recurrenceType) }.getOrDefault(RecurrenceType.NONE),
                            dueDateMillis = q.dueDateMillis,
                            completedAtMillis = q.completedAtMillis,
                            createdAtMillis = q.createdAtMillis,
                            sortOrder = q.sortOrder
                        )
                    )
                }
            }

            _snackbarChannel.send("Profile imported successfully!")
        }
    }

    // ── Notifications ─────────────────────────────────────────────────────────

    fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            val profile = playerRepo.getOrCreateProfile()
            playerRepo.updateProfile(profile.copy(notificationEnabled = enabled))
            if (enabled) {
                NotificationUpdateWorker.scheduleImmediate(getApplication())
            } else {
                QuestNotificationManager.cancelQuestBoard(getApplication())
            }
        }
    }

    fun togglePersistentNotification(persistent: Boolean) {
        viewModelScope.launch {
            val profile = playerRepo.getOrCreateProfile()
            playerRepo.updateProfile(profile.copy(notificationPersistent = persistent))
            if (profile.notificationEnabled) {
                NotificationUpdateWorker.scheduleImmediate(getApplication())
            }
        }
    }
}
