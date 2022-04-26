package com.tarasovms.application.data.repository

import com.tarasovms.application.data.db.PosterDao
import com.tarasovms.application.data.db.PosterLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailRepository @Inject constructor(
  private val posterDao: PosterDao
) {

  fun getPosterById(id: Long) = flow {
    val poster = posterDao.getPosterId(id)
    emit(poster)
  }.flowOn(Dispatchers.IO)

  suspend fun updatePoster(poster: PosterLocal) {
    posterDao.updatePoster(poster)
  }

}
