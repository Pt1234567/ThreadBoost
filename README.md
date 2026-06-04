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
| 4 | Planned | Redis locks and rate limiting |
| 5 | Planned | Spring AI agents |
| 6 | Planned | Observability |
| 7 | Planned | JMeter benchmarks |

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

## Repository

https://github.com/Pt1234567/ThreadBoost

## License

MIT, once a license file is added.
