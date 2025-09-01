package com.naruto.world.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naruto.world.app.data.model.Village
import com.naruto.world.app.data.repository.VillageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class VillageDetailState {
    object Loading : VillageDetailState()
    data class Success(val village: Village) : VillageDetailState()
    data class Error(val message: String) : VillageDetailState()
}

class VillageDetailViewModel(private val villageId: Long) : ViewModel(), KoinComponent {

    private val villageRepository: VillageRepository by inject()

    private val _villageDetailState = MutableStateFlow<VillageDetailState>(VillageDetailState.Loading)
    val villageDetailState: StateFlow<VillageDetailState> = _villageDetailState

    init {
        loadVillageDetail()
    }

    private fun loadVillageDetail() {
        viewModelScope.launch {
            villageRepository.getVillageById(villageId)
                .onStart { _villageDetailState.value = VillageDetailState.Loading }
                .catch { e ->
                    _villageDetailState.value = VillageDetailState.Error(e.message ?: "Failed to load village")
                }
                .collect { result ->
                    result.onSuccess { village ->
                        _villageDetailState.value = VillageDetailState.Success(village)
                    }
                    result.onFailure { e ->
                        _villageDetailState.value = VillageDetailState.Error(e.message ?: "Failed to load village")
                    }
                }
        }
    }

    fun retry() {
        loadVillageDetail()
    }
}