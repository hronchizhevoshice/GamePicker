package com.example.gamepicker.di

import com.example.gamepicker.data.repository.FavoriteRepositoryImpl
import com.example.gamepicker.data.repository.GameRepositoryImpl
import com.example.gamepicker.domain.repository.FavoriteRepository
import com.example.gamepicker.domain.repository.GameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}