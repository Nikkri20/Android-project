package com.example.android_project.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android_project.data.model.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Insert
    suspend fun insertProfile(profile: Profile)

    @Query("SELECT * FROM profile LIMIT 1")
    fun getProfile(): Flow<Profile>
}
