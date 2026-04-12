package com.thexm.todoquest.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.ui.theme.*
import com.thexm.todoquest.util.LevelCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.snackbarEvents.collect { message ->
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    }

    if (uiState.showClassDialog) {
        ClassTreeDialog(
            selectedClassId = profile.selectedClassId,
            xpPerClass = uiState.xpPerClass,
            onSelectClass = viewModel::selectClass,
            onDismiss = viewModel::closeClassDialog
        )
    }

    if (uiState.showBackgroundsDialog) {
        BackgroundsDialog(
            profile = profile,
            xpPerClass = uiState.xpPerClass,
            selectedBackgroundId = profile.selectedBackgroundId,
            onSelect = viewModel::selectBackground,
            onDismiss = viewModel::closeBackgroundsDialog
        )
    }

    if (uiState.showTitlesDialog) {
        TitlesDialog(
            playerLevel = profile.level,
            selectedTitleId = profile.selectedTitleId,
            xpPerClass = uiState.xpPerClass,
            onSelectTitle = viewModel::selectTitle,
            onDismiss = viewModel::closeTitlesDialog
        )
    }

    if (uiState.showResetConfirm) {
        AlertDialog(
            onDismissRequest = viewModel::cancelReset,
            title = { Text("Reset Progress?", fontWeight = FontWeight.Bold) },
            text = {
                Text("This will reset your level, XP, quests completed and title back to the beginning. Your hero name will be kept.")
            },
            confirmButton = {
                TextButton(onClick = viewModel::confirmReset) {
                    Text("Reset", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelReset) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Hero Stats
            Text(
                "Hero Stats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = QuestPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(emoji = "⭐", label = "Total XP", value = "${profile.totalXP}", modifier = Modifier.weight(1f))
                StatCard(emoji = "🏆", label = "Quests Done", value = "${profile.totalQuestsCompleted}", modifier = Modifier.weight(1f))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(emoji = "⚔️", label = "Current Level", value = "Level ${profile.level}", modifier = Modifier.weight(1f))
                StatCard(
                    emoji = "💎",
                    label = "XP to Next",
                    value = "${LevelCalculator.xpRequiredForNextLevel(profile.level) - LevelCalculator.xpInCurrentLevel(profile.totalXP)}",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))
            Text(
                "Customisation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = QuestPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            ActionRow(
                emoji = uiState.selectedClass.emoji,
                title = "Hero Class",
                subtitle = uiState.selectedClass.displayName,
                onClick = viewModel::openClassDialog
            )

            ActionRow(
                emoji = "📜",
                title = "Title",
                subtitle = uiState.selectedTitle.displayName,
                onClick = viewModel::openTitlesDialog
            )

            ActionRow(
                emoji = "🖼️",
                title = "Banner Background",
                subtitle = uiState.selectedBackground.displayName,
                onClick = viewModel::openBackgroundsDialog
            )

            ActionRow(
                emoji = "💀",
                title = "Reset Progress",
                subtitle = "Wipe XP, level and title",
                onClick = viewModel::promptReset,
                destructive = true
            )

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            // Notification settings
            Text(
                "Notification Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = QuestPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            SettingRow(
                emoji = "🔔",
                title = "Quest Board Notification",
                subtitle = "Show active quests in notification bar",
                checked = profile.notificationEnabled,
                onCheckedChange = viewModel::toggleNotification
            )

            androidx.compose.animation.AnimatedVisibility(visible = profile.notificationEnabled) {
                SettingRow(
                    emoji = "📌",
                    title = "Persistent Notification",
                    subtitle = "Keep notification visible at all times",
                    checked = profile.notificationPersistent,
                    onCheckedChange = viewModel::togglePersistentNotification
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.align(Alignment.BottomCenter)
    )
    }
}

@Composable
private fun ActionRow(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    destructive: Boolean = false
) {
    val accentColor = if (destructive) MaterialTheme.colorScheme.error else QuestPurple
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (destructive) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = accentColor
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = accentColor.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun StatCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 22.sp)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SettingRow(
    emoji: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = QuestPurple,
                checkedTrackColor = QuestPurple.copy(alpha = 0.4f)
            )
        )
    }
}
