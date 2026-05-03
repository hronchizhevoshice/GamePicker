package com.example.gamepicker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_games")
data class FavoriteGameEntity(
    @PrimaryKey
    val gameId: Int,
    val name: String,
    val rating: Double,
    val released: String?,
    val backgroundImage: String?,
    val genres: String,
    val status: String = "PLAN_TO_PLAY",
    val addedAt: Long = System.currentTimeMillis()
)

enum class GameStatus {
    PLAYING,
    COMPLETED,
    ON_HOLD,
    DROPPED,
    PLAN_TO_PLAY
}

fun GameStatus.getDisplayName(): String {
    return when (this) {
        GameStatus.PLAYING -> "В процессе"
        GameStatus.COMPLETED -> "Пройдена"
        GameStatus.ON_HOLD -> "Отложена"
        GameStatus.DROPPED -> "Брошена"
        GameStatus.PLAN_TO_PLAY -> "Планирую"
    }
}