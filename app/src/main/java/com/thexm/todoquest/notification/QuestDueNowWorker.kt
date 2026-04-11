package com.thexm.todoquest.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.thexm.todoquest.QuestApplication

class QuestDueNowWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val questId = inputData.getLong("quest_id", -1L)
        if (questId == -1L) return Result.failure()

        val app = applicationContext as QuestApplication

        val quest = app.questRepository.getQuestById(questId) ?: return Result.success()
        if (quest.isCompleted) return Result.success()

        val list = app.questRepository.getListById(quest.listId)

        QuestNotificationManager.showQuestDueNowNotification(
            context = applicationContext,
            quest = quest,
            listName = list?.name ?: "Unknown Board",
            listEmoji = list?.emoji ?: "📜"
        )

        return Result.success()
    }
}
