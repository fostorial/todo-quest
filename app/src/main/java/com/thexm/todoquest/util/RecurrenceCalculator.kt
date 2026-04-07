package com.thexm.todoquest.util

import com.thexm.todoquest.data.model.RecurrenceType
import java.util.Calendar

object RecurrenceCalculator {

    /**
     * Returns the millis for the next occurrence after [fromMillis], or null for NONE.
     * Preserves the original time-of-day (hour/minute) from [fromMillis].
     */
    fun nextDueDate(fromMillis: Long, recurrenceType: RecurrenceType): Long? {
        if (recurrenceType == RecurrenceType.NONE) return null

        val cal = Calendar.getInstance().apply { timeInMillis = fromMillis }

        when (recurrenceType) {
            RecurrenceType.DAILY -> cal.add(Calendar.DAY_OF_MONTH, 1)
            RecurrenceType.WEEKDAYS -> {
                // Advance at least one day, then skip over weekend days
                do { cal.add(Calendar.DAY_OF_MONTH, 1) }
                while (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                       cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            }
            RecurrenceType.WEEKLY -> cal.add(Calendar.WEEK_OF_YEAR, 1)
            RecurrenceType.FORTNIGHTLY -> cal.add(Calendar.WEEK_OF_YEAR, 2)
            RecurrenceType.MONTHLY -> cal.add(Calendar.MONTH, 1)
            RecurrenceType.NONE -> return null
        }

        return cal.timeInMillis
    }
}
