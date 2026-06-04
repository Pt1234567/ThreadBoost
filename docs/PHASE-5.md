# Phase 5 - AI Agent Engine

This phase adds AI-agent-shaped services using deterministic rules.

The project does not call an LLM yet. That is intentional: the controller contracts, inputs, outputs, tests, and service boundaries are now clear. Spring AI can later replace the heuristic logic.

## Agents

| Agent | Endpoint | Purpose |
|-------|----------|---------|
| Job Classification Agent | `POST /api/ai/classify` | Pick job type, priority, and executor |
| Thread Optimizer Agent | `POST /api/ai/optimize` | Recommend executor from system pressure |
| Failure Analysis Agent | `POST /api/ai/failures/analyze` | Explain common failure patterns |

## Classify workload

```http
POST /api/ai/classify
Content-Type: application/json

{
  "description": "Generate reports and email users"
}
```

Example response:

```json
{
  "type": "EMAIL",
  "priority": "HIGH",
  "executor": "VIRTUAL_THREAD",
  "reason": "Email and notification jobs are usually IO-heavy."
}
```

## Optimize executor

```http
POST /api/ai/optimize
Content-Type: application/json

{
  "cpuUsage": 80,
  "queueSize": 1000,
  "activeThreads": 200
}
```

High pressure recommends `KAFKA_WORKER`.

## Analyze failure

```http
POST /api/ai/failures/analyze
Content-Type: application/json

{
  "logs": "RejectedExecutionException while submitting task"
}
```

## Why heuristic first?

For a fresher backend project, a predictable agent is easier to test and explain. It also keeps the app runnable without API keys.

## Spring AI next step

Replace `AiAgentService` internals with Spring AI prompts while keeping the same request and response DTOs.

## Interview questions

1. Why keep AI logic behind a service?

It keeps controllers stable and lets us swap heuristics for LLM calls later.

2. Why should AI output be structured?

Backend services need predictable fields for validation, persistence, and execution decisions.

3. Why not let AI execute jobs directly?

The AI should recommend. The backend should validate and enforce rules.
