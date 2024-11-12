import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android_project.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CharacterListScreen(
    navController: NavController, viewModel: CharacterViewModel = viewModel()
) {
    val characters = viewModel.characters.collectAsStateWithLifecycle()
    val loading = viewModel.loading.collectAsStateWithLifecycle()
    val error = viewModel.error.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
        )

        when {
            loading.value -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            error.value != null -> Text(
                text = "Error: ${error.value}",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = listState
            ) {
                items(characters.value) { character ->
                    CharacterListItem(character) {
                        navController.navigate("detail_screen/${character.id.toString()}")
                    }
                }
            }
        }
    }
}
