import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTime

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

class ApiClient {
    // count = 100
    suspend fun getAllTasksBy(projectId: Long): List<Task> {
        delay(150.milliseconds) // Rough estimation of network call
        return dummyTasks
    }

    suspend fun getSingleUserBy(id: Long): User {
        delay(100.milliseconds)
        return dummyUsers.find { it.id == id }!!
    }

    // count = 250
    suspend fun getAllProjectUsers(projectId: Long): List<User> {
        delay(250.milliseconds)
        return dummyUsers
    }

    suspend fun getUsersBy(ids: List<Long>): List<User> {
        delay(200.milliseconds)
        return dummyUsers.filter { ids.contains(it.id) }
    }
}

data class User(val id: Long, val name: String)

data class UiUser(val id: Long, val name: String)

fun User.toUi() = UiUser(id, name)

fun Task.toUi(user: UiUser) = UiTask(
    taskId = id,
    name = user.name,
    user = user,
)

data class Task(val id: Long, val createdById: Long)

data class UiTask(val taskId: Long, val name: String, val user: UiUser)

suspend fun main() {
    println(measureTime { fetchAllTasksAndUsersThenCombine() }) // 419.879ms
    println(measureTime { fetchAllTasksAndOnlyNeededUsersThenCombine() }) // 353.481ms <--- Winner
    println(measureTime { fetchAllTasksWithUsersOneByOne() }) // 10.267423800s
}

suspend fun fetchAllTasksAndUsersThenCombine(): List<UiTask> {
    val apiClient = ApiClient()
    val apiTasks = apiClient.getAllTasksBy(1L)
    val users = apiClient.getAllProjectUsers(1L)
    return apiTasks.map { task -> task.toUi(users.find { it.id == task.createdById }!!.toUi()) }
}

suspend fun fetchAllTasksAndOnlyNeededUsersThenCombine(): List<UiTask> {
    val apiClient = ApiClient()
    val apiTasks = apiClient.getAllTasksBy(1L)
    val usersId = apiTasks.map { it.createdById }
    val users = apiClient.getUsersBy(usersId)
    return apiTasks.map { task -> task.toUi(users.find { it.id == task.createdById }!!.toUi()) }
}

suspend fun fetchAllTasksWithUsersOneByOne(): List<UiTask> {
    val apiClient = ApiClient()
    val apiTasks = apiClient.getAllTasksBy(1L)
    return apiTasks.map {
        val user = apiClient.getSingleUserBy(it.createdById).toUi()
        it.toUi(user)
    }
}