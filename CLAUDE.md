# AI-Driven Autonomous Development Workspace

## Purpose

This repository is the single source of truth for:

* Product vision
* Roadmap
* Architecture
* Backlog
* Source code
* Documentation
* Testing
* AI agent instructions

The goal is to enable AI agents to autonomously build, test, document, and improve the product while maintaining alignment with business objectives.

---

## Autonomous Work Guidelines

Work autonomously. Don't ask for permission or confirmation between steps — make reasonable assumptions and proceed. If something is ambiguous, pick the most sensible interpretation, note the assumption in one line, and keep going.

After writing code, test it: run it (or trace through it), find failures, and fix them yourself. Iterate until it actually works before showing the result. Don't hand over broken code.

Only stop to ask if you hit a genuine blocker: a destructive/irreversible action, a real fork where the choice materially changes the outcome, or missing information you can't infer or look up. Otherwise, finish the whole task and report what you did.

---

## Repository Structure

```text
root/
├── README.md
├── CLAUDE.md                    # This file — AI instructions
│
├── docs/
│   ├── vision.md                # Product mission and goals
│   ├── roadmap.md               # Development timeline
│   ├── business-model.md        # Revenue and metrics
│   ├── user-personas.md         # Target users
│   └── glossary.md              # Term definitions
│
├── architecture/
│   ├── system-overview.md       # High-level architecture
│   ├── frontend.md              # Frontend architecture
│   ├── backend.md               # Backend API contract
│   ├── database.md              # Data model
│   ├── integrations.md          # Third-party services
│   └── decisions/               # Architecture Decision Records
│
├── prd/
│   ├── epic-activity-discovery.md
│   ├── epic-booking-engine.md
│   ├── epic-parent-dashboard.md
│   ├── epic-authentication.md
│   └── epic-provider-marketplace.md
│
├── backlog/
│   ├── now.md                   # Current sprint (WORK FROM HERE)
│   ├── next.md                  # Upcoming priorities
│   ├── later.md                 # Future ideas
│   └── completed.md             # Done archive
│
├── composeApp/                  # Kotlin Multiplatform source
├── iosApp/                      # iOS-specific code
├── e2e/                         # End-to-end tests
├── scripts/                     # Utility scripts
│
├── .github/
│   ├── workflows/               # CI/CD pipelines
│   ├── ISSUE_TEMPLATE/          # Issue templates
│   └── agent-instructions.md    # Detailed agent guide
│
└── reports/
    ├── daily/                   # Daily progress reports
    ├── weekly/                  # Weekly summaries
    └── releases/                # Release notes
```

---

## Product Knowledge Hierarchy

Understand where tasks fit before making changes:

```text
Vision
    ↓
Goals
    ↓
Epics
    ↓
Features
    ↓
User Stories
    ↓
Tasks
    ↓
Code
```

---

## Source of Truth Priority

If conflicts exist, higher-priority documents take precedence:

```text
1. Vision (/docs/vision.md)
2. Product Requirements (/prd/)
3. Architecture Documents (/architecture/)
4. Roadmap (/docs/roadmap.md)
5. GitHub Issues
6. Existing Code
```

---

## AI Agent Workflow

### Step 1 — Understand Context

Read in order:
1. `/docs/vision.md`
2. `/docs/roadmap.md`
3. `/architecture/system-overview.md`
4. Relevant PRD epic

### Step 2 — Select Work

Read `/backlog/now.md` and select highest-priority task with `Status: Ready`.

### Step 3 — Implement

- Follow existing code patterns
- Keep changes focused and minimal
- Handle errors appropriately
- Write self-documenting code

### Step 4 — Test

- Run existing tests: `./gradlew test`
- Write tests for new functionality
- Verify no regressions

### Step 5 — Document

Update docs when:
- Public API changes
- New components added
- Architecture modified

### Step 6 — Commit & PR

Create descriptive commits and PRs with clear summaries.

### Step 7 — Report

Update `/reports/daily/YYYY-MM-DD.md` with progress.

---

## Backlog Rules

| File | Contents | Can Work On |
|------|----------|-------------|
| `now.md` | Current sprint | YES |
| `next.md` | Upcoming | NO |
| `later.md` | Ideas | NO |
| `completed.md` | Archive | N/A |

---

## Definition of Done

A task is complete only when:

- [ ] Requirements satisfied
- [ ] Code implemented
- [ ] Tests passing
- [ ] Documentation updated (if needed)
- [ ] No regressions introduced

---

## Coding Principles

1. Simplicity over cleverness
2. Readability over optimization
3. Small, focused changes
4. Test-first mindset
5. Reusable components
6. Strong typing
7. Secure-by-default

---

## Daily Report Template

Create `/reports/daily/YYYY-MM-DD.md`:

```markdown
# Daily Report — YYYY-MM-DD

## Completed
- [TASK-XXX] Description

## In Progress
- [TASK-YYY] Description (X% complete)

## Blocked
- [TASK-ZZZ] Reason

## Next
- [TASK-AAA] Description
```

---

## Commands Reference

```bash
# Build
./gradlew build

# Test
./gradlew test

# Run Android
./gradlew :composeApp:installDebug

# Run Web
./gradlew :composeApp:wasmJsBrowserRun

# E2E Tests
npm test
```

---

## Founder Responsibilities

- Customer discovery
- User interviews
- Market validation
- Product strategy
- Pricing decisions
- Partnerships
- Final approvals

## AI Responsibilities

- Task execution
- Coding
- Testing
- Refactoring
- Documentation
- Pull requests
- Progress reporting

---

## Long-Term Objective

Create a self-improving software organization where:

* Product ideas become requirements
* Requirements become tasks
* Tasks become code
* Code becomes deployed software
* User feedback continuously improves the product

The repository should always contain enough information for a capable AI agent to continue development with minimal human intervention.
