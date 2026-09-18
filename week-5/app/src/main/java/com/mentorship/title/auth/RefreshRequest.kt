package com.mentorship.title.auth

data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int = 1,
)