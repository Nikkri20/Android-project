package com.example.android_project.data.repository

import com.example.android_project.data.model.Character
import com.example.android_project.data.network.CharacterApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class CharacterRepository {
    private val apiService: CharacterApiService

    init {
        val retrofit =
            Retrofit.Builder().baseUrl("https://prod-southpark-cc-com.webplex.viacom.com/")
                .addConverterFactory(ScalarsConverterFactory.create()).build()

        apiService = retrofit.create(CharacterApiService::class.java)
    }

    suspend fun getCharacterList(): List<Character> = withContext(Dispatchers.IO) {
        val response = apiService.getCharacterHtml("wiki/List_of_Characters").execute()
        val characters = mutableListOf<Character>()

        if (response.isSuccessful) {
            response.body()?.let { html ->
                val document = Jsoup.parse(html)
                val elements = document.select("div.character")

                for (element in elements) {
                    val name = element.select("a").text()
                    val imageUrl = element.select("img").attr("src")
                    val absoluteImageUrl =
                        if (imageUrl.startsWith("http")) imageUrl else "https://prod-southpark-cc-com.webplex.viacom.com/$imageUrl"

                    characters.add(
                        Character(
                            id = name, name = name, imageUrl = absoluteImageUrl
                        )
                    )
                }
            }
        }

        characters
    }


    suspend fun getCharacterDetails(name: String): Character? = withContext(Dispatchers.IO) {
        val url = "https://prod-southpark-cc-com.webplex.viacom.com/w/index.php/$name"
        val response = apiService.getCharacterHtml(url).execute()

        if (response.isSuccessful) {
            response.body()?.let { html ->
                val document: Document = Jsoup.parse(html)
                val rows: Elements = document.select(".table tr")

                val detailsMap = mutableMapOf<String, String>()

                for (row in rows) {
                    val key = row.select(".key").text()
                    val value = row.select(".value").text()
                    if (key.isNotBlank() && value.isNotBlank()) {
                        detailsMap[key] = value
                    }
                }

                val imageUrl = document.select(".image img").attr("src")

                return@withContext Character(
                    id = name, name = name, imageUrl = imageUrl, details = detailsMap
                )
            }
        }
        null
    }
}