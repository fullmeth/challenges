package com.mentorship.project.api

import kotlinx.serialization.Serializable

@Serializable
data class DummyDto(
    val text: String,
    val status: String,
)
