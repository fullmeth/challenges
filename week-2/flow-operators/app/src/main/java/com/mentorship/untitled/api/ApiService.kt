package com.mentorship.untitled.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/products/search")
    suspend fun search(@Query("q") query: String, @Query("delay") delay: Int): DummySearch
}