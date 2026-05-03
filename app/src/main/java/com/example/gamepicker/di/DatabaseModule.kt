package com.example.gamepicker.di

import android.content.Context
import com.example.gamepicker.data.local.AppDatabase
import com.example.gamepicker.data.local.dao.FavoriteGameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFavoriteGameDao(database: AppDatabase): FavoriteGameDao {
        return database.favoriteGameDao()
    }
}