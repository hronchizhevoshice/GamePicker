package com.example.gamepicker.domain.usecase

import com.example.gamepicker.data.remote.dto.GameDto
import com.example.gamepicker.domain.repository.GameRepository
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(
    private val repository: GameRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        pageSize: Int = 20,
        search: String? = null
    ): Result<List<GameDto>> {
        return try {
            val response = repository.getGames(page, pageSize, search)
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}