package com.example.android_project.data.network

import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.Call

interface CharacterApiService {
    @GET
    fun getCharacterHtml(@Url url: String): Call<String>
}