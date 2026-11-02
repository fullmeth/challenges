package com.mentorship.persistence

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Transaction
    @Query("SELECT * FROM UserEntity")
    fun getAllUsersWithCreatedTasks(): Flow<List<UserWithCreatedTasks>>

    @Transaction
    @Query("SELECT * FROM UserEntity")
    fun getAllUsersWithAssignedTasks(): Flow<List<UserWithAssignedTasks>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg assignedTasksCrossRef: AssignedTasksCrossRef)
}