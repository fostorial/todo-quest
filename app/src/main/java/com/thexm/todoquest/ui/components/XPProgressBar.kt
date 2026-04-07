package com.thexm.todoquest.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.thexm.todoquest.ui.theme.QuestGold
import com.thexm.todoquest.ui.theme.QuestGoldLight
import com.thexm.todoquest.ui.theme.QuestPurple
import com.thexm.todoquest.ui.theme.QuestPurpleLight
import com.thexm.todoquest.util.LevelCalculator

@Composable
fun XPProgressBar(
    totalXP: Long,
    modifier: Modifier = Modifier
) {
    val level = LevelCalculator.levelForXP(totalXP)
    val progress = LevelCalculator.progressFraction(totalXP)
    val xpInLevel = LevelCalculator.xpInCurrentLevel(totalXP)
    val xpNeeded = LevelCalculator.xpRequiredForNextLevel(level)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "xpProgress"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Level $level",
                style = MaterialTheme.typography.labelLarge,
                color = QuestPurple
            )
            Text(
                text = "$xpInLevel / $xpNeeded XP",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.horizontalGradient(listOf(QuestPurple, QuestGold))
                    )
            )
            // Shimmer stars along the bar
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (animatedProgress > 0.05f) {
                    Text(
                        text = "✨",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }
            }
        }
    }
}
