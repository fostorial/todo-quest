package com.thexm.todoquest.notification

import android.content.Context
import androidx.work.*
import com.thexm.todoquest.data.model.Quest
import java.util.concurrent.TimeUnit

object QuestReminderScheduler {

    private const val REMINDER_HOURS_BEFORE = 4L
    private fun workName(questId: Long) = "quest_reminder_$questId"

    /**
     * Schedule (or re-schedule) a 4-hour-before reminder for [quest].
     * Does nothing if the quest has no due date, or the reminder time is already in the past.
     */
    fun schedule(context: Context, quest: Quest) {
        val dueDateMillis = quest.dueDateMillis ?: run {
            cancel(context, quest.id) // due date removed — clear any old reminder
            return
        }

        val reminderAtMillis = dueDateMillis - REMINDER_HOURS_BEFORE * 60 * 60 * 1000L
        val delayMs = reminderAtMillis - System.currentTimeMillis()

        // Always cancel the previous work first so REPLACE takes effect correctly
        cancel(context, quest.id)

        // Skip if we're already past the reminder window (> 1 min late)
        if (delayMs < -60_000L) return

        val data = workDataOf("quest_id" to quest.id)

        val request = OneTimeWorkRequestBuilder<QuestReminderWorker>()
            .setInitialDelay(maxOf(0L, delayMs), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("quest_reminder")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            workName(quest.id),
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    /** Cancel any pending reminder for [questId]. */
    fun cancel(context: Context, questId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork(workName(questId))
    }
}
