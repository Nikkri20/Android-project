package com.example.android_project.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterBadgeCache {
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> get() = _showBadge

    fun setShowBadge(show: Boolean) {
        _showBadge.value = show
    }
}
