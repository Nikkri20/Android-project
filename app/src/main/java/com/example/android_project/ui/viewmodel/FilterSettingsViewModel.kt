package com.example.android_project.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.android_project.data.store.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterSettingsViewModel : ViewModel() {
    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender

    private val _minAge = MutableStateFlow(0)
    val minAge: StateFlow<Int> = _minAge

    fun setGender(newGender: String) {
        _gender.value = newGender
    }

    fun setMinAge(newMinAge: Int) {
        _minAge.value = newMinAge
    }

    suspend fun saveFilters(context: Context) {
        withContext(Dispatchers.IO) {
            SettingsDataStore(context).saveFilters(
                gender = _gender.value,
                minAge = _minAge.value
            )
        }
    }

    suspend fun loadFilters(context: Context) {
        withContext(Dispatchers.IO) {
            val settings = SettingsDataStore(context).loadFilters()
            _gender.value = settings.gender
            _minAge.value = settings.minAge
        }
    }
}
