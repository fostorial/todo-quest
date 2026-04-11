package com.thexm.todoquest.ui.screens.home

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.ShareManager
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.QuestList
import com.thexm.todoquest.data.model.RecurrenceType
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.notification.NotificationUpdateWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ImportResult {
    data class Success(val boardName: String, val listId: Long, val wasUpdate: Boolean) : ImportResult()
    data class Error(val message: String) : ImportResult()
}

data class HomeUiState(
    val lists: List<QuestList> = emptyList(),
    val activeCountPerList: Map<Long, Int> = emptyMap(),
    val totalActiveQuests: Int = 0,
    val isLoading: Boolean = true
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as QuestApplication
    private val questRepo = app.questRepository

    val uiState: StateFlow<HomeUiState> = combine(
        questRepo.getAllLists(),
        questRepo.getActiveQuestCount()
    ) { lists, totalActive ->
        HomeUiState(
            lists = lists,
            totalActiveQuests = totalActive,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    // Per-list active counts (collected separately for simplicity)
    private val _activeCountPerList = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val activeCountPerList: StateFlow<Map<Long, Int>> = _activeCountPerList

    init {
        viewModelScope.launch {
            questRepo.getAllLists().collect { lists ->
                lists.forEach { list ->
                    launch {
                        questRepo.getQuestsForList(list.id).collect { quests ->
                            val count = quests.count { !it.isCompleted }
                            _activeCountPerList.update { current ->
                                current.toMutableMap().apply { put(list.id, count) }
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteList(questList: QuestList) {
        viewModelScope.launch {
            questRepo.deleteList(questList)
            refreshNotification()
        }
    }

    fun refreshNotification() {
        NotificationUpdateWorker.scheduleImmediate(getApplication())
    }

    // ── Import ───────────────────────────────────────────────────────────────

    private val _importResult = MutableStateFlow<ImportResult?>(null)
    val importResult: StateFlow<ImportResult?> = _importResult

    fun importBoard(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val export = ShareManager.readBoardFromUri(context, uri)
            if (export == null) {
                _importResult.value = ImportResult.Error("Couldn't read the quest board scroll. The file may be corrupted.")
                return@launch
            }

            val boardDto = export.board
            val existingList = questRepo.getListByShareId(boardDto.shareId)

            if (existingList != null) {
                // Update board metadata
                questRepo.updateList(existingList.copy(name = boardDto.name, emoji = boardDto.emoji, colorHex = boardDto.colorHex))

                // Merge quests — preserve completion/pinned state on existing quests
                val existingByTitle = questRepo.getQuestsForListOnce(existingList.id).associateBy { it.title }
                boardDto.quests.forEach { dto ->
                    val local = existingByTitle[dto.title]
                    if (local != null) {
                        questRepo.updateQuest(local.copy(
                            description = dto.description,
                            xpTier = XPTier.valueOf(dto.xpTier),
                            recurrenceType = RecurrenceType.valueOf(dto.recurrenceType),
                            dueDateMillis = dto.dueDateMillis,
                            sortOrder = dto.sortOrder
                            // isCompleted, completedAtMillis, isPinned preserved
                        ))
                    } else {
                        questRepo.insertQuest(Quest(
                            listId = existingList.id,
                            title = dto.title,
                            description = dto.description,
                            xpTier = XPTier.valueOf(dto.xpTier),
                            recurrenceType = RecurrenceType.valueOf(dto.recurrenceType),
                            dueDateMillis = dto.dueDateMillis,
                            sortOrder = dto.sortOrder
                        ))
                    }
                }
                _importResult.value = ImportResult.Success(boardDto.name, existingList.id, wasUpdate = true)
            } else {
                val newListId = questRepo.insertList(QuestList(
                    name = boardDto.name, emoji = boardDto.emoji,
                    colorHex = boardDto.colorHex, shareId = boardDto.shareId
                ))
                boardDto.quests.forEach { dto ->
                    questRepo.insertQuest(Quest(
                        listId = newListId,
                        title = dto.title,
                        description = dto.description,
                        xpTier = XPTier.valueOf(dto.xpTier),
                        recurrenceType = RecurrenceType.valueOf(dto.recurrenceType),
                        dueDateMillis = dto.dueDateMillis,
                        sortOrder = dto.sortOrder
                    ))
                }
                _importResult.value = ImportResult.Success(boardDto.name, newListId, wasUpdate = false)
            }

            refreshNotification()
        }
    }

    fun clearImportResult() { _importResult.value = null }
}
