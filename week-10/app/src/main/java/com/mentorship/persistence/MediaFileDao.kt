package com.mentorship.persistence

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaFileDao {

    @Query("SELECT * FROM MediaFileEntity")
    fun getAllMediaFiles(): Flow<List<MediaFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mediaFile: MediaFileEntity): Long
}