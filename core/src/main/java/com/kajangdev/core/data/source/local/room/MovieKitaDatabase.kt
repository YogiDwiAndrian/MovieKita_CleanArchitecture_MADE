package com.kajangdev.core.data.source.local.room


import androidx.room.Database
import androidx.room.RoomDatabase
import com.kajangdev.core.data.source.local.entity.MovieEntity

@Database(
        entities = [MovieEntity::class],
        version = 4,
        exportSchema = false
)
abstract class MovieKitaDatabase : RoomDatabase() {

    abstract fun movieKitaDao(): MovieKitaDao

}