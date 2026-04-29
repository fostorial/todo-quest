package com.thexm.todoquest.ui.screens.createlist

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
import com.thexm.todoquest.data.model.PlayerProfile
import com.thexm.todoquest.data.model.ProfileBackground
import com.thexm.todoquest.data.model.QuestBoardBackgroundRegistry
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.ui.components.ProfileBackgroundCanvas
import com.thexm.todoquest.ui.theme.*

@Composable
fun QuestBoardBackgroundsDialog(
    profile: PlayerProfile,
    xpPerClass: Map<String, Long>,
    selectedBackgroundId: String?,
    onSelect: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Board Background",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = QuestPurple
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // "None" option at the top
                item {
                    BoardBgNoneRow(
                        selected = selectedBackgroundId == null,
                        onClick = { onSelect(null) }
                    )
                }

                // Tier header then backgrounds grouped by xpTier
                val tierOrder: List<XPTier?> = XPTier.entries.toList()
                tierOrder.forEach { tier ->
                    val group = QuestBoardBackgroundRegistry.ALL.filter { it.xpTier == tier }
                    if (group.isNotEmpty()) {
                        item { BoardBgTierHeader(tier) }
                        items(group) { bg ->
                            val unlocked = bg.unlockRequirement.isMet(profile, xpPerClass)
                            BoardBgRow(
                                background = bg,
                                unlocked = unlocked,
                                selected = bg.id == selectedBackgroundId,
                                onClick = { if (unlocked) onSelect(bg.id) }
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
private fun BoardBgNoneRow(selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) QuestPurple else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder thumbnail
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                .padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("✕", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f).padding(vertical = 10.dp)) {
            Text(
                "None",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
            )
            Text(
                "Default board appearance",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }

        if (selected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                tint = QuestPurple,
                modifier = Modifier.size(20.dp).padding(end = 8.dp)
            )
        } else {
            Spacer(Modifier.width(28.dp))
        }
    }
}

@Composable
private fun BoardBgTierHeader(tier: XPTier?) {
    val (label, color, emoji) = when (tier) {
        XPTier.SMALL  -> Triple("Uncommon", TierSmall,  "⚔️")
        XPTier.MEDIUM -> Triple("Rare",     TierMedium, "🗡️")
        XPTier.LARGE  -> Triple("Epic",     TierLarge,  "🏆")
        XPTier.EPIC   -> Triple("Legendary",TierEpic,   "💎")
        else          -> Triple("Common",   TierPaltry, "🪨")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
    ) {
        Text(emoji, fontSize = 14.sp)
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
        Spacer(Modifier.width(8.dp))
        Divider(modifier = Modifier.weight(1f), color = color.copy(alpha = 0.4f))
    }
}

@Composable
private fun BoardBgRow(
    background: ProfileBackground,
    unlocked: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tierColor = when (background.xpTier) {
        XPTier.PALTRY -> TierPaltry
        XPTier.SMALL  -> TierSmall
        XPTier.MEDIUM -> TierMedium
        XPTier.LARGE  -> TierLarge
        XPTier.EPIC   -> TierEpic
        null          -> Color(0xFF94A3B8)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) tierColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = unlocked, onClick = onClick)
            .alpha(if (unlocked) 1f else 0.45f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
        ) {
            ProfileBackgroundCanvas(background = background, modifier = Modifier.fillMaxSize())
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
            Text(
                text = background.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
            )
            Text(
                text = background.flavorText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
            if (!unlocked) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = background.unlockRequirement.description(),
                    style = MaterialTheme.typography.labelSmall,
                    color = tierColor.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.width(8.dp))
        if (selected) {
            Icon(Icons.Default.Check, contentDescription = "Selected", tint = tierColor, modifier = Modifier.size(20.dp).padding(end = 8.dp))
        } else if (!unlocked) {
            Icon(Icons.Default.Lock, contentDescription = "Locked", tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(18.dp).padding(end = 10.dp))
        } else {
            Spacer(Modifier.width(28.dp))
        }
    }
}
