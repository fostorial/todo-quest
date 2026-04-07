package com.thexm.todoquest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profile")
data class PlayerProfile(
    @PrimaryKey val id: Int = 1, // singleton row
    val heroName: String = "Hero",
    val level: Int = 1,
    val totalXP: Long = 0,
    val totalQuestsCompleted: Int = 0,
    // Per-tier completion counts (for background unlock requirements)
    val paltryQuestsCompleted: Int = 0,
    val smallQuestsCompleted: Int = 0,
    val mediumQuestsCompleted: Int = 0,
    val largeQuestsCompleted: Int = 0,
    val epicQuestsCompleted: Int = 0,
    val notificationEnabled: Boolean = true,
    val notificationPersistent: Boolean = false,
    val selectedTitleId: String = "novice_adventurer",
    val selectedBackgroundId: String = "stone_grey",
    val selectedClassId: String = "adventurer"
)
