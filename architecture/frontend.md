# Frontend Architecture

## Overview

The KinderHub frontend is built with Kotlin Multiplatform (KMP) and Compose Multiplatform, enabling a single codebase to target Android, iOS, and Web platforms.

## Technology Choices

### Kotlin Multiplatform

**Why KMP:**
- Single codebase for business logic
- Native performance on each platform
- Type safety and null safety
- Kotlin coroutines for async operations
- Growing ecosystem and Jetbrains support

### Compose Multiplatform

**Why Compose:**
- Declarative UI programming
- Shared UI code across platforms
- Modern, reactive paradigm
- Strong tooling support
- Native look and feel capability

---

## Layer Architecture

### 1. Presentation Layer

**Screens** — Full-page composables representing navigation destinations.

```kotlin
@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel) {
    val state by viewModel.state.collectAsState()
    // UI rendering
}
```

**Components** — Reusable UI building blocks.

```kotlin
@Composable
fun ActivityCard(activity: Activity, onClick: () -> Unit) {
    // Reusable card component
}
```

### 2. ViewModel Layer

ViewModels manage screen state and business logic.

```kotlin
class DiscoverViewModel(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DiscoverState())
    val state: StateFlow<DiscoverState> = _state.asStateFlow()

    fun loadActivities() {
        viewModelScope.launch {
            // Load and update state
        }
    }
}
```

### 3. Repository Layer

Repositories abstract data sources from ViewModels.

```kotlin
class ActivityRepository(
    private val apiService: ApiService,
    private val localCache: LocalCache
) {
    suspend fun getActivities(): List<Activity> {
        // Fetch from API or cache
    }
}
```

### 4. Data Layer

**Models** — Data classes representing domain entities.

**Services** — API clients and external service integrations.

**Local Storage** — Platform-specific persistence.

---

## State Management

### Unidirectional Data Flow

```
┌─────────────────────────────────────────────┐
│                                             │
│    User Event                               │
│        │                                    │
│        ▼                                    │
│    ViewModel                                │
│        │                                    │
│        ▼                                    │
│    State Update ──────────► UI Render       │
│                                             │
└─────────────────────────────────────────────┘
```

### State Classes

```kotlin
data class DiscoverState(
    val isLoading: Boolean = false,
    val activities: List<Activity> = emptyList(),
    val error: String? = null,
    val selectedCategory: Category? = null
)
```

---

## Navigation

Using Compose Navigation with type-safe routes.

```kotlin
sealed class Route(val path: String) {
    object Home : Route("home")
    object Discover : Route("discover")
    data class ActivityDetail(val id: String) : Route("activity/{id}")
}
```

---

## Dependency Injection

Using Koin for dependency injection.

```kotlin
val appModule = module {
    single { Auth0Service() }
    single { ActivityRepository(get()) }
    viewModel { DiscoverViewModel(get()) }
}
```

---

## Platform-Specific Code

Using `expect/actual` pattern for platform differences.

```kotlin
// commonMain
expect fun getPlatform(): Platform

// androidMain
actual fun getPlatform(): Platform = AndroidPlatform()

// iosMain
actual fun getPlatform(): Platform = IOSPlatform()
```

---

## Design System

### Theme

Centralized theming with Material 3.

- Colors (light/dark modes)
- Typography scales
- Spacing system
- Shape definitions

### Components

Reusable components prefixed with `Kh`:

- `KhButton` — Primary, secondary, text buttons
- `KhTextField` — Input fields with validation
- `KhCard` — Content containers
- `KhDivider` — Visual separators

---

## Performance Considerations

1. **Lazy Loading** — Use `LazyColumn`/`LazyRow` for lists
2. **Image Caching** — Coil for efficient image loading
3. **State Optimization** — Minimize recomposition
4. **Code Splitting** — Modular feature organization
