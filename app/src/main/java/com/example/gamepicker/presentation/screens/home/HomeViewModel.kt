package com.example.gamepicker.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamepicker.data.remote.dto.GameDto
import com.example.gamepicker.domain.usecase.GetGamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames(page: Int = 1) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val result = getGamesUseCase(page = page, pageSize = 20)

            result.onSuccess { games ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    games = if (page == 1) games else _state.value.games + games,
                    currentPage = page,
                    hasMore = games.size == 20
                )
            }.onFailure { error ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun loadNextPage() {
        if (!_state.value.isLoading && _state.value.hasMore) {
            loadGames(_state.value.currentPage + 1)
        }
    }

    fun searchGames(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, searchQuery = query)

            val result = getGamesUseCase(page = 1, pageSize = 20, search = query.takeIf { it.isNotBlank() })

            result.onSuccess { games ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    games = games,
                    currentPage = 1,
                    hasMore = games.size == 20
                )
            }.onFailure { error ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val games: List<GameDto> = emptyList(),
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val searchQuery: String = "",
    val error: String? = null
)