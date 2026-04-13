package com.omodauda.splitwise.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omodauda.splitwise.data.network.FriendPagingSource
import com.omodauda.splitwise.data.network.api.FriendApi
import com.omodauda.splitwise.data.network.model.Friend
import kotlinx.coroutines.flow.Flow

class FriendRepository(private val friendApi: FriendApi) {
    fun getFriendsStream(searchQuery: String?): Flow<PagingData<Friend>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FriendPagingSource(friendApi, searchQuery)
            }
        ).flow
    }
}