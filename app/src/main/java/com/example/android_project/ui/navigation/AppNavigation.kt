package com.example.android_project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.composable
import com.example.android_project.data.local.DatabaseProvider
import com.example.android_project.data.repository.CharacterRepository
import com.example.android_project.ui.screens.EditProfileScreen
import com.example.android_project.ui.screens.ProfileScreen
import com.example.android_project.ui.view.CharacterDetailScreen
import com.example.android_project.ui.view.CharacterListScreen
import com.example.android_project.ui.view.FavoriteScreen
import com.example.android_project.ui.view.FilterSettingsScreen
import com.example.android_project.utils.FilterBadgeCache
import com.example.android_project.ui.viewmodel.CharacterViewModel
import com.example.android_project.viewmodel.FilterSettingsViewModel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val database = DatabaseProvider.getDatabase(context)
    val repository = remember { CharacterRepository(database.favoriteDao()) }
    val filterSettingsViewModel = remember { FilterSettingsViewModel() }
    val filterBadgeCache = remember { FilterBadgeCache() }
    val characterViewModel = remember {
        CharacterViewModel(repository, context, filterBadgeCache)
    }

    NavHost(
        navController = navController,
        startDestination = "list_screen",
        modifier = modifier
    ) {
        composable("list_screen") {
            CharacterListScreen(
                navController = navController,
                viewModel = characterViewModel,
                filterBadgeCache = filterBadgeCache
            )
        }

        composable("detail_screen/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            LaunchedEffect(characterId) {
                characterId?.let { characterViewModel.fetchCharacterDetails(it) }
            }
            val character = characterViewModel.characterDetails.collectAsStateWithLifecycle().value
            CharacterDetailScreen(
                navController = navController,
                character = character
            )
        }

        composable("filter_settings") {
            FilterSettingsScreen(
                viewModel = filterSettingsViewModel,
                characterViewModel = characterViewModel,
                filterBadgeCache = filterBadgeCache,
                onApplyFilters = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable("favorite_screen") {
            FavoriteScreen(
                viewModel = characterViewModel,
                navController = navController
            )
        }

        composable("profile_screen") {
            ProfileScreen(navController = navController)
        }

        // Экран редактирования профиля
        composable("edit_profile_screen") {
            EditProfileScreen(navController = navController)
        }
    }
}



