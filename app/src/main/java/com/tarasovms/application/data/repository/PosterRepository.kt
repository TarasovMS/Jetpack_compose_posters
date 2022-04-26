package com.tarasovms.application.data.repository

import com.tarasovms.application.data.db.PosterLocal
import com.tarasovms.application.data.db.PosterDao
import com.tarasovms.application.data.service.PosterService
import com.tarasovms.application.data.service.PostersApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PosterRepository @Inject constructor(
    private val disneyService: PosterService,
    private val posterDao: PosterDao
) {

    fun getAllPoster(
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) = flow {
        val posters: Flow<List<PosterLocal>> = posterDao.getPoster()

        posters.collect { posterList ->
            if (posterList.isNullOrEmpty()) {
                val response = disneyService.getPosterList()
                if (response.isSuccessful) {
                    response.body()?.let { postersResponse ->
                        val convertData = convertData(postersResponse)
                        posterDao.insertTotalPoster(convertData)
                        emit(convertData)
                    }
                } else onError(response.message())

            } else emit(posterList)
        }
    }.onStart {
        onStart.invoke()
    }
        .onCompletion {
            onCompletion.invoke()
        }
        .flowOn(Dispatchers.IO)

    fun getPosterLike(
        onStart: () -> Unit,
        onCompletion: () -> Unit
    ) = flow {
        val posters: Flow<List<PosterLocal?>> = posterDao.getPosterLike(true)
        posters.collect { emit(it) }
    }
        .onStart {
            onStart.invoke()
        }
        .onCompletion {
            onCompletion.invoke()
        }
        .flowOn(Dispatchers.IO)

    private fun convertData(posterApi: List<PostersApi>): List<PosterLocal> {
        return posterApi.map {
            PosterLocal(
                id = it.id,
                name = it.name,
                release = it.release,
                playtime = it.playtime,
                description = it.description,
                plot = it.plot,
                poster = it.poster,
                like = false
            )
        }
    }

}
