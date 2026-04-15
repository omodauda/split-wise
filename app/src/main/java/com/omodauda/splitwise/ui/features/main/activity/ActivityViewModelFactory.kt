package com.omodauda.splitwise.ui.features.main.activity


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omodauda.splitwise.data.repository.ActivityRepository

class ActivityViewModelFactory(private val repo: ActivityRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}