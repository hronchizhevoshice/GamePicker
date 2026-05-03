package com.example.gamepicker.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.data.remote.dto.GameDto
import com.example.gamepicker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getGamesByCategoryUseCase: GetGamesByCategoryUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadGames()
        loadFavorites()
        loadCategories()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _state.value = _state.value.copy(
                    favoriteIds = favorites.map { it.gameId }.toSet()
                )
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val topRatedResult = getGamesByCategoryUseCase(CategoryType.TOP_RATED)
            if (topRatedResult.isSuccess) {
                _state.value = _state.value.copy(topRatedGames = topRatedResult.getOrNull() ?: emptyList())
            }

            val popularResult = getGamesByCategoryUseCase(CategoryType.POPULAR)
            if (popularResult.isSuccess) {
                _state.value = _state.value.copy(popularGames = popularResult.getOrNull() ?: emptyList())
            }

            val newReleasesResult = getGamesByCategoryUseCase(CategoryType.NEW_RELEASES)
            if (newReleasesResult.isSuccess) {
                _state.value = _state.value.copy(newReleases = newReleasesResult.getOrNull() ?: emptyList())
            }
        }
    }

    fun loadGames(page: Int = 1) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val searchQuery = _state.value.searchQuery
            val result = getGamesUseCase(page = page, pageSize = 20, search = searchQuery.takeIf { it.isNotBlank() })

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

    fun toggleFavorite(game: GameDto) {
        viewModelScope.launch {
            if (_state.value.favoriteIds.contains(game.id)) {
                removeFromFavoritesUseCase(game.id)
            } else {
                addToFavoritesUseCase(
                    FavoriteGameEntity(
                        gameId = game.id,
                        name = game.name,
                        rating = game.rating,
                        released = game.released,
                        backgroundImage = game.backgroundImage,
                        genres = game.genres.take(2).joinToString { it.name }
                    )
                )
            }
        }
    }
}

data class HomeState(
    val isLoading: Boolean = false,
    val games: List<GameDto> = emptyList(),
    val topRatedGames: List<GameDto> = emptyList(),
    val popularGames: List<GameDto> = emptyList(),
    val newReleases: List<GameDto> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val currentPage: Int = 1,
    val hasMore: Boolean = true,
    val searchQuery: String = "",
    val error: String? = null
)