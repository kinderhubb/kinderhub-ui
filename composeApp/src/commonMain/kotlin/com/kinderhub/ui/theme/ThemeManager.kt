package com.kinderhub.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ThemeManager - Singleton for managing app theme state
 *
 * Handles:
 * - Current theme selection
 * - Theme switching
 * - Theme persistence (to be implemented per platform)
 */
object ThemeManager {

    private val _currentTheme = MutableStateFlow(AppTheme.Playful)
    val currentTheme: StateFlow<AppTheme> = _currentTheme.asStateFlow()

    private val _currentThemeConfig = MutableStateFlow(PlayfulTheme)
    val currentThemeConfig: StateFlow<ThemeConfig> = _currentThemeConfig.asStateFlow()

    /**
     * Available themes with their display information
     */
    val availableThemes = listOf(
        ThemeInfo(
            theme = AppTheme.Playful,
            displayName = "Playful",
            description = "Fun and colorful, perfect for kids",
            emoji = "\uD83C\uDF88" // balloon
        ),
        ThemeInfo(
            theme = AppTheme.Professional,
            displayName = "Professional",
            description = "Clean and minimal design",
            emoji = "\uD83D\uDCBC" // briefcase
        ),
        ThemeInfo(
            theme = AppTheme.Cozy,
            displayName = "Cozy",
            description = "Warm and welcoming atmosphere",
            emoji = "\u2615" // coffee
        ),
        ThemeInfo(
            theme = AppTheme.Modern,
            displayName = "Modern",
            description = "High contrast, minimalist style",
            emoji = "\u2728" // sparkles
        ),
        ThemeInfo(
            theme = AppTheme.Accessible,
            displayName = "Accessible",
            description = "Enhanced visibility and larger targets",
            emoji = "\uD83D\uDC41" // eye
        )
    )

    /**
     * Switch to a new theme
     */
    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
        _currentThemeConfig.value = getThemeConfig(theme)
        // TODO: Persist to local storage per platform
    }

    /**
     * Get theme info for a specific theme
     */
    fun getThemeInfo(theme: AppTheme): ThemeInfo {
        return availableThemes.find { it.theme == theme } ?: availableThemes.first()
    }

    /**
     * Cycle to the next theme (for quick switching)
     */
    fun cycleTheme() {
        val themes = AppTheme.entries
        val currentIndex = themes.indexOf(_currentTheme.value)
        val nextIndex = (currentIndex + 1) % themes.size
        setTheme(themes[nextIndex])
    }

    /**
     * Reset to default theme
     */
    fun resetToDefault() {
        setTheme(AppTheme.Playful)
    }
}

/**
 * Theme display information
 */
data class ThemeInfo(
    val theme: AppTheme,
    val displayName: String,
    val description: String,
    val emoji: String
) {
    val config: ThemeConfig get() = getThemeConfig(theme)
}

/**
 * Theme preview data for UI
 */
data class ThemePreview(
    val primaryColor: androidx.compose.ui.graphics.Color,
    val accentColor: androidx.compose.ui.graphics.Color,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val surfaceColor: androidx.compose.ui.graphics.Color
)

/**
 * Get preview colors for a theme
 */
fun AppTheme.preview(): ThemePreview {
    val config = getThemeConfig(this)
    return ThemePreview(
        primaryColor = config.colors.p6,
        accentColor = config.colors.ac,
        backgroundColor = config.colors.bg,
        surfaceColor = config.colors.surface
    )
}

/**
 * Extension to check if current theme is high contrast
 */
val AppTheme.isHighContrast: Boolean
    get() = this == AppTheme.Accessible || this == AppTheme.Modern

/**
 * Extension to check if current theme uses rounded corners
 */
val AppTheme.isRounded: Boolean
    get() = this == AppTheme.Playful || this == AppTheme.Cozy

/**
 * Extension to check if current theme uses animations
 */
val AppTheme.hasAnimations: Boolean
    get() = getThemeConfig(this).motion.enableAnimations

/**
 * Extension to check if current theme uses haptics
 */
val AppTheme.hasHaptics: Boolean
    get() = getThemeConfig(this).motion.enableHaptics
