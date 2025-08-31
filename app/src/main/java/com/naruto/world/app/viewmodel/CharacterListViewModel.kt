package com.naruto.world.app.viewmodel

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

sealed class CharacterListState {
    object Loading : CharacterListState()
    data class Success(val characters: List<Character>) : CharacterListState()
    data class Error(val message: String) : CharacterListState()
}

class CharacterListViewModel : ViewModel(), KoinComponent {

    private val repository: CharacterRepository by inject()

    private val _state = MutableStateFlow<CharacterListState>(CharacterListState.Loading)
    val state: StateFlow<CharacterListState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentPage = 1
    private val _allCharacters = mutableListOf<Character>()
    val allCharacters: List<Character> = _allCharacters
    private var currentSearchQuery = ""

    init {
        loadCharacters()
    }

    fun loadCharacters(page: Int = 1, limit: Int = 20) {
        if (page == 1) {
            _state.value = CharacterListState.Loading
            _allCharacters.clear()
        }

        viewModelScope.launch {
            repository.getCharacters(page, limit, currentSearchQuery.takeIf { it.isNotEmpty() }).collect { result ->
                result.fold(
                    onSuccess = { response ->
                        if (page == 1) {
                            _allCharacters.clear()
                        }
                        _allCharacters.addAll(response.characters)
                        _state.value = CharacterListState.Success(_allCharacters.toList())
                        currentPage = page
                    },
                    onFailure = { error ->
                        _state.value = CharacterListState.Error(
                            error.localizedMessage ?: "Failed to load characters"
                        )
                    }
                )
            }
        }
    }

    fun loadMoreCharacters() {
        loadCharacters(currentPage + 1)
    }

    fun retry() {
        loadCharacters(1)
    }

    fun searchCharacters(query: String) {
        _searchQuery.value = query
        currentSearchQuery = query
        currentPage = 1
        loadCharacters(1)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        currentSearchQuery = ""
        currentPage = 1
        loadCharacters(1)
    }
}