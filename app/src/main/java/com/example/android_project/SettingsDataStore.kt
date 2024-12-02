package com.example.android_project.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")
private val FAVORITE_KEY = stringPreferencesKey("favorite_ids")

class SettingsDataStore(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val NAME_KEY = stringPreferencesKey("name")
        val GROUP_KEY = stringPreferencesKey("group")
    }

    suspend fun saveFilters(name: String, group: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[GROUP_KEY] = group
        }
    }

    fun loadFilters(): Flow<Settings> = dataStore.data.map { preferences ->
        Settings(
            name = preferences[NAME_KEY] ?: "",
            group = preferences[GROUP_KEY] ?: ""
        )
    }

    data class Settings(
        val name: String,
        val group: String
    )

    suspend fun saveFavorites(favorites: List<String>) {
        dataStore.edit { preferences ->
            preferences[FAVORITE_KEY] = favorites.joinToString(",")
        }
    }

    fun loadFavorites(): Flow<List<String>> = dataStore.data.map { preferences ->
        preferences[FAVORITE_KEY]?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    }
}
