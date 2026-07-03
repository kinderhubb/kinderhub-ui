package com.kinderhub.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class KhColors(
    // Primary
    val p6: Color,      // Primary (buttons, links, active)
    val p7: Color,      // Primary dark (text-safe, pressed)
    val p5: Color,      // Primary light
    val p50: Color,     // Primary tint (selected chip bg, fills)
    val p100: Color,    // Primary tint 2
    val pb: Color,      // Primary border

    // Accent
    val ac: Color,      // Accent (highlights)
    val ac7: Color,     // Accent dark (text-safe)
    val ac50: Color,    // Accent tint
    val ac100: Color,   // Accent tint 2

    // Sunshine
    val sun: Color,     // Sunshine (ratings star, highlight)
    val sun50: Color,   // Sunshine tint
    val sun100: Color,  // Sunshine tint 2

    // Semantic
    val succ: Color,    // Success
    val succ50: Color,  // Success bg
    val warn: Color,    // Warning
    val warn50: Color,  // Warning bg
    val error: Color,   // Error/destructive
    val errorBg: Color, // Error bg
    val errorBorder: Color, // Error border

    // Text
    val tx: Color,      // Text primary
    val tx2: Color,     // Text secondary
    val tx3: Color,     // Text muted
    val tx3b: Color,    // Text meta

    // Surface
    val bd: Color,      // Border/divider
    val bg: Color,      // Screen background
    val surface: Color, // Card surface

    // Special
    val applePay: Color, // Apple Pay button
)

enum class Palette {
    DuskyRose,
    WarmEnglish,
    Heritage,
    TrustBlue
}

val DuskyRoseColors = KhColors(
    p6 = Color(0xFF9E5C6B),
    p7 = Color(0xFF7A4451),
    p5 = Color(0xFFB37886),
    p50 = Color(0xFFF5ECEE),
    p100 = Color(0xFFECDCE0),
    pb = Color(0xFFDDC4CB),
    ac = Color(0xFFC08160),
    ac7 = Color(0xFF96543A),
    ac50 = Color(0xFFF6E7DF),
    ac100 = Color(0xFFEBD5C9),
    sun = Color(0xFFCFA24E),
    sun50 = Color(0xFFF4EAD4),
    sun100 = Color(0xFFE9D6AE),
    succ = Color(0xFF5E8C57),
    succ50 = Color(0xFFE7EEE1),
    warn = Color(0xFFB5771E),
    warn50 = Color(0xFFF3E7CC),
    error = Color(0xFFC0453B),
    errorBg = Color(0xFFF6E0DC),
    errorBorder = Color(0xFFEEC6BF),
    tx = Color(0xFF2E2529),
    tx2 = Color(0xFF5E5158),
    tx3 = Color(0xFFA6969D),
    tx3b = Color(0xFF87757C),
    bd = Color(0xFFEADFDD),
    bg = Color(0xFFF5EBE9),
    surface = Color(0xFFFFFFFF),
    applePay = Color(0xFF1B1712),
)

val WarmEnglishColors = KhColors(
    p6 = Color(0xFF37637F),
    p7 = Color(0xFF2A4E66),
    p5 = Color(0xFF4E7CA1),
    p50 = Color(0xFFEDF1F4),
    p100 = Color(0xFFDCE6EC),
    pb = Color(0xFFC4D6E2),
    ac = Color(0xFFD0765A),
    ac7 = Color(0xFFA64B32),
    ac50 = Color(0xFFF6E7DF),
    ac100 = Color(0xFFEDD8CD),
    sun = Color(0xFFE0A32E),
    sun50 = Color(0xFFF7EDD6),
    sun100 = Color(0xFFEDDFB9),
    succ = Color(0xFF5E8C57),
    succ50 = Color(0xFFE6EFE0),
    warn = Color(0xFFB5771E),
    warn50 = Color(0xFFF5E7C8),
    error = Color(0xFFC0453B),
    errorBg = Color(0xFFF6E0DC),
    errorBorder = Color(0xFFEEC6BF),
    tx = Color(0xFF2B2723),
    tx2 = Color(0xFF5B534A),
    tx3 = Color(0xFFA39A8D),
    tx3b = Color(0xFF857C70),
    bd = Color(0xFFE8E1D6),
    bg = Color(0xFFF6F1E9),
    surface = Color(0xFFFFFFFF),
    applePay = Color(0xFF1B1712),
)

val HeritageColors = KhColors(
    p6 = Color(0xFF3E6B63),
    p7 = Color(0xFF2E4F49),
    p5 = Color(0xFF568079),
    p50 = Color(0xFFEAF1EE),
    p100 = Color(0xFFD5E4DF),
    pb = Color(0xFFBBD3CB),
    ac = Color(0xFFC06B4E),
    ac7 = Color(0xFF98452F),
    ac50 = Color(0xFFF3E5DC),
    ac100 = Color(0xFFE8D3C7),
    sun = Color(0xFFCE9A3A),
    sun50 = Color(0xFFF5EDD8),
    sun100 = Color(0xFFEBDCB7),
    succ = Color(0xFF5E8C57),
    succ50 = Color(0xFFE7EFE1),
    warn = Color(0xFFB5771E),
    warn50 = Color(0xFFF3E7CC),
    error = Color(0xFFC0453B),
    errorBg = Color(0xFFF6E0DC),
    errorBorder = Color(0xFFEEC6BF),
    tx = Color(0xFF28251F),
    tx2 = Color(0xFF57524A),
    tx3 = Color(0xFF9C948A),
    tx3b = Color(0xFF7C746A),
    bd = Color(0xFFE4DDD0),
    bg = Color(0xFFF4EFE4),
    surface = Color(0xFFFFFFFF),
    applePay = Color(0xFF1B1712),
)

val TrustBlueColors = KhColors(
    p6 = Color(0xFF2563EB),
    p7 = Color(0xFF1D4ED8),
    p5 = Color(0xFF3B82F6),
    p50 = Color(0xFFEFF4FF),
    p100 = Color(0xFFDBE7FF),
    pb = Color(0xFFBFD3FF),
    ac = Color(0xFFFF6B6B),
    ac7 = Color(0xFFD64545),
    ac50 = Color(0xFFFFF0F0),
    ac100 = Color(0xFFFFE6E6),
    sun = Color(0xFFFBBF24),
    sun50 = Color(0xFFFEF6E0),
    sun100 = Color(0xFFFCEFCB),
    succ = Color(0xFF16A34A),
    succ50 = Color(0xFFDCFCE7),
    warn = Color(0xFFD97706),
    warn50 = Color(0xFFFEF3C7),
    error = Color(0xFFC0453B),
    errorBg = Color(0xFFF6E0DC),
    errorBorder = Color(0xFFEEC6BF),
    tx = Color(0xFF1A202C),
    tx2 = Color(0xFF4B5563),
    tx3 = Color(0xFF9CA3AF),
    tx3b = Color(0xFF6B7280),
    bd = Color(0xFFE5E7EB),
    bg = Color(0xFFFAF8F5),
    surface = Color(0xFFFFFFFF),
    applePay = Color(0xFF1B1712),
)

val LocalKhColors = staticCompositionLocalOf { DuskyRoseColors }
