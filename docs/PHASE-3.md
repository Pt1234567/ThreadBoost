# Phase 3 - Event Architecture

This phase adds the event-driven shape of the system without requiring Kafka to run locally.

## Current implementation

ThreadBoost now has a `JobEventPublisher` interface and an `InMemoryJobEventPublisher` implementation.

Published topics:

- `job-created`
- `job-completed`
- `job-failed`

## API

```http
GET /api/events
```

Returns recent local events. This is a learning-friendly stand-in for Kafka topic inspection.

## Why in-memory first?

Kafka adds brokers, serializers, networking, retries, and local setup complexity. The important first step is learning where events belong in the service flow. Once that is clear, `InMemoryJobEventPublisher` can be replaced by a Kafka producer.

## Code flow

```text
JobService
  -> JobEventPublisher
  -> InMemoryJobEventPublisher
  -> /api/events
```

## Next production step

Replace the in-memory publisher with a Kafka-backed implementation:

- `job-created` producer from Job Service
- Worker Service consumer
- retry topic
- dead-letter topic
- JSON serialization tests

## Interview questions

1. Why publish events from the service layer?

The service layer owns lifecycle decisions, so it knows when a job is created, completed, or failed.

2. Why use an interface before Kafka?

It keeps business code independent from the transport. Kafka becomes an implementation detail.

3. What is a DLQ?

A dead-letter queue stores messages that repeatedly fail processing, so they can be inspected without blocking healthy messages.
