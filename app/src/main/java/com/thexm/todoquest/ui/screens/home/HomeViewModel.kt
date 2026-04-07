package com.thexm.todoquest.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.model.QuestList
import com.thexm.todoquest.notification.NotificationUpdateWorker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
}
