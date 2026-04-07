package com.thexm.todoquest.data.model

enum class XPTier(val displayName: String, val xpValue: Int, val emoji: String, val description: String) {
    PALTRY("Paltry", 1, "🪨", "A minor errand"),
    SMALL("Small", 3, "⚔️", "A simple task"),
    MEDIUM("Medium", 5, "🗡️", "A decent challenge"),
    LARGE("Large", 8, "🏆", "A worthy quest"),
    EPIC("Epic", 12, "💎", "A legendary feat")
}
