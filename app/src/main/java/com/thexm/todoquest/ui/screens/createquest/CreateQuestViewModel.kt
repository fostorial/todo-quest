package com.thexm.todoquest.ui.screens.createquest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.RecurrenceType
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.notification.NotificationUpdateWorker
import com.thexm.todoquest.notification.QuestReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

data class CreateQuestState(
    val title: String = "",
    val description: String = "",
    val xpTier: XPTier = XPTier.SMALL,
    val recurrenceType: RecurrenceType = RecurrenceType.NONE,
    val isPinned: Boolean = false,
    val hasDueDate: Boolean = false,
    val dueDateMillis: Long? = null,
    val dueHour: Int = 9,
    val dueMinute: Int = 0,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val titleError: String? = null
)

class CreateQuestViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val listId: Long = checkNotNull(savedStateHandle["listId"])
    private val questId: Long = savedStateHandle["questId"] ?: -1L
    private val app = application as QuestApplication
    private val questRepo = app.questRepository

    private val _state = MutableStateFlow(CreateQuestState(isEditing = questId != -1L))
    val state: StateFlow<CreateQuestState> = _state.asStateFlow()

    init {
        if (questId != -1L) {
            viewModelScope.launch {
                val quest = questRepo.getQuestById(questId)
                if (quest != null) {
                    val cal = Calendar.getInstance()
                    val hour: Int
                    val minute: Int
                    if (quest.dueDateMillis != null) {
                        cal.timeInMillis = quest.dueDateMillis
                        hour = cal.get(Calendar.HOUR_OF_DAY)
                        minute = cal.get(Calendar.MINUTE)
                    } else {
                        hour = 9; minute = 0
                    }
                    _state.update {
                        it.copy(
                            title = quest.title,
                            description = quest.description,
                            xpTier = quest.xpTier,
                            recurrenceType = quest.recurrenceType,
                            isPinned = quest.isPinned,
                            hasDueDate = quest.dueDateMillis != null,
                            dueDateMillis = quest.dueDateMillis,
                            dueHour = hour,
                            dueMinute = minute
                        )
                    }
                }
            }
        }
    }

    fun setTitle(title: String) = _state.update { it.copy(title = title, titleError = null) }
    fun setDescription(desc: String) = _state.update { it.copy(description = desc) }
    fun setXpTier(tier: XPTier) = _state.update { it.copy(xpTier = tier) }
    fun setRecurrence(type: RecurrenceType) = _state.update { it.copy(recurrenceType = type) }
    fun togglePin() = _state.update { it.copy(isPinned = !it.isPinned) }

    fun toggleDueDate() = _state.update {
        if (it.hasDueDate) it.copy(hasDueDate = false, dueDateMillis = null)
        else it.copy(hasDueDate = true)
    }

    /** Called when the user confirms the date picker. Preserves the stored hour/minute. */
    fun setDueDate(dateOnlyMillis: Long?) {
        if (dateOnlyMillis == null) {
            _state.update { it.copy(dueDateMillis = null, hasDueDate = false) }
            return
        }
        val s = _state.value
        _state.update { it.copy(dueDateMillis = combineDateAndTime(dateOnlyMillis, s.dueHour, s.dueMinute)) }
    }

    /** Called when the user confirms the time picker. Re-applies to the stored date. */
    fun setDueTime(hour: Int, minute: Int) {
        val s = _state.value
        // Use today as base date if no date selected yet
        val baseDate = s.dueDateMillis ?: run {
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }
            cal.timeInMillis
        }
        _state.update {
            it.copy(
                dueHour = hour,
                dueMinute = minute,
                dueDateMillis = combineDateAndTime(baseDate, hour, minute)
            )
        }
    }

    private fun combineDateAndTime(dateMillis: Long, hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = dateMillis
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun save(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.title.isBlank()) {
            _state.update { it.copy(titleError = "Quest name cannot be empty") }
            return
        }
        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val savedId: Long
            if (questId == -1L) {
                savedId = questRepo.insertQuest(
                    Quest(
                        listId = listId,
                        title = s.title.trim(),
                        description = s.description.trim(),
                        xpTier = s.xpTier,
                        recurrenceType = s.recurrenceType,
                        isPinned = s.isPinned,
                        dueDateMillis = s.dueDateMillis
                    )
                )
            } else {
                val existing = questRepo.getQuestById(questId) ?: return@launch
                questRepo.updateQuest(
                    existing.copy(
                        title = s.title.trim(),
                        description = s.description.trim(),
                        xpTier = s.xpTier,
                        recurrenceType = s.recurrenceType,
                        isPinned = s.isPinned,
                        dueDateMillis = s.dueDateMillis
                    )
                )
                savedId = questId
            }
            // Schedule (or cancel) the 4-hour reminder for this quest
            val savedQuest = questRepo.getQuestById(savedId)
            if (savedQuest != null) {
                QuestReminderScheduler.schedule(getApplication(), savedQuest)
            }
            NotificationUpdateWorker.scheduleImmediate(getApplication())
            onSuccess()
        }
    }
}
