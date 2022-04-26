package com.tarasovms.application.di

import android.app.Application
import androidx.room.Room
import com.tarasovms.application.data.db.AppDatabase
import com.tarasovms.application.data.db.PosterDao
import com.tarasovms.application.utils.DATA_BASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                DATA_BASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePosterDao(appDatabase: AppDatabase): PosterDao {
        return appDatabase.posterDao()
    }
}
