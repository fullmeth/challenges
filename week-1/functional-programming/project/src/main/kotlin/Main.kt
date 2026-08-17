package org.example


fun main() {

    println(fibonacciLoop(30))
    println(fibonacciRecursive(30))
    println(fibonacciTailRecursive(30))
}

fun fibonacciLoop(n: Int): Int {
    val list = arrayListOf<Int>()
    for (i in 0..n) {
        if (i < 2) list.add(i)
        else list.add(list[i - 1] + list[i - 2])
    }
    return list[n]
}

fun fibonacciRecursive(n: Int): Int {
    return if (n < 2) n else fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2)
}

fun fibonacciTailRecursive(n: Int): Int = fibonacciTailRec(n)

private tailrec fun fibonacciTailRec(n: Int, first: Int = 0, second: Int = 1): Int {
    return if (n == 0) first else fibonacciTailRec(n - 1, second, first + second)
}