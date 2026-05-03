package com.example.gamepicker.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamepicker.data.remote.dto.GameDetailsDto
import com.example.gamepicker.domain.usecase.GetGameDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getGameDetailsUseCase: GetGameDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state

    fun loadGameDetails(gameId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

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
}

data class DetailsState(
    val isLoading: Boolean = false,
    val game: GameDetailsDto? = null,
    val error: String? = null
)