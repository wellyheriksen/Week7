package com.example.week7.soal1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week7.soal1.model.PokemonCard
import com.example.week7.soal1.model.UiState
import com.example.week7.soal1.repository.ApiException
import com.example.week7.soal1.repository.PokemonRepository
import com.example.week7.soal1.repository.PokemonRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val repository: PokemonRepository = PokemonRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("Charizard")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCard = MutableStateFlow<PokemonCard?>(null)
    val selectedCard: StateFlow<PokemonCard?> = _selectedCard.asStateFlow()

    init {
        loadCards("Charizard")
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        loadCards(newQuery)
    }

    fun loadCards(query: String = _searchQuery.value, forceError: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.getCards(query, forceError)
            result.onSuccess { cards ->
                if (cards.isEmpty()) {
                    _uiState.value = UiState.Error(404, "No Pokemon card found")
                } else {
                    _uiState.value = UiState.Success(cards)
                }
            }.onFailure { exception ->
                val code = if (exception is ApiException) exception.code else 401
                _uiState.value = UiState.Error(code, "HTTP $code")
            }
        }
    }

    fun selectCard(card: PokemonCard) {
        _selectedCard.value = card
    }

    fun dismissDetail() {
        _selectedCard.value = null
    }

    fun retry() {
        loadCards(_searchQuery.value, forceError = false)
    }

    fun triggerErrorTest() {
        loadCards(_searchQuery.value, forceError = true)
    }
}
