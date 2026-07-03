# Epic: Provider Marketplace

## Overview

Enable activity providers to list, manage, and promote their offerings on KinderHub.

## Business Value

- Supply side of marketplace
- Provider retention drives activity variety
- Commission revenue from bookings

## User Stories

### US-040: Provider Registration

**As a** potential provider
**I want to** register as an activity provider
**So that** I can list my activities on KinderHub

**Acceptance Criteria:**
- [ ] Provider signup form
- [ ] Business name and type
- [ ] Contact information
- [ ] Business verification documents upload
- [ ] Terms acceptance
- [ ] Pending approval state
- [ ] Approval notification

---

### US-041: Create Activity Listing

**As a** provider
**I want to** create an activity listing
**So that** parents can discover and book my activity

**Acceptance Criteria:**
- [ ] Activity title and description
- [ ] Category selection
- [ ] Age range specification
- [ ] Location/venue details
- [ ] Capacity setting
- [ ] Photo upload (multiple)
- [ ] Draft/publish states
- [ ] Preview before publishing

---

### US-042: Set Pricing

**As a** provider
**I want to** set pricing for my activities
**So that** parents know the cost

**Acceptance Criteria:**
- [ ] Price per session
- [ ] Package pricing (multi-session)
- [ ] Sibling discounts
- [ ] Early bird pricing
- [ ] Promotional pricing

---

### US-043: Manage Schedule

**As a** provider
**I want to** set my activity schedule
**So that** parents can book available times

**Acceptance Criteria:**
- [ ] Calendar interface
- [ ] Recurring session setup
- [ ] One-time session creation
- [ ] Block out unavailable times
- [ ] Capacity per session
- [ ] View bookings on calendar

---

### US-044: View Bookings

**As a** provider
**I want to** see all bookings for my activities
**So that** I can manage attendance

**Acceptance Criteria:**
- [ ] List of all bookings
- [ ] Filter by activity
- [ ] Filter by date
- [ ] Booking status indicators
- [ ] Attendee details
- [ ] Export attendee list

---

### US-045: Manage Messages

**As a** provider
**I want to** communicate with parents
**So that** I can answer questions and provide updates

**Acceptance Criteria:**
- [ ] Inbox of conversations
- [ ] Reply to parent messages
- [ ] Send broadcast to all booked parents
- [ ] Message templates

---

### US-046: View Analytics

**As a** provider
**I want to** see performance analytics
**So that** I can optimize my offerings

**Acceptance Criteria:**
- [ ] Views and impressions
- [ ] Booking conversion rate
- [ ] Revenue summary
- [ ] Attendance rates
- [ ] Popular time slots
- [ ] Export reports

---

## Technical Requirements

- Provider-specific navigation/UI
- Image upload and management
- Calendar/scheduling components
- Real-time booking updates
- Analytics dashboards

## Dependencies

- Provider API endpoints
- Image storage service
- Analytics service
- Payout service

## Success Metrics

- Provider signup rate
- Listing completion rate
- Time to first booking
- Provider retention rate
- Average listings per provider

---

> **Note:** Provider features may be implemented in a separate provider app or as role-based screens within the main app. Architecture decision pending.
