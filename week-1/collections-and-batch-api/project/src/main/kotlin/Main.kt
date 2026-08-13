import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

class ApiClient(private val withDelay: Boolean) {
    // count = 100
    suspend fun getAllTasksBy(projectId: Long): List<Task> {
        if (withDelay) delay(150.milliseconds) // Rough estimation of network latency
        // delay(if (withDelay) 150.milliseconds else 1.nanoseconds)
        return dummyTasks
    }

    suspend fun getSingleUserBy(id: Long): User {
        if (withDelay) delay(100.milliseconds)
        return dummyUsers.find { it.id == id }!!
    }

    // count = 250
    suspend fun getAllProjectUsers(projectId: Long): List<User> {
        if (withDelay) delay(250.milliseconds)
        return dummyUsers
    }

    suspend fun getUsersBy(ids: List<Long>): List<User> {
        if (withDelay) delay(200.milliseconds)
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

/***
 * |-------------------- Approach ------------------|--- Delayed ---|- Non-delayed -|-- Delayed (1us) --|
 * | Fetch all tasks and all users (Sequential)     | 404.148600ms  | 1.574100ms    | 19.256700ms       |
 * | Fetch all tasks and all users (Parallel)       | 251.908800ms  | 27.391300ms   | 18.987700ms       |
 * | Fetch all tasks and only used users            | 351.641600ms  | 1.097900ms    | 5.448200ms        |
 * | Fetch all tasks and each user in .map()        | 10.245033100s | 783us         | 218.568900ms      |
 * |----------------------------------------------------------------------------------------------------|
 *
 * with that dataset fastest would be:
 *   fetchAllTasksAndUsersThenCombineParallel() with network latency simulation
 *   fetchAllTasksWithUsersOneByOne() without delay
 *   fetchAllTasksAndOnlyNeededUsersThenCombine() with 1.nanosecond delay (it seems that async is ditched w/o delay() just to test all cases :))
 */

suspend fun main() {
    listOf(false, true).forEach {
        println("Fetch all tasks and all users (Sequential) (delayed=$it): ${measureTime { fetchAllTasksAndUsersThenCombineSequential(it) }}")
        println("Fetch all tasks and all users (Parallel) (delayed=$it): ${measureTime { fetchAllTasksAndUsersThenCombineParallel(it) }}")
        println("Fetch all tasks and only used users (delayed=$it): ${measureTime { fetchAllTasksAndOnlyNeededUsersThenCombine(it) }}")
        println("Fetch all tasks and each user in .map() (delayed=$it): ${measureTime { fetchAllTasksWithUsersOneByOne(it) }}")
    }
}

suspend fun fetchAllTasksAndUsersThenCombineSequential(withDelay: Boolean): List<UiTask> {
    val apiClient = ApiClient(withDelay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    val users = apiClient.getAllProjectUsers(1L)
    return apiTasks.map { task -> task.toUi(users.find { it.id == task.createdById }?.toUi().orEmptyUser()) }
}

suspend fun fetchAllTasksAndUsersThenCombineParallel(withDelay: Boolean): List<UiTask> {
    val apiClient = ApiClient(withDelay)
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

suspend fun fetchAllTasksAndOnlyNeededUsersThenCombine(withDelay: Boolean): List<UiTask> {
    val apiClient = ApiClient(withDelay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    val usersId = apiTasks.map { it.createdById }
    val users = apiClient.getUsersBy(usersId)
    return apiTasks.map { task -> task.toUi(users.find { it.id == task.createdById }?.toUi().orEmptyUser()) }
}

suspend fun fetchAllTasksWithUsersOneByOne(withDelay: Boolean): List<UiTask> {
    val apiClient = ApiClient(withDelay)
    val apiTasks = apiClient.getAllTasksBy(1L)
    return apiTasks.map {
        val user = apiClient.getSingleUserBy(it.createdById).toUi()
        it.toUi(user)
    }
}