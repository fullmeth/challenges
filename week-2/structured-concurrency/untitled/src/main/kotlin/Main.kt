package org.example

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

suspend fun main() {
    val api = Api()

    println("Strict: " + loadDashboardStrict(api))
    println("Tolerant: " + loadDashboardTolerant(api))
    println("TolerantWithSupervisor: " + loadDashboardTolerantWithSupervisor(api))
}

suspend fun loadDashboardStrict(api: Api): Dashboard? = runOrNull {
    coroutineScope {
        val profile = async { api.getUserProfile() }
        val tasks = async { api.getUserTasksSummary() }
        val stats = async { api.getUserStats() }
        Dashboard(profile.await(), tasks.await(), stats.await())
    }
}

suspend fun loadDashboardTolerant(api: Api): Dashboard = coroutineScope {
    val profile = async { runOrNull { api.getUserProfile() } }
    val tasks = async { runOrNull { api.getUserTasksSummary() } }
    val stats = async { runOrNull { api.getUserStats() } }
    Dashboard(profile.await() ?: Profile(), tasks.await() ?: emptyList(), stats.await())
}

suspend fun loadDashboardTolerantWithSupervisor(api: Api): Dashboard = supervisorScope {
    val profile = async { api.getUserProfile() }
    val tasks = async { api.getUserTasksSummary() }
    val stats = async { api.getUserStats() }
    Dashboard(
        runOrNull { profile.await() } ?: Profile(),
        runOrNull { tasks.await() } ?: emptyList(),
        runOrNull { stats.await() },
    )
}

class Api {
    suspend fun getUserProfile(): Profile {
        delay(250.milliseconds)
        return Profile("John", 15)
    }

    suspend fun getUserTasksSummary(): List<Task> {
        delay(500.milliseconds)
        return listOf(
            Task(name = "Task1", description = "DetailedTask1"),
            Task(name = "Task2", description = "DetailedTask2"),
        )
    }

    suspend fun getUserStats(): Stats {
        delay(150.milliseconds)
        throw Exception("Something went wrong")
    }
}

data class Profile(val name: String = "No user", val age: Int = -1)

data class Task(val name: String, val description: String)

data class Stats(val stats: String)

data class Dashboard(
    val profile: Profile,
    val tasks: List<Task>,
    val stats: Stats? = null,
)

suspend fun <T> runOrNull(block: suspend () -> T): T? = try {
    block()
} catch (e: Exception) {
    if (e is CancellationException) throw e
    null
}