package com.example.gamepicker.domain.usecase

import com.example.gamepicker.data.local.entity.GameStatus
import com.example.gamepicker.domain.repository.FavoriteRepository
import javax.inject.Inject

class UpdateGameStatusUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(gameId: Int, status: GameStatus) {
        repository.updateStatus(gameId, status)
    }
}