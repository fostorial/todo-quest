package com.thexm.todoquest.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.data.model.RecurrenceType
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun xpTierColor(tier: XPTier): Color = when (tier) {
    XPTier.PALTRY -> TierPaltry
    XPTier.SMALL -> TierSmall
    XPTier.MEDIUM -> TierMedium
    XPTier.LARGE -> TierLarge
    XPTier.EPIC -> TierEpic
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestCard(
    quest: Quest,
    onComplete: (Quest) -> Unit,
    onPin: (Quest) -> Unit,
    onDelete: (Quest) -> Unit,
    onEdit: (Quest) -> Unit,
    modifier: Modifier = Modifier
) {
    val tierColor = xpTierColor(quest.xpTier)
    val alpha by animateColorAsState(
        targetValue = if (quest.isCompleted) Color.Gray else Color.Transparent,
        animationSpec = tween(300),
        label = "cardAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (quest.isCompleted) 0.65f else 1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (quest.isPinned) 4.dp else 2.dp),
        onClick = { if (!quest.isCompleted) onEdit(quest) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // XP tier colour strip
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(tierColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Complete checkbox
            IconButton(
                onClick = { if (!quest.isCompleted) onComplete(quest) },
                modifier = Modifier.size(36.dp)
            ) {
                if (quest.isCompleted) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = QuestEmerald,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Mark complete",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quest info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (quest.isPinned) {
                        Text("📌 ", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(
                        text = quest.title,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = if (quest.isCompleted) TextDecoration.LineThrough else null,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (quest.description.isNotBlank()) {
                    Text(
                        text = quest.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // XP chip
                    XPChip(tier = quest.xpTier, color = tierColor)

                    // Recurrence chip
                    if (quest.recurrenceType != RecurrenceType.NONE) {
                        RecurrenceChip(quest.recurrenceType)
                    }

                    // Due date
                    quest.dueDateMillis?.let { due ->
                        val isOverdue = due < System.currentTimeMillis() && !quest.isCompleted
                        Text(
                            text = "⏰ ${SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(due))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Actions
            if (!quest.isCompleted) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { onPin(quest) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            if (quest.isPinned) Icons.Default.PushPin else Icons.Default.PinDrop,
                            contentDescription = if (quest.isPinned) "Unpin" else "Pin",
                            tint = if (quest.isPinned) QuestPurple else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { onDelete(quest) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Delete quest",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun XPChip(tier: XPTier, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${tier.emoji} ${tier.displayName} (+${tier.xpValue} XP)",
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun RecurrenceChip(recurrenceType: RecurrenceType) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(QuestEmerald.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${recurrenceType.emoji} ${recurrenceType.displayName}",
            style = MaterialTheme.typography.labelSmall,
            color = QuestEmerald
        )
    }
}
