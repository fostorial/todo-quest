package com.thexm.todoquest.notification

import android.content.Context
import androidx.work.*
import com.thexm.todoquest.QuestApplication
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class NotificationUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as QuestApplication
        val profile = app.playerRepository.getOrCreateProfile()
        if (profile.notificationEnabled) {
            val count = app.questRepository.getActiveQuestCount().first()
            val pinned = app.questRepository.getPinnedAndUpcomingQuests().first()
            QuestNotificationManager.showQuestBoardNotification(
                applicationContext, count, pinned, profile.notificationPersistent
            )
        }
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "quest_notification_update"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<NotificationUpdateWorker>(
                1, TimeUnit.HOURS
            ).setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun scheduleImmediate(context: Context) {
            val request = OneTimeWorkRequestBuilder<NotificationUpdateWorker>().build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
