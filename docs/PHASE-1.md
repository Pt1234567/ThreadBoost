# Phase 1 — Job Management System

## Architecture

Phase 1 is a **single Spring Boot service** with a classic layered design:

```
Client (HTTP)
    → JobController
    → JobService
    → JobRepository (JPA)
    → PostgreSQL
```

**Why single service first?** As a fresher project, one deployable app keeps debugging simple. Kafka, workers, and AI agents come in later phases without rewriting the job model.

## Folder structure

```
src/main/java/com/threadboost/
├── ThreadBoostApplication.java
├── controller/          # REST endpoints
├── service/             # business logic + transactions
├── repository/          # Spring Data JPA
├── domain/
│   ├── entity/          # Job table mapping
│   └── enums/           # Status, Priority, Strategy, Type
├── dto/
│   ├── request/
│   └── response/
├── mapper/              # Entity ↔ DTO conversion
└── exception/           # API error handling
```

## APIs

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/jobs` | Create job (body: `{ "description": "..." }`) |
| GET | `/api/jobs/{id}` | Get job by UUID |
| GET | `/api/jobs` | List all jobs |
| DELETE | `/api/jobs/{id}` | Delete job |

## Design choices

1. **UUID primary key** — Safe for distributed workers later (no DB sequence collisions across services).
2. **Enums stored as STRING** — Readable in SQL and safe when you add new enum values carefully.
3. **`@PrePersist` defaults** — New jobs always start as `PENDING` with sensible defaults; API stays minimal (only `description` required for now).
4. **`JobMapper` component** — Keeps controllers thin; easy to extend when AI agents fill `type`, `priority`, `strategy` in Phase 5.
5. **`GlobalExceptionHandler`** — Consistent JSON errors for interviews and frontend clients.
6. **`@Transactional`** — Read-only on queries; write on create/delete.

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

## Interview questions (Phase 1)

1. **Why separate DTO from Entity?**  
   Entity maps to DB schema and JPA lifecycle; DTO exposes only what the API should return (hide internal fields, avoid lazy-load issues in JSON).

2. **What does `@Transactional` do here?**  
   One persistence context per service method; `readOnly=true` can optimize read queries and prevent accidental writes.

3. **Why UUID instead of Long id?**  
   Good for microservices and client-generated ids; tradeoff is slightly larger index size vs BIGINT.

4. **How does validation work?**  
   Jakarta Bean Validation on `CreateJobRequest` + `@Valid` on controller; failures handled by `MethodArgumentNotValidException` handler.

5. **What is Testcontainers used for?**  
   Spins real PostgreSQL in Docker for integration tests — closer to production than H2.

## Next: Phase 2

- `JobExecutor` interface + strategy implementations
- Thread pool metrics API
- Virtual threads, CompletableFuture, deadlock tools
