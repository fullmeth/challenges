# CollectionsBenchmark — Tasks & Users Combination Strategies — JMH Results

**Benchmark class:** `org.example.CollectionsBenchmark`  
**Mode:** Average time (`avgt`)  
**Units:** ns/op across all three `withDelay` tiers — `simulated` (rough network latency), `1 ns` (a
1-nanosecond delay added purely to force `suspend` functions to actually suspend, preventing the compiler/runtime from
optimizing away the coroutine suspension point), and `0 ns` (no delay).  
**Iterations:** 10 warm-up + 20 measurement per configuration  
**Strategies covered:** `AndOnlyNeededUsersThenCombine`, `AndUsersThenCombineParallel`, `AndUsersThenCombineSequential`
(each in `find`-based and `Map`-based lookup variants), plus `WithUsersOneByOne`

## Summary

## with network latency simulation

| Benchmark                     | Lookup | Delay |      Score (ns/op) | Error (±99.9%) |
|-------------------------------|:------:|:-----:|-------------------:|---------------:|
| AndOnlyNeededUsersThenCombine |  find  | true  |    352,031,480.000 |    387,693.968 |
| AndOnlyNeededUsersThenCombine |  map   | true  |    352,219,325.000 |    341,332.018 |
| AndUsersThenCombineParallel   |  find  | true  |    250,927,740.000 |    242,151.240 |
| AndUsersThenCombineParallel   |  map   | true  |    251,076,981.250 |    205,240.030 |
| AndUsersThenCombineSequential |  find  | true  |    401,873,118.333 |    418,679.855 |
| AndUsersThenCombineSequential |  map   | true  |    402,010,075.000 |    513,637.927 |
| WithUsersOneByOne             |        | true  | 10,241,903,820.000 |  5,825,952.790 |

## with 1 nanosecond delay

| Benchmark                     | Lookup | Delay |     Score (ns/op) | Error (±99.9%) |
|-------------------------------|:------:|:-----:|------------------:|---------------:|
| AndOnlyNeededUsersThenCombine |  find  | 1 ns  |     4,076,963.507 |     43,176.702 |
| AndOnlyNeededUsersThenCombine |  map   | 1 ns  |     4,076,810.021 |     60,770.725 |
| AndUsersThenCombineParallel   |  find  | 1 ns  |     2,103,376.391 |     29,711.611 |
| AndUsersThenCombineParallel   |  map   | 1 ns  |     2,146,753.422 |     28,356.407 |
| AndUsersThenCombineSequential |  find  | 1 ns  |     4,006,650.619 |    102,019.182 |
| AndUsersThenCombineSequential |  map   | 1 ns  |     4,062,284.721 |     65,506.899 |
| WithUsersOneByOne             |        | 1 ns  |   189,624,466.667 |  2,260,885.614 |

## without delay

| Benchmark                     | Lookup | Delay |     Score (ns/op) | Error (±99.9%) |
|-------------------------------|:------:|:-----:|------------------:|---------------:|
| AndOnlyNeededUsersThenCombine |  find  | 0 ns  |         9,379.193 |         18.433 |
| AndOnlyNeededUsersThenCombine |  map   | 0 ns  |         9,362.527 |         24.322 |
| AndUsersThenCombineParallel   |  find  | 0 ns  |         1,465.652 |          3.337 |
| AndUsersThenCombineParallel   |  map   | 0 ns  |         4,114.942 |         29.442 |
| AndUsersThenCombineSequential |  find  | 0 ns  |           896.915 |          3.045 |
| AndUsersThenCombineSequential |  map   | 0 ns  |         3,371.960 |         33.671 |
| WithUsersOneByOne             |        | 0 ns  |         1,377.354 |          2.988 |

## master table

| Benchmark                     | Lookup | Delay |      Score (ns/op) | Error (±99.9%) |
|-------------------------------|:------:|:-----:|-------------------:|---------------:|
| AndOnlyNeededUsersThenCombine |  find  | true  |    352,031,480.000 |    387,693.968 |
| AndOnlyNeededUsersThenCombine |  find  | 1 ns  |      4,076,963.507 |     43,176.702 |
| AndOnlyNeededUsersThenCombine |  find  | 0 ns  |          9,379.193 |         18.433 |
| AndOnlyNeededUsersThenCombine |  map   | true  |    352,219,325.000 |    341,332.018 |
| AndOnlyNeededUsersThenCombine |  map   | 1 ns  |      4,076,810.021 |     60,770.725 |
| AndOnlyNeededUsersThenCombine |  map   | 0 ns  |          9,362.527 |         24.322 |
| AndUsersThenCombineParallel   |  find  | true  |    250,927,740.000 |    242,151.240 |
| AndUsersThenCombineParallel   |  find  | 1 ns  |      2,103,376.391 |     29,711.611 |
| AndUsersThenCombineParallel   |  find  | 0 ns  |          1,465.652 |          3.337 |
| AndUsersThenCombineParallel   |  map   | true  |    251,076,981.250 |    205,240.030 |
| AndUsersThenCombineParallel   |  map   | 1 ns  |      2,146,753.422 |     28,356.407 |
| AndUsersThenCombineParallel   |  map   | 0 ns  |          4,114.942 |         29.442 |
| AndUsersThenCombineSequential |  find  | true  |    401,873,118.333 |    418,679.855 |
| AndUsersThenCombineSequential |  find  | 1 ns  |      4,006,650.619 |    102,019.182 |
| AndUsersThenCombineSequential |  find  | 0 ns  |            896.915 |          3.045 |
| AndUsersThenCombineSequential |  map   | true  |    402,010,075.000 |    513,637.927 |
| AndUsersThenCombineSequential |  map   | 1 ns  |      4,062,284.721 |     65,506.899 |
| AndUsersThenCombineSequential |  map   | 0 ns  |          3,371.960 |         33.671 |
| WithUsersOneByOne             |        | true  | 10,241,903,820.000 |  5,825,952.790 |
| WithUsersOneByOne             |        | 1 ns  |    189,624,466.667 |  2,260,885.614 |
| WithUsersOneByOne             |        | 0 ns  |          1,377.354 |          2.988 |

## Observations

### with network latency simulation

**`Parallel`** is the fastest, ~251 ms/op (`find` 250,927,740.000 ns/op, `Map` 251,076,981.250 ns/op).  
**`WithUsersOneByOne`** is the worst, ~10 seconds!/op.

### 1 nanosecond delay

**`Parallel`** is the fastest at ~2.1–2.15 ms/op (`find` 2,103,376.391 ns/op, `Map` 2,146,753.422 ns/op).  
**`WithUsersOneByOne`** is the worst at ~189 ms/op.

### `withDelay=false`

**`Sequential`** (find) is the fastest at 896.915 ns/op.  
**`OnlyNeededUsersThenCombine`** (`find` and `Map`) is the worst at ~9,362-9,379 ns/op.