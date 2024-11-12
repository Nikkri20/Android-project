import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.android_project.R
import com.example.android_project.data.model.Character

@Composable
fun CharacterDetailScreen(navController: NavController, character: Character?) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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

        if (character != null) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.back_button),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() })

                    Image(painter = painterResource(id = R.drawable.share),
                        contentDescription = "Share",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { shareCharacterInfo(context, character) })
                }

                AsyncImage(
                    model = character.imageUrl,
                    contentDescription = "${character.name} Image",
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xB3000000), Color(0x33000000)),
                                startY = 0f,
                                endY = 1000f
                            ), shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = character.name,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        character.details.forEach { (key, value) ->
                            TextRow(label = "$key:", value = value)
                        }
                    }
                }
            }
        } else {
            Text(
                text = "Loading...",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TextRow(label: String, value: String?) {
    Row(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD0D0D0),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = value ?: "Unknown", color = Color.White
        )
    }
}

fun shareCharacterInfo(context: Context, character: Character) {
    val detailsText = character.details.entries.joinToString("\n") { (key, value) ->
        "$key: ${value.ifBlank { "Unknown" }}"
    }

    val shareText = """
        Name: ${character.name}
        $detailsText
    """.trimIndent()

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

    context.startActivity(Intent.createChooser(intent, "Share Character Info"))
}


