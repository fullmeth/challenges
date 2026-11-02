package com.mentorship.persistence

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class,
        MediaFileEntity::class,
        FeedPostEntity::class,
        AssignedTasksCrossRef::class,
        TaskMediaFileCrossRef::class,
        FeedPostMediaFileCrossRef::class
    ],
    version = 1
)
@ColumnTypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun feedPostDao(): FeedPostDao
    abstract fun mediaFileDao(): MediaFileDao
}