package com.kinderhub.ui.theme

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Material 3 Theme Manager - Manages app theme state
 */
object M3ThemeManager {

    private val _currentVariant = MutableStateFlow(M3ThemeVariant.Ocean)
    val currentVariant: StateFlow<M3ThemeVariant> = _currentVariant.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    /**
     * Available theme variants with display information
     */
    val availableThemes = listOf(
        M3ThemeInfo(
            variant = M3ThemeVariant.Playful,
            displayName = "Playful",
            description = "Fun pink and purple tones",
            emoji = "\uD83C\uDF88",
            primaryLight = Color(0xFFD81B60),
            primaryDark = Color(0xFFFFB1C8)
        ),
        M3ThemeInfo(
            variant = M3ThemeVariant.Ocean,
            displayName = "Ocean",
            description = "Calm blue and cyan",
            emoji = "\uD83C\uDF0A",
            primaryLight = Color(0xFF0061A4),
            primaryDark = Color(0xFF9ECAFF)
        ),
        M3ThemeInfo(
            variant = M3ThemeVariant.Forest,
            displayName = "Forest",
            description = "Fresh green and teal",
            emoji = "\uD83C\uDF32",
            primaryLight = Color(0xFF006C4C),
            primaryDark = Color(0xFF6CDBAC)
        ),
        M3ThemeInfo(
            variant = M3ThemeVariant.Sunset,
            displayName = "Sunset",
            description = "Warm orange and red",
            emoji = "\uD83C\uDF05",
            primaryLight = Color(0xFFBF360C),
            primaryDark = Color(0xFFFFB59D)
        ),
        M3ThemeInfo(
            variant = M3ThemeVariant.Lavender,
            displayName = "Lavender",
            description = "Creative purple and violet",
            emoji = "\uD83D\uDC9C",
            primaryLight = Color(0xFF6750A4),
            primaryDark = Color(0xFFCFBCFF)
        ),
        M3ThemeInfo(
            variant = M3ThemeVariant.Monochrome,
            displayName = "Monochrome",
            description = "Modern and minimal",
            emoji = "\u26AB",
            primaryLight = Color(0xFF006495),
            primaryDark = Color(0xFF8DCDFF)
        )
    )

    /**
     * Set the current theme variant
     */
    fun setVariant(variant: M3ThemeVariant) {
        _currentVariant.value = variant
    }

    /**
     * Toggle dark mode
     */
    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    /**
     * Set dark mode explicitly
     */
    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    /**
     * Get theme info for a variant
     */
    fun getThemeInfo(variant: M3ThemeVariant): M3ThemeInfo {
        return availableThemes.find { it.variant == variant } ?: availableThemes.first()
    }

    /**
     * Cycle to next theme
     */
    fun cycleTheme() {
        val variants = M3ThemeVariant.entries
        val currentIndex = variants.indexOf(_currentVariant.value)
        val nextIndex = (currentIndex + 1) % variants.size
        setVariant(variants[nextIndex])
    }
}

/**
 * Theme info for display purposes
 */
data class M3ThemeInfo(
    val variant: M3ThemeVariant,
    val displayName: String,
    val description: String,
    val emoji: String,
    val primaryLight: Color,
    val primaryDark: Color
)
