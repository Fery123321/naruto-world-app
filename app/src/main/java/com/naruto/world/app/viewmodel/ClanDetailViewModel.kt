package com.naruto.world.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naruto.world.app.data.model.Clan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.naruto.world.app.data.repository.ClanRepository

sealed class ClanDetailState {
    object Loading : ClanDetailState()
    data class Success(val clan: Clan) : ClanDetailState()
    data class Error(val message: String) : ClanDetailState()
}

class ClanDetailViewModel(private val clanId: Long) : ViewModel(), KoinComponent {

    private val clanRepository: ClanRepository by inject()

    private val _clanDetailState = MutableStateFlow<ClanDetailState>(ClanDetailState.Loading)
    val clanDetailState: StateFlow<ClanDetailState> = _clanDetailState.asStateFlow()

    init {
        loadClanDetail()
    }

    private fun loadClanDetail() {
        viewModelScope.launch {
            _clanDetailState.value = ClanDetailState.Loading

            clanRepository.getClanById(clanId).collect { result ->
                result.fold(
                    onSuccess = { clan ->
                        _clanDetailState.value = ClanDetailState.Success(clan)
                    },
                    onFailure = { exception ->
                        _clanDetailState.value = ClanDetailState.Error(
                            exception.message ?: "Failed to load clan details"
                        )
                    }
                )
            }
        }
    }

    fun refreshClanDetail() {
        loadClanDetail()
    }
}