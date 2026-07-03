# Database Architecture

> **Note:** Database is managed by the backend team. This document provides context for frontend developers.

## Overview

KinderHub uses PostgreSQL as the primary database with Redis for caching.

## Entity Relationship Diagram

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    User     │       │  Provider   │       │  Activity   │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id          │       │ id          │       │ id          │
│ email       │       │ user_id     │──┐    │ provider_id │──┐
│ name        │       │ name        │  │    │ title       │  │
│ role        │       │ verified    │  │    │ category    │  │
│ auth0_id    │       │ ...         │  │    │ ...         │  │
└──────┬──────┘       └─────────────┘  │    └──────┬──────┘  │
       │                               │           │         │
       │                               └───────────┼─────────┘
       │                                           │
       │     ┌─────────────┐                       │
       │     │   Child     │                       │
       │     ├─────────────┤                       │
       └────►│ id          │                       │
             │ parent_id   │                       │
             │ name        │                       │
             │ birthdate   │                       │
             └──────┬──────┘                       │
                    │                              │
                    │     ┌─────────────┐          │
                    │     │  Booking    │          │
                    │     ├─────────────┤          │
                    └────►│ id          │◄─────────┘
                          │ child_id    │
                          │ activity_id │
                          │ session_id  │
                          │ status      │
                          │ ...         │
                          └─────────────┘
```

## Key Tables

### users
Stores all platform users (parents, providers, admins).

### providers
Business information for activity providers.

### activities
All activities listed on the platform.

### activity_sessions
Individual bookable sessions/slots for activities.

### children
Child profiles linked to parent users.

### bookings
Booking records linking children to activity sessions.

### payments
Payment transaction records.

### messages
Conversation messages between users.

### reviews
Activity reviews from parents.

---

## Caching Strategy

### Redis Cache

| Key Pattern | TTL | Usage |
|-------------|-----|-------|
| `activity:{id}` | 5 min | Activity details |
| `activities:featured` | 15 min | Featured list |
| `user:{id}:bookings` | 2 min | User bookings |
| `search:{hash}` | 10 min | Search results |

---

## Data Retention

| Data Type | Retention |
|-----------|-----------|
| User Data | Until deletion request |
| Bookings | 7 years (legal requirement) |
| Messages | 2 years |
| Logs | 90 days |
| Analytics | Aggregated indefinitely |
