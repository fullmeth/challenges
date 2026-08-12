package com.mentorship.project.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /***
     * https://mentorship-challanges-kotlin-syntax.kennixer.workers.dev/
     * GET /status/[200, 400, 401, 403, 404, 408, 429, 500, 502, 503]?delay=[delay in millis]
     * GET /random
     * GET /json/malformed
     * GET /timeout
     */
    @GET("/status/{code}")
    suspend fun getDummyJson(@Path("code") code: Int, @Query("delay") delay: Int? = null): DummyDto
}