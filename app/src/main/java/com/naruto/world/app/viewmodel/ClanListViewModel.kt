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

    init {
        loadClans()
    }

    fun loadClans(page: Int? = null, limit: Int? = null) {
        viewModelScope.launch {
            _clanListState.value = ClanListState.Loading

            clanRepository.getClans(page, limit).collect { result ->
                result.fold(
                    onSuccess = { clanResponse ->
                        _clanListState.value = ClanListState.Success(clanResponse.clans)
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

    fun searchClans(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            loadClans()
            return
        }

        viewModelScope.launch {
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

    fun refreshClans() {
        loadClans()
    }

    fun clearSearch() {
        _searchQuery.value = ""
        loadClans()
    }
}