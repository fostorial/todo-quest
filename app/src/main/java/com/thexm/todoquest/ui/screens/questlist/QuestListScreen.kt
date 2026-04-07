package com.thexm.todoquest.ui.screens.questlist

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.data.model.Quest
import com.thexm.todoquest.ui.components.QuestCard
import com.thexm.todoquest.ui.components.parseHexColor
import com.thexm.todoquest.ui.theme.QuestGold
import com.thexm.todoquest.ui.theme.QuestPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestListScreen(
    listId: Long,
    onBack: () -> Unit,
    onAddQuest: (Long) -> Unit,
    onEditQuest: (Quest) -> Unit,
    viewModel: QuestListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Level-up dialog
    uiState.levelUpEvent?.let { newLevel ->
        LevelUpDialog(newLevel = newLevel, onDismiss = viewModel::dismissLevelUp)
    }

    // Delete confirmation dialog
    uiState.pendingDeleteQuest?.let { quest ->
        DeleteQuestDialog(
            questTitle = quest.title,
            onConfirm = viewModel::confirmDeleteQuest,
            onDismiss = viewModel::cancelDeleteQuest
        )
    }

    // Completion snackbar
    uiState.completionEvent?.let { event ->
        LaunchedEffect(event) {
            val message = completionMessage(event)
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
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
        },
        topBar = {
            uiState.questList?.let { list ->
                val listColor = parseHexColor(list.colorHex)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(listColor, listColor.copy(alpha = 0.6f)))
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "${list.emoji} ${list.name}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        // Clear completed button
                        if (uiState.completedQuests.isNotEmpty()) {
                            IconButton(onClick = viewModel::clearCompleted) {
                                Icon(
                                    Icons.Default.CleaningServices,
                                    contentDescription = "Clear completed",
                                    tint = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddQuest(listId) },
                containerColor = QuestPurple,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Quest")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = QuestPurple)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Active quests
            if (uiState.activeQuests.isEmpty()) {
                item { EmptyQuestsMessage() }
            } else {
                items(uiState.activeQuests, key = { it.id }) { quest ->
                    QuestCard(
                        quest = quest,
                        onComplete = viewModel::completeQuest,
                        onPin = viewModel::togglePin,
                        onDelete = viewModel::promptDeleteQuest,
                        onEdit = onEditQuest,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            // Completed section toggle
            if (uiState.completedQuests.isNotEmpty()) {
                item {
                    TextButton(
                        onClick = viewModel::toggleShowCompleted,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            if (uiState.showCompleted) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${uiState.completedQuests.size} Completed Quest${if (uiState.completedQuests.size != 1) "s" else ""}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                if (uiState.showCompleted) {
                    items(uiState.completedQuests, key = { "c_${it.id}" }) { quest ->
                        QuestCard(
                            quest = quest,
                            onComplete = {},
                            onPin = {},
                            onDelete = viewModel::promptDeleteQuest,
                            onEdit = {},
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
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
private fun EmptyQuestsMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🗡️", fontSize = 56.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No Quests Yet!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = QuestPurple
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Tap the + button to add your first quest",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LevelUpDialog(newLevel: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
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
