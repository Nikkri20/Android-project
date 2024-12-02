package com.example.android_project.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.android_project.data.store.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterSettingsViewModel : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _group = MutableStateFlow("")
    val group: StateFlow<String> = _group

    private val _availableGroups = MutableStateFlow<List<String>>(emptyList())
    val availableGroups: StateFlow<List<String>> = _availableGroups

    fun setName(newName: String) {
        _name.value = newName
    }

    fun setGroup(newGroup: String) {
        _group.value = newGroup
    }

    fun setAvailableGroups(groups: List<String>) {
        _availableGroups.value = groups
    }

    suspend fun saveFilters(context: Context) {
        withContext(Dispatchers.IO) {
            SettingsDataStore(context).saveFilters(
                name = _name.value,
                group = _group.value
            )
        }
    }

    suspend fun loadFilters(context: Context) {
        withContext(Dispatchers.IO) {
            SettingsDataStore(context).loadFilters().collect { settings ->
                _name.value = settings.name
                _group.value = settings.group
            }
        }
    }

    fun isFilterDefault(): Boolean = _name.value.isEmpty() && _group.value.isEmpty()
}
