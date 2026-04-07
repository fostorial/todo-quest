package com.thexm.todoquest.ui.screens.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.ui.components.QuestCard
import com.thexm.todoquest.ui.components.parseHexColor
import com.thexm.todoquest.ui.screens.questlist.CompletionEvent
import com.thexm.todoquest.ui.theme.QuestGold
import com.thexm.todoquest.ui.theme.QuestPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActiveScreen(
    onEditQuest: (com.thexm.todoquest.data.model.Quest) -> Unit,
    viewModel: ActiveViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.levelUpEvent?.let { newLevel ->
        LevelUpDialog(newLevel = newLevel, onDismiss = viewModel::dismissLevelUp)
    }

    uiState.pendingDeleteQuest?.let { quest ->
        DeleteQuestDialog(
            questTitle = quest.title,
            onConfirm = viewModel::confirmDeleteQuest,
            onDismiss = viewModel::cancelDeleteQuest
        )
    }

    uiState.completionEvent?.let { event ->
        LaunchedEffect(event) {
            snackbarHostState.showSnackbar(
                message = completionMessage(event),
                duration = SnackbarDuration.Short
            )
            viewModel.dismissCompletion()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = QuestPurple,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { padding ->
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFDC2626), QuestPurple)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⏳", fontSize = 28.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Active Quests",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    val overdueCount = uiState.overdueQuests.size
                    val upcomingCount = uiState.upcomingQuests.size
                    Text(
                        text = buildString {
                            if (overdueCount > 0) append("$overdueCount overdue  •  ")
                            append("$upcomingCount upcoming")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // ── Loading ──────────────────────────────────────────────────────────
        if (uiState.isLoading) {
            item {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = QuestPurple)
                }
            }
            return@LazyColumn
        }

        // ── Overdue section ──────────────────────────────────────────────────
        if (uiState.overdueQuests.isNotEmpty()) {
            item {
                SectionHeader(
                    emoji = "🔥",
                    title = "Overdue",
                    count = uiState.overdueQuests.size,
                    headerColor = Color(0xFFDC2626)
                )
            }
            items(uiState.overdueQuests, key = { "overdue_${it.quest.id}" }) { qwl ->
                QuestWithListCard(
                    questWithList = qwl,
                    onComplete = { viewModel.completeQuest(qwl.quest) },
                    onPin = { viewModel.togglePin(qwl.quest) },
                    onDelete = { viewModel.promptDeleteQuest(qwl.quest) },
                    onEdit = { onEditQuest(qwl.quest) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }

        // ── Upcoming section ─────────────────────────────────────────────────
        if (uiState.upcomingQuests.isNotEmpty()) {
            item {
                SectionHeader(
                    emoji = "⏰",
                    title = "Upcoming",
                    count = uiState.upcomingQuests.size,
                    headerColor = QuestPurple
                )
            }
            items(uiState.upcomingQuests, key = { "upcoming_${it.quest.id}" }) { qwl ->
                QuestWithListCard(
                    questWithList = qwl,
                    onComplete = { viewModel.completeQuest(qwl.quest) },
                    onPin = { viewModel.togglePin(qwl.quest) },
                    onDelete = { viewModel.promptDeleteQuest(qwl.quest) },
                    onEdit = { onEditQuest(qwl.quest) }
                )
            }
        }

        // ── Empty state ──────────────────────────────────────────────────────
        if (!uiState.isLoading && uiState.overdueQuests.isEmpty() && uiState.upcomingQuests.isEmpty()) {
            item { EmptyActiveState() }
        }
    }
    } // end Scaffold
}

private fun completionMessage(event: CompletionEvent): String {
    return if (event.isRecurring) {
        val nextDate = event.nextDueDateMillis?.let {
            SimpleDateFormat("MMM d 'at' h:mm a", Locale.getDefault()).format(Date(it))
        }
        if (nextDate != null)
            "✓ Quest complete! +${event.xpEarned} XP  •  🔄 Next due $nextDate"
        else
            "✓ Quest complete! +${event.xpEarned} XP  •  🔄 Rescheduled"
    } else {
        "✓ Quest complete! +${event.xpEarned} XP ⭐"
    }
}

@Composable
private fun SectionHeader(
    emoji: String,
    title: String,
    count: Int,
    headerColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 18.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = headerColor
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(headerColor.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = headerColor
            )
        }
    }
}

@Composable
private fun QuestWithListCard(
    questWithList: QuestWithList,
    onComplete: () -> Unit,
    onPin: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val listColor = parseHexColor(questWithList.listColorHex)

    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
        // Board label
        Row(
            modifier = Modifier.padding(start = 4.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(listColor.copy(alpha = 0.15f))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${questWithList.listEmoji} ${questWithList.listName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = listColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        QuestCard(
            quest = questWithList.quest,
            onComplete = { onComplete() },
            onPin = { onPin() },
            onDelete = { onDelete() },
            onEdit = { onEdit() }
        )
    }
}

@Composable
private fun EmptyActiveState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🌟", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "All Clear, Hero!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = QuestPurple
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "No quests with due dates yet.\nAdd a due date to a quest to see it here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DeleteQuestDialog(
    questTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Quest?", fontWeight = FontWeight.Bold) },
        text = { Text("\"$questTitle\" will be permanently deleted.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun LevelUpDialog(newLevel: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🎉", fontSize = 48.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Level Up!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = QuestGold
                )
            }
        },
        text = {
            Text(
                text = "You reached Level $newLevel!\nYour legendary deeds grow greater!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = QuestPurple)
            ) {
                Text("Onward, Hero!", color = Color.White)
            }
        }
    )
}
