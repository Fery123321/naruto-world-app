package com.naruto.world.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naruto.world.app.data.model.Village
import com.naruto.world.app.data.model.VillageResponse
import com.naruto.world.app.data.repository.VillageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class VillageListState {
    object Loading : VillageListState()
    data class Success(val villages: List<Village>) : VillageListState()
    data class Error(val message: String) : VillageListState()
}

class VillageListViewModel : ViewModel(), KoinComponent {

    private val villageRepository: VillageRepository by inject()

    private val _villageListState = MutableStateFlow<VillageListState>(VillageListState.Loading)
    val villageListState: StateFlow<VillageListState> = _villageListState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages

    private var currentPage = 0
    private val pageSize = VillageRepository.DEFAULT_PAGE_SIZE
    private var isLoading = false

    init {
        loadVillages()
    }

    fun loadVillages() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            villageRepository.getVillages(page = currentPage, limit = pageSize)
                .onStart {
                    if (currentPage == 0) {
                        _villageListState.value = VillageListState.Loading
                    }
                }
                .catch { e ->
                    _villageListState.value = VillageListState.Error(e.message ?: "Unknown error")
                    isLoading = false
                }
                .collect { result ->
                    result.onSuccess { response ->
                        val currentVillages = when (val currentState = _villageListState.value) {
                            is VillageListState.Success -> currentState.villages
                            else -> emptyList()
                        }

                        val newVillages = if (currentPage == 0) {
                            response.villages
                        } else {
                            currentVillages + response.villages
                        }

                        _villageListState.value = VillageListState.Success(newVillages)
                        _hasMorePages.value = response.villages.size >= pageSize
                        _isLoadingMore.value = false
                    }
                    result.onFailure { e ->
                        _villageListState.value = VillageListState.Error(e.message ?: "Unknown error")
                        _isLoadingMore.value = false
                    }
                    isLoading = false
                }
        }
    }

    fun loadMoreVillages() {
        if (isLoading || !_hasMorePages.value) return

        currentPage++
        _isLoadingMore.value = true
        loadVillages()
    }

    fun refreshVillages() {
        currentPage = 0
        _hasMorePages.value = true
        loadVillages()
    }

    fun searchVillages(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            refreshVillages()
            return
        }

        viewModelScope.launch {
            villageRepository.searchVillages(query)
                .onStart { _villageListState.value = VillageListState.Loading }
                .catch { e ->
                    _villageListState.value = VillageListState.Error(e.message ?: "Search failed")
                }
                .collect { result ->
                    result.onSuccess { villages ->
                        _villageListState.value = VillageListState.Success(villages)
                    }
                    result.onFailure { e ->
                        _villageListState.value = VillageListState.Error(e.message ?: "Search failed")
                    }
                }
        }
    }

    fun shouldLoadMoreVillages(lastVisibleItemIndex: Int): Boolean {
        val currentVillages = when (val state = _villageListState.value) {
            is VillageListState.Success -> state.villages
            else -> return false
        }

        // Load more when user is within 5 items of the end
        return lastVisibleItemIndex >= currentVillages.size - 5 && _hasMorePages.value && !_isLoadingMore.value
    }
}