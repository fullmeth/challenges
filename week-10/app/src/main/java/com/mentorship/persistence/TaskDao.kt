package com.mentorship.persistence

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskEntity")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Transaction
    @Query("SELECT * FROM TaskEntity")
    fun getAllTasksWithMediaFiles(): Flow<List<TaskWithMediaFiles>>

    @Transaction
    @Query("SELECT * FROM TaskEntity")
    fun getAllTasksWithAssignedUsers(): Flow<List<TaskWithAssignedUsers>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskMediaFileCrossRef: TaskMediaFileCrossRef)
}