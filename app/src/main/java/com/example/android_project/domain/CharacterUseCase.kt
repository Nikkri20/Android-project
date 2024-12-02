package com.example.android_project.domain

import com.example.android_project.data.model.Character
import com.example.android_project.data.repository.CharacterRepository

class CharacterUseCase(private val repository: CharacterRepository) {

    suspend fun getCharacterList(): List<Character> = repository.getCharacterList()

    suspend fun getCharacterDetails(characterId: String): Character? = repository.getCharacterDetails(characterId)
}
