package com.thexm.todoquest.ui.screens.profile

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
import com.thexm.todoquest.data.model.BackgroundRegistry
import com.thexm.todoquest.data.model.PlayerProfile
import com.thexm.todoquest.data.model.ProfileBackground
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.ui.components.ProfileBackgroundCanvas
import com.thexm.todoquest.ui.theme.*

@Composable
fun BackgroundsDialog(
    profile: PlayerProfile,
    selectedBackgroundId: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Backgrounds",
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
                val tierOrder: List<XPTier?> = listOf(null) + XPTier.entries.toList()
                tierOrder.forEach { tier ->
                    val group = BackgroundRegistry.ALL.filter { it.xpTier == tier }
                    if (group.isNotEmpty()) {
                        item { TierHeader(tier) }
                        items(group) { bg ->
                            val unlocked = bg.unlockRequirement.isMet(profile)
                            val selected = bg.id == selectedBackgroundId
                            BackgroundRow(
                                background = bg,
                                unlocked = unlocked,
                                selected = selected,
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
private fun TierHeader(tier: XPTier?) {
    val (label, color, emoji) = when (tier) {
        null          -> Triple("Default",   Color(0xFF94A3B8), "🌟")
        XPTier.PALTRY -> Triple("Common",    TierPaltry,        "🪨")
        XPTier.SMALL  -> Triple("Uncommon",  TierSmall,         "⚔️")
        XPTier.MEDIUM -> Triple("Rare",      TierMedium,        "🗡️")
        XPTier.LARGE  -> Triple("Epic",      TierLarge,         "🏆")
        XPTier.EPIC   -> Triple("Legendary", TierEpic,          "💎")
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    ) {
        Text(emoji, fontSize = 14.sp)
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
        Spacer(Modifier.width(8.dp))
        Divider(modifier = Modifier.weight(1f), color = color.copy(alpha = 0.4f))
    }
}

@Composable
private fun BackgroundRow(
    background: ProfileBackground,
    unlocked: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tierColor = when (background.xpTier) {
        null          -> Color(0xFF94A3B8)
        XPTier.PALTRY -> TierPaltry
        XPTier.SMALL  -> TierSmall
        XPTier.MEDIUM -> TierMedium
        XPTier.LARGE  -> TierLarge
        XPTier.EPIC   -> TierEpic
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
        // Preview thumbnail
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
        ) {
            ProfileBackgroundCanvas(
                background = background,
                modifier = Modifier.fillMaxSize()
            )
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
