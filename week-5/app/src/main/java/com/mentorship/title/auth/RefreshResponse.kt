package com.mentorship.title.auth

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)