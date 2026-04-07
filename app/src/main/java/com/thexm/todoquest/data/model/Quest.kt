package com.thexm.todoquest.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quests",
    foreignKeys = [ForeignKey(
        entity = QuestList::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("listId")]
)
data class Quest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val listId: Long,
    val title: String,
    val description: String = "",
    val xpTier: XPTier = XPTier.SMALL,
    val isCompleted: Boolean = false,
    val isPinned: Boolean = false,
    val recurrenceType: RecurrenceType = RecurrenceType.NONE,
    val dueDateMillis: Long? = null,
    val completedAtMillis: Long? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val sortOrder: Int = 0
)
