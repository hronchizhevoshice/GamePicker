package com.example.gamepicker.data.repository

import com.example.gamepicker.BuildConfig
import com.example.gamepicker.data.remote.api.RawgApi
import com.example.gamepicker.data.remote.dto.GameDetailsDto
import com.example.gamepicker.data.remote.dto.GamesResponse
import com.example.gamepicker.domain.repository.GameRepository
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val api: RawgApi
) : GameRepository {

    override suspend fun getGames(
        page: Int,
        pageSize: Int,
        search: String?
    ): GamesResponse {
        return api.getGames(
            apiKey = BuildConfig.RAWG_API_KEY,
            page = page,
            pageSize = pageSize,
            search = search,
            ordering = "-rating",
            language = "ru"
        )
    }

    override suspend fun getGameDetails(gameId: Int): GameDetailsDto {
        return api.getGameDetails(
            gameId = gameId,
            apiKey = BuildConfig.RAWG_API_KEY,
            language = "ru"
        )
    }
}