package com.example.gamepicker.domain.repository

import com.example.gamepicker.data.remote.api.GenreDto
import com.example.gamepicker.data.remote.dto.GameDetailsDto
import com.example.gamepicker.data.remote.dto.GamesResponse

interface GameRepository {
    suspend fun getGames(
        page: Int,
        pageSize: Int,
        search: String? = null,
        ordering: String? = null
    ): GamesResponse

    suspend fun getGamesByDateRange(
        dates: String,
        pageSize: Int = 10,
        ordering: String? = null
    ): GamesResponse

    suspend fun getGameDetails(gameId: Int): GameDetailsDto

    suspend fun getGenres(): List<GenreDto>
}