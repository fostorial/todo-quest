package com.thexm.todoquest.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.thexm.todoquest.QuestApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.thexm.todoquest.ACTION_COMPLETE_QUEST" -> {
                val questId = intent.getLongExtra("quest_id", -1L)
                if (questId != -1L) {
                    val app = context.applicationContext as QuestApplication
                    CoroutineScope(Dispatchers.IO).launch {
                        val quest = app.questRepository.getQuestById(questId)
                        if (quest != null && !quest.isCompleted) {
                            app.questRepository.completeQuest(questId)
                            app.playerRepository.awardXP(quest.xpTier.xpValue)
                            app.playerRepository.recordQuestCompletion(quest.xpTier)
                            QuestNotificationManager.cancelQuestReminder(context, questId)
                            QuestNotificationManager.cancelQuestDueNow(context, questId)
                            NotificationUpdateWorker.scheduleImmediate(context)
                        }
                    }
                }
            }
            "com.thexm.todoquest.ACTION_DISMISS_NOTIFICATION" -> {
                QuestNotificationManager.cancelQuestBoard(context)
            }
        }
    }
}
