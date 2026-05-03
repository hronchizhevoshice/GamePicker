package com.example.gamepicker.domain.repository

import com.example.gamepicker.data.remote.dto.GameDetailsDto
import com.example.gamepicker.data.remote.dto.GamesResponse

interface GameRepository {
    suspend fun getGames(
        page: Int,
        pageSize: Int,
        search: String? = null
    ): GamesResponse

    suspend fun getGameDetails(gameId: Int): GameDetailsDto
}