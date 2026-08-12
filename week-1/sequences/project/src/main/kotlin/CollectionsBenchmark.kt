package org.example

import kotlinx.benchmark.*

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@State(Scope.Benchmark)
class CollectionsBenchmark {

    @Param("20", "1000000")
    var size: Int = 0

    private val list = ArrayList<Task>()

    @Setup
    fun setup() {
        for (i in 0 until size) {
            list.add(
                Task(
                    id = i.toLong(),
                    createdById = (i + 1000).toLong(),
                    isOverdue = (i % 2 == 0)
                )
            )
        }
    }

    @TearDown
    fun tearDown() {
        list.clear()
    }

    @Benchmark
    fun benchmarkEagerly(blackHole: Blackhole) {
        val newList = list
            .map { it.toUi() }
            .filter { it.isOverdue }
            .take(5)
        blackHole.consume(newList)
    }

    @Benchmark
    fun benchmarkLazily(blackHole: Blackhole) {
        val newList = list
            .asSequence()
            .map { it.toUi() }
            .filter { it.isOverdue }
            .take(5)
            .toList()
        blackHole.consume(newList)
    }
}