package com.example.gamepicker.domain.usecase

import com.example.gamepicker.domain.repository.FavoriteRepository
import javax.inject.Inject

class UpdateUserRatingUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(gameId: Int, userRating: Int) {
        repository.updateUserRating(gameId, userRating)
    }
}