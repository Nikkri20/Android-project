import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController, startDestination = "list_screen", modifier = modifier
    ) {
        composable("list_screen") {
            val viewModel: CharacterViewModel = viewModel()
            CharacterListScreen(navController = navController, viewModel = viewModel)
        }

        composable("detail_screen/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            val viewModel: CharacterViewModel = viewModel()

            LaunchedEffect(characterId) {
                if (characterId != null) {
                    viewModel.fetchCharacterDetails(characterId)
                }
            }

            val character = viewModel.characterDetails.collectAsStateWithLifecycle().value
            CharacterDetailScreen(
                navController = navController, character = character
            )
        }
    }
}


