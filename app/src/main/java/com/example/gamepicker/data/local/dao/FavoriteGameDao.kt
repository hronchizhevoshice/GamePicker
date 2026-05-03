package com.example.gamepicker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteGameEntity)

    @Update
    suspend fun update(favorite: FavoriteGameEntity)

    @Query("DELETE FROM favorite_games WHERE gameId = :gameId")
    suspend fun delete(gameId: Int)

    @Query("SELECT * FROM favorite_games ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteGameEntity>>

    @Query("SELECT * FROM favorite_games WHERE status = :status")
    fun getFavoritesByStatus(status: String): Flow<List<FavoriteGameEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_games WHERE gameId = :gameId)")
    suspend fun isFavorite(gameId: Int): Boolean

    @Query("UPDATE favorite_games SET status = :status WHERE gameId = :gameId")
    suspend fun updateStatus(gameId: Int, status: String)

    @Query("UPDATE favorite_games SET notes = :notes WHERE gameId = :gameId")
    suspend fun updateNotes(gameId: Int, notes: String)
}