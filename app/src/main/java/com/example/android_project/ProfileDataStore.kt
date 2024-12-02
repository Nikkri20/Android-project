package com.example.android_project.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.android_project.data.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_data_store")

class ProfileDataStore(private val context: Context) {

    private val dataStore = context.profileDataStore

    private val NAME_KEY = stringPreferencesKey("name")
    private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
    private val RESUME_URL_KEY = stringPreferencesKey("resume_url")
    private val ROLE_KEY = stringPreferencesKey("role")

    val profileFlow: Flow<Profile> = dataStore.data.map { preferences ->
        Profile(
            id = 0,
            name = preferences[NAME_KEY] ?: "",
            avatarUri = preferences[AVATAR_URI_KEY],
            resumeUrl = preferences[RESUME_URL_KEY] ?: "",
            role = preferences[ROLE_KEY] ?: ""
        )
    }

    suspend fun saveProfile(name: String, avatarUri: String?, resumeUrl: String, role: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
            preferences[AVATAR_URI_KEY] = avatarUri.orEmpty()
            preferences[RESUME_URL_KEY] = resumeUrl
            preferences[ROLE_KEY] = role
        }
    }
}