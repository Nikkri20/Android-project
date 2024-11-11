package com.example.android_project.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android_project.data.repository.CharacterRepository
import com.example.android_project.ui.view.CharacterDetailScreen
import com.example.android_project.ui.view.CharacterListScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "list_screen",
        modifier = modifier
    ) {
        // Экран списка персонажей
        composable("list_screen") {
            CharacterListScreen(navController = navController)
        }
        // Экран деталей персонажа
        composable("detail_screen/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            val character = CharacterRepository().getCharacters().find { it.id == characterId }
            if (character != null) {
                CharacterDetailScreen(navController = navController, character = character)
            } else {
                Text("Character not found")
            }
        }
    }
}
