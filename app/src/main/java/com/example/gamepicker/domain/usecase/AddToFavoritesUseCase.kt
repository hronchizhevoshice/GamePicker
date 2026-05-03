package com.example.gamepicker.domain.usecase

import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(game: FavoriteGameEntity) {
        repository.addToFavorites(game)
    }
}