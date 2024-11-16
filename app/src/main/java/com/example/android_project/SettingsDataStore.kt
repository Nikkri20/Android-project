package com.example.android_project.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "filter_settings")

data class FilterSettings(val gender: String, val minAge: Int)

class SettingsDataStore(private val context: Context) {
    companion object {
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val MIN_AGE_KEY = intPreferencesKey("min_age")
    }

    suspend fun saveFilters(gender: String, minAge: Int) {
        context.dataStore.edit { preferences ->
            preferences[GENDER_KEY] = gender
            preferences[MIN_AGE_KEY] = minAge
        }
    }

    suspend fun loadFilters(): FilterSettings {
        val preferences = context.dataStore.data.first()
        val gender = preferences[GENDER_KEY] ?: ""
        val minAge = preferences[MIN_AGE_KEY] ?: 0
        return FilterSettings(gender, minAge)
    }
}
