package com.thexm.todoquest.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.thexm.todoquest.data.dto.BoardExportDto
import com.thexm.todoquest.data.dto.QuestBoardExport
import com.thexm.todoquest.data.dto.QuestExportDto
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.QuestList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object ShareManager {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Serialises the board and its active quests to a .questboard file in the app
     * cache and returns a FileProvider URI ready for use in a share Intent.
     */
    fun createShareUri(context: Context, questList: QuestList, quests: List<Quest>): Uri {
        val export = QuestBoardExport(
            board = BoardExportDto(
                shareId = requireNotNull(questList.shareId),
                name = questList.name,
                emoji = questList.emoji,
                colorHex = questList.colorHex,
                quests = quests.map { q ->
                    QuestExportDto(
                        title = q.title,
                        description = q.description,
                        xpTier = q.xpTier.name,
                        isPinned = q.isPinned,
                        recurrenceType = q.recurrenceType.name,
                        dueDateMillis = q.dueDateMillis,
                        sortOrder = q.sortOrder
                    )
                }
            )
        )
        val safeName = questList.name
            .replace(Regex("[^a-zA-Z0-9_\\- ]"), "")
            .trim().replace(' ', '_').take(50).ifEmpty { "questboard" }

        val file = File(context.cacheDir, "$safeName.questboard")
        file.writeText(json.encodeToString(export))

        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    /**
     * Reads and deserialises a .questboard file from the given URI.
     * Returns null if the file cannot be read or parsed.
     */
    fun readBoardFromUri(context: Context, uri: Uri): QuestBoardExport? = try {
        context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText()
            ?.let { json.decodeFromString<QuestBoardExport>(it) }
    } catch (e: Exception) {
        null
    }
}
