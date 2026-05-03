package com.example.gamepicker.domain.repository

import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.data.local.entity.GameStatus
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addToFavorites(game: FavoriteGameEntity)
    suspend fun removeFromFavorites(gameId: Int)
    fun getFavorites(): Flow<List<FavoriteGameEntity>>
    suspend fun isFavorite(gameId: Int): Boolean
    suspend fun updateStatus(gameId: Int, status: GameStatus)
    suspend fun updateNotes(gameId: Int, notes: String)
}