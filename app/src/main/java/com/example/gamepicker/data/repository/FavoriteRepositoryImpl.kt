package com.example.gamepicker.data.repository

import com.example.gamepicker.data.local.dao.FavoriteGameDao
import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.data.local.entity.GameStatus
import com.example.gamepicker.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteGameDao
) : FavoriteRepository {

    override suspend fun addToFavorites(game: FavoriteGameEntity) {
        dao.insert(game)
    }

    override suspend fun removeFromFavorites(gameId: Int) {
        dao.delete(gameId)
    }

    override fun getFavorites(): Flow<List<FavoriteGameEntity>> {
        return dao.getAllFavorites()
    }

    override suspend fun isFavorite(gameId: Int): Boolean {
        return dao.isFavorite(gameId)
    }

    override suspend fun updateStatus(gameId: Int, status: GameStatus) {
        dao.updateStatus(gameId, status.name)
    }

    override suspend fun updateNotes(gameId: Int, notes: String) {
        dao.updateNotes(gameId, notes)
    }
}