package com.example.android_project.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String? = null,
    val group: String,
    val details: String = ""
)