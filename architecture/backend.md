# Backend Architecture

> **Note:** Backend is in a separate repository. This document outlines the expected API contract and integration points for the frontend.

## API Overview

### Base URL

```
Production: https://api.kinderhub.app/v1
Staging:    https://api.staging.kinderhub.app/v1
```

### Authentication

All authenticated endpoints require:

```
Authorization: Bearer <access_token>
```

Tokens are obtained via Auth0.

---

## API Endpoints

### Activities

```
GET    /activities                 # List activities
GET    /activities/:id             # Get activity details
GET    /activities/search          # Search activities
GET    /activities/categories      # List categories
GET    /activities/featured        # Featured activities
```

### Bookings

```
GET    /bookings                   # User's bookings
POST   /bookings                   # Create booking
GET    /bookings/:id               # Booking details
PATCH  /bookings/:id               # Update booking
DELETE /bookings/:id               # Cancel booking
```

### Users

```
GET    /users/me                   # Current user profile
PATCH  /users/me                   # Update profile
GET    /users/me/children          # User's children
POST   /users/me/children          # Add child
PATCH  /users/me/children/:id      # Update child
DELETE /users/me/children/:id      # Remove child
```

### Messages

```
GET    /conversations              # User's conversations
GET    /conversations/:id          # Conversation messages
POST   /conversations/:id/messages # Send message
```

### Payments

```
POST   /payments/intent            # Create payment intent
POST   /payments/confirm           # Confirm payment
GET    /payments/methods           # User's payment methods
POST   /payments/methods           # Add payment method
```

---

## Request/Response Format

### Standard Response

```json
{
  "data": { ... },
  "meta": {
    "page": 1,
    "limit": 20,
    "total": 100
  }
}
```

### Error Response

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input",
    "details": [
      { "field": "email", "message": "Invalid email format" }
    ]
  }
}
```

---

## Data Models

### Activity

```json
{
  "id": "uuid",
  "title": "string",
  "description": "string",
  "category": "string",
  "provider": { ... },
  "location": { ... },
  "schedule": { ... },
  "pricing": { ... },
  "ageRange": { "min": 5, "max": 12 },
  "capacity": 15,
  "spotsAvailable": 8,
  "images": ["url"],
  "rating": 4.5,
  "reviewCount": 23
}
```

### Booking

```json
{
  "id": "uuid",
  "activity": { ... },
  "child": { ... },
  "session": { ... },
  "status": "confirmed",
  "payment": { ... },
  "createdAt": "ISO8601",
  "updatedAt": "ISO8601"
}
```

---

## Webhooks (Provider to KinderHub)

```
POST /webhooks/booking-confirmed
POST /webhooks/booking-cancelled
POST /webhooks/activity-updated
```

---

## Rate Limits

| Endpoint Type | Limit |
|---------------|-------|
| Public | 100 req/min |
| Authenticated | 1000 req/min |
| Search | 30 req/min |

---

## Integration Points

### Auth0

- User authentication
- Social login (Google, Apple)
- Token management

### Stripe

- Payment processing
- Subscription management
- Payout to providers

### SendGrid / Twilio

- Email notifications
- SMS notifications

### Firebase

- Push notifications
- Analytics
