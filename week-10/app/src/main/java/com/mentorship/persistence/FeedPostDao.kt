package com.mentorship.persistence

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedPostDao {

    @Query("SELECT * FROM FeedPostEntity")
    fun getAllFeedPosts(): Flow<List<FeedPostEntity>>

    @Transaction
    @Query("SELECT * FROM FeedPostEntity")
    fun getAllFeedPostWithMediaFiles(): Flow<List<FeedPostWithMediaFiles>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg feedPost: FeedPostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(feedPostMediaFileCrossRef: FeedPostMediaFileCrossRef)
}