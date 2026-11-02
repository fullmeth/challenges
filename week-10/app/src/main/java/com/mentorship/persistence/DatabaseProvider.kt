package com.mentorship.persistence

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver

object DatabaseProvider {

    private var database: AppDatabase? = null

    @Synchronized
    fun getDatabase(context: Context): AppDatabase = database ?: Room
        .databaseBuilder<AppDatabase>(context, "persistence_assignment")
        .setDriver(AndroidSQLiteDriver())
        .build()
        .also { database = it }
}