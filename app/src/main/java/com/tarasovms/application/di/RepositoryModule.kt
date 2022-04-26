package com.tarasovms.application.di

import com.tarasovms.application.data.db.PosterDao
import com.tarasovms.application.data.service.PosterService
import com.tarasovms.application.data.repository.PosterRepository
//import com.tarasovms.application.data.service.DisneyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

  @Provides
  @ViewModelScoped
  fun provideArticlesRepository(
      remoteService: PosterService,
      posterDao: PosterDao
  ): PosterRepository {
    return PosterRepository(remoteService, posterDao)
  }
}
