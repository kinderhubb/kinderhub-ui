package com.kinderhub.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class AiStyle(
    val useGradient: Boolean = true
)

val LocalAiStyle = staticCompositionLocalOf { AiStyle() }

/**
 * KhTheme - Main theme accessor object
 * Provides access to all theme properties throughout the app
 */
object KhTheme {
    /**
     * Current theme configuration
     */
    val config: ThemeConfig
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeConfig.current

    /**
     * Current color scheme
     */
    val colors: KhColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKhColors.current

    /**
     * Current typography styles
     */
    val typography: KhTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalKhTypography.current

    /**
     * Current dimensions (spacing, sizes)
     */
    val dimensions: ThemeDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeDimensions.current

    /**
     * Current shape definitions
     */
    val shapes: ThemeShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeShapes.current

    /**
     * Current motion/animation settings
     */
    val motion: ThemeMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeMotion.current

    /**
     * Current component style configurations
     */
    val componentStyles: ComponentStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalComponentStyles.current

    /**
     * AI style settings (gradients, etc.)
     */
    val aiStyle: AiStyle
        @Composable
        @ReadOnlyComposable
        get() = LocalAiStyle.current

    /**
     * Generate AI gradient brush using theme colors
     */
    @Composable
    fun aiGradient(): Brush {
        val c = colors
        return if (aiStyle.useGradient) {
            Brush.linearGradient(listOf(c.p6, c.ac))
        } else {
            Brush.linearGradient(listOf(c.p6, c.p6))
        }
    }

    // ========================================================================
    // Convenience Dimension Accessors
    // ========================================================================

    /**
     * Quick spacing accessors
     */
    object spacing {
        val xs: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.spacingXs
        val sm: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.spacingSm
        val md: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.spacingMd
        val lg: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.spacingLg
        val xl: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.spacingXl
        val xxl: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.spacingXxl
        val screen: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.screenPadding
    }

    /**
     * Quick radius accessors
     */
    object radius {
        val xs: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = shapes.radiusXs
        val sm: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = shapes.radiusSm
        val md: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = shapes.radiusMd
        val lg: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = shapes.radiusLg
        val xl: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = shapes.radiusXl
    }

    /**
     * Quick icon size accessors
     */
    object iconSize {
        val sm: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.iconSizeSm
        val md: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.iconSizeMd
        val lg: androidx.compose.ui.unit.Dp
            @Composable @ReadOnlyComposable get() = dimensions.iconSizeLg
    }
}

// Shorthand color accessors for common use
val KhColors.acc: Color get() = ac       // Accent shorthand
val KhColors.suc: Color get() = succ     // Success shorthand
val KhColors.err: Color get() = error    // Error shorthand

/**
 * Main theme composable - wraps content with comprehensive theming
 *
 * @param theme The app theme to apply (Playful, Professional, Cozy, Modern, Accessible)
 * @param aiStyleGradient Whether to use gradients for AI elements
 * @param content The composable content to wrap
 */
@Composable
fun KinderHubTheme(
    theme: AppTheme = AppTheme.Playful,
    aiStyleGradient: Boolean = true,
    content: @Composable () -> Unit
) {
    val themeConfig = getThemeConfig(theme)
    val aiStyle = AiStyle(useGradient = aiStyleGradient)

    CompositionLocalProvider(
        LocalThemeConfig provides themeConfig,
        LocalKhColors provides themeConfig.colors,
        LocalKhTypography provides themeConfig.typography,
        LocalThemeDimensions provides themeConfig.dimensions,
        LocalThemeShapes provides themeConfig.shapes,
        LocalThemeMotion provides themeConfig.motion,
        LocalComponentStyles provides themeConfig.componentStyles,
        LocalAiStyle provides aiStyle,
        content = content
    )
}

/**
 * Legacy palette-based theming (for backward compatibility)
 * @deprecated Use KinderHubTheme with AppTheme instead
 */
@Deprecated(
    message = "Use KinderHubTheme with AppTheme parameter instead",
    replaceWith = ReplaceWith("KinderHubTheme(theme = AppTheme.Playful, content = content)")
)
@Composable
fun KinderHubTheme(
    palette: Palette = Palette.DuskyRose,
    aiStyleGradient: Boolean = true,
    content: @Composable () -> Unit
) {
    // Map old palette to closest new theme
    val theme = when (palette) {
        Palette.DuskyRose -> AppTheme.Cozy
        Palette.WarmEnglish -> AppTheme.Professional
        Palette.Heritage -> AppTheme.Cozy
        Palette.TrustBlue -> AppTheme.Modern
    }

    val colors = when (palette) {
        Palette.DuskyRose -> DuskyRoseColors
        Palette.WarmEnglish -> WarmEnglishColors
        Palette.Heritage -> HeritageColors
        Palette.TrustBlue -> TrustBlueColors
    }

    val themeConfig = getThemeConfig(theme)
    val aiStyle = AiStyle(useGradient = aiStyleGradient)

    CompositionLocalProvider(
        LocalThemeConfig provides themeConfig,
        LocalKhColors provides colors,
        LocalKhTypography provides themeConfig.typography,
        LocalThemeDimensions provides themeConfig.dimensions,
        LocalThemeShapes provides themeConfig.shapes,
        LocalThemeMotion provides themeConfig.motion,
        LocalComponentStyles provides themeConfig.componentStyles,
        LocalAiStyle provides aiStyle,
        content = content
    )
}
