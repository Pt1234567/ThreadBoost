# Phase 1 - Job Management System

Phase 1 builds the base backend service for ThreadBoost AI. The focus is a clean job API, database persistence, validation, error handling, and tests.

## Architecture

Phase 1 is a single Spring Boot service with a classic layered design:

```text
Client (HTTP)
  -> JobController
  -> JobService
  -> JobRepository (JPA)
  -> PostgreSQL / H2 in tests
```

Starting with one service keeps the project easy to understand and debug. Kafka, Redis, AI agents, and distributed workers can be added later without changing the basic job model.

## Folder structure

```text
src/main/java/com/threadboost/
  ThreadBoostApplication.java
  controller/          REST endpoints
  service/             business logic and transactions
  repository/          Spring Data JPA
  domain/
    entity/            Job table mapping
    enums/             Status, priority, strategy, type
  dto/
    request/           incoming API payloads
    response/          outgoing API payloads
  mapper/              Entity to DTO conversion
  exception/           API error handling
```

## APIs

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/jobs` | Create job with `{ "description": "..." }` |
| GET | `/api/jobs/{id}` | Get job by UUID |
| GET | `/api/jobs` | List all jobs |
| DELETE | `/api/jobs/{id}` | Delete job |

## Design choices

1. UUID primary key: useful when jobs are later handled by multiple workers or services.
2. Enums stored as strings: easier to read in SQL and safer than ordinal enum storage.
3. `@PrePersist` defaults: new jobs always get an id, timestamp, status, type, priority, strategy, and retry count.
4. DTOs separate from entity: the API can evolve without exposing every database detail directly.
5. `JobMapper`: keeps controller and service code simple.
6. `GlobalExceptionHandler`: returns consistent JSON errors for validation and not-found cases.
7. `@Transactional`: keeps database work inside clear service method boundaries.

## Run locally

```bash
docker compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Create a job:

```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -d "{\"description\":\"Generate customer reports\"}"
```

## Tests added in Phase 1

- Controller tests for create, list, get, delete, validation, and not-found handling.
- Service tests for creation and missing jobs.
- H2 integration test for the create-get-list-delete lifecycle.
- PostgreSQL Testcontainers test that runs only when Docker is available.

## Interview questions

1. Why separate DTO from entity?

Entity maps to the database. DTO controls what the API accepts and returns.

2. What does `@Transactional` do here?

It gives each service method a clear database transaction boundary. Read methods use `readOnly = true`.

3. Why use UUID instead of `Long`?

UUIDs work well when multiple systems may create or process jobs. The tradeoff is a larger database index.

4. How does validation work?

`CreateJobRequest` uses Jakarta Bean Validation, and the controller uses `@Valid`. Validation failures are converted into JSON errors.

5. Why use Testcontainers?

It tests against a real PostgreSQL database, which is closer to production than H2.

## Next phase

Phase 2 starts the execution engine with `POST /api/jobs/{id}/execute`.
