package com.example.gamepicker.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.data.local.entity.GameStatus
import com.example.gamepicker.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val updateGameStatusUseCase: UpdateGameStatusUseCase,
    private val updateGameNotesUseCase: UpdateGameNotesUseCase,
    private val updateUserRatingUseCase: UpdateUserRatingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    favorites = favorites
                )
            }
        }
    }

    fun removeFromFavorites(gameId: Int) {
        viewModelScope.launch {
            removeFromFavoritesUseCase(gameId)
        }
    }

    fun removeMultipleFromFavorites(gameIds: List<Int>) {
        viewModelScope.launch {
            gameIds.forEach { gameId ->
                removeFromFavoritesUseCase(gameId)
            }
        }
    }

    fun updateStatus(gameId: Int, status: GameStatus) {
        viewModelScope.launch {
            updateGameStatusUseCase(gameId, status)
        }
    }

    fun updateNotes(gameId: Int, notes: String) {
        viewModelScope.launch {
            updateGameNotesUseCase(gameId, notes)
        }
    }

    fun updateUserRating(gameId: Int, userRating: Int) {
        viewModelScope.launch {
            updateUserRatingUseCase(gameId, userRating)
        }
    }
}

data class FavoritesState(
    val isLoading: Boolean = true,
    val favorites: List<FavoriteGameEntity> = emptyList()
)