package com.example.android_project.data.repository

import com.example.android_project.data.dao.ProfileDao
import com.example.android_project.data.model.Profile
import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val profileDao: ProfileDao) {

    fun getProfile(): Flow<Profile> {
        return profileDao.getProfile()
    }

    suspend fun saveProfile(profile: Profile) {
        profileDao.insertProfile(profile)
    }
}
