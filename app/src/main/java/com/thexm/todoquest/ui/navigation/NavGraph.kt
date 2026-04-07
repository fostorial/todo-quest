package com.thexm.todoquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thexm.todoquest.data.model.QuestList
import com.thexm.todoquest.ui.screens.active.ActiveScreen
import com.thexm.todoquest.ui.screens.createlist.CreateListScreen
import com.thexm.todoquest.ui.screens.createquest.CreateQuestScreen
import com.thexm.todoquest.ui.screens.home.HomeScreen
import com.thexm.todoquest.ui.screens.profile.ProfileScreen
import com.thexm.todoquest.ui.screens.questlist.QuestListScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Active : Screen("active")
    object Profile : Screen("profile")
    object QuestList : Screen("questlist/{listId}") {
        fun createRoute(listId: Long) = "questlist/$listId"
    }
    object CreateList : Screen("createlist?listId={listId}") {
        fun createRoute(listId: Long? = null) =
            if (listId != null) "createlist?listId=$listId" else "createlist"
    }
    object CreateQuest : Screen("createquest/{listId}?questId={questId}") {
        fun createRoute(listId: Long, questId: Long? = null) =
            if (questId != null) "createquest/$listId?questId=$questId"
            else "createquest/$listId"
    }
}

@Composable
fun QuestNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToList = { listId ->
                    navController.navigate(Screen.QuestList.createRoute(listId))
                },
                onCreateList = { navController.navigate(Screen.CreateList.createRoute()) },
                onEditList = { list ->
                    navController.navigate(Screen.CreateList.createRoute(list.id))
                }
            )
        }

        composable(Screen.Active.route) {
            ActiveScreen(
                onEditQuest = { quest ->
                    navController.navigate(Screen.CreateQuest.createRoute(quest.listId, quest.id))
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }

        composable(
            route = Screen.QuestList.route,
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { backStack ->
            val listId = backStack.arguments?.getLong("listId") ?: return@composable
            QuestListScreen(
                listId = listId,
                onBack = { navController.popBackStack() },
                onAddQuest = { id -> navController.navigate(Screen.CreateQuest.createRoute(id)) },
                onEditQuest = { quest ->
                    navController.navigate(Screen.CreateQuest.createRoute(quest.listId, quest.id))
                }
            )
        }

        composable(
            route = Screen.CreateList.route,
            arguments = listOf(
                navArgument("listId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            CreateListScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.CreateQuest.route,
            arguments = listOf(
                navArgument("listId") { type = NavType.LongType },
                navArgument("questId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            CreateQuestScreen(onBack = { navController.popBackStack() })
        }
    }
}
