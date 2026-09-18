package com.mentorship.title.auth

import android.content.Context

class AuthRepository(context: Context) {

    private val tokenPreferences = TokenPreferences(context)
    private val service = AuthNetwork(tokenPreferences).createService()
    suspend fun login(username: String, password: String) {
        val result = safeApiCall {
            service.login(LoginRequest(username, password))
        }
        when (result) {
            is Result.Error -> {
                /* no-op */
            }

            is Result.Success -> {
                tokenPreferences.accessToken = result.data.accessToken
                tokenPreferences.refreshToken = result.data.refreshToken
            }
        }
    }

    suspend fun profile(): Result<MeResponse> = safeApiCall {
        val accessToken = tokenPreferences.accessToken
        if (accessToken.isBlank()) throw Exception("no access token")
        service.me()
    }
}

