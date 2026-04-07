package com.thexm.todoquest.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.thexm.todoquest.data.model.HeroClass
import com.thexm.todoquest.data.model.HeroClassRegistry
import com.thexm.todoquest.ui.theme.*

// Tier display metadata
private data class TierMeta(val label: String, val color: Color, val emoji: String)

private fun tierMeta(tier: Int) = when (tier) {
    0    -> TierMeta("Origin",     Color(0xFF94A3B8), "🌱")
    1    -> TierMeta("Journeyman", TierSmall,         "⚔️")
    2    -> TierMeta("Specialist", TierMedium,        "🔷")
    3    -> TierMeta("Master",     TierEpic,          "💎")
    else -> TierMeta("Unknown",    Color.Gray,        "❓")
}

@Composable
fun ClassTreeDialog(
    selectedClassId: String,
    xpPerClass: Map<String, Long>,
    onSelectClass: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            QuestPurpleDark,
                            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "⚔️  Hero Class",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Text(
                    text = "XP earned while a class is active unlocks its branches.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )

                // Tree list
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    val tiers = HeroClassRegistry.ALL.groupBy { it.tier }
                    (0..3).forEach { tier ->
                        val classes = tiers[tier] ?: return@forEach
                        val meta = tierMeta(tier)

                        item {
                            TierHeader(meta = meta, tier = tier)
                        }

                        items(classes) { heroClass ->
                            val unlocked = HeroClassRegistry.isUnlocked(heroClass.id, xpPerClass)
                            val isSelected = heroClass.id == selectedClassId
                            val parentXpEarned = xpPerClass[heroClass.parentId] ?: 0L
                            val indent = tier * 12

                            ClassRow(
                                heroClass = heroClass,
                                unlocked = unlocked,
                                isSelected = isSelected,
                                parentXpEarned = parentXpEarned,
                                tierColor = meta.color,
                                indent = indent,
                                onClick = { if (unlocked) onSelectClass(heroClass.id) }
                            )
                        }

                        item { Spacer(Modifier.height(4.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TierHeader(meta: TierMeta, tier: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = if (tier == 0) 0.dp else 8.dp, bottom = 4.dp)
    ) {
        Text(meta.emoji, fontSize = 14.sp)
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Tier $tier  ·  ${meta.label}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = meta.color
        )
        Spacer(Modifier.width(8.dp))
        Divider(modifier = Modifier.weight(1f), color = meta.color.copy(alpha = 0.35f))
    }
}

@Composable
private fun ClassRow(
    heroClass: HeroClass,
    unlocked: Boolean,
    isSelected: Boolean,
    parentXpEarned: Long,
    tierColor: Color,
    indent: Int,
    onClick: () -> Unit
) {
    val parent = heroClass.parentId?.let { HeroClassRegistry.getById(it) }
    val progressFraction = if (heroClass.xpRequiredInParent > 0)
        (parentXpEarned.toFloat() / heroClass.xpRequiredInParent).coerceIn(0f, 1f)
    else 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indent.dp)
    ) {
        // Tree connector line
        if (heroClass.tier > 0) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(56.dp)
                    .align(Alignment.CenterVertically)
                    .background(tierColor.copy(alpha = if (unlocked) 0.5f else 0.2f))
            )
            Spacer(Modifier.width(8.dp))
        }

        // Card
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (isSelected) tierColor.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) tierColor else Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable(enabled = unlocked, onClick = onClick)
                .alpha(if (unlocked) 1f else 0.5f)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Class emoji badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(tierColor.copy(alpha = if (unlocked) 0.2f else 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(heroClass.emoji, fontSize = 18.sp)
                }
                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = heroClass.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                        color = if (isSelected) tierColor else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = heroClass.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer(Modifier.width(8.dp))
                when {
                    isSelected -> Icon(
                        Icons.Default.Check, null,
                        tint = tierColor, modifier = Modifier.size(20.dp)
                    )
                    !unlocked -> Icon(
                        Icons.Default.Lock, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // XP progress bar — shown for locked classes or newly unlocked ones
            if (heroClass.parentId != null && !isSelected) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (unlocked) tierColor else tierColor.copy(alpha = 0.6f),
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (unlocked) "✓ ${heroClass.xpRequiredInParent} XP as ${parent?.displayName}"
                               else "${parentXpEarned} / ${heroClass.xpRequiredInParent} XP as ${parent?.displayName}",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = if (unlocked) tierColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
