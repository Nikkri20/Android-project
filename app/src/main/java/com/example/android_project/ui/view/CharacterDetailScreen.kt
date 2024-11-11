package com.example.android_project.ui.view

import android.content.Context
import android.content.Intent
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
import com.example.android_project.R
import com.example.android_project.data.model.Character

@Composable
fun CharacterDetailScreen(navController: NavController, character: Character) {
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
                Image(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )

                Image(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "Share",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { shareCharacterInfo(context, character) }
                )
            }

            Image(
                painter = painterResource(id = character.imageResId),
                contentDescription = "${character.name} Image",
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
                        ),
                        shape = RoundedCornerShape(8.dp)
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

                    TextRow(label = "Gender:", value = character.gender)
                    TextRow(label = "Age:", value = character.age?.toString())
                    TextRow(label = "Hair Color:", value = character.hairColor)
                    TextRow(label = "Occupation:", value = character.occupation)
                    TextRow(label = "Grade:", value = character.grade)
                    TextRow(label = "Aliases:", value = character.aliases)
                    TextRow(label = "Religion:", value = character.religion)
                    TextRow(label = "Mother:", value = character.mother)
                    TextRow(label = "Father:", value = character.father)
                    TextRow(label = "Half-brother:", value = character.halfBrother)
                    TextRow(label = "Grandmother:", value = character.grandmother)
                    TextRow(label = "Grandfather:", value = character.grandfather)
                    TextRow(label = "Aunt:", value = character.aunt)
                    TextRow(label = "Uncle:", value = character.uncle)
                    TextRow(label = "Cousin:", value = character.cousin)
                    TextRow(label = "Voiced by:", value = character.voicedBy)
                    TextRow(label = "First Appearance:", value = character.firstAppearance)
                }
            }
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
            text = value ?: "Unknown",
            color = Color.White
        )
    }
}

fun shareCharacterInfo(context: Context, character: Character) {
    val shareText = """
        Name: ${character.name}
        Gender: ${character.gender ?: "Unknown"}
        Age: ${character.age ?: "Unknown"}
        Hair Color: ${character.hairColor ?: "Unknown"}
        Occupation: ${character.occupation ?: "Unknown"}
        Grade: ${character.grade ?: "Unknown"}
        Aliases: ${character.aliases ?: "Unknown"}
        Religion: ${character.religion ?: "Unknown"}
        Mother: ${character.mother ?: "Unknown"}
        Father: ${character.father ?: "Unknown"}
        Half-brother: ${character.halfBrother ?: "Unknown"}
        Grandmother: ${character.grandmother ?: "Unknown"}
        Grandfather: ${character.grandfather ?: "Unknown"}
        Aunt: ${character.aunt ?: "Unknown"}
        Uncle: ${character.uncle ?: "Unknown"}
        Cousin: ${character.cousin ?: "Unknown"}
        Voiced by: ${character.voicedBy ?: "Unknown"}
        First Appearance: ${character.firstAppearance ?: "Unknown"}
    """.trimIndent()

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

    context.startActivity(Intent.createChooser(intent, "Share Character Info"))
}
