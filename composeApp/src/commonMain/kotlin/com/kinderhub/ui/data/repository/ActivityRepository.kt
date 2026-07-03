package com.kinderhub.ui.data.repository

import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.data.model.ActivitySession
import com.kinderhub.ui.data.model.AiSuggestion
import com.kinderhub.ui.data.model.Category
import com.kinderhub.ui.data.model.CategoryColorTint
import com.kinderhub.ui.data.model.DiscoverFeed
import kotlinx.coroutines.delay

interface ActivityRepository {
    suspend fun getDiscoverFeed(childId: String? = null, childName: String? = null): DiscoverFeed
    suspend fun searchActivities(query: String, filters: SearchFilters? = null): List<Activity>
    suspend fun getActivityById(id: String): Activity?
    suspend fun getCategories(): List<Category>
}

data class SearchFilters(
    val categoryId: String? = null,
    val childAge: Int? = null,
    val maxDistance: Float? = null,
    val maxPrice: Double? = null,
    val dayOfWeek: String? = null,
)

class MockActivityRepository : ActivityRepository {

    private val categories = listOf(
        Category("cat_swim", "Swimming", "waves", CategoryColorTint.Primary),
        Category("cat_steam", "STEAM", "flask-conical", CategoryColorTint.Success),
        Category("cat_sports", "Sports", "trophy", CategoryColorTint.Accent),
        Category("cat_music", "Music", "music", CategoryColorTint.Sunshine),
        Category("cat_tutors", "Tutors", "graduation-cap", CategoryColorTint.Primary),
    )

    private val aiSuggestions = listOf(
        AiSuggestion("sug_1", "Swim Saturdays", "swimming Saturday morning"),
        AiSuggestion("sug_2", "Half-term camps", "half term holiday camp"),
        AiSuggestion("sug_3", "After-school", "after school activities near me"),
    )

    private val activities = listOf(
        Activity(
            id = "act_001",
            title = "Little Otters Swim School",
            providerName = "Little Otters",
            category = categories[0],
            description = "Fun, safe swimming lessons for children aged 4-6. Small class sizes with qualified instructors.",
            imagePlaceholder = "photo · kids swimming",
            rating = 4.9f,
            reviewCount = 128,
            pricePerSession = 14.0,
            ageMin = 4,
            ageMax = 6,
            distance = 0.6f,
            nextSessionDay = "Sat",
            nextSessionTime = "9:00am",
            isVerified = true,
            isAiPick = true,
            spotsLeft = null,
            whatIncluded = listOf("DBS-checked instructors", "Towels provided", "Small groups (max 6)"),
            sessions = listOf(
                ActivitySession("sess_001", "Saturday", "9:00am", "45 mins", 4, 6, 14.0),
                ActivitySession("sess_002", "Saturday", "10:00am", "45 mins", 2, 6, 14.0),
                ActivitySession("sess_003", "Sunday", "9:00am", "45 mins", 0, 6, 14.0),
            ),
            cancellationPolicy = "Free cancellation up to 24 hours before"
        ),
        Activity(
            id = "act_002",
            title = "Code Ninjas Junior",
            providerName = "Code Ninjas",
            category = categories[1],
            description = "Introduction to coding for kids. Learn through games and fun projects.",
            imagePlaceholder = "photo · kids coding",
            rating = 4.7f,
            reviewCount = 89,
            pricePerSession = 22.0,
            ageMin = 5,
            ageMax = 8,
            distance = 1.2f,
            nextSessionDay = "Wed",
            nextSessionTime = "4:30pm",
            isVerified = true,
            isAiPick = false,
            spotsLeft = 3,
            whatIncluded = listOf("All equipment provided", "Take-home projects"),
            sessions = listOf(
                ActivitySession("sess_004", "Wednesday", "4:30pm", "60 mins", 3, 8, 22.0),
            )
        ),
        Activity(
            id = "act_003",
            title = "Riverside Football Academy",
            providerName = "Riverside Sports",
            category = categories[2],
            description = "Football coaching for all abilities. FA qualified coaches.",
            imagePlaceholder = "photo · kids football",
            rating = 4.8f,
            reviewCount = 156,
            pricePerSession = 9.0,
            ageMin = 5,
            ageMax = 11,
            distance = 0.8f,
            nextSessionDay = "Sun",
            nextSessionTime = "10:00am",
            isVerified = true,
            isAiPick = false,
            spotsLeft = null,
            whatIncluded = listOf("FA qualified coaches", "All equipment provided"),
            sessions = listOf(
                ActivitySession("sess_006", "Sunday", "10:00am", "90 mins", 8, 16, 9.0),
            )
        ),
        Activity(
            id = "act_004",
            title = "Bright Sparks Music",
            providerName = "Bright Sparks",
            category = categories[3],
            description = "Fun music classes introducing children to rhythm and instruments.",
            imagePlaceholder = "photo · music class",
            rating = 4.7f,
            reviewCount = 64,
            pricePerSession = 11.0,
            ageMin = 3,
            ageMax = 7,
            distance = 0.4f,
            nextSessionDay = "Wed",
            nextSessionTime = "4:30pm",
            isVerified = true,
            isAiPick = true,
            spotsLeft = 2,
            whatIncluded = listOf("Instruments provided", "End of term performance"),
            sessions = listOf(
                ActivitySession("sess_007", "Wednesday", "4:30pm", "45 mins", 2, 10, 11.0),
            )
        ),
        Activity(
            id = "act_005",
            title = "Splash & Play",
            providerName = "Aqua Fun",
            category = categories[0],
            description = "Water confidence and play sessions for younger children.",
            imagePlaceholder = "photo · splash play",
            rating = 4.6f,
            reviewCount = 42,
            pricePerSession = 12.0,
            ageMin = 2,
            ageMax = 5,
            distance = 1.5f,
            nextSessionDay = "Tue",
            nextSessionTime = "10:00am",
            isVerified = true,
            isAiPick = false,
            spotsLeft = null,
            whatIncluded = listOf("Parent can join", "Floats and toys provided"),
            sessions = listOf(
                ActivitySession("sess_008", "Tuesday", "10:00am", "30 mins", 6, 8, 12.0),
            )
        ),
    )

    override suspend fun getDiscoverFeed(childId: String?, childName: String?): DiscoverFeed {
        // Simulate network delay
        delay(1200)

        val greeting = if (childName != null) {
            "Good morning, Sarah"
        } else {
            "Discover activities"
        }

        // Filter activities based on child age if provided
        val filteredActivities = activities.sortedByDescending { it.isAiPick }

        return DiscoverFeed(
            greeting = greeting,
            activities = filteredActivities,
            categories = categories,
            aiSuggestions = aiSuggestions,
            selectedChildId = childId,
            selectedChildName = childName
        )
    }

    override suspend fun searchActivities(query: String, filters: SearchFilters?): List<Activity> {
        delay(800)

        return activities.filter { activity ->
            val matchesQuery = query.isBlank() ||
                    activity.title.contains(query, ignoreCase = true) ||
                    activity.category.name.contains(query, ignoreCase = true) ||
                    activity.description.contains(query, ignoreCase = true)

            val matchesCategory = filters?.categoryId == null ||
                    activity.category.id == filters.categoryId

            val matchesAge = filters?.childAge == null ||
                    (activity.ageMin <= filters.childAge && activity.ageMax >= filters.childAge)

            val matchesDistance = filters?.maxDistance == null ||
                    activity.distance <= filters.maxDistance

            val matchesPrice = filters?.maxPrice == null ||
                    activity.pricePerSession <= filters.maxPrice

            matchesQuery && matchesCategory && matchesAge && matchesDistance && matchesPrice
        }
    }

    override suspend fun getActivityById(id: String): Activity? {
        delay(500)
        return activities.find { it.id == id }
    }

    override suspend fun getCategories(): List<Category> {
        delay(300)
        return categories
    }
}
