package com.example.android_project.data.repository

import com.example.android_project.data.local.FavoriteDao
import com.example.android_project.data.local.FavoriteEntity
import com.example.android_project.data.model.Character
import com.example.android_project.data.network.CharacterApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class CharacterRepository(private val favoriteDao: FavoriteDao) {

    private val apiService: CharacterApiService = Retrofit.Builder()
        .baseUrl("https://prod-southpark-cc-com.webplex.viacom.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(CharacterApiService::class.java)

    suspend fun getCharacterList(): List<Character> = withContext(Dispatchers.IO) {
        val response = apiService.getCharacterHtml("wiki/List_of_Characters").execute()
        val characters = mutableListOf<Character>()

        if (response.isSuccessful) {
            response.body()?.let { html ->
                val document = Jsoup.parse(html)
                val elements = document.select("h2, h3, div.character")

                var currentGroup = ""
                for (element in elements) {
                    when {
                        element.tagName() in listOf("h2", "h3") -> {
                            currentGroup = element.text()
                        }
                        element.hasClass("character") -> {
                            val name = element.select("a").text()
                            val imageUrl = element.select("img").attr("src")
                            val absoluteImageUrl =
                                if (imageUrl.startsWith("http")) imageUrl else "https://prod-southpark-cc-com.webplex.viacom.com/$imageUrl"

                            characters.add(
                                Character(
                                    id = name,
                                    name = name,
                                    imageUrl = absoluteImageUrl,
                                    group = currentGroup
                                )
                            )
                        }
                    }
                }
            }
        }
        characters
    }

    suspend fun getCharacterDetails(name: String): Character? = withContext(Dispatchers.IO) {
        try {
            val url = "https://prod-southpark-cc-com.webplex.viacom.com/w/index.php/$name"
            val response = apiService.getCharacterHtml(url).execute()

            if (response.isSuccessful) {
                response.body()?.let { html ->
                    val document = Jsoup.parse(html)
                    val rows = document.select(".table tr")

                    val detailsMap = mutableMapOf<String, String>()
                    for (row in rows) {
                        val key = row.select(".key").text()
                        val value = row.select(".value").text()
                        if (key.isNotBlank() && value.isNotBlank()) {
                            detailsMap[key] = value
                        }
                    }

                    val imageUrl = document.select(".image img").attr("src")

                    favoriteDao.insertFavorite(
                        FavoriteEntity(
                            id = name,
                            name = name,
                            imageUrl = imageUrl,
                            group = "",
                            details = detailsMap.entries.joinToString(";") { "${it.key}:${it.value}" }
                        )
                    )

                    return@withContext Character(
                        id = name,
                        name = name,
                        imageUrl = imageUrl,
                        details = detailsMap
                    )
                }
            }
            null
        } catch (e: Exception) {
            val cachedFavorite = favoriteDao.getFavoriteById(name)
            cachedFavorite?.let { favorite ->
                Character(
                    id = favorite.id,
                    name = favorite.name,
                    imageUrl = favorite.imageUrl,
                    group = favorite.group,
                    details = favorite.details.split(";").associate {
                        val (key, value) = it.split(":")
                        key to value
                    },
                    isFavorite = true
                )
            }
        }
    }

    suspend fun addFavorite(character: Character) {
        favoriteDao.insertFavorite(
            FavoriteEntity(
                id = character.id,
                name = character.name,
                imageUrl = character.imageUrl,
                group = character.group
            )
        )
    }

    suspend fun removeFavorite(character: Character) {
        favoriteDao.deleteFavorite(
            FavoriteEntity(
                id = character.id,
                name = character.name,
                imageUrl = character.imageUrl,
                group = character.group
            )
        )
    }

    fun getFavorites(): Flow<List<Character>> {
        return favoriteDao.getFavorites().map { favoriteList ->
            favoriteList.map { entity ->
                Character(
                    id = entity.id,
                    name = entity.name,
                    imageUrl = entity.imageUrl,
                    group = entity.group,
                    isFavorite = true
                )
            }
        }
    }
}

