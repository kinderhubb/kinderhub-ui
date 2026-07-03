# AI Agent Instructions

## Introduction

This document provides instructions for AI agents working on the KinderHub codebase. Follow these guidelines to ensure consistent, high-quality autonomous development.

---

## Before You Start

### 1. Understand the Context

Read these files in order:

```
1. /docs/vision.md          — Understand the mission
2. /docs/roadmap.md         — Know current priorities
3. /architecture/           — Understand the system
4. /prd/                    — Know the requirements
5. /backlog/now.md          — Find work to do
```

### 2. Verify Your Task

- Only work on tasks in `/backlog/now.md`
- Ensure the task has `Status: Ready`
- Check for dependencies before starting
- Understand acceptance criteria

---

## Development Workflow

### Step 1: Select Task

```
Read /backlog/now.md
Select highest priority task with Status: Ready
Verify no blocking dependencies
```

### Step 2: Understand Requirements

```
Read the task description fully
Review related PRD epic
Check architecture implications
Identify files to modify
```

### Step 3: Implement

Follow these coding principles:

**Do:**
- Write clean, readable code
- Follow existing patterns in the codebase
- Add appropriate error handling
- Keep changes focused and minimal
- Write self-documenting code

**Don't:**
- Over-engineer solutions
- Add features not requested
- Refactor unrelated code
- Break existing functionality
- Skip error handling

### Step 4: Test

```
Run existing tests: ./gradlew test
Write tests for new functionality
Verify on target platforms
Check for regressions
```

### Step 5: Document

Update documentation if:
- Public API changes
- New components added
- Architecture modified
- Configuration required

### Step 6: Commit

```
git add <specific files>
git commit -m "feat(scope): description

- Detail 1
- Detail 2

Closes #TASK-XXX"
```

### Step 7: Report

Update daily report in `/reports/daily/YYYY-MM-DD.md`

---

## Code Conventions

### Kotlin Style

- Follow Kotlin coding conventions
- Use meaningful variable names
- Prefer immutability (`val` over `var`)
- Use `data class` for models
- Handle nullability explicitly

### Compose Style

- One composable per file for screens
- Reusable components in `/components/`
- Preview annotations for UI components
- Modifiers as first optional parameter
- State hoisting pattern

### File Organization

```
Screen files:     XxxScreen.kt
ViewModel files:  XxxViewModel.kt
Model files:      Xxx.kt (in /data/model/)
Repository files: XxxRepository.kt
Component files:  XxxComponent.kt or Xxx.kt
```

### Naming Conventions

```
Classes:      PascalCase      (ActivityCard)
Functions:    camelCase       (loadActivities)
Variables:    camelCase       (isLoading)
Constants:    SCREAMING_CASE  (MAX_RETRIES)
Files:        PascalCase.kt   (ActivityCard.kt)
```

---

## Architecture Decisions

When making technical decisions:

1. Check `/architecture/decisions/` for existing decisions
2. Follow established patterns in the codebase
3. Document significant new decisions
4. Prefer simplicity over cleverness
5. Consider all target platforms (Android, iOS, Web)

---

## Testing Requirements

### Unit Tests

- Test ViewModels with mock repositories
- Test repository logic
- Test utility functions
- Aim for high coverage of business logic

### UI Tests

- Test critical user flows
- Visual regression tests for components
- Platform-specific tests where needed

### Running Tests

```bash
# All tests
./gradlew test

# Specific module
./gradlew :composeApp:test

# E2E tests
npm test
```

---

## Error Handling

### User-Facing Errors

- Display friendly error messages
- Provide retry options where appropriate
- Log errors for debugging
- Never expose technical details to users

### Code Errors

```kotlin
// Prefer Result type
fun loadData(): Result<Data>

// Handle in ViewModel
when (val result = repository.loadData()) {
    is Result.Success -> updateState(result.data)
    is Result.Error -> showError(result.message)
}
```

---

## Pull Request Guidelines

### PR Title

```
feat(discovery): Add activity search functionality
fix(booking): Handle payment timeout correctly
refactor(auth): Simplify token refresh logic
docs(readme): Update setup instructions
```

### PR Description Template

```markdown
## Summary
Brief description of changes.

## Changes
- Change 1
- Change 2

## Testing
- [ ] Unit tests pass
- [ ] Manual testing completed
- [ ] No regressions

## Screenshots
(if UI changes)
```

---

## Reporting Progress

### Daily Report Format

Create/update `/reports/daily/YYYY-MM-DD.md`:

```markdown
# Daily Report — YYYY-MM-DD

## Completed
- [TASK-001] Implemented API client
- [TASK-002] Connected discover screen to API

## In Progress
- [TASK-003] Activity search (80% complete)

## Blocked
- [TASK-005] Payment integration — awaiting Stripe keys

## Next
- [TASK-004] Session selection UI

## Notes
Any relevant observations or decisions made.
```

---

## Getting Help

If blocked:

1. Check documentation thoroughly
2. Search existing code for patterns
3. Review related PRDs and architecture docs
4. Document the blocker clearly
5. Mark task as blocked in report

---

## Summary Checklist

Before completing any task:

- [ ] Code follows project conventions
- [ ] All tests pass
- [ ] No TypeScript/Kotlin errors
- [ ] Documentation updated if needed
- [ ] Commit message follows convention
- [ ] Daily report updated
- [ ] Task marked as complete in backlog
