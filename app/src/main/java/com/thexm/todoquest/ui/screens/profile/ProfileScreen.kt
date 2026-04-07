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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.ui.components.LevelBadge
import com.thexm.todoquest.ui.components.ProfileBackgroundCanvas
import com.thexm.todoquest.ui.components.XPProgressBar
import com.thexm.todoquest.ui.theme.*
import com.thexm.todoquest.util.LevelCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile

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
            selectedBackgroundId = profile.selectedBackgroundId,
            onSelect = viewModel::selectBackground,
            onDismiss = viewModel::closeBackgroundsDialog
        )
    }

    if (uiState.showTitlesDialog) {
        TitlesDialog(
            playerLevel = profile.level,
            selectedTitleId = profile.selectedTitleId,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero banner — selected background pattern with content overlaid
        Box(modifier = Modifier.fillMaxWidth()) {
            ProfileBackgroundCanvas(
                background = uiState.selectedBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
            // Readability scrim
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.Black.copy(alpha = 0.30f))
            )
            // Content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                LevelBadge(level = profile.level, size = 72.dp)
                Spacer(Modifier.height(12.dp))

                if (uiState.isEditingName) {
                    OutlinedTextField(
                        value = uiState.editName,
                        onValueChange = viewModel::setEditName,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        trailingIcon = {
                            Row {
                                IconButton(onClick = viewModel::saveHeroName) {
                                    Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
                                }
                                IconButton(onClick = viewModel::cancelEditName) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.White)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = profile.heroName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        IconButton(onClick = viewModel::startEditName, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit name",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                val titleColor = when (uiState.selectedTitle.xpTier) {
                    null          -> Color.White.copy(alpha = 0.85f)
                    XPTier.PALTRY -> TierPaltry
                    XPTier.SMALL  -> TierSmall
                    XPTier.MEDIUM -> Color(0xFF93C5FD)
                    XPTier.LARGE  -> QuestPurpleLight
                    XPTier.EPIC   -> QuestGoldLight
                }
                Text(
                    text = uiState.selectedTitle.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = titleColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { viewModel.openTitlesDialog() }
                )

                Spacer(Modifier.height(16.dp))

                XPProgressBar(
                    totalXP = profile.totalXP,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Stats grid
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Text(
                "Hero Stats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = QuestPurple,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    emoji = "⭐",
                    label = "Total XP",
                    value = "${profile.totalXP}",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    emoji = "🏆",
                    label = "Quests Done",
                    value = "${profile.totalQuestsCompleted}",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    emoji = "⚔️",
                    label = "Current Level",
                    value = "Level ${profile.level}",
                    modifier = Modifier.weight(1f)
                )
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

            // Titles section
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
}

@Composable
private fun StatCard(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = QuestPurple
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
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
