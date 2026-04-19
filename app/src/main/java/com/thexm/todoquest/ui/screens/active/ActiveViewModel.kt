package com.thexm.todoquest.ui.screens.active

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.QuestList
import com.thexm.todoquest.data.model.RecurrenceType
import com.thexm.todoquest.notification.NotificationUpdateWorker
import com.thexm.todoquest.notification.QuestNotificationManager
import com.thexm.todoquest.notification.QuestReminderScheduler
import com.thexm.todoquest.ui.screens.questlist.CompletionEvent
import com.thexm.todoquest.util.RecurrenceCalculator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class QuestWithList(
    val quest: Quest,
    val listName: String,
    val listEmoji: String,
    val listColorHex: String
)

data class ActiveUiState(
    val overdueQuests: List<QuestWithList> = emptyList(),
    val upcomingQuests: List<QuestWithList> = emptyList(),
    val noDueDateQuests: List<QuestWithList> = emptyList(),
    val isLoading: Boolean = true,
    val levelUpEvent: Int? = null,
    val completionEvent: CompletionEvent? = null,
    val pendingDeleteQuest: Quest? = null
)

class ActiveViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as QuestApplication
    private val questRepo = app.questRepository
    private val playerRepo = app.playerRepository

    // Ticks every 60 s so overdue/upcoming boundary stays fresh
    private val ticker: StateFlow<Long> = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(60_000L)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), System.currentTimeMillis())

    private val _levelUpEvent = MutableStateFlow<Int?>(null)
    private val _completionEvent = MutableStateFlow<CompletionEvent?>(null)
    private val _pendingDeleteQuest = MutableStateFlow<Quest?>(null)

    val uiState: StateFlow<ActiveUiState> = ticker
        .flatMapLatest { now ->
            combine(
                questRepo.getOverdueQuests(now),
                questRepo.getUpcomingQuests(now),
                combine(questRepo.getNoDueDateQuests(), questRepo.getAllLists()) { noDue, lists -> noDue to lists }
            ) { overdue, upcoming, (noDue, lists) ->
                val listMap = lists.associateBy { it.id }
                ActiveUiState(
                    overdueQuests = overdue.map { it.toWithList(listMap) },
                    upcomingQuests = upcoming.map { it.toWithList(listMap) },
                    noDueDateQuests = noDue.map { it.toWithList(listMap) },
                    isLoading = false
                )
            }
        }
        .combine(_levelUpEvent) { state, levelUp ->
            state.copy(levelUpEvent = levelUp)
        }
        .combine(_completionEvent) { state, completion ->
            state.copy(completionEvent = completion)
        }
        .combine(_pendingDeleteQuest) { state, pending ->
            state.copy(pendingDeleteQuest = pending)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ActiveUiState())

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

            // Refresh the notification immediately inline rather than via WorkManager,
            // so the persistent notification updates the moment the quest is completed.
            val profile = playerRepo.getOrCreateProfile()
            if (profile.notificationEnabled) {
                val count = questRepo.getActiveQuestCount().first()
                val pinned = questRepo.getPinnedAndUpcomingQuests().first()
                QuestNotificationManager.showQuestBoardNotification(
                    getApplication(), count, pinned, profile.notificationPersistent
                )
            }
        }
    }

    fun togglePin(quest: Quest) {
        viewModelScope.launch {
            questRepo.updateQuest(quest.copy(isPinned = !quest.isPinned))
            NotificationUpdateWorker.scheduleImmediate(getApplication())
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
            NotificationUpdateWorker.scheduleImmediate(getApplication())
        }
    }

    fun dismissLevelUp() { _levelUpEvent.value = null }
    fun dismissCompletion() { _completionEvent.value = null }

    private fun Quest.toWithList(listMap: Map<Long, QuestList>): QuestWithList {
        val list = listMap[listId]
        return QuestWithList(
            quest = this,
            listName = list?.name ?: "Unknown Board",
            listEmoji = list?.emoji ?: "📜",
            listColorHex = list?.colorHex ?: "#8B5CF6"
        )
    }
}
