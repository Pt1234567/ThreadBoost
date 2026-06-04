# Phase 2 - Basic Execution Engine

Phase 2 begins converting stored jobs into executable work. This is intentionally small for now: execute one pending job through an HTTP endpoint and record the status changes.

## Current scope

| Feature | Status |
|---------|--------|
| Execute pending job endpoint | Done |
| `JobExecutor` interface | Done |
| Simple executor implementation | Done |
| Status transition tracking | Done |
| Conflict response for invalid state | Done |
| Simple executor stats endpoint | Done |
| Custom thread pool execution | Done |
| Runtime thread pool config | Done |
| JVM thread health analyzer | Done |
| Deadlock detection API | Done |
| Race condition simulator | Done |
| CompletableFuture pipeline demo | Done |
| Virtual thread demo | Done |
| Virtual thread strategy | Planned |

## API

```http
POST /api/jobs/{id}/execute
```

For a pending job, the current flow is:

```text
PENDING -> RUNNING -> SUCCESS
```

If the job is not `PENDING`, the API returns `409 Conflict`.

```http
GET /api/executor/stats
```

Returns the current executor mode, number of finished executions, active jobs, and available processors.

```http
GET /api/thread/metrics
```

Returns core pool size, max pool size, active threads, queue size, completed tasks, and rejected tasks.

```http
POST /api/thread/config
Content-Type: application/json

{
  "corePoolSize": 4,
  "maxPoolSize": 12
}
```

Updates the executor size at runtime. `corePoolSize` must be less than or equal to `maxPoolSize`.

```http
GET /api/thread/health
```

Counts JVM threads by state: `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, and `TERMINATED`.

```http
GET /api/thread/deadlocks
```

Uses `ThreadMXBean.findDeadlockedThreads()` to report whether the JVM currently has deadlocked threads.

```http
POST /api/simulation/race
```

Runs multiple threads against three counters:

- unsafe integer counter
- `AtomicInteger` counter
- `ReentrantLock` protected counter

The unsafe count may lose updates. The atomic and locked counts should match the expected count.

```http
POST /api/lab/completable-future/report
```

Runs a small pipeline:

```text
fetch user data + fetch analytics
  -> combine
  -> format report
  -> notify user
```

This demonstrates `supplyAsync`, `thenCombine`, `thenApply`, `exceptionally`, and `join`.

```http
POST /api/lab/virtual-threads/demo
Content-Type: application/json

{
  "taskCount": 100
}
```

Runs many small blocking-style tasks using `Executors.newVirtualThreadPerTaskExecutor()`.

## Code flow

```text
JobController
  -> JobService.executeJob(id)
  -> JobExecutor.execute(job)
  -> Job entity status/timestamps updated
```

## Why keep execution synchronous first?

As a fresher backend project, synchronous execution is easier to inspect and test. It lets us learn status transitions, error handling, and API behavior before adding thread pools, queues, or background workers.

## What changed in this phase

- Added `JobExecutor` as a small interface.
- Added `SimpleJobExecutor` as the first implementation.
- Added `POST /api/jobs/{id}/execute`.
- Added `GET /api/executor/stats`.
- Added custom `ThreadPoolExecutor` with named worker threads.
- Added priority-aware task queue using `PriorityBlockingQueue`.
- Added `GET /api/thread/metrics` and `POST /api/thread/config`.
- Added `ThreadHealthAnalyzer` using JVM `ThreadMXBean`.
- Added race condition simulator using unsafe, atomic, and locked counters.
- Added CompletableFuture report pipeline.
- Added Java 21 virtual thread demo.
- Added `InvalidJobStateException` and `409 Conflict` handling.
- Added service and controller tests for execution.

## Next steps

1. Move real job execution into background work while returning quickly from the API.
2. Add a proper execution strategy selector.
3. Add rejection-policy examples.
4. Add Kafka event publishing for `job-created`, `job-completed`, and `job-failed`.

## Interview questions

1. Why use an interface for `JobExecutor`?

It lets the service depend on a simple contract. Later, thread pool, virtual thread, and Kafka worker implementations can use the same service flow.

2. Why return `409 Conflict` for already executed jobs?

The request is valid, but the current job state does not allow the operation.

3. Why not start with async execution immediately?

Async execution adds lifecycle complexity. First we should make the state model correct, then move execution to the background.

4. Why can the unsafe counter lose updates?

`count++` is not one atomic operation. It reads, increments, and writes, so two threads can overwrite each other.

5. Why use `ThreadMXBean`?

It exposes JVM thread information without external tooling, which is useful for debugging blocked, waiting, and deadlocked threads.

6. When are virtual threads useful?

They are useful when tasks spend a lot of time blocked on IO. They are not a magic speedup for CPU-heavy work.

7. Why use `thenCombine` in the report pipeline?

It waits for two independent async results and combines them without blocking the calling thread early.
