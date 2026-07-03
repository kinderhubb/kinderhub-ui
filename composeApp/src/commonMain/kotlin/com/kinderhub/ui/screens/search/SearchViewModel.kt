package com.kinderhub.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.data.model.Category
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.repository.ActivityRepository
import com.kinderhub.ui.data.repository.SearchFilters
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SearchScreenState {
    Idle,
    Loading,
    Results,
    Empty,
    Error
}

enum class SortOption(val label: String) {
    Recommended("Recommended"),
    Distance("Distance"),
    Price("Price"),
    Rating("Rating")
}

data class SearchUiState(
    val screenState: SearchScreenState = SearchScreenState.Idle,
    val query: String = "",
    val results: List<Activity> = emptyList(),
    val resultCount: Int = 0,

    // Filters
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val children: List<Child> = emptyList(),
    val selectedChildId: String? = null,
    val selectedDay: String? = null,
    val maxPrice: Double = 50.0,
    val maxDistance: Float = 5.0f,
    val sortBy: SortOption = SortOption.Recommended,

    // Filter chips (active filters shown)
    val activeFilters: List<FilterChip> = emptyList(),

    // Location
    val locationText: String = "SW11, London",

    // UI
    val showFiltersSheet: Boolean = false,
)

data class FilterChip(
    val id: String,
    val label: String,
    val type: FilterType
)

enum class FilterType {
    Category,
    Child,
    Day,
    Distance,
    Price
}

class SearchViewModel(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    // Mock children - in real app from user profile
    private val mockChildren = listOf(
        Child("child_001", "Ella", "2019-03-15", 5, AvatarColor.Accent),
        Child("child_002", "Max", "2016-07-22", 8, AvatarColor.Sunshine),
    )

    private val dayOptions = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categories = activityRepository.getCategories()
            _uiState.update {
                it.copy(
                    categories = categories,
                    children = mockChildren
                )
            }
        }
    }

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        debounceSearch()
    }

    fun search() {
        val state = _uiState.value
        if (state.query.isBlank() && state.selectedCategoryId == null) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(screenState = SearchScreenState.Loading) }

            try {
                val childAge = state.selectedChildId?.let { id ->
                    mockChildren.find { it.id == id }?.age
                }

                val filters = SearchFilters(
                    categoryId = state.selectedCategoryId,
                    childAge = childAge,
                    maxDistance = state.maxDistance,
                    maxPrice = state.maxPrice,
                    dayOfWeek = state.selectedDay
                )

                val results = activityRepository.searchActivities(state.query, filters)

                val sortedResults = when (state.sortBy) {
                    SortOption.Recommended -> results.sortedByDescending { it.isAiPick }
                    SortOption.Distance -> results.sortedBy { it.distance }
                    SortOption.Price -> results.sortedBy { it.pricePerSession }
                    SortOption.Rating -> results.sortedByDescending { it.rating }
                }

                _uiState.update {
                    it.copy(
                        screenState = if (sortedResults.isEmpty()) SearchScreenState.Empty else SearchScreenState.Results,
                        results = sortedResults,
                        resultCount = sortedResults.size
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = SearchScreenState.Error) }
            }
        }
    }

    private fun debounceSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            search()
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
        updateActiveFilters()
        search()
    }

    fun selectChild(childId: String?) {
        _uiState.update { it.copy(selectedChildId = childId) }
        updateActiveFilters()
        search()
    }

    fun selectDay(day: String?) {
        _uiState.update { it.copy(selectedDay = day) }
        updateActiveFilters()
        search()
    }

    fun updateMaxPrice(price: Double) {
        _uiState.update { it.copy(maxPrice = price) }
        updateActiveFilters()
    }

    fun updateMaxDistance(distance: Float) {
        _uiState.update { it.copy(maxDistance = distance) }
        updateActiveFilters()
    }

    fun updateSortBy(sort: SortOption) {
        _uiState.update { it.copy(sortBy = sort) }
        search()
    }

    fun removeFilter(chip: FilterChip) {
        when (chip.type) {
            FilterType.Category -> selectCategory(null)
            FilterType.Child -> selectChild(null)
            FilterType.Day -> selectDay(null)
            FilterType.Distance -> updateMaxDistance(10f)
            FilterType.Price -> updateMaxPrice(100.0)
        }
    }

    fun clearAllFilters() {
        _uiState.update {
            it.copy(
                selectedCategoryId = null,
                selectedChildId = null,
                selectedDay = null,
                maxPrice = 50.0,
                maxDistance = 5.0f,
                activeFilters = emptyList()
            )
        }
        search()
    }

    fun showFilters() {
        _uiState.update { it.copy(showFiltersSheet = true) }
    }

    fun hideFilters() {
        _uiState.update { it.copy(showFiltersSheet = false) }
        search()
    }

    fun letAiFindAlternatives() {
        // AI would expand search criteria
        _uiState.update {
            it.copy(
                maxDistance = it.maxDistance * 2,
                maxPrice = it.maxPrice * 1.5
            )
        }
        search()
    }

    private fun updateActiveFilters() {
        val state = _uiState.value
        val filters = mutableListOf<FilterChip>()

        state.selectedCategoryId?.let { catId ->
            val category = state.categories.find { it.id == catId }
            if (category != null) {
                filters.add(FilterChip(catId, category.name, FilterType.Category))
            }
        }

        state.selectedChildId?.let { childId ->
            val child = mockChildren.find { it.id == childId }
            if (child != null) {
                filters.add(FilterChip(childId, "Ages ${child.age - 1}-${child.age + 1}", FilterType.Child))
            }
        }

        state.selectedDay?.let { day ->
            filters.add(FilterChip(day, day, FilterType.Day))
        }

        if (state.maxDistance < 5f) {
            filters.add(FilterChip("distance", "≤ ${state.maxDistance} mi", FilterType.Distance))
        }

        if (state.maxPrice < 50.0) {
            filters.add(FilterChip("price", "≤ £${state.maxPrice.toInt()}", FilterType.Price))
        }

        _uiState.update { it.copy(activeFilters = filters) }
    }
}
