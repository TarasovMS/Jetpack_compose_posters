package com.tarasovms.application.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PosterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotalPoster(article: List<PosterLocal>)

    @Query("SELECT * FROM PosterLocal WHERE id = :id_")
    fun getPosterId(id_: Long): PosterLocal?

    @Query("SELECT * FROM PosterLocal WHERE `like` = :like")
    fun getPosterLike(like: Boolean = false): Flow<List<PosterLocal?>>

    @Query("SELECT * FROM PosterLocal")
    fun getPoster(): Flow<List<PosterLocal>>

    @Update
    suspend fun updatePoster(poster: PosterLocal)
}
