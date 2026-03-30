# java-microservices-template

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.5.13-6DB33F?style=flat&logo=spring&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Maven-3.9.14-C71A36?style=flat&logo=apache-maven&logoColor=white" alt="Maven"/>
  <img src="https://img.shields.io/badge/Docker-29.2.1-2496ED?style=flat&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/tests-JUnit_5_·_Testcontainers-25A162?style=flat&logo=junit5&logoColor=white" alt="Tests"/>
  <img src="https://img.shields.io/badge/license-MIT-blue?style=flat" alt="License MIT"/>
</p>

> A professional Java archetype demonstrating enterprise-grade microservice design with
> **Clean Architecture**, **Hexagonal (Ports & Adapters)**, and **CQRS** — ready to clone, study, and adapt.

---

## 📌 What is this?

This repository is a **portfolio archetype** — not a deployable product, but a structured starting point that encodes production-level architectural decisions into a single, runnable project. It is designed to answer one question that matters in every engineering interview: *"How do you actually structure a Java microservice?"*

The template demonstrates how a single bounded context (`Order`) is implemented across all architectural layers: from a pure Java domain model with no framework dependencies, through explicit use case interfaces (ports), down to Spring Boot adapters for REST, JPA, Kafka, and RabbitMQ. Every class exists for a reason.

The test suite follows the same architectural discipline: pure unit tests for the domain, Mockito-based tests for application logic, and Testcontainers-powered integration tests that spin up real PostgreSQL, MongoDB, and RabbitMQ containers — no H2, no mocks in the infrastructure layer.

---

## 🛠 Tech Stack

| Technology | Version | Role |
|---|---|---|
| Java (Microsoft OpenJDK) | 21.0.10 | Language — Records, sealed types, virtual thread ready |
| Spring Boot | 3.5.13 | Application framework — autoconfiguration, DI, web |
| Apache Maven | 3.9.14 | Build tool — dependency management, lifecycle |
| PostgreSQL | 16 (Alpine) | Relational store — JPA/Hibernate, ACID transactions |
| MongoDB | 7.0 | Document store — polyglot persistence pattern |
| Apache Kafka (Confluent) | 7.6.0 | Event streaming — async domain event publishing |
| RabbitMQ | 3.13 + Management UI | Message broker — AMQP, task queues |
| Docker | 29.2.1 | Container runtime — full local stack in one command |
| JUnit 5 + Mockito | (Spring Boot BOM) | Unit and mock-based testing |
| Testcontainers | (Spring Boot BOM) | Real-infrastructure integration tests |
| SpringDoc OpenAPI | 2.8.16 | Auto-generated Swagger UI from code |
| Lombok | (Spring Boot BOM) | Compile-time boilerplate reduction |

---

## 🏛 Architecture

This project applies **Clean Architecture** combined with the **Hexagonal (Ports & Adapters)** pattern. The fundamental rule is that **dependencies always point inward**: infrastructure knows about application, application knows about domain — never the reverse.

```
┌──────────────────────────────────────────────────────────────┐
│                      INFRASTRUCTURE                          │
│                                                              │
│   REST Controllers · JPA Adapters · Kafka · RabbitMQ         │
│   Spring @Component, @RestController, @Repository            │
│                                                              │
│   ┌──────────────────────────────────────────────────────┐   │
│   │                   APPLICATION                        │   │
│   │                                                      │   │
│   │   Command Handlers · Query Handlers                  │   │
│   │   Port interfaces (in / out)                         │   │
│   │   @Service — orchestrates, never touches HTTP/SQL    │   │
│   │                                                      │   │
│   │   ┌────────────────────────────────────────────┐     │   │
│   │   │                 DOMAIN                     │     │   │
│   │   │                                            │     │   │
│   │   │   Order (aggregate root)                   │     │   │
│   │   │   OrderCreatedEvent · CreateOrderCommand   │     │   │
│   │   │                                            │     │   │
│   │   │   ✦ Zero Spring imports                    │     │   │
│   │   │   ✦ Pure Java — testable without context   │     │   │
│   │   └────────────────────────────────────────────┘     │   │
│   └──────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘

  shared/   ApiResponse<T> · BusinessException · ValidationUtils
            (cross-cutting — used by any layer, depends on none)

  Dependency rule:  INFRASTRUCTURE → APPLICATION → DOMAIN
                    ← never in the opposite direction →
```

### CQRS Split

Command and query responsibilities are separated at the handler level:

- **Write path**: `POST /api/v1/orders` → `CreateOrderCommandHandler` → `OrderRepositoryAdapter` → PostgreSQL
- **Read path**: `GET /api/v1/orders/{id}` → `GetOrderByIdQueryHandler` → `OrderRepositoryAdapter` → PostgreSQL

---

## 📁 Project Structure

```
src/main/java/pe/resteban/template/
│
├── domain/
│   ├── model/          # Order — aggregate root (@Getter @Builder, no Spring)
│   ├── events/         # OrderCreatedEvent — Java record (orderId, timestamp)
│   └── commands/       # CreateOrderCommand — Java record (orderId, description)
│
├── application/
│   ├── port/
│   │   ├── in/         # CreateOrderUseCase — inbound port (interface)
│   │   └── out/        # OrderRepository — outbound port (interface)
│   ├── command/        # CreateOrderCommandHandler — implements CreateOrderUseCase
│   └── query/          # GetOrderByIdQueryHandler — delegates to outbound port
│
├── infrastructure/
│   ├── rest/           # OrderController — @RestController, /api/v1/orders
│   ├── persistence/    # OrderEntity, OrderJpaRepository, OrderRepositoryAdapter
│   ├── messaging/      # OrderEventPublisher — KafkaTemplate wrapper
│   └── config/         # KafkaConfig — producer factory and template beans
│
└── shared/
    ├── exception/      # BusinessException, NotFoundException
    ├── response/       # ApiResponse<T> — ok() / error() factory methods
    └── validation/     # ValidationUtils — static null/blank guards

src/test/java/pe/resteban/template/
├── domain/model/               # OrderTest — pure unit, zero Spring
├── application/command/        # CreateOrderCommandHandlerTest — Mockito
└── infrastructure/rest/        # OrderControllerIT — Testcontainers (PG + Mongo + Rabbit)
```

---

## 🚀 Quick Start

**Prerequisites:** JDK 21, Maven 3.9+, Docker 29+

```bash
# 1. Clone the repository
git clone https://github.com/resteban-pe/java-microservices-template.git
cd java-microservices-template

# 2. Start the full infrastructure stack
docker compose up -d

# 3. Build the project (skip tests on first run — Docker must be up for Testcontainers)
mvn clean install -DskipTests

# 4. Run the application
mvn spring-boot:run

# 5. Open Swagger UI
open http://localhost:8080/swagger-ui.html
```

The active profile is `dev` by default. All connection strings in `application-dev.yml` point to the services started by Docker Compose using the credentials defined there.

---

## 🧪 Running Tests

The test suite is split by layer. Each layer uses a different testing strategy.

```bash
# Domain unit tests — no Spring context, runs in milliseconds
mvn test -Dtest=OrderTest

# Application unit tests — Mockito only, no Spring context
mvn test -Dtest=CreateOrderCommandHandlerTest

# Infrastructure integration test — requires Docker (spins up 3 real containers)
mvn test -Dtest=OrderControllerIT

# Full suite
mvn test
```

> The integration test (`OrderControllerIT`) uses `@Testcontainers` to automatically start isolated containers for PostgreSQL, MongoDB, and RabbitMQ. Docker must be running. Kafka connects lazily and does not require a container for the tested write flow.

---

## 🔌 API Endpoints

Base URL: `http://localhost:8080`
Interactive docs: `http://localhost:8080/swagger-ui.html`

| Method | Endpoint | Description | Request Body | Success |
|---|---|---|---|---|
| `POST` | `/api/v1/orders` | Create a new order | `{"orderId":"ORD-001","description":"..."}` | `201 Created` |
| `GET` | `/api/v1/orders/{id}` | Retrieve an order by ID | — | `200 OK` |

All responses are wrapped in `ApiResponse<T>`:

```json
{
  "success": true,
  "data": { "orderId": "ORD-001", "status": "CREATED" },
  "message": null
}
```

---

## 🤔 Why This Stack?

| Technology | Role in the Template | Why this, not another? |
|---|---|---|
| **Java 21** | Language baseline | Latest LTS — Java records are used for commands and events; virtual threads available for future reactive flows |
| **Spring Boot 3.5** | Framework backbone | Industry standard in enterprise Java; integrates Spring Data, Actuator, and autoconfiguration cohesively |
| **PostgreSQL** | Relational persistence | Demonstrates JPA/Hibernate with a production-grade engine — H2 hides dialect differences that matter at runtime |
| **MongoDB** | Document persistence | Shows polyglot persistence in the same service, a common pattern in microservice data strategies |
| **Kafka** | Domain event streaming | Standard for high-throughput event-driven pipelines; `OrderCreatedEvent` is the publishing example |
| **RabbitMQ** | AMQP message broker | Demonstrates competing consumers and task-queue patterns alongside Kafka's log-based approach |
| **Testcontainers** | Integration testing | Tests run against real engines, not mocks — surfaces dialect issues, schema mismatches, and connection behaviour |
| **SpringDoc OpenAPI** | API documentation | Generated directly from code; no drift between docs and implementation, zero YAML maintenance |
| **Lombok** | Boilerplate reduction | `@Builder` and `@Getter` keep domain classes readable without sacrificing immutability intent |

---

## 🗺 Portfolio Roadmap

This template is the foundation of a broader architecture portfolio. Planned repositories:

- [ ] **api-gateway-template** — Spring Cloud Gateway with rate limiting, JWT validation, and route composition
- [ ] **event-sourcing-template** — Full event store implementation with aggregate rehydration from event log
- [ ] **saga-orchestration-template** — Distributed transaction pattern with compensating commands across services
- [ ] **observability-template** — OpenTelemetry tracing, Micrometer metrics, Grafana + Loki + Tempo stack
- [ ] **grpc-service-template** — gRPC service definition, protobuf contracts, and bidirectional streaming

---

## 👤 Author

**Roosevelt Esteban Torres**
Technical Lead & Integration Architect

[![LinkedIn](https://img.shields.io/badge/LinkedIn-roosevelt--esteban-0A66C2?style=flat&logo=linkedin&logoColor=white)](https://linkedin.com/in/roosevelt-esteban)
[![GitHub](https://img.shields.io/badge/GitHub-resteban--pe-181717?style=flat&logo=github&logoColor=white)](https://github.com/resteban-pe)

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
Free to use as a starting point for your own projects — attribution appreciated.
