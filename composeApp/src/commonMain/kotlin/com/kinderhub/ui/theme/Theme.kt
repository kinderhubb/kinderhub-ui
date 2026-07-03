package com.kinderhub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
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

object KhTheme {
    val colors: KhColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKhColors.current

    val typography: KhTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalKhTypography.current

    val aiStyle: AiStyle
        @Composable
        @ReadOnlyComposable
        get() = LocalAiStyle.current

    @Composable
    fun aiGradient(): Brush {
        val c = colors
        return if (aiStyle.useGradient) {
            Brush.linearGradient(listOf(c.p6, c.ac))
        } else {
            Brush.linearGradient(listOf(c.p6, c.p6))
        }
    }
}

// Shorthand color accessors for common use
val KhColors.acc: Color get() = ac       // Accent shorthand
val KhColors.suc: Color get() = succ     // Success shorthand
val KhColors.err: Color get() = error    // Error shorthand

@Composable
fun KinderHubTheme(
    palette: Palette = Palette.DuskyRose,
    aiStyleGradient: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = when (palette) {
        Palette.DuskyRose -> DuskyRoseColors
        Palette.WarmEnglish -> WarmEnglishColors
        Palette.Heritage -> HeritageColors
        Palette.TrustBlue -> TrustBlueColors
    }

    val aiStyle = AiStyle(useGradient = aiStyleGradient)

    CompositionLocalProvider(
        LocalKhColors provides colors,
        LocalKhTypography provides DefaultKhTypography,
        LocalAiStyle provides aiStyle,
        content = content
    )
}
