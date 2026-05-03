package com.example.gamepicker.domain.usecase

import com.example.gamepicker.data.remote.dto.GameDetailsDto
import com.example.gamepicker.domain.repository.GameRepository
import javax.inject.Inject

class GetGameDetailsUseCase @Inject constructor(
    private val repository: GameRepository
) {
    suspend operator fun invoke(gameId: Int): Result<GameDetailsDto> {
        return try {
            val response = repository.getGameDetails(gameId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}