# Phase 7 - Performance Testing

This phase adds the first performance testing artifact.

## Current implementation

Starter JMeter plan:

```text
performance/jmeter/threadboost-smoke.jmx
```

It sends simple `POST /api/jobs` requests against `localhost:8080`.

## Benchmark roadmap

Run these test sizes later:

- 1,000 jobs
- 10,000 jobs
- 50,000 jobs

Compare:

- single-thread execution
- custom thread pool
- virtual threads
- fork/join
- Kafka worker

Track:

- requests per second
- p95 latency
- p99 latency
- CPU usage
- memory usage
- queue size
- failed jobs

## Fresher-friendly benchmark rule

Change one variable at a time. For example, compare only `corePoolSize` changes before also changing queue size or workload type.

## Interview questions

1. Why use p95 instead of only average latency?

Average latency hides slow requests. p95 shows what most real users experience under load.

2. Why benchmark virtual threads separately?

Virtual threads help blocking IO-heavy work, but CPU-heavy work still needs CPU cores.

3. Why document benchmark setup?

Benchmarks are only useful when someone else can reproduce them.
