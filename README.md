# ThreadBoost AI

**Intelligent Distributed Thread Execution Platform** — a Java 21 + Spring Boot project focused on concurrency, distributed systems, and backend engineering (not CRUD).

## Tech stack

- Java 21 · Spring Boot 3.x · PostgreSQL · (later: Kafka, Redis, Spring AI)
- Docker · Prometheus/Grafana · JUnit · Testcontainers

## Project phases

| Phase | Status | Focus |
|-------|--------|-------|
| 1 | ✅ Done | Job management APIs |
| 2 | Planned | ThreadBoost execution engine |
| 3 | Planned | Kafka event bus |
| 4 | Planned | Redis locks & rate limiting |
| 5 | Planned | Spring AI agents |
| 6 | Planned | Observability |
| 7 | Planned | JMeter benchmarks |

Details: [docs/PHASE-1.md](docs/PHASE-1.md)

## Quick start

**Prerequisites:** Java 21+, Docker

```bash
# Start PostgreSQL
docker compose up -d

# Run app (Maven wrapper)
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

**Create a job:**

```http
POST http://localhost:8080/api/jobs
Content-Type: application/json

{ "description": "Generate customer reports" }
```

**Health:** `GET http://localhost:8080/actuator/health`

## Run tests

```bash
./mvnw test
```

Integration tests need Docker (Testcontainers).

## Repo

https://github.com/Pt1234567/ThreadBoost

## License

MIT (add license file if you publish publicly)
