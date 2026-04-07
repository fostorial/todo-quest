package com.thexm.todoquest.ui.screens.createquest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.data.model.RecurrenceType
import com.thexm.todoquest.data.model.XPTier
import com.thexm.todoquest.ui.components.xpTierColor
import com.thexm.todoquest.ui.theme.QuestGold
import com.thexm.todoquest.ui.theme.QuestPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestScreen(
    onBack: () -> Unit,
    viewModel: CreateQuestViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.dueDateMillis
    )
    val timePickerState = rememberTimePickerState(
        initialHour = state.dueHour,
        initialMinute = state.dueMinute,
        is24Hour = false
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (state.isEditing) "Edit Quest" else "New Quest",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = QuestPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = { viewModel.save(onBack) },
                        enabled = !state.isSaving
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Title field
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::setTitle,
                label = { Text("Quest Name *") },
                leadingIcon = { Text("⚔️", fontSize = 18.sp) },
                isError = state.titleError != null,
                supportingText = { state.titleError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = QuestPurple,
                    focusedLabelColor = QuestPurple
                )
            )

            // Description field
            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::setDescription,
                label = { Text("Description (optional)") },
                leadingIcon = { Text("📜", fontSize = 18.sp) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = QuestPurple,
                    focusedLabelColor = QuestPurple
                )
            )

            // XP Tier selection
            SectionLabel(icon = "💎", label = "Quest Difficulty & XP Reward")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                XPTier.values().forEach { tier ->
                    XPTierChip(
                        tier = tier,
                        selected = state.xpTier == tier,
                        onClick = { viewModel.setXpTier(tier) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recurrence
            SectionLabel(icon = "🔄", label = "Recurrence")
            val recurrenceRows = listOf(
                listOf(RecurrenceType.NONE, RecurrenceType.DAILY, RecurrenceType.WEEKDAYS),
                listOf(RecurrenceType.WEEKLY, RecurrenceType.FORTNIGHTLY, RecurrenceType.MONTHLY)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                recurrenceRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        row.forEach { type ->
                            FilterChip(
                                selected = state.recurrenceType == type,
                                onClick = { viewModel.setRecurrence(type) },
                                label = {
                                    Text(
                                        text = "${type.emoji} ${type.displayName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = QuestPurple.copy(alpha = 0.15f),
                                    selectedLabelColor = QuestPurple
                                )
                            )
                        }
                    }
                }
            }

            // Due date
            SectionLabel(icon = "⏰", label = "Due Date & Time")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Switch(
                    checked = state.hasDueDate,
                    onCheckedChange = { viewModel.toggleDueDate() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = QuestPurple,
                        checkedTrackColor = QuestPurple.copy(alpha = 0.4f)
                    )
                )
                if (state.hasDueDate) {
                    // Date button
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = state.dueDateMillis?.let {
                                SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(it))
                            } ?: "Set date",
                            maxLines = 1,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    // Time button
                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                state.dueHour,
                                state.dueMinute
                            ),
                            maxLines = 1,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            // Pin toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📌", fontSize = 20.sp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            "Pin to Notification",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Show in Quest Board notification",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(
                    checked = state.isPinned,
                    onCheckedChange = { viewModel.togglePin() },
                    colors = SwitchDefaults.colors(checkedThumbColor = QuestPurple, checkedTrackColor = QuestPurple.copy(alpha = 0.4f))
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.save(onBack) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = !state.isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = QuestPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        if (state.isEditing) "Save Changes" else "Accept Quest!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setDueDate(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("OK", color = QuestPurple) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time picker dialog
    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                viewModel.setDueTime(timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⏰ Set Time",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = QuestPurple,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = onConfirm) { Text("OK", color = QuestPurple) }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(icon: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 16.sp)
        Spacer(Modifier.width(6.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun XPTierChip(
    tier: XPTier,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = xpTierColor(tier)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) color else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(tier.emoji, fontSize = 16.sp)
            Text(
                tier.displayName,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "+${tier.xpValue}",
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}
