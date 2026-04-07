package com.thexm.todoquest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quest_lists")
data class QuestList(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val emoji: String = "📜",
    val colorHex: String = "#8B5CF6",
    val createdAtMillis: Long = System.currentTimeMillis(),
    val sortOrder: Int = 0
)
