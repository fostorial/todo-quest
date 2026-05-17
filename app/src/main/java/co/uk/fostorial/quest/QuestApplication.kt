package co.uk.fostorial.quest

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import co.uk.fostorial.quest.data.db.QuestDatabase
import co.uk.fostorial.quest.data.repository.PlayerRepository
import co.uk.fostorial.quest.data.repository.QuestRepository
import co.uk.fostorial.quest.notification.NotificationUpdateWorker
import co.uk.fostorial.quest.notification.QuestNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestApplication : Application() {

    val database by lazy { QuestDatabase.getInstance(this) }
    val questRepository by lazy { QuestRepository(database.questDao(), database.questListDao()) }
    val playerRepository by lazy { PlayerRepository(database.playerProfileDao(), database.heroClassProgressDao()) }

    /** URI of an incoming .questboard file; observed by Compose so onNewIntent updates propagate. */
    val pendingImportUri = mutableStateOf<Uri?>(null)

    override fun onCreate() {
        super.onCreate()
        QuestNotificationManager.createChannels(this)
        CoroutineScope(Dispatchers.IO).launch {
            playerRepository.ensureProfileExists()
        }
        NotificationUpdateWorker.schedule(this)
    }
}
