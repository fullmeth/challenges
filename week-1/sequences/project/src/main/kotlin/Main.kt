package org.example

data class Task(val id: Long, val createdById: Long, val isOverdue: Boolean)

data class UiTask(val id: Long, val name: String, val isOverdue: Boolean)

fun Task.toUi() = UiTask(id = id, name = "Created by $createdById", isOverdue = isOverdue)

fun main() {
    println("Hello World!")
    var size: Int = 30

    val list = ArrayList<Task>()

    for (i in 0 until size) {
        list.add(
            Task(
                id = i.toLong(),
                createdById = (i + 1000).toLong(),
                isOverdue = (i % 2 == 0)
            )
        )
    }

    val newListEager = list
        .map { it.toUi() }
        .filter { it.isOverdue }
        .take(5)

    println(newListEager)

    val newListLazy = list
        .asSequence()
        .map { it.toUi() }
        .filter { it.isOverdue }
        .take(5)
        .toList()

    println(newListLazy)
}