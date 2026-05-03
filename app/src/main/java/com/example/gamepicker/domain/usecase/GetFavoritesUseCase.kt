package com.example.gamepicker.domain.usecase

import com.example.gamepicker.data.local.entity.FavoriteGameEntity
import com.example.gamepicker.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<FavoriteGameEntity>> {
        return repository.getFavorites()
    }
}