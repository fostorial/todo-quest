package com.thexm.todoquest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/** One row per class — records how much XP the player has earned while that class was active. */
@Entity(tableName = "hero_class_progress")
data class HeroClassProgress(
    @PrimaryKey val classId: String,
    val xpEarned: Long = 0
)
