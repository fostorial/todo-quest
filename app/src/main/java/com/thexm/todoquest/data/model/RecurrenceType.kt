package com.thexm.todoquest.data.model

enum class RecurrenceType(val displayName: String, val emoji: String) {
    NONE("None", "🚫"),
    DAILY("Daily", "☀️"),
    WEEKDAYS("Weekdays", "💼"),
    WEEKLY("Weekly", "📅"),
    FORTNIGHTLY("Fortnightly", "🌓"),
    MONTHLY("Monthly", "🌙")
}
