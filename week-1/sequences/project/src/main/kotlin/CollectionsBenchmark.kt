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
    fun benchmarkEagerlyTake5(blackhole: Blackhole) {
        val newList = list
            .map { it.toUi() }
            .filter { it.isOverdue }
            .take(5)
        blackhole.consume(newList)
    }

    @Benchmark
    fun benchmarkEagerlyWholeList(blackhole: Blackhole) {
        val newList = list
            .map { it.toUi() }
            .filter { it.isOverdue }
        blackhole.consume(newList)
    }

    @Benchmark
    fun benchmarkEagerlyWholeListOnlyMap(blackhole: Blackhole) {
        val newList = list
            .map { it.toUi() }
        blackhole.consume(newList)
    }

    @Benchmark
    fun benchmarkLazilyTake5(blackhole: Blackhole) {
        val newList = list
            .asSequence()
            .map { it.toUi() }
            .filter { it.isOverdue }
            .take(5)
            .toList()
        blackhole.consume(newList)
    }

    @Benchmark
    fun benchmarkLazilyWholeList(blackhole: Blackhole) {
        val newList = list
            .asSequence()
            .map { it.toUi() }
            .filter { it.isOverdue }
            .toList()
        blackhole.consume(newList)
    }

    @Benchmark
    fun benchmarkLazilyWholeListOnlyMap(blackhole: Blackhole) {
        val newList = list
            .asSequence()
            .map { it.toUi() }
            .toList()
        blackhole.consume(newList)
    }
}