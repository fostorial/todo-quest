package com.thexm.todoquest.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.thexm.todoquest.QuestApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val app = context.applicationContext as QuestApplication
            CoroutineScope(Dispatchers.IO).launch {
                val profile = app.playerRepository.getOrCreateProfile()
                if (profile.notificationEnabled) {
                    val count = app.questRepository.getActiveQuestCount().first()
                    val pinned = app.questRepository.getPinnedAndUpcomingQuests().first()
                    QuestNotificationManager.showQuestBoardNotification(
                        context, count, pinned, profile.notificationPersistent
                    )
                }
            }
        }
    }
}
