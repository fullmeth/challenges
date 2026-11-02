package com.mentorship.persistence

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class MediaFileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val fullPathInStorage: String,
    val type: Int,
)