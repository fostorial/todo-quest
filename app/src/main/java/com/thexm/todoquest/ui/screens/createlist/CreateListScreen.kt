package com.thexm.todoquest.ui.screens.createlist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thexm.todoquest.ui.components.parseHexColor
import com.thexm.todoquest.ui.theme.QuestPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListScreen(
    onBack: () -> Unit,
    viewModel: CreateListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val unlockedClassEmojis by viewModel.unlockedClassEmojis.collectAsState()
    val availableEmojis = remember(unlockedClassEmojis) { PRESET_EMOJIS + unlockedClassEmojis }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (state.isEditing) "Edit Quest Board" else "New Quest Board",
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
                    IconButton(onClick = { viewModel.save(onBack) }, enabled = !state.isSaving) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Preview card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(parseHexColor(state.colorHex).copy(alpha = 0.15f))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(parseHexColor(state.colorHex).copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(state.emoji, fontSize = 26.sp)
                    }
                    Spacer(Modifier.width(14.dp))
                    Text(
                        text = state.name.ifBlank { "My Quest Board" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = parseHexColor(state.colorHex)
                    )
                }
            }

            // Name field
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::setName,
                label = { Text("Board Name *") },
                isError = state.nameError != null,
                supportingText = { state.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = QuestPurple,
                    focusedLabelColor = QuestPurple
                )
            )

            // Emoji picker — 3 rows of 8
            Text("Choose an Icon", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                modifier = Modifier.heightIn(max = 220.dp),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(availableEmojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                if (state.emoji == emoji) QuestPurple.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                if (state.emoji == emoji) 2.dp else 0.dp,
                                QuestPurple,
                                CircleShape
                            )
                            .clickable { viewModel.setEmoji(emoji) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 20.sp)
                    }
                }
            }

            // Colour picker — 2 rows of 8
            Text("Choose a Colour", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                modifier = Modifier.heightIn(max = 100.dp),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(PRESET_COLORS) { hex ->
                    val color = parseHexColor(hex)
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                if (state.colorHex == hex) 3.dp else 0.dp,
                                Color.White,
                                CircleShape
                            )
                            .border(
                                if (state.colorHex == hex) 5.dp else 0.dp,
                                color,
                                CircleShape
                            )
                            .clickable { viewModel.setColor(hex) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.colorHex == hex) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.save(onBack) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = !state.isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = QuestPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    if (state.isEditing) "Save Changes" else "Create Quest Board!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
