package com.example.gamepicker.data.local.entity

import androidx.compose.ui.graphics.Color
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
    val status: String = GameStatus.PLAN_TO_PLAY.name,
    val notes: String = "",
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

fun getStatusColor(status: GameStatus): Color {
    return when (status) {
        GameStatus.PLAYING -> Color(0xFF2196F3)
        GameStatus.COMPLETED -> Color(0xFF4CAF50)
        GameStatus.ON_HOLD -> Color(0xFFFF9800)
        GameStatus.DROPPED -> Color(0xFFF44336)
        GameStatus.PLAN_TO_PLAY -> Color(0xFF9C27B0)
    }
}