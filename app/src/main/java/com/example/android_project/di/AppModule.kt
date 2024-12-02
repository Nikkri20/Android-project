package com.example.android_project.di

import android.content.Context
import com.example.android_project.utils.FilterBadgeCache
import com.example.android_project.data.repository.CharacterRepository
import com.example.android_project.data.local.DatabaseProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(context: Context): CharacterRepository {
        val database = DatabaseProvider.getDatabase(context)
        return CharacterRepository(database.favoriteDao())
    }

    @Provides
    @Singleton
    fun provideFilterBadgeCache(): FilterBadgeCache {
        return FilterBadgeCache()
    }
}
