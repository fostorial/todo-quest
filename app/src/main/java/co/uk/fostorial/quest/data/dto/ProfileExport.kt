package co.uk.fostorial.quest.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileExport(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val profile: ProfileDto,
    val classProgress: List<ClassProgressDto>,
    val questLists: List<QuestListExportDto>
)

@Serializable
data class ProfileDto(
    val heroName: String,
    val level: Int,
    val totalXP: Long,
    val totalQuestsCompleted: Int,
    val paltryQuestsCompleted: Int,
    val smallQuestsCompleted: Int,
    val mediumQuestsCompleted: Int,
    val largeQuestsCompleted: Int,
    val epicQuestsCompleted: Int,
    val selectedTitleId: String,
    val selectedBackgroundId: String,
    val selectedClassId: String,
    val notificationEnabled: Boolean,
    val notificationPersistent: Boolean
)

@Serializable
data class ClassProgressDto(
    val classId: String,
    val xpEarned: Long
)

@Serializable
data class QuestListExportDto(
    val shareId: String,
    val name: String,
    val emoji: String,
    val colorHex: String,
    val boardBackgroundId: String? = null,
    val sortOrder: Int,
    val quests: List<FullQuestExportDto>
)

@Serializable
data class FullQuestExportDto(
    val title: String,
    val description: String,
    val xpTier: String,
    val isCompleted: Boolean,
    val isPinned: Boolean,
    val recurrenceType: String,
    val dueDateMillis: Long? = null,
    val completedAtMillis: Long? = null,
    val createdAtMillis: Long,
    val sortOrder: Int
)
