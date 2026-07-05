package com.kinderhub.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kinderhub.ui.navigation.KinderHubNavHost
import com.kinderhub.ui.theme.KinderHubM3Theme
import com.kinderhub.ui.theme.M3ThemeManager
import com.kinderhub.ui.theme.M3ThemeVariant
import com.kinderhub.ui.util.AppLanguage
import com.kinderhub.ui.util.LocalizationProvider
import org.koin.compose.KoinContext

/**
 * Main App composable with Material 3 theme support
 *
 * The theme can be changed at runtime via M3ThemeManager.setVariant()
 * Supports both light and dark mode with 6 color schemes:
 * - Playful (Pink/Purple)
 * - Ocean (Blue/Cyan)
 * - Forest (Green/Teal)
 * - Sunset (Orange/Red)
 * - Lavender (Purple/Violet)
 * - Monochrome (Gray/Black)
 */
@Composable
fun App(
    initialVariant: M3ThemeVariant = M3ThemeVariant.Ocean,
    initialLanguage: AppLanguage = AppLanguage.English
) {
    // Observe theme changes from M3ThemeManager
    val currentVariant by M3ThemeManager.currentVariant.collectAsState()
    val isDarkMode by M3ThemeManager.isDarkMode.collectAsState()

    KoinContext {
        LocalizationProvider(initialLanguage = initialLanguage) {
            KinderHubM3Theme(
                variant = currentVariant,
                darkTheme = isDarkMode
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KinderHubNavHost()
                }
            }
        }
    }
}
