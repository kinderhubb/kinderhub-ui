# KinderHub System Overview

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         Clients                                  │
├─────────────────┬─────────────────┬─────────────────────────────┤
│   iOS App       │   Android App   │         Web App             │
│   (Swift/KMP)   │   (Kotlin/KMP)  │       (WASM/KMP)            │
└────────┬────────┴────────┬────────┴────────────┬────────────────┘
         │                 │                      │
         └─────────────────┼──────────────────────┘
                           │
                    ┌──────▼──────┐
                    │   API GW    │
                    │  (Gateway)  │
                    └──────┬──────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
   ┌─────▼─────┐    ┌──────▼──────┐   ┌──────▼──────┐
   │   Auth    │    │   Core API  │   │  Media API  │
   │  (Auth0)  │    │  (Backend)  │   │   (CDN)     │
   └───────────┘    └──────┬──────┘   └─────────────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
        ┌─────▼────┐ ┌─────▼────┐ ┌─────▼────┐
        │ Database │ │  Cache   │ │  Search  │
        │(Postgres)│ │ (Redis)  │ │(Elastic) │
        └──────────┘ └──────────┘ └──────────┘
```

## Technology Stack

### Frontend (This Repository)

| Layer | Technology |
|-------|------------|
| Framework | Kotlin Multiplatform |
| UI | Compose Multiplatform |
| State Management | ViewModel + StateFlow |
| Dependency Injection | Koin |
| Networking | Ktor Client |
| Navigation | Compose Navigation |
| Authentication | Auth0 |

### Backend (Separate Repository)

| Layer | Technology |
|-------|------------|
| Runtime | Node.js / Kotlin (TBD) |
| API | REST / GraphQL |
| Database | PostgreSQL |
| Cache | Redis |
| Search | Elasticsearch |
| Storage | AWS S3 |

### Infrastructure

| Component | Technology |
|-----------|------------|
| Cloud | AWS / GCP |
| CI/CD | GitHub Actions |
| Monitoring | DataDog / CloudWatch |
| Error Tracking | Sentry |

---

## Key Design Principles

1. **Shared Codebase** — Maximum code reuse across platforms via KMP
2. **Offline-First** — Core functionality works without connectivity
3. **API-First** — All features accessible via documented APIs
4. **Modular** — Features organized as independent modules
5. **Observable** — Comprehensive logging and monitoring
6. **Secure** — Security built into every layer

---

## Module Structure

```
composeApp/
├── src/
│   ├── commonMain/          # Shared code (all platforms)
│   │   ├── kotlin/
│   │   │   ├── components/  # Reusable UI components
│   │   │   ├── data/        # Data layer (models, repos, services)
│   │   │   ├── di/          # Dependency injection
│   │   │   ├── navigation/  # Navigation logic
│   │   │   ├── screens/     # Screen composables
│   │   │   ├── theme/       # Design system
│   │   │   └── util/        # Utilities
│   │   └── composeResources/ # Strings, images, etc.
│   │
│   ├── androidMain/         # Android-specific code
│   ├── iosMain/             # iOS-specific code
│   └── wasmJsMain/          # Web-specific code
```

---

## Data Flow

```
User Action
     │
     ▼
┌─────────┐
│  Screen │ ─────────────┐
└────┬────┘              │
     │                   │
     ▼                   ▼
┌─────────┐        ┌──────────┐
│ ViewModel│◄──────│  State   │
└────┬────┘        └──────────┘
     │
     ▼
┌─────────────┐
│ Repository  │
└──────┬──────┘
       │
       ▼
┌──────────────────┐
│  Remote / Local  │
│    Data Source   │
└──────────────────┘
```

---

## Security Architecture

1. **Authentication** — Auth0 with JWT tokens
2. **Authorization** — Role-based access control (RBAC)
3. **Data Protection** — TLS in transit, encryption at rest
4. **Input Validation** — Client and server-side validation
5. **API Security** — Rate limiting, request signing
