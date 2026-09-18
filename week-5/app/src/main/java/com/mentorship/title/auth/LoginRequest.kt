package com.mentorship.title.auth

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = 1,
)