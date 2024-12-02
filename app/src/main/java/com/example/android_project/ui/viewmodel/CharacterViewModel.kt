package com.example.android_project.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_project.data.model.Character
import com.example.android_project.data.repository.CharacterRepository
import com.example.android_project.data.store.SettingsDataStore
import com.example.android_project.utils.FilterBadgeCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val repository: CharacterRepository,
    private val context: Context,
    private val filterBadgeCache: FilterBadgeCache
) : ViewModel() {

    private val _allCharacters = MutableStateFlow<List<Character>>(emptyList())
    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    private val _favorites = MutableStateFlow<List<Character>>(emptyList())
    val favorites: StateFlow<List<Character>> = _favorites

    private val _characterDetails = MutableStateFlow<Character?>(null)
    val characterDetails: StateFlow<Character?> = _characterDetails

    private val _availableGroups = MutableStateFlow<List<String>>(emptyList())
    val availableGroups: StateFlow<List<String>> = _availableGroups

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    init {
        fetchCharacters()
        loadFavorites()
        loadAndApplyFilters()
        checkBadgeStatus()
    }

    private fun fetchCharacters() {
        _loading.value = true
        viewModelScope.launch {
            _characterDetails.value = null
            try {
                val allCharacters = repository.getCharacterList()
                val favorites = repository.getFavorites().first()

                _allCharacters.value = allCharacters.map { character ->
                    character.copy(isFavorite = favorites.any { it.id == character.id })
                }

                val settingsDataStore = SettingsDataStore(context)
                val filters = settingsDataStore.loadFilters().first()

                applyFilters(filters.group, filters.name)

                _availableGroups.value = allCharacters.map { it.group }.distinct().sorted()

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect { favoriteList ->
                _favorites.value = favoriteList
            }
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            try {
                if (character.isFavorite) {
                    repository.removeFavorite(character)
                } else {
                    repository.addFavorite(character)
                }

                _characters.value = _characters.value.map { c ->
                    if (c.id == character.id) c.copy(isFavorite = !c.isFavorite) else c
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun applyFilters(group: String, name: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _characters.value = _allCharacters.value.filter { character ->
                    (group.isEmpty() || character.group == group) &&
                            (name.isEmpty() || character.name.contains(name, ignoreCase = true))
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchCharacterDetails(characterId: String) {
        viewModelScope.launch {
            _loading.value = true
            _characterDetails.value = null

            try {
                _characterDetails.value = repository.getCharacterDetails(characterId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    private fun loadAndApplyFilters() {
        viewModelScope.launch {
            val settingsDataStore = SettingsDataStore(context)
            settingsDataStore.loadFilters().collect { filters ->
                applyFilters(filters.group, filters.name)
            }
        }
    }

    private fun checkBadgeStatus() {
        viewModelScope.launch {
            val settingsDataStore = SettingsDataStore(context)
            val filters = settingsDataStore.loadFilters().first()
            filterBadgeCache.setShowBadge(filters.name.isNotEmpty() || filters.group.isNotEmpty())
        }
    }
}


