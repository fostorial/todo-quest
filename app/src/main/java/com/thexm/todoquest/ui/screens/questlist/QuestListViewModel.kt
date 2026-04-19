package com.thexm.todoquest.ui.screens.questlist

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.ShareManager
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.QuestList
import com.thexm.todoquest.data.model.RecurrenceType
import com.thexm.todoquest.notification.NotificationUpdateWorker
import com.thexm.todoquest.notification.QuestNotificationManager
import com.thexm.todoquest.notification.QuestReminderScheduler
import com.thexm.todoquest.util.RecurrenceCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

data class CompletionEvent(
    val questTitle: String,
    val xpEarned: Int,
    val isRecurring: Boolean,
    val nextDueDateMillis: Long?
)

data class QuestListUiState(
    val questList: QuestList? = null,
    val activeQuests: List<Quest> = emptyList(),
    val completedQuests: List<Quest> = emptyList(),
    val showCompleted: Boolean = false,
    val isLoading: Boolean = true,
    val levelUpEvent: Int? = null,
    val completionEvent: CompletionEvent? = null,
    val pendingDeleteQuest: Quest? = null
)

class QuestListViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val listId: Long = checkNotNull(savedStateHandle["listId"])
    private val app = application as QuestApplication
    private val questRepo = app.questRepository
    private val playerRepo = app.playerRepository

    private val _showCompleted = MutableStateFlow(false)
    private val _levelUpEvent = MutableStateFlow<Int?>(null)
    private val _completionEvent = MutableStateFlow<CompletionEvent?>(null)
    private val _pendingDeleteQuest = MutableStateFlow<Quest?>(null)

    val uiState: StateFlow<QuestListUiState> = combine(
        questRepo.getQuestsForList(listId),
        _showCompleted,
        combine(_levelUpEvent, _completionEvent) { l, c -> l to c }
    ) { quests, showCompleted, (levelUp, completion) ->
        val listMeta = questRepo.getListById(listId)
        QuestListUiState(
            questList = listMeta,
            activeQuests = quests.filter { !it.isCompleted },
            completedQuests = quests.filter { it.isCompleted },
            showCompleted = showCompleted,
            isLoading = false,
            levelUpEvent = levelUp,
            completionEvent = completion
        )
    }.combine(_pendingDeleteQuest) { state, pending ->
        state.copy(pendingDeleteQuest = pending)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuestListUiState())

    fun toggleShowCompleted() { _showCompleted.update { !it } }

    fun completeQuest(quest: Quest) {
        viewModelScope.launch {
            val profileBefore = playerRepo.getOrCreateProfile()
            questRepo.completeQuest(quest.id)
            QuestReminderScheduler.cancel(getApplication(), quest.id)
            QuestNotificationManager.cancelQuestReminder(getApplication(), quest.id)
            playerRepo.awardXP(quest.xpTier.xpValue)
            playerRepo.recordQuestCompletion(quest.xpTier)

            val profileAfter = playerRepo.getOrCreateProfile()
            if (profileAfter.level > profileBefore.level) {
                _levelUpEvent.value = profileAfter.level
            }

            var nextDueMillis: Long? = null

            if (quest.recurrenceType != RecurrenceType.NONE) {
                // Calculate proper next occurrence date
                // If the quest is overdue, base the next occurrence off today rather than the old due date
                val now = System.currentTimeMillis()
                val baseMillis = if (quest.dueDateMillis != null && quest.dueDateMillis >= now) quest.dueDateMillis else now
                nextDueMillis = RecurrenceCalculator.nextDueDate(baseMillis, quest.recurrenceType)

                val nextQuest = quest.copy(
                    id = 0,
                    isCompleted = false,
                    completedAtMillis = null,
                    createdAtMillis = System.currentTimeMillis(),
                    dueDateMillis = nextDueMillis
                )
                val newId = questRepo.insertQuest(nextQuest)
                // Schedule the reminder for the new occurrence
                if (nextDueMillis != null) {
                    QuestReminderScheduler.schedule(getApplication(), nextQuest.copy(id = newId))
                }
            }

            _completionEvent.value = CompletionEvent(
                questTitle = quest.title,
                xpEarned = quest.xpTier.xpValue,
                isRecurring = quest.recurrenceType != RecurrenceType.NONE,
                nextDueDateMillis = nextDueMillis
            )

            refreshNotification()
        }
    }

    fun togglePin(quest: Quest) {
        viewModelScope.launch {
            questRepo.updateQuest(quest.copy(isPinned = !quest.isPinned))
            refreshNotification()
        }
    }

    fun promptDeleteQuest(quest: Quest) { _pendingDeleteQuest.value = quest }

    fun cancelDeleteQuest() { _pendingDeleteQuest.value = null }

    fun confirmDeleteQuest() {
        val quest = _pendingDeleteQuest.value ?: return
        _pendingDeleteQuest.value = null
        viewModelScope.launch {
            questRepo.deleteQuest(quest)
            QuestReminderScheduler.cancel(getApplication(), quest.id)
            QuestNotificationManager.cancelQuestReminder(getApplication(), quest.id)
            refreshNotification()
        }
    }

    fun clearCompleted() {
        viewModelScope.launch { questRepo.clearCompletedInList(listId) }
    }

    fun dismissLevelUp() { _levelUpEvent.value = null }
    fun dismissCompletion() { _completionEvent.value = null }

    // ── Share ────────────────────────────────────────────────────────────────

    private val _shareEvent = Channel<Pair<Uri, String>>(Channel.BUFFERED)
    val shareEvent: Flow<Pair<Uri, String>> = _shareEvent.receiveAsFlow()

    fun shareBoard(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = questRepo.getListById(listId) ?: return@launch
            val listToShare = if (list.shareId == null) {
                val updated = list.copy(shareId = UUID.randomUUID().toString())
                questRepo.updateList(updated)
                updated
            } else list

            val quests = questRepo.getQuestsForListOnce(listToShare.id).filter { !it.isCompleted }
            val uri = ShareManager.createShareUri(context, listToShare, quests)
            _shareEvent.send(uri to listToShare.name)
        }
    }

    private fun refreshNotification() {
        NotificationUpdateWorker.scheduleImmediate(getApplication())
    }
}
