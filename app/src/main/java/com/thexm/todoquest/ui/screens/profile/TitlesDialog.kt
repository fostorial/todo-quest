package com.thexm.todoquest.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thexm.todoquest.data.model.Title
import com.thexm.todoquest.data.model.TitleRegistry
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.ui.theme.*

@Composable
fun TitlesDialog(
    playerLevel: Int,
    selectedTitleId: String,
    onSelectTitle: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Titles",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = QuestPurple
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Group order: null (default) first, then each XPTier in order
                val tierOrder: List<XPTier?> = listOf(null) + XPTier.entries.toList()
                tierOrder.forEach { tier ->
                    val titlesInGroup = TitleRegistry.ALL.filter { it.xpTier == tier }
                    if (titlesInGroup.isNotEmpty()) {
                        item {
                            TierSectionHeader(tier = tier)
                        }
                        items(titlesInGroup) { title ->
                            val unlocked = playerLevel >= title.levelRequired
                            val isSelected = title.id == selectedTitleId
                            TitleRow(
                                title = title,
                                unlocked = unlocked,
                                isSelected = isSelected,
                                onClick = { if (unlocked) onSelectTitle(title.id) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = QuestPurple)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun TierSectionHeader(tier: XPTier?) {
    val (label, color, emoji) = when (tier) {
        null          -> Triple("Default", Color(0xFF94A3B8), "🌟")
        XPTier.PALTRY -> Triple("Common", TierPaltry, "🪨")
        XPTier.SMALL  -> Triple("Uncommon", TierSmall, "⚔️")
        XPTier.MEDIUM -> Triple("Rare", TierMedium, "🗡️")
        XPTier.LARGE  -> Triple("Epic", TierLarge, "🏆")
        XPTier.EPIC   -> Triple("Legendary", TierEpic, "💎")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    ) {
        Text(emoji, fontSize = 14.sp)
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(Modifier.width(8.dp))
        Divider(modifier = Modifier.weight(1f), color = color.copy(alpha = 0.4f))
    }
}

@Composable
private fun TitleRow(
    title: Title,
    unlocked: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tierColor = when (title.xpTier) {
        null          -> Color(0xFF94A3B8)
        XPTier.PALTRY -> TierPaltry
        XPTier.SMALL  -> TierSmall
        XPTier.MEDIUM -> TierMedium
        XPTier.LARGE  -> TierLarge
        XPTier.EPIC   -> TierEpic
    }

    val borderColor = when {
        isSelected -> tierColor
        else       -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) tierColor.copy(alpha = 0.12f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = unlocked, onClick = onClick)
            .alpha(if (unlocked) 1f else 0.45f)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left color strip
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(tierColor)
        )
        Spacer(Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
            )
            Text(
                text = title.flavorText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
            if (!unlocked) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Unlocks at Level ${title.levelRequired}",
                    style = MaterialTheme.typography.labelSmall,
                    color = tierColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.width(8.dp))
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = tierColor,
                modifier = Modifier.size(20.dp)
            )
        } else if (!unlocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
