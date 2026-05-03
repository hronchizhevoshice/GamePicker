package com.example.gamepicker.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.domain.usecase.GetFavoritesUseCase
import com.example.gamepicker.domain.usecase.RemoveFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase
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
}

data class FavoritesState(
    val isLoading: Boolean = true,
    val favorites: List<FavoriteGameEntity> = emptyList()
)