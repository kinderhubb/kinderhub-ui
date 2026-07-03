package com.kinderhub.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.data.model.AiSuggestion
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Category
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.data.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DiscoverScreenState {
    Loading,
    Loaded,
    Empty,
    Error,
    LocationRequest,
    LocationDenied,
}

data class DiscoverUiState(
    val screenState: DiscoverScreenState = DiscoverScreenState.Loading,
    val greeting: String = "Discover activities",
    val subGreeting: String = "Trusted clubs, camps & classes nearby",

    // User data
    val userInitials: String = "SB",
    val children: List<Child> = emptyList(),
    val selectedChildId: String? = null,
    val showAllChildren: Boolean = true, // "All children" selected

    // Location
    val locationText: String = "SW11, London",
    val hasLocationPermission: Boolean = true,

    // Search
    val searchQuery: String = "",
    val searchPlaceholder: String = "swimming for my 5-year-old on Saturday mornings near me",

    // Content
    val categories: List<Category> = emptyList(),
    val aiSuggestions: List<AiSuggestion> = emptyList(),
    val activities: List<Activity> = emptyList(),

    // UI State
    val isRefreshing: Boolean = false,
    val unreadMessagesCount: Int = 2,
)

class DiscoverViewModel(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    // Mock children data - in real app would come from user profile
    private val mockChildren = listOf(
        Child("child_001", "Ella", "2019-03-15", 5, AvatarColor.Accent),
        Child("child_002", "Max", "2016-07-22", 8, AvatarColor.Sunshine),
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = DiscoverScreenState.Loading) }

            try {
                val feed = activityRepository.getDiscoverFeed(
                    childId = _uiState.value.selectedChildId,
                    childName = if (_uiState.value.showAllChildren) null else mockChildren.find { it.id == _uiState.value.selectedChildId }?.firstName
                )

                _uiState.update {
                    it.copy(
                        screenState = if (feed.activities.isEmpty()) DiscoverScreenState.Empty else DiscoverScreenState.Loaded,
                        greeting = if (mockChildren.isNotEmpty()) "Good morning, Sarah" else "Discover activities",
                        subGreeting = if (mockChildren.isEmpty()) "Trusted clubs, camps & classes nearby" else "",
                        children = mockChildren,
                        categories = feed.categories,
                        aiSuggestions = feed.aiSuggestions,
                        activities = feed.activities,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(screenState = DiscoverScreenState.Error)
                }
            }
        }
    }

    fun selectChild(childId: String?) {
        _uiState.update {
            it.copy(
                selectedChildId = childId,
                showAllChildren = childId == null
            )
        }
        loadInitialData()
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun searchWithAiSuggestion(suggestion: AiSuggestion) {
        _uiState.update { it.copy(searchQuery = suggestion.query) }
        // Would trigger search
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadInitialData()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun requestLocationPermission() {
        // Would trigger platform-specific location request
        _uiState.update { it.copy(screenState = DiscoverScreenState.LocationRequest) }
    }

    fun onLocationGranted() {
        _uiState.update {
            it.copy(
                hasLocationPermission = true,
                screenState = DiscoverScreenState.Loading
            )
        }
        loadInitialData()
    }

    fun onLocationDenied() {
        _uiState.update {
            it.copy(
                hasLocationPermission = false,
                screenState = DiscoverScreenState.LocationDenied
            )
        }
    }

    fun updatePostcode(postcode: String) {
        _uiState.update {
            it.copy(
                locationText = postcode,
                screenState = DiscoverScreenState.Loading
            )
        }
        loadInitialData()
    }

    fun toggleFavorite(activityId: String) {
        // Would update favorites in repository
    }
}
