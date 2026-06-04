# ThreadBoost AI

ThreadBoost AI is a Java 21 + Spring Boot backend project for learning concurrency, job execution, and distributed backend systems step by step.

The goal is not to build another CRUD app. Phase 1 starts with a clean job API, and Phase 2 begins turning those jobs into executable work.

## Tech stack

- Java 21
- Spring Boot 3.x
- Spring Web, Spring Data JPA, Validation, Actuator
- PostgreSQL for local development
- H2 for fast integration tests
- Testcontainers for real PostgreSQL tests when Docker is available
- Maven wrapper

## Project phases

| Phase | Status | Focus |
|-------|--------|-------|
| 1 | Done | Job management APIs |
| 2 | In progress | Basic execution engine |
| 3 | Planned | Kafka event bus |
| 3 | Done locally | Kafka-shaped local event bus |
| 4 | Done locally | Redis-shaped locks and rate limiting |
| 5 | Done locally | Heuristic AI agents |
| 6 | Starter | Observability artifacts |
| 7 | Starter | JMeter smoke benchmark |

## Current APIs

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/jobs` | Create a new pending job |
| GET | `/api/jobs/{id}` | Get one job |
| GET | `/api/jobs` | List jobs |
| POST | `/api/jobs/{id}/execute` | Execute a pending job |
| DELETE | `/api/jobs/{id}` | Delete a job |
| GET | `/api/executor/stats` | View simple execution counters |
| GET | `/api/thread/metrics` | View thread pool metrics |
| POST | `/api/thread/config` | Tune thread pool sizes at runtime |
| GET | `/api/thread/health` | Count JVM thread states |
| GET | `/api/thread/deadlocks` | Detect JVM deadlocks |
| POST | `/api/simulation/race` | Compare unsafe, atomic, and locked counters |
| POST | `/api/lab/completable-future/report` | Run a small CompletableFuture pipeline |
| POST | `/api/lab/virtual-threads/demo` | Run a Java 21 virtual thread demo |
| GET | `/api/events` | View local job events (`job-created`, `job-completed`, `job-failed`) |
| POST | `/api/distributed/locks/{lockKey}/acquire` | Try to acquire a local Redis-style lock |
| POST | `/api/distributed/locks/{lockKey}/release` | Release a local Redis-style lock |
| POST | `/api/distributed/rate-limit/check` | Run token-bucket rate limit check |
| POST | `/api/ai/classify` | Classify workload and recommend executor |
| POST | `/api/ai/optimize` | Recommend executor from system pressure |
| POST | `/api/ai/failures/analyze` | Analyze failure logs heuristically |
| GET | `/actuator/health` | Application health |

## Quick start

Prerequisites:

- Java 21+
- Docker, only required for PostgreSQL locally and Testcontainers tests

Start PostgreSQL:

```bash
docker compose up -d
```

Run the app:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Create a job:

```http
POST http://localhost:8080/api/jobs
Content-Type: application/json

{
  "description": "Generate customer reports"
}
```

Execute the job:

```http
POST http://localhost:8080/api/jobs/{id}/execute
```

Expected status flow for now:

```text
PENDING -> RUNNING -> SUCCESS
```

Check executor stats:

```http
GET http://localhost:8080/api/executor/stats
```

## Run tests

```bash
./mvnw test
```

On Windows, if `JAVA_HOME` is not configured:

```powershell
$env:JAVA_HOME='C:\PROGRA~1\Java\jdk-25.0.2'
.\mvnw.cmd test
```

The H2 tests run without Docker. The PostgreSQL Testcontainers test is skipped when Docker is not available.

## Documentation

- [Phase 1: Job Management](docs/PHASE-1.md)
- [Phase 2: Basic Execution Engine](docs/PHASE-2.md)
- [Phase 3: Event Architecture](docs/PHASE-3.md)
- [Phase 4: Distributed Features](docs/PHASE-4.md)
- [Phase 5: AI Agent Engine](docs/PHASE-5.md)
- [Phase 6: Observability](docs/PHASE-6.md)
- [Phase 7: Performance Testing](docs/PHASE-7.md)

## Repository

https://github.com/Pt1234567/ThreadBoost

## License

MIT, once a license file is added.
