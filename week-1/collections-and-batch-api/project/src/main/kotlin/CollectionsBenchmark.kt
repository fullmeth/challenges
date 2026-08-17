package org.example

import kotlinx.benchmark.*
import kotlinx.coroutines.runBlocking

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@State(Scope.Benchmark)
class CollectionsBenchmark {

    @Param("simulation", "no_delay_forced_suspend", "no_delay")
    var delay: Delay = Delay.simulation

    @Benchmark
    fun benchmarkAllTasksAndUsersThenCombineSequential(blackhole: Blackhole) {
        runBlocking {
            val result = fetchAllTasksAndUsersThenCombineSequential(delay)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun benchmarkAllTasksAndUsersThenCombineParallel(blackhole: Blackhole) {
        runBlocking {
            val result = fetchAllTasksAndUsersThenCombineParallel(delay)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun benchmarkAllTasksAndOnlyNeededUsersThenCombine(blackhole: Blackhole) {
        runBlocking {
            val result = fetchAllTasksAndOnlyNeededUsersThenCombine(delay)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun benchmarkAllTasksWithUsersOneByOne(blackhole: Blackhole) {
        runBlocking {
            val result = fetchAllTasksWithUsersOneByOne(delay)
            blackhole.consume(result)
        }
    }

    @Benchmark
    fun benchmarkAllTasksAndUsersThenCombineSequentialMap(blackhole: Blackhole) {
        runBlocking {
            val result = fetchAllTasksAndUsersThenCombineSequentialMap(delay)
            blackhole.consume(result)
        }
    }


    @Benchmark
    fun benchmarkAllTasksAndUsersThenCombineParallelMap(blackhole: Blackhole) {
        runBlocking {
            val result = fetchAllTasksAndUsersThenCombineParallelMap(delay)
            blackhole.consume(result)
        }
    }


    @Benchmark
    fun benchmarkAllTasksAndOnlyNeededUsersThenCombineMap(blackhole: Blackhole) {
        runBlocking {
            val result = fetchAllTasksAndOnlyNeededUsersThenCombineMap(delay)
            blackhole.consume(result)
        }
    }
}