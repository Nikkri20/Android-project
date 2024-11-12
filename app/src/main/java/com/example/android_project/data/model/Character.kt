package com.example.android_project.data.model

data class Character(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val details: Map<String, String> = emptyMap()
)
