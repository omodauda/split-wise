package com.example.splitwise.ui.features.main.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.splitwise.data.network.model.Friend
import com.example.splitwise.data.repository.FriendRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class FriendViewModel(private val friendRepository: FriendRepository): ViewModel() {
    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery = _searchQuery.asStateFlow()

    // This is the Flow that the UI will collect.
    // `flatMapLatest` is crucial: whenever the search query changes, it cancels the old
    // pager and creates a new one with the new query.
    // `cachedIn` ensures the data survives configuration changes (like screen rotation).
    val friendsPagingData: Flow<PagingData<Friend>> = _searchQuery
        .flatMapLatest { query ->
            friendRepository.getFriendsStream(query)
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String?) {
        _searchQuery.value = query
    }
}