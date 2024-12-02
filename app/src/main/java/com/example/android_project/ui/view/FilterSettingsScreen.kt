package com.example.android_project.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_project.R
import com.example.android_project.utils.FilterBadgeCache
import com.example.android_project.ui.viewmodel.CharacterViewModel
import com.example.android_project.viewmodel.FilterSettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun FilterSettingsScreen(
    viewModel: FilterSettingsViewModel = viewModel(),
    characterViewModel: CharacterViewModel,
    filterBadgeCache: FilterBadgeCache,
    onApplyFilters: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val name by viewModel.name.collectAsStateWithLifecycle()
    val group by viewModel.group.collectAsStateWithLifecycle()
    val availableGroups by characterViewModel.availableGroups.collectAsStateWithLifecycle()
    val isLoading by characterViewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFilters(context)
    }

    LaunchedEffect(availableGroups) {
        viewModel.setAvailableGroups(availableGroups)
    }

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
        )

        Image(
            painter = painterResource(id = R.drawable.back_button),
            contentDescription = "Back",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(32.dp)
                .clickable { onBack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = name,
                onValueChange = { input ->
                    if (input.all { it.isLetter() || it.isWhitespace() }) {
                        viewModel.setName(input)
                    }
                },
                label = { Text("Имя персонажа") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.8f))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Группа",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading...",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(availableGroups) { groupName ->
                        GroupOption(
                            selected = group == groupName,
                            label = groupName,
                            onClick = { viewModel.setGroup(groupName) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveFilters(context)
                            characterViewModel.applyFilters(name = name, group = group)
                            onApplyFilters()
                            filterBadgeCache.setShowBadge(!viewModel.isFilterDefault())
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = "Готово",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Button(
                    onClick = {
                        viewModel.setName("")
                        viewModel.setGroup("")
                        filterBadgeCache.setShowBadge(false)
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Gray.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Text(text = "Сбросить", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun GroupOption(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) Color.White else Color.Gray.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.Black else Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


