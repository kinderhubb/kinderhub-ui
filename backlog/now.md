# Current Sprint — NOW

Items in this file are approved for immediate autonomous development.

---

## Priority 1: Backend Integration

### TASK-001: Implement API Client

**Epic:** Foundation
**Type:** Task
**Status:** Ready

**Description:**
Create a Ktor-based HTTP client for backend API communication.

**Requirements:**
- Base URL configuration (per environment)
- Auth token injection
- Request/response logging
- Error handling and mapping
- Retry logic with exponential backoff

**Files to modify:**
- `composeApp/src/commonMain/kotlin/com/kinderhub/ui/data/api/`

---

### TASK-002: Connect Activity Discovery to API

**Epic:** Activity Discovery
**Type:** Task
**Status:** Ready

**Description:**
Replace mock data in DiscoverViewModel with real API calls.

**Requirements:**
- Fetch activities from `/activities` endpoint
- Implement pagination (infinite scroll)
- Handle loading and error states
- Cache results locally

**Dependencies:**
- TASK-001 (API Client)

---

### TASK-003: Implement Activity Search

**Epic:** Activity Discovery
**Type:** Feature
**Status:** Ready

**Description:**
Add search functionality to discover screen.

**Requirements:**
- Search input with debouncing
- Call `/activities/search` endpoint
- Display search results
- Show recent searches
- Handle no results state

---

## Priority 2: Booking Flow Completion

### TASK-004: Implement Session Selection

**Epic:** Booking Engine
**Type:** Feature
**Status:** Ready

**Description:**
Allow users to select available dates/sessions when booking.

**Requirements:**
- Fetch available sessions from API
- Calendar date picker component
- Time slot selection
- Show availability (spots remaining)
- Handle sold-out sessions

---

### TASK-005: Payment Integration

**Epic:** Booking Engine
**Type:** Feature
**Status:** Ready

**Description:**
Integrate Stripe for payment processing.

**Requirements:**
- Stripe SDK integration
- Payment sheet implementation
- Handle payment success/failure
- Store payment method option
- Apple Pay / Google Pay support

---

## Priority 3: User Experience

### TASK-006: Add Pull-to-Refresh

**Epic:** Parent Dashboard
**Type:** Enhancement
**Status:** Ready

**Description:**
Add pull-to-refresh to list screens.

**Requirements:**
- Implement on Discover screen
- Implement on My Bookings screen
- Loading indicator during refresh
- Error handling

---

### TASK-007: Implement Skeleton Loading

**Epic:** Foundation
**Type:** Enhancement
**Status:** Ready

**Description:**
Add skeleton loading states to improve perceived performance.

**Requirements:**
- Skeleton for activity cards
- Skeleton for activity detail
- Skeleton for booking list
- Smooth transition to loaded state

---

## Acceptance

Before moving to completed.md:
- [ ] Code implemented and tested
- [ ] Unit tests passing
- [ ] No regressions introduced
- [ ] Documentation updated if needed
- [ ] PR reviewed and merged
