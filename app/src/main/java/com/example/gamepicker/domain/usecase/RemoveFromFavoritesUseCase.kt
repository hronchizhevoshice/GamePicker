package com.example.gamepicker.domain.usecase

import com.example.gamepicker.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(gameId: Int) {
        repository.removeFromFavorites(gameId)
    }
}