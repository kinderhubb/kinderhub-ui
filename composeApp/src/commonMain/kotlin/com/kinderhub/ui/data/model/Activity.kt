package com.kinderhub.ui.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val id: String,
    val title: String,
    val providerName: String,
    val category: Category,
    val description: String,
    val imageUrl: String? = null,
    val imagePlaceholder: String = "photo · activity", // For mock display
    val rating: Float,
    val reviewCount: Int,
    val pricePerSession: Double,
    val currency: String = "£",
    val ageMin: Int,
    val ageMax: Int,
    val distance: Float, // in miles
    val nextSessionDay: String, // e.g., "Sat"
    val nextSessionTime: String, // e.g., "9:00am"
    val isVerified: Boolean = true,
    val isAiPick: Boolean = false,
    val spotsLeft: Int? = null, // null means plenty available
    val sessions: List<ActivitySession> = emptyList(),
    val whatIncluded: List<String> = emptyList(),
    val cancellationPolicy: String = "Free cancellation up to 24 hours before",
)

@Serializable
data class ActivitySession(
    val id: String,
    val dayOfWeek: String,
    val time: String,
    val duration: String, // e.g., "45 mins"
    val spotsLeft: Int,
    val totalSpots: Int,
    val pricePerSession: Double,
    val dates: List<String> = emptyList(), // Available dates
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val icon: String, // Icon name like "waves", "flask-conical", etc.
    val colorTint: CategoryColorTint,
)

@Serializable
enum class CategoryColorTint {
    Primary,    // Swimming - blue/primary
    Success,    // STEAM - green
    Accent,     // Sports - terracotta
    Sunshine,   // Music - gold
}

@Serializable
data class AiSuggestion(
    val id: String,
    val label: String,
    val query: String,
)

@Serializable
data class DiscoverFeed(
    val greeting: String,
    val activities: List<Activity>,
    val categories: List<Category>,
    val aiSuggestions: List<AiSuggestion>,
    val selectedChildId: String? = null,
    val selectedChildName: String? = null,
)
