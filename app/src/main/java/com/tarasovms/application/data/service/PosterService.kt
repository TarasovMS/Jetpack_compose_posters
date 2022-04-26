package com.tarasovms.application.data.service

import retrofit2.Response
import retrofit2.http.GET

interface PosterService {

    @GET("postersFilms.json")
    suspend fun getPosterList(): Response<List<PostersApi>>
}
