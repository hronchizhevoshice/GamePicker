package com.example.gamepicker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GameDto>
)

data class GameDto(
    val id: Int,
    val name: String,
    val released: String?,
    @SerializedName("background_image")
    val backgroundImage: String?,
    val rating: Double,
    val metacritic: Int?,
    val playtime: Int,
    val genres: List<GenreDto>,
    @SerializedName("platforms")
    val platformsRaw: List<PlatformEntryDto>
)

data class GenreDto(
    val id: Int,
    val name: String,
    val slug: String
)

data class PlatformEntryDto(
    val platform: PlatformDto
)

data class PlatformDto(
    val id: Int,
    val name: String,
    val slug: String
)

data class GameDetailsDto(
    val id: Int,
    val name: String,
    @SerializedName("name_original")
    val nameOriginal: String,
    val description: String?,
    @SerializedName("description_raw")
    val descriptionRaw: String?,
    val released: String?,
    @SerializedName("background_image")
    val backgroundImage: String?,
    val rating: Double,
    @SerializedName("ratings_count")
    val ratingsCount: Int,
    val metacritic: Int?,
    val playtime: Int,
    val genres: List<GenreDto>,
    val developers: List<DeveloperDto>,
    val publishers: List<PublisherDto>,
    @SerializedName("platforms")
    val platformsRaw: List<PlatformEntryDto>
)

data class DeveloperDto(
    val id: Int,
    val name: String
)

data class PublisherDto(
    val id: Int,
    val name: String
)