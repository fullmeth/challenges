package com.mentorship.title.auth

import kotlinx.coroutines.CancellationException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

inline fun <T> safeApiCall(block: () -> T): Result<T> = try {
    val data = block()
    Result.Success(data = data)
} catch (e: Exception) {
    if (e is CancellationException) throw e
    Result.Error(e)
}