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
    val addedAt: Long = System.currentTimeMillis()
)