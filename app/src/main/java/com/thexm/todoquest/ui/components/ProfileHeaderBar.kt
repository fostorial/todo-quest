package com.thexm.todoquest.ui.components

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.QuestApplication
import com.thexm.todoquest.data.model.HeroClassRegistry
import com.thexm.todoquest.data.model.PlayerProfile
import com.thexm.todoquest.data.model.Title
import com.thexm.todoquest.data.model.TitleRegistry
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.ui.theme.*
import com.thexm.todoquest.util.LevelCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// ── ViewModel ────────────────────────────────────────────────────────────────

data class ProfileHeaderState(
    val heroName: String = "Hero",
    val level: Int = 1,
    val selectedTitle: Title = TitleRegistry.ALL.first(),
    val classEmoji: String = "🧭",
    val xpProgressFraction: Float = 0f,
    val xpInLevel: Long = 0,
    val xpForNextLevel: Long = 100
)

class ProfileHeaderViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as QuestApplication).playerRepository

    val state = repo.getProfile()
        .map { profile ->
            val p = profile ?: PlayerProfile()
            ProfileHeaderState(
                heroName = p.heroName,
                level = p.level,
                selectedTitle = TitleRegistry.getById(p.selectedTitleId),
                classEmoji = HeroClassRegistry.getById(p.selectedClassId).emoji,
                xpProgressFraction = LevelCalculator.progressFraction(p.totalXP),
                xpInLevel = LevelCalculator.xpInCurrentLevel(p.totalXP),
                xpForNextLevel = LevelCalculator.xpRequiredForNextLevel(p.level)
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileHeaderState())
}

// ── Composable ────────────────────────────────────────────────────────────────

@Composable
fun ProfileHeaderBar(
    onTap: () -> Unit,
    viewModel: ProfileHeaderViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val titleColor: Color = when (state.selectedTitle.xpTier) {
        null          -> Color.White.copy(alpha = 0.75f)
        XPTier.PALTRY -> TierPaltry
        XPTier.SMALL  -> TierSmall
        XPTier.MEDIUM -> Color(0xFF93C5FD)   // lighter blue — readable on purple bg
        XPTier.LARGE  -> QuestPurpleLight
        XPTier.EPIC   -> QuestGoldLight
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(QuestPurpleDark, QuestPurple))
            )
            .statusBarsPadding()
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Level badge with class emoji overlay
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(QuestGoldLight, QuestGold))),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${state.level}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = QuestPurpleDark
                    )
                    Text(
                        text = "LVL",
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuestPurpleDark.copy(alpha = 0.75f),
                        lineHeight = 7.sp
                    )
                }
            }
            // Class emoji badge
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(QuestPurpleDark),
                contentAlignment = Alignment.Center
            ) {
                Text(state.classEmoji, fontSize = 9.sp)
            }
        }

        // Name + title
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.heroName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = state.selectedTitle.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // XP progress
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(100.dp)
        ) {
            Text(
                text = "${state.xpInLevel} / ${state.xpForNextLevel} XP",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 9.sp
            )
            Spacer(Modifier.height(3.dp))
            LinearProgressIndicator(
                progress = { state.xpProgressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = QuestGold,
                trackColor = Color.White.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}
