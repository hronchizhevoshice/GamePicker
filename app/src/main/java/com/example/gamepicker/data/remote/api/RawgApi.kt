package com.example.gamepicker.data.remote.api

import com.example.gamepicker.data.remote.dto.GameDetailsDto
import com.example.gamepicker.data.remote.dto.GamesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgApi {
    @GET("games")
    suspend fun getGames(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("search") search: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("language") language: String = "ru"
    ): GamesResponse

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String,
        @Query("language") language: String = "ru"
    ): GameDetailsDto

    @GET("games")
    suspend fun getGamesByCategory(
        @Query("key") apiKey: String,
        @Query("dates") dates: String? = null,
        @Query("ordering") ordering: String? = null,
        @Query("page_size") pageSize: Int = 10,
        @Query("language") language: String = "ru"
    ): GamesResponse

    @GET("genres")
    suspend fun getGenres(
        @Query("key") apiKey: String,
        @Query("language") language: String = "ru"
    ): GenresResponse
}

data class GenresResponse(
    val results: List<GenreDto>
)

data class GenreDto(
    val id: Int,
    val name: String,
    val slug: String
)