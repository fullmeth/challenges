package org.example

data class Task(val id: Long, val createdById: Long, val isOverdue: Boolean)

data class UiTask(val id: Long, val name: String, val isOverdue: Boolean)

fun Task.toUi() = UiTask(id = id, name = "Created by $createdById", isOverdue = isOverdue)

fun main() {
    println("Hello World!")
}