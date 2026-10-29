package com.mentorship.compose.data.repository

import com.mentorship.compose.data.entity.User
import com.mentorship.compose.data.mock.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    override fun getAll(): Flow<List<User>> = flowOf(MockData.users)

    override fun getByIds(ids: Set<String>): Flow<List<User>> =
        flowOf(MockData.users.filter { it.id in ids })

    override fun getById(id: String): Flow<User?> =
        flowOf(MockData.users.firstOrNull { it.id == id })
}