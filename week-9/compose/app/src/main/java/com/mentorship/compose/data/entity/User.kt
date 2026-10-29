package com.mentorship.compose.data.entity

data class User(
    val firstName: String,
    val lastName: String,
    val position: String,
) {
    val id: String get() = firstName + lastName
    val displayName: String get() = "$firstName $lastName"
    val initials: String get() = "${firstName.first()}${lastName.first()}"

    companion object {
        fun empty() = User(
            firstName = "",
            lastName = "",
            position = ""
        )
    }
}