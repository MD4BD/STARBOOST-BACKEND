# 🚀 STARBOOST-BACKEND

A Spring Boot microservice powering **STARBOOST**, a flexible sales‑challenge engine. Define custom challenges (contracts, revenue, averages, weighted averages), auto‑enroll participants, ingest transactions, compute scores & leaderboards, evaluate eligibility, and calculate payouts—all via REST APIs.

---

## 📚 Tech Stack & Versions

| Layer         | Technology                     | Version  |
| ------------- | ------------------------------ | -------- |
| Language      | Java                           | 17 (LTS) |
| Framework     | Spring Boot                    | 3.x      |
| Build Tool    | Maven (with Wrapper)           | 3.8.x    |
| ORM           | Spring Data JPA / Hibernate    | 6.x      |
| Database      | PostgreSQL                     | 14+      |
| Security      | Spring Security + JWT          | 6.x      |
| DTOs & Models | Lombok                         | 1.18.x   |
| API Docs      | Springdoc OpenAPI (Swagger UI) | 1.6.x    |

---

## 🏗️ High-Level Architecture

```
┌───────────────┐ HTTP ┌─────────────────┐ JPA ┌──────────────┐
│ Controllers ├────────▶│ Services/Logic ├────────▶│ Repositories │
└───────────────┘       └─────────────────┘       └──────────────┘
      ▲                      ▲      ▲                    ▲
      │                      │      │                    │
      ▼                      ▼      ▼                    ▼
   REST                   Business  Data               PostgreSQL
   JSON                   Rules     Access
   DTOs/Entities
```

**Package layout**

```
com.starboost.starboost_backend_demo
├── controller      – REST endpoints  
├── service         – Business interfaces  
│   └── impl        – Service implementations  
├── dto             – Request/response models  
├── entity          – JPA @Entity classes & enums  
├── repository      – Spring Data JPA interfaces  
└── util            – JWT filter, security helpers  
```

---

## 📦 Key Models & Endpoints

| Model / DTO          | Purpose                                                  | Sample Endpoint                            |
| -------------------- | -------------------------------------------------------- | ------------------------------------------ |
| **ChallengeDto**     | Create/update a challenge (dates, rules, roles)          | `POST /api/challenges`                     |
| **WinningRuleDto**   | Define eligibility gates (min‑contracts, revenue)        | `POST /api/challenges/{id}/winning‑rules`  |
| **RewardRuleDto**    | Configure payout tiers (fixed/percent/rank)              | `POST /api/challenges/{id}/rewards/{role}` |
| **PerformanceDto**   | Leaderboard entry (contracts, revenue, score, rank)      | `GET /api/challenges/{id}/scores/agents`   |
| **WinnerDto**        | Final winners (userId, metrics, payout)                  | `GET /api/challenges/{id}/winners`         |
| **SalesTransaction** | Raw sales data (sellerId, contractType, premium)         | `POST /api/sales‑transactions`             |
| **User / Role**      | User profiles & roles (AGENT, MANAGER, etc.)             | Internal/auth endpoints                    |
| **ConditionType**    | Eligibility types (MIN\_CONTRACTS, WEIGHTED\_AVG)        | Used in `ChallengeWinningRule`             |
| **PayoutType**       | Reward types (FIXED\_TIERS, PERCENT\_TIERS, RANK\_TIERS) | Used in `ChallengeRewardRule`              |

---

## ⚙️ Setup & Run Instructions

1. **Clone & Build**

   ```bash
   git clone https://github.com/MD4BD/STARBOOST-BACKEND.git
   cd STARBOOST-BACKEND
   ./mvnw clean package -DskipTests
   ```

2. **Configuration**
   Copy and edit `src/main/resources/application.properties.example` → `application.properties`, then set:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/starboost
   spring.datasource.username=postgres
   spring.datasource.password=secret
   jwt.secret=YourJWTSecretKey
   server.port=8080
   ```

3. **Run**

   ```bash
   java -jar target/starboost-backend-demo-0.1.0.jar
   ```

4. **Explore APIs**

   * **Swagger UI:** `http://localhost:8080/swagger-ui.html`
   * **Health Check:** `GET /actuator/health`

---

## 🚧 Project Roadmap

| Status  | Feature                                                   |
| ------- | --------------------------------------------------------- |
| ✔️ Done | Challenge CRUD, Participant Enrollment, Sales ingestion   |
| ✔️ Done | ScoringService (custom score rules)                       |
| ✔️ Done | PerformanceService & Leaderboards                         |
| ✔️ Done | WinningRuleService (eligibility gates)                    |
| ✔️ Done | RewardRuleService (tiered payouts)                        |
| ✔️ Done | ChallengeEvaluationService (final winners & payouts)      |
| 🚀 Next | Push notifications (email/SMS) for winners                |
| 🚀 Next | Analytics dashboard (challenge summaries, trends)         |
| 🚀 Next | Role-based UI & multi-tenant support                      |
| 🚀 Next | Rate limiting, metrics (Micrometer) & distributed tracing |

---

## 🤖 How to Use with ChatGPT

Your GitHub connector is enabled. To fetch or edit code, just ask:

> “Hey ChatGPT, open `src/main/java/com/starboost/.../ScoreRuleServiceImpl.java` and explain the `calculateScore()` method.”
> “Please add Javadoc to `ChallengeEvaluationServiceImpl`.”

---

— Made with ❤️ by the STARBOOST Team
