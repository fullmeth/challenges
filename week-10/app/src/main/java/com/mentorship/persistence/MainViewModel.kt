package com.mentorship.persistence

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.room3.withWriteTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DatabaseProvider.getDatabase(application)

    val usersWithAssignedTasks = db.userDao().getAllUsersWithAssignedTasks()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    val usersWithCreatedTasks = db.userDao().getAllUsersWithCreatedTasks()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())
    val mediaFiles = db.mediaFileDao().getAllMediaFiles()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    val users = db.userDao().getAllUsers()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    val tasks = db.taskDao().getAllTasks()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    val feedPosts = db.feedPostDao().getAllFeedPosts()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    val tasksWithAssignedUsers = db.taskDao().getAllTasksWithAssignedUsers()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    val tasksWithMediaFiles = db.taskDao().getAllTasksWithMediaFiles()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    val feedPostsWithMediaFiles = db.feedPostDao().getAllFeedPostWithMediaFiles()
        .stateIn(viewModelScope, WhileSubscribed(5_000L), emptyList())

    suspend fun seedDatabase() = db.withWriteTransaction {
        db.clearAllTables()
        val user1 = UserEntity(1, "John", "Janitor")
        val user2 = UserEntity(2, "Marc", "DevOps")
        db.userDao().insert(user1, user2)
        val task1 = TaskEntity(10, "Do the CI/CD", LocalDateTime.now().plusDays(1), 1)
        val task2 = TaskEntity(20, "Do the cleanup", LocalDateTime.now().plusDays(1), 2)
        db.taskDao().insert(task1, task2)
        db.userDao().insert(AssignedTasksCrossRef(10, 2), AssignedTasksCrossRef(20, 2))
        val post1 = FeedPostEntity(100, "FeedPost1", "FeedPost1Desc")
        val post2 = FeedPostEntity(200, "FeedPost2", "FeedPost2Desc")
        db.feedPostDao().insert(post1, post2)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onLoadDocument(uri: Uri?) = with(application.applicationContext) {
        uri?.let { uri ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val type = contentResolver.getType(uri)?.let {
                        when {
                            it.startsWith("image/")  -> 12
                            it.startsWith("video/")  -> 24
                            else -> return@withContext
                        }
                    } ?: return@withContext
                    val name = Uuid.generateV7().toString()
                    contentResolver.openInputStream(uri).use { inputStream ->
                        inputStream?.let {
                            openFileOutput(
                                /* name = */ name,
                                /* mode = */ Context.MODE_PRIVATE
                            ).use { outputStream -> inputStream.copyTo(outputStream) }
                        }
                    }

                    val file = File("${filesDir}/$name")
                    if (file.exists()) {
                        db.withWriteTransaction {
                            val id = db.mediaFileDao().insert(
                                MediaFileEntity(
                                    name = name,
                                    fullPathInStorage = file.absolutePath,
                                    type = type
                                )
                            )
                            if (Random.nextBoolean()) {
                                val taskId = if (Random.nextBoolean()) 20L else 10L
                                db.taskDao().insert(TaskMediaFileCrossRef(taskId, id))
                            } else {
                                val feedPostId = if (Random.nextBoolean()) 200L else 100L
                                db.feedPostDao().insert(FeedPostMediaFileCrossRef(feedPostId, id))
                            }
                        }
                    }
                }
            }
        }
    }
}