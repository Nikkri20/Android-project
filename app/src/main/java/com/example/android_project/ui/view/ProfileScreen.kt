package com.example.android_project.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.android_project.R
import com.example.android_project.data.store.ProfileDataStore
import com.example.android_project.data.model.Profile

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val profileDataStore = ProfileDataStore(context)

    var profile by remember {
        mutableStateOf(Profile(id = 0, name = "", role = "", avatarUri = null, resumeUrl = null))
    }

    LaunchedEffect(Unit) {
        profileDataStore.profileFlow.collect { loadedProfile ->
            profile = loadedProfile
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { navController.navigate("edit_profile_screen") },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Редактировать профиль"
                )
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                if (!profile.avatarUri.isNullOrEmpty()) {
                    Image(
                        painter = rememberImagePainter(profile.avatarUri),
                        contentDescription = "Профиль",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Дефолт профиль",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = profile.name.ifEmpty { "Не указано" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = profile.role.ifEmpty { "Не указано" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!profile.resumeUrl.isNullOrEmpty()) {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.resumeUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Не удалось открыть ссылку", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    Text(text = "Резюме", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}






