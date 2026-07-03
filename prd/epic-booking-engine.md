# Epic: Booking Engine

## Overview

Enable parents to book activities for their children with a seamless, secure checkout experience.

## Business Value

- Primary revenue driver (transaction fees)
- Core platform functionality
- Key conversion point

## User Stories

### US-010: Select Session/Date

**As a** parent
**I want to** select a specific session or date for an activity
**So that** I can book at a time that works for my schedule

**Acceptance Criteria:**
- [ ] Display available dates/sessions
- [ ] Show availability (spots remaining)
- [ ] Calendar view for date selection
- [ ] Time slot selection for that date
- [ ] Indicate sold-out sessions
- [ ] Show price per session

---

### US-011: Select Child for Booking

**As a** parent
**I want to** select which child the booking is for
**So that** the provider knows who to expect

**Acceptance Criteria:**
- [ ] Show list of registered children
- [ ] Display child's age for eligibility check
- [ ] Warn if child's age outside activity range
- [ ] Option to add new child profile
- [ ] Support booking for multiple children

---

### US-012: Review Booking Details

**As a** parent
**I want to** review my booking before payment
**So that** I can confirm everything is correct

**Acceptance Criteria:**
- [ ] Summary of activity, date, time
- [ ] Child/children being booked
- [ ] Price breakdown (subtotal, fees, total)
- [ ] Provider cancellation policy
- [ ] Terms and conditions link
- [ ] Edit booking option
- [ ] Proceed to payment button

---

### US-013: Complete Payment

**As a** parent
**I want to** pay securely for my booking
**So that** my spot is confirmed

**Acceptance Criteria:**
- [ ] Secure card input (Stripe Elements)
- [ ] Save card for future use option
- [ ] Use saved payment method
- [ ] Apple Pay / Google Pay support
- [ ] Clear error messages for failures
- [ ] Processing indicator during payment
- [ ] Redirect to confirmation on success

---

### US-014: View Booking Confirmation

**As a** parent
**I want to** see confirmation of my booking
**So that** I know it was successful

**Acceptance Criteria:**
- [ ] Confirmation screen with booking reference
- [ ] Activity and session details
- [ ] Add to calendar button
- [ ] Share booking option
- [ ] View in My Bookings link
- [ ] Email confirmation sent

---

### US-015: Apply Promo Code

**As a** parent
**I want to** apply a promotional code
**So that** I can receive a discount

**Acceptance Criteria:**
- [ ] Promo code input on checkout
- [ ] Validate code and show discount
- [ ] Display error for invalid codes
- [ ] Show original and discounted price

---

## Technical Requirements

- PCI-compliant payment handling (Stripe)
- Optimistic UI during booking process
- Handle concurrent booking conflicts
- Secure session management
- Retry logic for payment failures

## Dependencies

- Backend booking API
- Stripe integration
- Email service for confirmations
- Calendar APIs (Google, Apple)

## Success Metrics

- Cart abandonment rate
- Time to complete booking
- Payment success rate
- Repeat booking rate
