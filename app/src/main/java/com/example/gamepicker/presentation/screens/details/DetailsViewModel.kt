package com.example.gamepicker.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.data.remote.dto.GameDetailsDto
import com.example.gamepicker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getGameDetailsUseCase: GetGameDetailsUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state

    fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val isFav = isFavoriteUseCase(gameId)
            _state.value = _state.value.copy(isFavorite = isFav)

            val result = getGameDetailsUseCase(gameId)

            result.onSuccess { game ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    game = game
                )
            }.onFailure { error ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val game = _state.value.game ?: return@launch
            if (_state.value.isFavorite) {
                removeFromFavoritesUseCase(game.id)
                _state.value = _state.value.copy(isFavorite = false)
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
                _state.value = _state.value.copy(isFavorite = true)
            }
        }
    }
}

data class DetailsState(
    val isLoading: Boolean = false,
    val game: GameDetailsDto? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)