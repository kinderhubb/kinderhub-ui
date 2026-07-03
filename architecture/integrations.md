# Third-Party Integrations

## Authentication — Auth0

### Purpose
User authentication and identity management.

### Integration Points

**Frontend:**
- Universal Login redirect
- Token storage and refresh
- Social login (Google, Apple, Facebook)

**Configuration:**
```kotlin
object Auth0Config {
    const val DOMAIN = "kinderhub.eu.auth0.com"
    const val CLIENT_ID = "<from_environment>"
    const val AUDIENCE = "https://api.kinderhub.app"
}
```

### Flows Supported
- Email/password signup and login
- Social login (Google, Apple)
- Password reset
- Email verification

---

## Payments — Stripe

### Purpose
Process payments and manage payouts to providers.

### Integration Points

**Frontend:**
- Stripe Elements for card input
- Payment Intent confirmation
- Saved payment methods

**Flow:**
```
1. Frontend requests payment intent from backend
2. Backend creates Stripe PaymentIntent
3. Frontend confirms payment with Stripe
4. Webhook notifies backend of result
5. Backend updates booking status
```

---

## Push Notifications — Firebase Cloud Messaging

### Purpose
Send push notifications to mobile devices.

### Notification Types

| Type | Trigger |
|------|---------|
| Booking Confirmed | After successful payment |
| Booking Reminder | 24h before activity |
| Message Received | New message from provider |
| Activity Update | Provider updates activity |

---

## Analytics

### Firebase Analytics
- User events
- Screen views
- Conversion tracking

### Mixpanel (Future)
- User journey analysis
- Funnel optimization

---

## Maps — Google Maps / Apple Maps

### Purpose
- Display activity locations
- Location-based search
- Directions to activities

### Integration
Platform-specific map SDKs with shared location data model.

---

## Email — SendGrid

### Purpose
Transactional emails.

### Email Types
- Welcome email
- Booking confirmation
- Booking reminder
- Password reset
- Provider notifications

---

## Storage — AWS S3 / CloudFront

### Purpose
Store and serve media files.

### Usage
- Activity images
- Provider logos
- User avatars

### URL Pattern
```
https://media.kinderhub.app/{type}/{id}/{filename}
```

---

## Calendar Integration (Future)

### Google Calendar
- Export bookings
- Sync availability

### Apple Calendar
- iCal export
- CalDAV sync

---

## Environment Configuration

All integration credentials are managed via environment variables:

```
AUTH0_DOMAIN=
AUTH0_CLIENT_ID=
STRIPE_PUBLISHABLE_KEY=
FIREBASE_CONFIG=
MAPS_API_KEY=
```

Frontend receives only public/publishable keys. Secret keys are never exposed to clients.
