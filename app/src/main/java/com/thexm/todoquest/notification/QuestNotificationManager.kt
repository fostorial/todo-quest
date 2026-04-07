package com.thexm.todoquest.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.thexm.todoquest.MainActivity
import com.thexm.todoquest.R
import com.thexm.todoquest.data.model.Quest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object QuestNotificationManager {

    const val CHANNEL_ID = "quest_notifications"
    const val CHANNEL_PERSISTENT_ID = "quest_persistent"
    const val CHANNEL_REMINDER_ID = "quest_reminders"
    const val SUMMARY_NOTIFICATION_ID = 1000
    const val QUEST_GROUP_KEY = "com.thexm.todoquest.QUEST_GROUP"

    fun createChannels(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        NotificationChannel(
            CHANNEL_ID,
            "Quest Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for quest reminders and completions"
            manager.createNotificationChannel(this)
        }

        NotificationChannel(
            CHANNEL_PERSISTENT_ID,
            "Quest Board",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Persistent notification showing your active quests"
            setShowBadge(false)
            manager.createNotificationChannel(this)
        }

        NotificationChannel(
            CHANNEL_REMINDER_ID,
            "Quest Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts 4 hours before a quest is due"
            enableVibration(true)
            manager.createNotificationChannel(this)
        }
    }

    fun showQuestBoardNotification(
        context: Context,
        activeCount: Int,
        pinnedAndUpcoming: List<Quest>,
        persistent: Boolean
    ) {
        if (activeCount == 0 && !persistent) {
            cancelQuestBoard(context)
            return
        }

        val channelId = if (persistent) CHANNEL_PERSISTENT_ID else CHANNEL_ID
        val openAppIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification_sword)
            .setContentTitle("$activeCount Quest${if (activeCount != 1) "s" else ""} Available")
            .setContentText(buildSummaryText(pinnedAndUpcoming))
            .setContentIntent(openAppIntent)
            .setGroup(QUEST_GROUP_KEY)
            .setGroupSummary(false)
            .setAutoCancel(!persistent)
            .setOngoing(persistent)
            .setPriority(if (persistent) NotificationCompat.PRIORITY_LOW else NotificationCompat.PRIORITY_DEFAULT)

        if (pinnedAndUpcoming.isNotEmpty()) {
            val inboxStyle = NotificationCompat.InboxStyle()
                .setBigContentTitle("$activeCount Quest${if (activeCount != 1) "s" else ""} Available")
                .setSummaryText("Tap to open Quest Board")

            pinnedAndUpcoming.take(5).forEach { quest ->
                val line = buildQuestLine(quest)
                inboxStyle.addLine(line)
            }
            builder.setStyle(inboxStyle)
        }

        try {
            NotificationManagerCompat.from(context).notify(SUMMARY_NOTIFICATION_ID, builder.build())
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    fun showQuestReminderNotification(
        context: Context,
        quest: Quest,
        listName: String,
        listEmoji: String
    ) {
        val dueDateMillis = quest.dueDateMillis ?: return
        val notificationId = (quest.id % 100_000L + 10_001L).toInt()

        val openAppIntent = PendingIntent.getActivity(
            context, notificationId,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val completeIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "com.thexm.todoquest.ACTION_COMPLETE_QUEST"
            putExtra("quest_id", quest.id)
        }
        val completePendingIntent = PendingIntent.getBroadcast(
            context, notificationId,
            completeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dueDateFormatted = SimpleDateFormat("MMM d 'at' h:mm a", Locale.getDefault())
            .format(Date(dueDateMillis))

        val expandedText = buildString {
            appendLine(quest.title)
            if (quest.description.isNotBlank()) appendLine(quest.description)
            appendLine("⏰ Due: $dueDateFormatted")
            appendLine("$listEmoji Board: $listName")
            append("${quest.xpTier.emoji} Reward: ${quest.xpTier.displayName} (+${quest.xpTier.xpValue} XP)")
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDER_ID)
            .setSmallIcon(R.drawable.ic_notification_sword)
            .setContentTitle("⏰ Quest Due in 4 Hours!")
            .setContentText(quest.title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(expandedText)
                    .setBigContentTitle("⏰ Quest Due in 4 Hours!")
                    .setSummaryText("$listEmoji $listName")
            )
            .setContentIntent(openAppIntent)
            .addAction(0, "✓ Complete Quest", completePendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }

    fun cancelQuestReminder(context: Context, questId: Long) {
        val notificationId = (questId % 100_000L + 10_001L).toInt()
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    fun cancelQuestBoard(context: Context) {
        NotificationManagerCompat.from(context).cancel(SUMMARY_NOTIFICATION_ID)
    }

    private fun buildSummaryText(quests: List<Quest>): String {
        if (quests.isEmpty()) return "Tap to view your quests"
        val pinned = quests.filter { it.isPinned }
        return when {
            pinned.isNotEmpty() -> "Pinned: ${pinned.first().title}"
            else -> quests.first().title
        }
    }

    private fun buildQuestLine(quest: Quest): String {
        val prefix = when {
            quest.isPinned -> "📌 "
            quest.dueDateMillis != null -> "⏰ "
            else -> "• "
        }
        val suffix = if (quest.dueDateMillis != null) {
            " — ${formatDueDate(quest.dueDateMillis)}"
        } else ""
        return "$prefix${quest.title}$suffix"
    }

    private fun formatDueDate(millis: Long): String {
        val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}
