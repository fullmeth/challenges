package org.example


fun main() {

    println(fibonacciLoop(30))
    println(fibonacciRecursive(30))
}

fun fibonacciLoop(n: Int): Int {
    val list = ArrayList<Int>(n)
    for (i in 0..n) {
        if (i < 2) list.add(i)
        else list.add(list[i - 1] + list[i - 2])
    }
    return list[n]
}

fun fibonacciRecursive(n: Int): Int {
    return if (n < 2) n else fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2)
}