package com.omodauda.splitwise.ui.features.main.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omodauda.splitwise.data.network.model.Activity
import com.omodauda.splitwise.data.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val repo: ActivityRepository): ViewModel() {

    val activitiesPagingData: Flow<PagingData<Activity>> =repo.getActivitiesStream()
        .cachedIn(viewModelScope)
}