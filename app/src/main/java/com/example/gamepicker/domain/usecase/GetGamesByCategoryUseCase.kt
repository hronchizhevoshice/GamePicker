package com.example.gamepicker.domain.usecase

import com.example.gamepicker.data.remote.dto.GameDto
import com.example.gamepicker.domain.repository.GameRepository
import javax.inject.Inject

class GetGamesByCategoryUseCase @Inject constructor(
    private val repository: GameRepository
) {
    suspend operator fun invoke(
        category: CategoryType
    ): Result<List<GameDto>> {
        return try {
            val response = when (category) {
                CategoryType.TOP_RATED -> repository.getGames(
                    page = 1,
                    pageSize = 10,
                    ordering = "-rating"
                )

                CategoryType.POPULAR -> repository.getGames(
                    page = 1,
                    pageSize = 10,
                    ordering = "-added"
                )

                CategoryType.NEW_RELEASES -> {
                    val currentYear = java.time.Year.now().toString()
                    val lastYear = (java.time.Year.now().value - 1).toString()
                    repository.getGamesByDateRange(
                        dates = "$lastYear-01-01,$currentYear-12-31",
                        ordering = "-released"
                    )
                }
            }
            Result.success(response.results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}