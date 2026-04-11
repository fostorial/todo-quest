package com.thexm.todoquest.ui.screens.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.data.model.QuestList
import com.thexm.todoquest.ui.components.QuestListCard
import com.thexm.todoquest.ui.theme.QuestGold
import com.thexm.todoquest.ui.theme.QuestPurple
import com.thexm.todoquest.ui.theme.QuestPurpleLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToList: (Long) -> Unit,
    onCreateList: () -> Unit,
    onEditList: (QuestList) -> Unit,
    pendingImportUri: Uri? = null,
    onImportConsumed: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val activeCountPerList by viewModel.activeCountPerList.collectAsState()
    val importResult by viewModel.importResult.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf<QuestList?>(null) }

    // Trigger import whenever a new URI arrives
    LaunchedEffect(pendingImportUri) {
        if (pendingImportUri != null) {
            onImportConsumed()
            viewModel.importBoard(context, pendingImportUri)
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateList,
                containerColor = QuestPurple,
                contentColor = androidx.compose.ui.graphics.Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("New Quest Board", fontWeight = FontWeight.SemiBold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                // Header banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(QuestPurple, QuestGold))
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⚔️",
                                fontSize = 28.sp
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Quest Boards",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                        if (uiState.totalActiveQuests > 0) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "${uiState.totalActiveQuests} quest${if (uiState.totalActiveQuests != 1) "s" else ""} await your glory!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.3f),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(64.dp)
                    )
                }
            }

            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = QuestPurple)
                    }
                }
            } else if (uiState.lists.isEmpty()) {
                item { EmptyQuestBoards() }
            } else {
                items(uiState.lists, key = { it.id }) { questList ->
                    QuestListCard(
                        questList = questList,
                        activeQuestCount = activeCountPerList[questList.id] ?: 0,
                        onClick = { onNavigateToList(questList.id) },
                        onEdit = { onEditList(questList) },
                        onDelete = { showDeleteConfirm = questList },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

    showDeleteConfirm?.let { list ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Delete \"${list.name}\"?") },
            text = { Text("All quests in this board will be permanently deleted. This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteList(list)
                        showDeleteConfirm = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) { Text("Cancel") }
            }
        )
    }

    // Import result dialog
    importResult?.let { result ->
        when (result) {
            is ImportResult.Success -> AlertDialog(
                onDismissRequest = { viewModel.clearImportResult() },
                title = { Text(if (result.wasUpdate) "📜 Board Updated!" else "📜 Board Received!") },
                text = {
                    Text(if (result.wasUpdate)
                        "\"${result.boardName}\" has been updated. Your completed quests are preserved."
                    else
                        "\"${result.boardName}\" has been added to your quest boards.")
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearImportResult(); onNavigateToList(result.listId) }) {
                        Text("Open Board", color = QuestPurple, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.clearImportResult() }) { Text("Dismiss") }
                }
            )
            is ImportResult.Error -> AlertDialog(
                onDismissRequest = { viewModel.clearImportResult() },
                title = { Text("Import Failed") },
                text = { Text(result.message) },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearImportResult() }) { Text("OK") }
                }
            )
        }
    }
}

@Composable
private fun EmptyQuestBoards() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🏰", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No Quest Boards Yet!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = QuestPurple
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Create your first quest board to begin your adventure!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
