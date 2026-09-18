package com.mentorship.title.auth

data class LoginResponse(
    val username: String,
    val firstName: String,
    val lastName: String,
    val accessToken: String,
    val refreshToken: String,
)