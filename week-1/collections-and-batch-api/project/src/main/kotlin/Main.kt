package org.example

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

val dummyTasks: List<Task> = List(100) { index ->
    Task(
        id = (index + 1).toLong(),
        createdById = (index % 10 + 1).toLong()
    )
}

val dummyUsers: List<User> = List(250) { index ->
    User(
        id = (index + 1).toLong(),
        name = "User ${index + 1}"
    )
}

class ApiClient(private val delay: Delay) {
    // count = 100
    suspend fun getAllTasksBy(projectId: Long): List<Task> {
        val latency = when (delay) {
            Delay.simulation -> 150.milliseconds
            Delay.no_delay_forced_suspend -> 1.nanoseconds
            Delay.no_delay -> 0.milliseconds
        }
        delay(latency) // Rough estimation of network latency
        // delay(if (withDelay) 150.milliseconds else 1.nanoseconds)
        return dummyTasks
    }

    suspend fun getSingleUserBy(id: Long): User {
        val latency = when (delay) {
            Delay.simulation -> 100.milliseconds
            Delay.no_delay_forced_suspend -> 1.nanoseconds
            Delay.no_delay -> 0.milliseconds
        }
        delay(latency)
        return dummyUsers.find { it.id == id }!!
    }

    // count = 250
    suspend fun getAllProjectUsers(projectId: Long): List<User> {
        val latency = when (delay) {
            Delay.simulation -> 250.milliseconds
            Delay.no_delay_forced_suspend -> 1.nanoseconds
            Delay.no_delay -> 0.milliseconds
        }
        delay(latency)
        return dummyUsers
    }

    suspend fun getUsersBy(ids: List<Long>): List<User> {
        val latency = when (delay) {
            Delay.simulation -> 200.milliseconds
            Delay.no_delay_forced_suspend -> 1.nanoseconds
            Delay.no_delay -> 0.milliseconds
        }
        delay(latency)
        return dummyUsers.filter { ids.contains(it.id) }
    }
}

data class User(val id: Long, val name: String)

data class UiUser(val id: Long, val name: String)

fun UiUser?.orEmptyUser() = this ?: UiUser(-1, "No user associated with this ID")

fun User.toUi() = UiUser(id, name)

fun Task.toUi(user: UiUser) = UiTask(
    taskId = id,
    name = user.name,
    user = user,
)

data class Task(val id: Long, val createdById: Long)

data class UiTask(val taskId: Long, val name: String, val user: UiUser)

enum class Delay {
    simulation, no_delay_forced_suspend, no_delay
}

fun main() {
    println("Hola World!")
}

suspend fun fetchAllTasksAndUsersThenCombineSequential(delay: Delay): List<UiTask> {
    val apiClient = ApiClient(delay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    val users = apiClient.getAllProjectUsers(1L)
    return apiTasks.map { task -> task.toUi(users.find { it.id == task.createdById }?.toUi().orEmptyUser()) }
}

suspend fun fetchAllTasksAndUsersThenCombineParallel(delay: Delay): List<UiTask> {
    val apiClient = ApiClient(delay)
    return coroutineScope {
        val apiTasks = async { apiClient.getAllTasksBy(1L) }
        val apiUsers = async { apiClient.getAllProjectUsers(1L) }
        val tasks = apiTasks.await()
        val users = apiUsers.await()
        return@coroutineScope tasks.map { task ->
            task.toUi(users.find { it.id == task.createdById }?.toUi().orEmptyUser())
        }
    }
}

suspend fun fetchAllTasksAndOnlyNeededUsersThenCombine(delay: Delay): List<UiTask> {
    val apiClient = ApiClient(delay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    val usersId = apiTasks.map { it.createdById }
    val users = apiClient.getUsersBy(usersId)
    return apiTasks.map { task -> task.toUi(users.find { it.id == task.createdById }?.toUi().orEmptyUser()) }
}

suspend fun fetchAllTasksWithUsersOneByOne(delay: Delay): List<UiTask> {
    val apiClient = ApiClient(delay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    return apiTasks.map { it.toUi(apiClient.getSingleUserBy(it.createdById).toUi()) }
}

suspend fun fetchAllTasksAndUsersThenCombineSequentialMap(delay: Delay): List<UiTask> {
    val apiClient = ApiClient(delay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    val users = apiClient.getAllProjectUsers(1L).associateBy { it.id }
    return apiTasks.map { task -> task.toUi(users[task.createdById]?.toUi().orEmptyUser()) }
}

suspend fun fetchAllTasksAndUsersThenCombineParallelMap(delay: Delay): List<UiTask> {
    val apiClient = ApiClient(delay)
    return coroutineScope {
        val apiTasks = async { apiClient.getAllTasksBy(1L) }
        val apiUsers = async { apiClient.getAllProjectUsers(1L) }
        val tasks = apiTasks.await()
        val users = apiUsers.await().associateBy { it.id }
        return@coroutineScope tasks.map { task -> task.toUi(users[task.createdById]?.toUi().orEmptyUser()) }
    }
}

suspend fun fetchAllTasksAndOnlyNeededUsersThenCombineMap(delay: Delay): List<UiTask> {
    val apiClient = ApiClient(delay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    val usersId = apiTasks.map { it.createdById }
    val users = apiClient.getUsersBy(usersId).associateBy { it.id }
    return apiTasks.map { task -> task.toUi(users[task.createdById]?.toUi().orEmptyUser()) }
}