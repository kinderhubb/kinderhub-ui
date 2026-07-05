package com.kinderhub.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kinderhub.ui.navigation.KinderHubNavHost
import com.kinderhub.ui.theme.AppTheme
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.KinderHubTheme
import com.kinderhub.ui.theme.Palette
import com.kinderhub.ui.theme.ThemeManager
import com.kinderhub.ui.util.AppLanguage
import com.kinderhub.ui.util.LocalizationProvider
import org.koin.compose.KoinContext

/**
 * Main App composable with dynamic theme support
 *
 * The theme can be changed at runtime via ThemeManager.setTheme()
 * and the entire UI will update to reflect the new theme's:
 * - Colors
 * - Typography
 * - Spacing and dimensions
 * - Shapes and corner radii
 * - Animation settings
 * - Component styles
 */
@Composable
fun App(
    initialTheme: AppTheme = AppTheme.Playful,
    initialLanguage: AppLanguage = AppLanguage.English
) {
    // Observe theme changes from ThemeManager
    val currentTheme by ThemeManager.currentTheme.collectAsState()

    KoinContext {
        LocalizationProvider(initialLanguage = initialLanguage) {
            KinderHubTheme(theme = currentTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = KhTheme.colors.bg
                ) {
                    KinderHubNavHost()
                }
            }
        }
    }
}

/**
 * Legacy App composable for backward compatibility
 * @deprecated Use App(initialTheme: AppTheme) instead
 */
@Deprecated(
    message = "Use App with AppTheme parameter instead",
    replaceWith = ReplaceWith("App(initialTheme = AppTheme.Playful, initialLanguage = initialLanguage)")
)
@Composable
fun App(
    palette: Palette,
    initialLanguage: AppLanguage = AppLanguage.English
) {
    // Map old palette to new theme
    val theme = when (palette) {
        Palette.DuskyRose -> AppTheme.Cozy
        Palette.WarmEnglish -> AppTheme.Professional
        Palette.Heritage -> AppTheme.Cozy
        Palette.TrustBlue -> AppTheme.Modern
    }

    App(initialTheme = theme, initialLanguage = initialLanguage)
}
