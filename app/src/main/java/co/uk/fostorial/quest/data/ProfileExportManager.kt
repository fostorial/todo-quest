package co.uk.fostorial.quest.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import co.uk.fostorial.quest.data.dto.ProfileExport
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object ProfileExportManager {

    private val json = Json { ignoreUnknownKeys = true }

    fun createExportUri(context: Context, export: ProfileExport): Uri {
        val safeName = export.profile.heroName
            .replace(Regex("[^a-zA-Z0-9_\\- ]"), "")
            .trim().replace(' ', '_').take(30).ifEmpty { "hero" }
        val file = File(context.cacheDir, "${safeName}_profile.heroexport")
        file.writeText(json.encodeToString(export))
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    fun readExportFromUri(context: Context, uri: Uri): ProfileExport? = try {
        context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText()
            ?.let { json.decodeFromString<ProfileExport>(it) }
    } catch (e: Exception) {
        null
    }
}
