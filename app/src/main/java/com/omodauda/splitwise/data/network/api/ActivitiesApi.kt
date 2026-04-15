package com.omodauda.splitwise.data.network.api

import com.omodauda.splitwise.data.network.model.GetActivitiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivitiesApi {
    @GET("activities")
    suspend fun getUserActivities(@Query("cursorId") cursorId: String?): Response<GetActivitiesResponse>
}