package com.omodauda.splitwise.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omodauda.splitwise.data.network.ActivitiesPagingSource
import com.omodauda.splitwise.data.network.api.ActivitiesApi
import com.omodauda.splitwise.data.network.model.Activity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActivityRepository @Inject constructor(private val api: ActivitiesApi) {

    fun getActivitiesStream(): Flow<PagingData<Activity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ActivitiesPagingSource(api)
            }
        ).flow
    }
}