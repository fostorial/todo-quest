package com.thexm.todoquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thexm.todoquest.data.model.QuestBoardBackgroundRegistry
import com.thexm.todoquest.data.model.QuestList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestListCard(
    questList: QuestList,
    activeQuestCount: Int,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listColor = parseHexColor(questList.colorHex)
    val boardBackground = questList.boardBackgroundId?.let { QuestBoardBackgroundRegistry.getById(it) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (boardBackground != null) Color.Transparent
                            else listColor.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        onClick = onClick
    ) {
        Box {
            // Board background layer (if set)
            if (boardBackground != null) {
                ProfileBackgroundCanvas(
                    background = boardBackground,
                    modifier = Modifier.matchParentSize()
                )
                // Dim overlay so content stays readable
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.45f))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji icon in coloured circle
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(listColor.copy(alpha = if (boardBackground != null) 0.5f else 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = questList.emoji,
                        fontSize = 26.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                val textColor = if (boardBackground != null) Color.White
                                else MaterialTheme.colorScheme.onSurface
                val subtextColor = if (boardBackground != null) Color.White.copy(alpha = 0.75f)
                                   else MaterialTheme.colorScheme.onSurfaceVariant

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = questList.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = if (activeQuestCount == 0) "No active quests"
                               else "$activeQuestCount quest${if (activeQuestCount != 1) "s" else ""} remaining",
                        style = MaterialTheme.typography.bodySmall,
                        color = subtextColor
                    )
                }

                // Quest count badge
                if (activeQuestCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(listColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activeQuestCount.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Edit / delete
                val iconTint = if (boardBackground != null) Color.White.copy(alpha = 0.85f) else listColor
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit list",
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete list",
                        tint = if (boardBackground != null) Color.White.copy(alpha = 0.7f)
                               else MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

fun parseHexColor(hex: String): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        Color(android.graphics.Color.parseColor("#$cleanHex"))
    } catch (e: Exception) {
        Color(0xFF8B5CF6)
    }
}
