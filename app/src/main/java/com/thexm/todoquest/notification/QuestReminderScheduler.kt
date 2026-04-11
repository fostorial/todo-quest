package com.thexm.todoquest.notification

import android.content.Context
import androidx.work.*
import com.thexm.todoquest.data.model.Quest
import java.util.concurrent.TimeUnit

object QuestReminderScheduler {

    private const val REMINDER_HOURS_BEFORE = 4L
    private fun workName(questId: Long) = "quest_reminder_$questId"
    private fun dueNowWorkName(questId: Long) = "quest_due_now_$questId"

    /**
     * Schedule (or re-schedule) a 4-hour-before reminder and a due-now notification for [quest].
     * Does nothing if the quest has no due date, or both trigger times are already in the past.
     */
    fun schedule(context: Context, quest: Quest) {
        val dueDateMillis = quest.dueDateMillis ?: run {
            cancel(context, quest.id) // due date removed — clear any old reminders
            return
        }

        val now = System.currentTimeMillis()
        val reminderAtMillis = dueDateMillis - REMINDER_HOURS_BEFORE * 60 * 60 * 1000L
        val reminderDelayMs = reminderAtMillis - now
        val dueNowDelayMs = dueDateMillis - now

        // Always cancel previous work first so REPLACE takes effect correctly
        cancel(context, quest.id)

        val data = workDataOf("quest_id" to quest.id)
        val wm = WorkManager.getInstance(context)

        // Schedule 4-hour advance reminder if not yet past the window (> 1 min late)
        if (reminderDelayMs >= -60_000L) {
            val request = OneTimeWorkRequestBuilder<QuestReminderWorker>()
                .setInitialDelay(maxOf(0L, reminderDelayMs), TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("quest_reminder")
                .build()
            wm.enqueueUniqueWork(workName(quest.id), ExistingWorkPolicy.REPLACE, request)
        }

        // Schedule due-now notification if the due date hasn't passed yet (> 1 min late)
        if (dueNowDelayMs >= -60_000L) {
            val request = OneTimeWorkRequestBuilder<QuestDueNowWorker>()
                .setInitialDelay(maxOf(0L, dueNowDelayMs), TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("quest_due_now")
                .build()
            wm.enqueueUniqueWork(dueNowWorkName(quest.id), ExistingWorkPolicy.REPLACE, request)
        }
    }

    /** Cancel any pending reminders for [questId]. */
    fun cancel(context: Context, questId: Long) {
        val wm = WorkManager.getInstance(context)
        wm.cancelUniqueWork(workName(questId))
        wm.cancelUniqueWork(dueNowWorkName(questId))
    }
}
