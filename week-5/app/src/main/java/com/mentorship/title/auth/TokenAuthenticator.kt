package com.mentorship.title.auth

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenPreferences: TokenPreferences,
    private val api: RefreshService
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if (responseCount(response) > 1) {
            return@runBlocking null
        }

        mutex.withLock {
            val accessToken = tokenPreferences.accessToken
            val refreshToken = tokenPreferences.refreshToken

            if (accessToken.isNotBlank() && response.request.header("Authorization")
                    ?.removePrefix("Bearer ") != accessToken
            ) {
                return@runBlocking response.request.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            }


            return@runBlocking if (refreshToken.isNotBlank()) {
                val newTokens = safeApiCall { api.refresh(RefreshRequest(refreshToken)) }
                return@runBlocking when(newTokens) {
                    is Result.Error -> return@runBlocking null
                    is Result.Success -> {
                        tokenPreferences.accessToken = newTokens.data.accessToken
                        tokenPreferences.refreshToken = newTokens.data.refreshToken
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${newTokens.data.accessToken}")
                            .build()
                    }
                }
            } else null
        }
    }

    private fun responseCount(response: Response): Int {
        var current: Response? = response
        var result = 0
        while (current != null) {
            result++
            current = current.priorResponse
        }
        return result
    }
}