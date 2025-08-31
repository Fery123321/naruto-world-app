package com.naruto.world.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naruto.world.app.data.model.Character
import com.naruto.world.app.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class CharacterDetailState {
    object Loading : CharacterDetailState()
    data class Success(val character: Character) : CharacterDetailState()
    data class Error(val message: String) : CharacterDetailState()
}

class CharacterDetailViewModel(
    private val characterId: Long
) : ViewModel(), KoinComponent {

    private val repository: CharacterRepository by inject()

    private val _state = MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)
    val state: StateFlow<CharacterDetailState> = _state.asStateFlow()

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            repository.getCharacterById(characterId).collect { result ->
                result.fold(
                    onSuccess = { character ->
                        _state.value = CharacterDetailState.Success(character)
                    },
                    onFailure = { error ->
                        _state.value = CharacterDetailState.Error(
                            error.localizedMessage ?: "Failed to load character details"
                        )
                    }
                )
            }
        }
    }

    fun retry() {
        _state.value = CharacterDetailState.Loading
        loadCharacter()
    }
}