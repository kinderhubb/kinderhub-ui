# Epic: Parent Dashboard

## Overview

Provide parents with a central hub to manage their bookings, children, account, and communications.

## Business Value

- Improves user retention
- Reduces support burden
- Enables self-service management

## User Stories

### US-020: View My Bookings

**As a** parent
**I want to** see all my upcoming and past bookings
**So that** I can manage my children's activities

**Acceptance Criteria:**
- [ ] Tab/toggle for Upcoming vs Past
- [ ] Booking cards with key details
- [ ] Sort by date
- [ ] Filter by child
- [ ] Empty state for no bookings
- [ ] Quick action to book again (past)

---

### US-021: View Booking Details

**As a** parent
**I want to** see full details of a booking
**So that** I have all the information I need

**Acceptance Criteria:**
- [ ] Activity information
- [ ] Date, time, location
- [ ] Child booked
- [ ] Provider contact info
- [ ] Payment summary
- [ ] Directions link
- [ ] Cancel booking option (if allowed)

---

### US-022: Cancel Booking

**As a** parent
**I want to** cancel a booking
**So that** I can free up my schedule if plans change

**Acceptance Criteria:**
- [ ] Cancel button on booking detail
- [ ] Show cancellation policy
- [ ] Indicate refund amount (if any)
- [ ] Confirmation dialog
- [ ] Cancellation confirmation screen
- [ ] Email confirmation of cancellation

---

### US-023: Manage Children Profiles

**As a** parent
**I want to** add and edit my children's profiles
**So that** I can book activities for them

**Acceptance Criteria:**
- [ ] View list of children
- [ ] Add new child (name, birthdate, notes)
- [ ] Edit child details
- [ ] Remove child profile
- [ ] Profile picture (optional)

---

### US-024: Manage Account Settings

**As a** parent
**I want to** manage my account settings
**So that** I can keep my information up to date

**Acceptance Criteria:**
- [ ] Edit name, email, phone
- [ ] Change password
- [ ] Manage notification preferences
- [ ] Payment methods management
- [ ] Language preference
- [ ] Delete account option

---

### US-025: View Messages

**As a** parent
**I want to** communicate with activity providers
**So that** I can ask questions and receive updates

**Acceptance Criteria:**
- [ ] List of conversations
- [ ] Unread indicator
- [ ] Message thread view
- [ ] Send new messages
- [ ] Receive push notifications
- [ ] Start new conversation from activity

---

### US-026: Manage Notifications

**As a** parent
**I want to** control my notification preferences
**So that** I receive relevant updates

**Acceptance Criteria:**
- [ ] Toggle email notifications
- [ ] Toggle push notifications
- [ ] Configure notification types:
  - Booking reminders
  - Provider messages
  - Promotions
  - Activity updates

---

## Technical Requirements

- Pull-to-refresh for bookings
- Offline access to upcoming bookings
- Real-time message updates
- Deep linking to specific bookings

## Dependencies

- Backend user API
- Backend booking API
- Messaging service
- Push notification service

## Success Metrics

- Dashboard engagement rate
- Self-service cancellation rate
- Message response rate
- Settings completion rate
