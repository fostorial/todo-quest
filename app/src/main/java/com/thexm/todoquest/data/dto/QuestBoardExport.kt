package com.thexm.todoquest.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestBoardExport(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val board: BoardExportDto
)

@Serializable
data class BoardExportDto(
    val shareId: String,
    val name: String,
    val emoji: String,
    val colorHex: String,
    val quests: List<QuestExportDto>
)

@Serializable
data class QuestExportDto(
    val title: String,
    val description: String,
    val xpTier: String,
    val isPinned: Boolean,
    val recurrenceType: String,
    val dueDateMillis: Long? = null,
    val sortOrder: Int
)
