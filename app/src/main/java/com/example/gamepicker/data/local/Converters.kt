package com.example.gamepicker.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGenreList(genres: String): String = genres

    @TypeConverter
    fun toGenreList(genres: String): String = genres
}