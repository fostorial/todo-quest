package com.thexm.todoquest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.thexm.todoquest.ui.components.ProfileHeaderBar
import com.thexm.todoquest.ui.navigation.QuestNavHost
import com.thexm.todoquest.ui.navigation.Screen
import com.thexm.todoquest.ui.theme.QuestGold
import com.thexm.todoquest.ui.theme.QuestPurple
import com.thexm.todoquest.ui.theme.ToDoQuestTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* granted or denied — no blocking required */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        setContent {
            ToDoQuestTheme {
                QuestApp()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(Screen.Home.route, Screen.Active.route, Screen.Profile.route)
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        topBar = {
            ProfileHeaderBar(
                onTap = {
                    if (currentRoute != Screen.Profile.route) {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    val navColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = QuestPurple,
                        selectedTextColor = QuestPurple,
                        indicatorColor = QuestPurple.copy(alpha = 0.12f)
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Text("🏰", fontSize = androidx.compose.ui.unit.TextUnit.Unspecified) },
                        label = { Text("Quests", fontWeight = FontWeight.SemiBold) },
                        colors = navColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Active.route,
                        onClick = {
                            navController.navigate(Screen.Active.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Text("⏳", fontSize = androidx.compose.ui.unit.TextUnit.Unspecified) },
                        label = { Text("Active", fontWeight = FontWeight.SemiBold) },
                        colors = navColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile.route,
                        onClick = {
                            navController.navigate(Screen.Profile.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Text("⚔️", fontSize = androidx.compose.ui.unit.TextUnit.Unspecified) },
                        label = { Text("Hero", fontWeight = FontWeight.SemiBold) },
                        colors = navColors
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            QuestNavHost(navController = navController)
        }
    }
}
