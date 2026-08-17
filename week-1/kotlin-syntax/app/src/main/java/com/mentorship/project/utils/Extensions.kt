package com.mentorship.project.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlinx.coroutines.CancellationException

suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> = try {
    val data = block()
    Result.Success(data = data)
} catch (e: Exception) {
    if (e is CancellationException) throw e
    Result.Error(e)
}

inline fun <T, R> Result.Success<T>.map(crossinline transform: (T) -> R): Result<R> {
    return Result.Success(transform(this.data))
}

inline fun <T> Result.Error.recover(crossinline transform: (Result.Error) -> T): Result<T> {
    return Result.Success(transform(this))
}

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key)
    }