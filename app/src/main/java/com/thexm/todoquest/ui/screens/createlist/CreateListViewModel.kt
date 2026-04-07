package com.thexm.todoquest.ui.screens.createlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.model.QuestList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateListState(
    val name: String = "",
    val emoji: String = "📜",
    val colorHex: String = "#8B5CF6",
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: String? = null
)

val PRESET_COLORS = listOf(
    // Row 1 — vivid
    "#8B5CF6", "#3B82F6", "#10B981", "#F59E0B",
    "#EF4444", "#EC4899", "#06B6D4", "#84CC16",
    // Row 2 — deep/pastel
    "#6D28D9", "#1D4ED8", "#065F46", "#B45309",
    "#991B1B", "#9D174D", "#164E63", "#3F6212"
)

val PRESET_EMOJIS = listOf(
    // Row 1 — fantasy combat
    "📜", "⚔️", "🏰", "🗡️", "🧙", "🐉", "🏆", "💎",
    // Row 2 — magic & exploration
    "🌟", "🔮", "🛡️", "🧪", "🗺️", "🌙", "🦄", "🔱",
    // Row 3 — nature & daily life
    "🌿", "🔥", "💀", "🎯", "🍀", "⚡", "🎪", "🏹"
)

class CreateListViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val listId: Long = savedStateHandle["listId"] ?: -1L
    private val app = application as QuestApplication
    private val questRepo = app.questRepository

    private val _state = MutableStateFlow(CreateListState(isEditing = listId != -1L))
    val state: StateFlow<CreateListState> = _state.asStateFlow()

    init {
        if (listId != -1L) {
            viewModelScope.launch {
                val list = questRepo.getListById(listId)
                if (list != null) {
                    _state.update {
                        it.copy(name = list.name, emoji = list.emoji, colorHex = list.colorHex)
                    }
                }
            }
        }
    }

    fun setName(name: String) = _state.update { it.copy(name = name, nameError = null) }
    fun setEmoji(emoji: String) = _state.update { it.copy(emoji = emoji) }
    fun setColor(colorHex: String) = _state.update { it.copy(colorHex = colorHex) }

    fun save(onSuccess: () -> Unit) {
        val s = _state.value
        if (s.name.isBlank()) {
            _state.update { it.copy(nameError = "Board name cannot be empty") }
            return
        }
        _state.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            if (listId == -1L) {
                questRepo.insertList(
                    QuestList(name = s.name.trim(), emoji = s.emoji, colorHex = s.colorHex)
                )
            } else {
                val existing = questRepo.getListById(listId) ?: return@launch
                questRepo.updateList(
                    existing.copy(name = s.name.trim(), emoji = s.emoji, colorHex = s.colorHex)
                )
            }
            onSuccess()
        }
    }
}
