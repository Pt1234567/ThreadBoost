# Phase 6 - Observability

This phase documents the observability setup.

## Current implementation

Spring Actuator is enabled and exposes:

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`
- `/actuator/prometheus`

The project also includes:

- `monitoring/prometheus.yml`
- `monitoring/grafana/threadboost-dashboard.json`

## Metrics to watch

- JVM live threads
- HTTP request count and latency
- process CPU usage
- executor metrics from `/api/thread/metrics`
- job outcomes from `/api/events`

## Prometheus scrape endpoint

Prometheus should scrape `/actuator/prometheus`.

## Interview questions

1. Why monitor queue size?

Queue growth usually means producers are faster than workers.

2. Why monitor thread count?

Unexpected thread growth can indicate leaks, blocking calls, or bad executor sizing.

3. Why keep business metrics and JVM metrics together?

Backend performance issues often need both views: what users are doing and what the JVM is doing.
