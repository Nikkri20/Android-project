package com.example.android_project.data.model

data class Character(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val group: String = "",
    val details: Map<String, String> = emptyMap(),
    var isFavorite: Boolean = false
)
