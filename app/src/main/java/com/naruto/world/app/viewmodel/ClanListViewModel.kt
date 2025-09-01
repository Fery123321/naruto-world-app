package com.naruto.world.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naruto.world.app.data.model.Clan
import com.naruto.world.app.data.model.ClanResponse
import com.naruto.world.app.data.repository.ClanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class ClanListState {
    object Loading : ClanListState()
    data class Success(val clans: List<Clan>) : ClanListState()
    data class Error(val message: String) : ClanListState()
}

class ClanListViewModel : ViewModel(), KoinComponent {

    private val clanRepository: ClanRepository by inject()

    private val _clanListState = MutableStateFlow<ClanListState>(ClanListState.Loading)
    val clanListState: StateFlow<ClanListState> = _clanListState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages.asStateFlow()

    private var allClans = mutableListOf<Clan>()

    init {
        loadInitialClans()
        preloadData()
    }

    private fun loadInitialClans() {
        viewModelScope.launch {
            _clanListState.value = ClanListState.Loading

            clanRepository.getClansPaged(0, PAGE_SIZE).collect { result ->
                result.fold(
                    onSuccess = { clanResponse ->
                        allClans.clear()
                        allClans.addAll(clanResponse.clans)
                        _clanListState.value = ClanListState.Success(allClans.toList())
                        _hasMorePages.value = clanResponse.clans.size >= PAGE_SIZE
                    },
                    onFailure = { exception ->
                        _clanListState.value = ClanListState.Error(
                            exception.message ?: "Failed to load clans"
                        )
                    }
                )
            }
        }
    }

    fun loadMoreClans() {
        if (_isLoadingMore.value || !_hasMorePages.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            val nextPage = _currentPage.value + 1

            clanRepository.getClansPaged(nextPage, PAGE_SIZE).collect { result ->
                result.fold(
                    onSuccess = { clanResponse ->
                        if (clanResponse.clans.isNotEmpty()) {
                            allClans.addAll(clanResponse.clans)
                            _clanListState.value = ClanListState.Success(allClans.toList())
                            _currentPage.value = nextPage
                            _hasMorePages.value = clanResponse.clans.size >= PAGE_SIZE
                        } else {
                            _hasMorePages.value = false
                        }
                    },
                    onFailure = { exception ->
                        // Don't show error for pagination failures, just stop loading more
                        _hasMorePages.value = false
                    }
                )
                _isLoadingMore.value = false
            }
        }
    }

    private fun preloadData() {
        viewModelScope.launch {
            // Preload popular clans in background
            clanRepository.preloadPopularClans()

            // Optimize cache periodically
            clanRepository.optimizeCache()
        }
    }

    fun searchClans(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            // Return to paginated view
            _clanListState.value = ClanListState.Success(allClans.toList())
            return
        }

        // Debounce search to avoid too many API calls
        viewModelScope.launch {
            kotlinx.coroutines.delay(SEARCH_DEBOUNCE_MS)

            if (_searchQuery.value == query) { // Check if search query hasn't changed
                _clanListState.value = ClanListState.Loading

                clanRepository.searchClans(query).collect { result ->
                    result.fold(
                        onSuccess = { clans ->
                            _clanListState.value = ClanListState.Success(clans)
                        },
                        onFailure = { exception ->
                            _clanListState.value = ClanListState.Error(
                                exception.message ?: "Failed to search clans"
                            )
                        }
                    )
                }
            }
        }
    }

    fun refreshClans() {
        viewModelScope.launch {
            _clanListState.value = ClanListState.Loading
            _currentPage.value = 0
            _hasMorePages.value = true
            allClans.clear()

            loadInitialClans()
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _clanListState.value = ClanListState.Success(allClans.toList())
    }

    fun shouldLoadMoreClans(lastVisibleItemIndex: Int): Boolean {
        return lastVisibleItemIndex >= allClans.size - LOAD_MORE_THRESHOLD &&
               _hasMorePages.value &&
               !_isLoadingMore.value &&
               _searchQuery.value.isBlank()
    }

    companion object {
        const val PAGE_SIZE = 20
        const val LOAD_MORE_THRESHOLD = 5
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}