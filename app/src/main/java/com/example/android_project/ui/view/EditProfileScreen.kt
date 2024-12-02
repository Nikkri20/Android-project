package com.example.android_project.ui.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.android_project.R
import com.example.android_project.data.store.ProfileDataStore
import com.example.android_project.receiver.AlarmReceiver
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.*

@Composable
fun EditProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val profileDataStore = ProfileDataStore(context)

    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var isProfileLoaded by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        profileDataStore.profileFlow.collect { loadedProfile ->
            name = loadedProfile.name
            role = loadedProfile.role
            avatarUri = loadedProfile.avatarUri ?: ""
            resumeUrl = loadedProfile.resumeUrl ?: ""
            isProfileLoaded = true
        }
    }

    val getImageFromGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                avatarUri = saveImageToInternalStorage(context, it)
            }
        }

    val takePhotoFromCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                avatarUri = saveBitmapToInternalStorage(context, it)
            }
        }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (isProfileLoaded) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_button_two),
                            contentDescription = "Назад"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Редактирование профиля",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .clickable { showImageSourceDialog = true }
                ) {
                    if (avatarUri.isNotBlank()) {
                        Image(
                            painter = rememberImagePainter(avatarUri),
                            contentDescription = "Profile Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Default Profile Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Роль") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = resumeUrl,
                    onValueChange = { resumeUrl = it },
                    label = { Text("Ссылка на резюме") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = reminderTime,
                    onValueChange = { reminderTime = it },
                    label = { Text("Время любимой пары") },
                    trailingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    reminderTime = String.format("%02d:%02d", hour, minute)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_notification),
                                contentDescription = "Выбрать время"
                            )
                        }
                    },
                    isError = reminderTime.isNotEmpty() && !isValidTimeFormat(reminderTime),
                    modifier = Modifier.fillMaxWidth()
                )
                if (reminderTime.isNotEmpty() && !isValidTimeFormat(reminderTime)) {
                    Text(
                        text = "Введите корректное время (HH:mm)",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!isValidTimeFormat(reminderTime)) {
                            Toast.makeText(
                                context,
                                "Введите корректное время в формате HH:mm",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        try {
                            coroutineScope.launch {
                                profileDataStore.saveProfile(
                                    name = name,
                                    avatarUri = avatarUri.takeIf { it.isNotBlank() },
                                    resumeUrl = resumeUrl,
                                    role = role
                                )
                                setAlarm(context, reminderTime, name)
                            }
                            navController.popBackStack()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                context,
                                "Ошибка при сохранении: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }

            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}


private fun isValidTimeFormat(time: String): Boolean {
    val regex = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
    return time.matches(regex)
}

fun setAlarm(context: Context, time: String, name: String) {
    try {
        val (hour, minute) = time.split(":").map { it.toIntOrNull() ?: 0 }
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("profile_name", name)
            putExtra("notification_id", System.currentTimeMillis().toInt())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            requestExactAlarmPermission(context)
            return
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Log.d("setAlarm", "Будильник установлен на $time для $name")
    } catch (e: Exception) {
        Log.e("setAlarm", "Ошибка установки будильника: ${e.message}")
    }
}

private fun saveImageToInternalStorage(context: Context, imageUri: Uri): String {
    val contentResolver = context.contentResolver
    val file = File(context.filesDir, "profile_avatar.jpg")
    contentResolver.openInputStream(imageUri)?.use { input ->
        FileOutputStream(file).use { output -> input.copyTo(output) }
    }
    return file.absolutePath
}

private fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): String {
    val file = File(context.filesDir, "profile_avatar_camera.jpg")
    FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) }
    return file.absolutePath
}


private fun requestExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}


