package com.thexm.todoquest.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = QuestPurple,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = QuestSurfaceVariant,
    onPrimaryContainer = QuestPurpleDark,
    secondary = QuestGold,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFFEF3C7),
    onSecondaryContainer = QuestGoldDark,
    tertiary = QuestEmerald,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    background = QuestBackground,
    onBackground = androidx.compose.ui.graphics.Color(0xFF1A0029),
    surface = QuestSurface,
    onSurface = androidx.compose.ui.graphics.Color(0xFF1A0029),
    surfaceVariant = QuestSurfaceVariant,
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF4A3060),
    outline = QuestPurpleLight
)

private val DarkColorScheme = darkColorScheme(
    primary = QuestPurpleDarkMode,
    onPrimary = androidx.compose.ui.graphics.Color(0xFF2D0068),
    primaryContainer = QuestPurpleDark,
    onPrimaryContainer = QuestPurpleLight,
    secondary = QuestGoldDarkMode,
    onSecondary = androidx.compose.ui.graphics.Color(0xFF3D2000),
    secondaryContainer = QuestGoldDark,
    onSecondaryContainer = QuestGoldLight,
    tertiary = QuestEmeraldLight,
    onTertiary = androidx.compose.ui.graphics.Color(0xFF003822),
    background = QuestBackgroundDark,
    onBackground = androidx.compose.ui.graphics.Color(0xFFEDE0FF),
    surface = QuestSurfaceDark,
    onSurface = androidx.compose.ui.graphics.Color(0xFFEDE0FF),
    surfaceVariant = QuestSurfaceVariantDark,
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFCBB8E8)
)

@Composable
fun ToDoQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuestTypography,
        content = content
    )
}
