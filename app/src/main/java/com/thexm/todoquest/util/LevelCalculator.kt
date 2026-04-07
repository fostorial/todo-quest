package com.thexm.todoquest.util

object LevelCalculator {

    // XP required to REACH a given level (cumulative from level 1)
    // Level 1 = 0, Level 2 = 100, Level 3 = 250, Level 4 = 450 ...
    // Formula: xpForLevel(n) = 50 * n * (n - 1) for n >= 1
    fun xpRequiredForLevel(level: Int): Long {
        if (level <= 1) return 0L
        return 50L * level * (level - 1)
    }

    fun levelForXP(totalXP: Long): Int {
        var level = 1
        while (xpRequiredForLevel(level + 1) <= totalXP) {
            level++
        }
        return level
    }

    fun xpInCurrentLevel(totalXP: Long): Long {
        val level = levelForXP(totalXP)
        return totalXP - xpRequiredForLevel(level)
    }

    fun xpRequiredForNextLevel(level: Int): Long {
        val xpForCurrent = xpRequiredForLevel(level)
        val xpForNext = xpRequiredForLevel(level + 1)
        return xpForNext - xpForCurrent
    }

    fun progressFraction(totalXP: Long): Float {
        val level = levelForXP(totalXP)
        val inLevel = xpInCurrentLevel(totalXP).toFloat()
        val needed = xpRequiredForNextLevel(level).toFloat()
        return if (needed <= 0f) 1f else (inLevel / needed).coerceIn(0f, 1f)
    }

    fun levelTitle(level: Int): String = when (level) {
        1 -> "Novice Adventurer"
        2 -> "Apprentice Hero"
        3 -> "Journeyman Quester"
        4 -> "Seasoned Wanderer"
        5 -> "Veteran Champion"
        6 -> "Elite Guardian"
        7 -> "Master Slayer"
        8 -> "Grandmaster"
        9 -> "Legendary Hero"
        10 -> "Mythic Champion"
        else -> if (level > 10) "Transcendent ${level}th Degree" else "Novice Adventurer"
    }
}
