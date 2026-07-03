# Epic: Activity Discovery

## Overview

Enable parents to discover children's activities through browsing, searching, and personalized recommendations.

## Business Value

- Primary user acquisition funnel
- Core value proposition of the platform
- Drives engagement and bookings

## User Stories

### US-001: Browse Activities by Category

**As a** parent
**I want to** browse activities by category
**So that** I can find relevant activities for my child's interests

**Acceptance Criteria:**
- [ ] Display category tiles on home/discover screen
- [ ] Tap category to see filtered activity list
- [ ] Categories include: Sports, Arts, Music, STEM, Languages, Dance, Outdoor
- [ ] Show activity count per category
- [ ] Support category icons

---

### US-002: Search Activities

**As a** parent
**I want to** search for activities by keyword
**So that** I can quickly find specific activities

**Acceptance Criteria:**
- [ ] Search input field prominent on discover screen
- [ ] Real-time search suggestions as user types
- [ ] Search by activity name, provider name, keywords
- [ ] Show recent searches
- [ ] Clear search with one tap

---

### US-003: Filter Search Results

**As a** parent
**I want to** filter activities by various criteria
**So that** I can narrow down to suitable options

**Acceptance Criteria:**
- [ ] Filter by age range
- [ ] Filter by location/distance
- [ ] Filter by price range
- [ ] Filter by availability (days of week)
- [ ] Filter by rating (minimum stars)
- [ ] Combine multiple filters
- [ ] Show active filter count
- [ ] Clear all filters

---

### US-004: View Activity Details

**As a** parent
**I want to** view complete activity details
**So that** I can decide if it's right for my child

**Acceptance Criteria:**
- [ ] Activity title and images
- [ ] Full description
- [ ] Provider information
- [ ] Location with map
- [ ] Schedule and availability
- [ ] Pricing
- [ ] Age range
- [ ] Reviews and ratings
- [ ] Similar activities
- [ ] Book Now CTA

---

### US-005: Save Favorite Activities

**As a** parent
**I want to** save activities to favorites
**So that** I can easily find them later

**Acceptance Criteria:**
- [ ] Heart icon on activity cards and detail page
- [ ] Tap to add/remove from favorites
- [ ] View all favorites in dedicated section
- [ ] Favorites sync across devices

---

### US-006: View Featured Activities

**As a** parent
**I want to** see featured and trending activities
**So that** I can discover popular options

**Acceptance Criteria:**
- [ ] Featured carousel on home screen
- [ ] Show trending activities in area
- [ ] New activities section
- [ ] Seasonal/themed collections

---

## Technical Requirements

- Implement efficient list rendering (LazyColumn)
- Cache search results locally
- Support offline browsing of cached activities
- Image lazy loading with placeholders
- Search debouncing (300ms)

## Dependencies

- Backend activity API
- Search infrastructure (Elasticsearch)
- Image CDN

## Success Metrics

- Search-to-view conversion rate
- Time to first meaningful result
- Filter usage rate
- Favorites engagement
