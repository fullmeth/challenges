package com.mentorship.project.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://mentorship-challanges-kotlin-syntax.kennixer.workers.dev")
        .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
        .build()

    fun dummyApiService(): ApiService = retrofit.create(ApiService::class.java)
}