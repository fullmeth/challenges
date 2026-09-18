package com.mentorship.title.auth

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthNetwork(private val tokenPreferences: TokenPreferences) {

    fun createService(): AuthService {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor { chain ->
                val accessToken = tokenPreferences.accessToken
                val request = chain.request().newBuilder()
                    .apply {
                        if (accessToken.isNotEmpty())
                            header("Authorization", "Bearer $accessToken")
                    }.build()
                chain.proceed(request)
            }
            .authenticator(TokenAuthenticator(tokenPreferences, createRefreshService()))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    fun createRefreshService(): RefreshService {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RefreshService::class.java)
    }
}