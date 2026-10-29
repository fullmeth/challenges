package com.mentorship.compose.data.repository

import com.mentorship.compose.data.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAll(): Flow<List<User>>
    fun getByIds(ids: Set<String>): Flow<List<User>>
    fun getById(id: String): Flow<User?>
}