package com.example.android_project.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_project.viewmodel.FilterSettingsViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun FilterSettingsScreen(
    viewModel: FilterSettingsViewModel = viewModel(),
    onApplyFilters: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val gender = viewModel.gender.collectAsState(initial = "")
    val minAge = viewModel.minAge.collectAsState(initial = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Настройки фильтров", style = MaterialTheme.typography.headlineSmall)

        // Фильтр по полу
        TextField(
            value = gender.value,
            onValueChange = { viewModel.setGender(it) },
            label = { Text("Пол (Male/Female)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Фильтр по минимальному возрасту
        TextField(
            value = if (minAge.value > 0) minAge.value.toString() else "",
            onValueChange = { viewModel.setMinAge(it.toIntOrNull() ?: 0) },
            label = { Text("Минимальный возраст") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Применить"
        Button(
            onClick = {
                scope.launch {
                    viewModel.saveFilters(context)
                    onApplyFilters()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Применить")
        }
    }
}
