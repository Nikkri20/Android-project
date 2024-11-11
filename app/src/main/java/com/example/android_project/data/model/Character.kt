package com.example.android_project.data.model

import androidx.annotation.DrawableRes

data class Character(
    val id: String,
    val name: String,
    val gender: String?,
    val age: Int?,
    val hairColor: String?,
    val occupation: String?,
    val grade: String?,
    val aliases: String?,
    val religion: String?,
    val mother: String?,
    val father: String?,
    val halfBrother: String?,
    val grandmother: String?,
    val grandfather: String?,
    val aunt: String?,
    val uncle: String?,
    val cousin: String?,
    val voicedBy: String?,
    val firstAppearance: String?,
    @DrawableRes val imageResId: Int
)
