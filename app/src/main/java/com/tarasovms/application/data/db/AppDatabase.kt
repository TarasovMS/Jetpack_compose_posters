package com.tarasovms.application.data.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [PosterLocal::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun posterDao(): PosterDao
}
