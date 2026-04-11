package com.thexm.todoquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thexm.todoquest.ui.theme.QuestGold
import com.thexm.todoquest.ui.theme.QuestGoldLight
import com.thexm.todoquest.ui.theme.QuestPurple
import com.thexm.todoquest.ui.theme.QuestPurpleLight

@Composable
fun LevelBadge(
    level: Int,
    classEmoji: String = "🧭",
    size: Dp = 56.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(listOf(QuestGoldLight, QuestGold))
            )
            .border(2.dp, QuestPurple, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = classEmoji,
                fontSize = (size.value * 0.3f).sp
            )
            Text(
                text = level.toString(),
                fontWeight = FontWeight.ExtraBold,
                fontSize = (size.value * 0.28f).sp,
                color = QuestPurple,
                lineHeight = (size.value * 0.28f).sp
            )
        }
    }
}
