package com.thexm.todoquest.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.model.BackgroundRegistry
import com.thexm.todoquest.data.model.HeroClass
import com.thexm.todoquest.data.model.HeroClassRegistry
import com.thexm.todoquest.data.model.PlayerProfile
import com.thexm.todoquest.data.model.ProfileBackground
import com.thexm.todoquest.data.model.Title
import com.thexm.todoquest.data.model.TitleRegistry
import com.thexm.todoquest.notification.NotificationUpdateWorker
import com.thexm.todoquest.notification.QuestNotificationManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: PlayerProfile = PlayerProfile(),
    val isEditingName: Boolean = false,
    val editName: String = "",
    val showTitlesDialog: Boolean = false,
    val showBackgroundsDialog: Boolean = false,
    val showClassDialog: Boolean = false,
    val showResetConfirm: Boolean = false,
    val selectedTitle: Title = TitleRegistry.ALL.first(),
    val selectedBackground: ProfileBackground = BackgroundRegistry.ALL.first(),
    val selectedClass: HeroClass = HeroClassRegistry.getById("adventurer"),
    val unlockedTitles: List<Title> = listOf(TitleRegistry.ALL.first()),
    val xpPerClass: Map<String, Long> = emptyMap()
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as QuestApplication
    private val playerRepo = app.playerRepository

    private val _isEditingName = MutableStateFlow(false)
    private val _editName = MutableStateFlow("")
    private val _showTitlesDialog = MutableStateFlow(false)
    private val _showBackgroundsDialog = MutableStateFlow(false)
    private val _showClassDialog = MutableStateFlow(false)
    private val _showResetConfirm = MutableStateFlow(false)

    private val _classProgress = playerRepo.getAllClassProgress()
        .map { list -> list.associate { it.classId to it.xpEarned } }

    val uiState: StateFlow<ProfileUiState> = combine(
        combine(playerRepo.getProfile(), _isEditingName, _editName) { p, e, n -> Triple(p, e, n) },
        combine(_showTitlesDialog, _showBackgroundsDialog) { t, b -> t to b },
        combine(_showClassDialog, _showResetConfirm) { c, r -> c to r },
        _classProgress
    ) { (profile, editing, editName), (showTitles, showBgs), (showClass, showReset), xpPerClass ->
        val safeProfile = profile ?: PlayerProfile()
        ProfileUiState(
            profile = safeProfile,
            isEditingName = editing,
            editName = editName,
            showTitlesDialog = showTitles,
            showBackgroundsDialog = showBgs,
            showClassDialog = showClass,
            showResetConfirm = showReset,
            selectedTitle = TitleRegistry.getById(safeProfile.selectedTitleId),
            selectedBackground = BackgroundRegistry.getById(safeProfile.selectedBackgroundId),
            selectedClass = HeroClassRegistry.getById(safeProfile.selectedClassId),
            unlockedTitles = TitleRegistry.unlockedFor(safeProfile.level),
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
            if (TitleRegistry.isUnlocked(titleId, profile.level)) {
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
            if (BackgroundRegistry.isUnlocked(backgroundId, profile)) {
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
