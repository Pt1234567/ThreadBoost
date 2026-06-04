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
| Real thread pool execution | Planned |
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
- Added `InvalidJobStateException` and `409 Conflict` handling.
- Added service and controller tests for execution.

## Next steps

1. Add a real `ThreadPoolTaskExecutor` bean.
2. Move execution into background work while returning quickly from the API.
3. Add tests for failed execution and retry count behavior.
4. Compare `THREAD_POOL` and `VIRTUAL_THREAD` strategies.

## Interview questions

1. Why use an interface for `JobExecutor`?

It lets the service depend on a simple contract. Later, thread pool, virtual thread, and Kafka worker implementations can use the same service flow.

2. Why return `409 Conflict` for already executed jobs?

The request is valid, but the current job state does not allow the operation.

3. Why not start with async execution immediately?

Async execution adds lifecycle complexity. First we should make the state model correct, then move execution to the background.
