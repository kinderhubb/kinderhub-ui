package com.kinderhub.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Available app themes - each provides a distinct UX feel
 */
enum class AppTheme {
    Playful,      // Fun, colorful, rounded, bouncy - great for kids
    Professional, // Clean, minimal, business-like
    Cozy,         // Warm, soft, comfortable - family friendly
    Modern,       // High contrast, sharp, minimalist
    Accessible    // High contrast, larger targets, clear typography
}

/**
 * Complete theme configuration controlling all UX aspects
 */
@Immutable
data class ThemeConfig(
    val name: String,
    val colors: KhColors,
    val typography: KhTypography,
    val dimensions: ThemeDimensions,
    val shapes: ThemeShapes,
    val motion: ThemeMotion,
    val componentStyles: ComponentStyles
)

/**
 * Theme-specific dimensions (spacing, sizes)
 */
@Immutable
data class ThemeDimensions(
    // Spacing scale
    val spacingXs: Dp,
    val spacingSm: Dp,
    val spacingMd: Dp,
    val spacingLg: Dp,
    val spacingXl: Dp,
    val spacingXxl: Dp,

    // Screen padding
    val screenPadding: Dp,
    val cardPadding: Dp,

    // Component sizes
    val buttonHeight: Dp,
    val buttonHeightSmall: Dp,
    val inputHeight: Dp,
    val iconSizeSm: Dp,
    val iconSizeMd: Dp,
    val iconSizeLg: Dp,
    val touchTarget: Dp,

    // Avatar sizes
    val avatarSm: Dp,
    val avatarMd: Dp,
    val avatarLg: Dp,

    // Card dimensions
    val cardElevation: Dp,
    val cardImageHeight: Dp,

    // Navigation
    val bottomNavHeight: Dp,
    val sidebarWidth: Dp,
    val sidebarCollapsedWidth: Dp,

    // Divider
    val dividerThickness: Dp,

    // Border widths
    val borderWidth: Dp,
    val borderWidthFocused: Dp
)

/**
 * Theme-specific shapes
 */
@Immutable
data class ThemeShapes(
    val buttonShape: Shape,
    val cardShape: Shape,
    val inputShape: Shape,
    val chipShape: Shape,
    val avatarShape: Shape,
    val bottomSheetShape: Shape,
    val dialogShape: Shape,
    val imageShape: Shape,

    // Raw radius values for custom use
    val radiusXs: Dp,
    val radiusSm: Dp,
    val radiusMd: Dp,
    val radiusLg: Dp,
    val radiusXl: Dp
)

/**
 * Theme-specific motion/animation settings
 */
@Immutable
data class ThemeMotion(
    val durationFast: Int,      // Quick micro-interactions
    val durationMedium: Int,    // Standard transitions
    val durationSlow: Int,      // Emphasis animations

    val easingStandard: String, // Standard easing curve name
    val easingEmphasized: String,

    val useSpringAnimations: Boolean,
    val springStiffness: Float,
    val springDamping: Float,

    val enableHaptics: Boolean,
    val enableAnimations: Boolean
)

/**
 * Theme-specific component style variations
 */
@Immutable
data class ComponentStyles(
    val buttonStyle: ButtonStyleConfig,
    val cardStyle: CardStyleConfig,
    val inputStyle: InputStyleConfig,
    val navigationStyle: NavigationStyleConfig
)

@Immutable
data class ButtonStyleConfig(
    val useShadow: Boolean,
    val useGradient: Boolean,
    val iconSpacing: Dp,
    val contentPadding: Dp,
    val textAllCaps: Boolean
)

@Immutable
data class CardStyleConfig(
    val useShadow: Boolean,
    val useBorder: Boolean,
    val useOverlay: Boolean,
    val imageAspectRatio: Float,
    val contentSpacing: Dp
)

@Immutable
data class InputStyleConfig(
    val useBorder: Boolean,
    val useBackground: Boolean,
    val labelPosition: LabelPosition,
    val helperTextVisible: Boolean
)

enum class LabelPosition {
    Above,    // Label above input
    Inside,   // Floating label inside
    Inline    // Label inline with input
}

@Immutable
data class NavigationStyleConfig(
    val showLabels: Boolean,
    val useIndicator: Boolean,
    val indicatorStyle: IndicatorStyle,
    val iconStyle: IconStyle
)

enum class IndicatorStyle {
    Pill,       // Rounded pill behind icon
    Underline,  // Line under icon
    Background, // Full item background
    None
}

enum class IconStyle {
    Filled,
    Outlined,
    TwoTone
}

// ============================================================================
// THEME DEFINITIONS
// ============================================================================

/**
 * Playful Theme - Fun, colorful, rounded, bouncy
 * Perfect for engaging children and families
 */
val PlayfulTheme = ThemeConfig(
    name = "Playful",
    colors = KhColors(
        // Vibrant, cheerful colors
        p6 = Color(0xFFFF6B9D),      // Playful pink
        p7 = Color(0xFFE54B7D),
        p5 = Color(0xFFFF8DB5),
        p50 = Color(0xFFFFF0F5),
        p100 = Color(0xFFFFE4ED),
        pb = Color(0xFFFFD0DF),
        ac = Color(0xFF6C63FF),       // Fun purple
        ac7 = Color(0xFF5046E5),
        ac50 = Color(0xFFF0EFFF),
        ac100 = Color(0xFFE5E3FF),
        sun = Color(0xFFFFD93D),      // Bright yellow
        sun50 = Color(0xFFFFF9E0),
        sun100 = Color(0xFFFFF3C4),
        succ = Color(0xFF4ADE80),     // Fresh green
        succ50 = Color(0xFFE8FBF0),
        warn = Color(0xFFFFA726),
        warn50 = Color(0xFFFFF3E0),
        error = Color(0xFFFF5252),
        errorBg = Color(0xFFFFEBEE),
        errorBorder = Color(0xFFFFCDD2),
        tx = Color(0xFF2D3142),
        tx2 = Color(0xFF575A6E),
        tx3 = Color(0xFF9395A4),
        tx3b = Color(0xFF767889),
        bd = Color(0xFFE8E9ED),
        bg = Color(0xFFFFF8FA),
        surface = Color(0xFFFFFFFF),
        applePay = Color(0xFF1B1712)
    ),
    typography = KhTypography(
        display = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 36.sp, lineHeight = 44.sp),
        h1 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 38.sp),
        h2 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp),
        h3 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp),
        bodyLg = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, lineHeight = 28.sp),
        body = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        small = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
        xs = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
        label = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 18.sp),
        button = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 24.sp)
    ),
    dimensions = ThemeDimensions(
        spacingXs = 6.dp,
        spacingSm = 10.dp,
        spacingMd = 16.dp,
        spacingLg = 24.dp,
        spacingXl = 36.dp,
        spacingXxl = 48.dp,
        screenPadding = 20.dp,
        cardPadding = 16.dp,
        buttonHeight = 52.dp,
        buttonHeightSmall = 40.dp,
        inputHeight = 52.dp,
        iconSizeSm = 18.dp,
        iconSizeMd = 24.dp,
        iconSizeLg = 28.dp,
        touchTarget = 48.dp,
        avatarSm = 36.dp,
        avatarMd = 48.dp,
        avatarLg = 72.dp,
        cardElevation = 8.dp,
        cardImageHeight = 180.dp,
        bottomNavHeight = 80.dp,
        sidebarWidth = 260.dp,
        sidebarCollapsedWidth = 80.dp,
        dividerThickness = 2.dp,
        borderWidth = 2.dp,
        borderWidthFocused = 3.dp
    ),
    shapes = ThemeShapes(
        buttonShape = RoundedCornerShape(16.dp),
        cardShape = RoundedCornerShape(20.dp),
        inputShape = RoundedCornerShape(14.dp),
        chipShape = RoundedCornerShape(50),
        avatarShape = RoundedCornerShape(50),
        bottomSheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dialogShape = RoundedCornerShape(24.dp),
        imageShape = RoundedCornerShape(16.dp),
        radiusXs = 6.dp,
        radiusSm = 10.dp,
        radiusMd = 14.dp,
        radiusLg = 20.dp,
        radiusXl = 28.dp
    ),
    motion = ThemeMotion(
        durationFast = 150,
        durationMedium = 300,
        durationSlow = 500,
        easingStandard = "bouncy",
        easingEmphasized = "bouncy",
        useSpringAnimations = true,
        springStiffness = 400f,
        springDamping = 15f,
        enableHaptics = true,
        enableAnimations = true
    ),
    componentStyles = ComponentStyles(
        buttonStyle = ButtonStyleConfig(
            useShadow = true,
            useGradient = true,
            iconSpacing = 10.dp,
            contentPadding = 20.dp,
            textAllCaps = false
        ),
        cardStyle = CardStyleConfig(
            useShadow = true,
            useBorder = false,
            useOverlay = false,
            imageAspectRatio = 1.5f,
            contentSpacing = 14.dp
        ),
        inputStyle = InputStyleConfig(
            useBorder = true,
            useBackground = true,
            labelPosition = LabelPosition.Above,
            helperTextVisible = true
        ),
        navigationStyle = NavigationStyleConfig(
            showLabels = true,
            useIndicator = true,
            indicatorStyle = IndicatorStyle.Pill,
            iconStyle = IconStyle.Filled
        )
    )
)

/**
 * Professional Theme - Clean, minimal, business-like
 * For a polished, trustworthy appearance
 */
val ProfessionalTheme = ThemeConfig(
    name = "Professional",
    colors = KhColors(
        p6 = Color(0xFF1E40AF),       // Deep blue
        p7 = Color(0xFF1E3A8A),
        p5 = Color(0xFF3B82F6),
        p50 = Color(0xFFF0F5FF),
        p100 = Color(0xFFDBE8FF),
        pb = Color(0xFFBFD4FF),
        ac = Color(0xFF0891B2),        // Teal accent
        ac7 = Color(0xFF0E7490),
        ac50 = Color(0xFFECFEFF),
        ac100 = Color(0xFFCFFAFE),
        sun = Color(0xFFF59E0B),
        sun50 = Color(0xFFFEF6E7),
        sun100 = Color(0xFFFDEBCB),
        succ = Color(0xFF059669),
        succ50 = Color(0xFFD1FAE5),
        warn = Color(0xFFD97706),
        warn50 = Color(0xFFFEF3C7),
        error = Color(0xFFDC2626),
        errorBg = Color(0xFFFEE2E2),
        errorBorder = Color(0xFFFECACA),
        tx = Color(0xFF111827),
        tx2 = Color(0xFF4B5563),
        tx3 = Color(0xFF9CA3AF),
        tx3b = Color(0xFF6B7280),
        bd = Color(0xFFE5E7EB),
        bg = Color(0xFFF9FAFB),
        surface = Color(0xFFFFFFFF),
        applePay = Color(0xFF1B1712)
    ),
    typography = KhTypography(
        display = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 40.sp),
        h1 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 26.sp, lineHeight = 34.sp),
        h2 = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp, lineHeight = 26.sp),
        h3 = TextStyle(fontWeight = FontWeight.Medium, fontSize = 17.sp, lineHeight = 24.sp),
        bodyLg = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 26.sp),
        body = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 22.sp),
        small = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
        xs = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 14.sp),
        label = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
        button = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp)
    ),
    dimensions = ThemeDimensions(
        spacingXs = 4.dp,
        spacingSm = 8.dp,
        spacingMd = 12.dp,
        spacingLg = 20.dp,
        spacingXl = 28.dp,
        spacingXxl = 40.dp,
        screenPadding = 16.dp,
        cardPadding = 14.dp,
        buttonHeight = 44.dp,
        buttonHeightSmall = 32.dp,
        inputHeight = 44.dp,
        iconSizeSm = 16.dp,
        iconSizeMd = 20.dp,
        iconSizeLg = 24.dp,
        touchTarget = 44.dp,
        avatarSm = 32.dp,
        avatarMd = 40.dp,
        avatarLg = 56.dp,
        cardElevation = 2.dp,
        cardImageHeight = 160.dp,
        bottomNavHeight = 64.dp,
        sidebarWidth = 220.dp,
        sidebarCollapsedWidth = 64.dp,
        dividerThickness = 1.dp,
        borderWidth = 1.dp,
        borderWidthFocused = 2.dp
    ),
    shapes = ThemeShapes(
        buttonShape = RoundedCornerShape(8.dp),
        cardShape = RoundedCornerShape(8.dp),
        inputShape = RoundedCornerShape(6.dp),
        chipShape = RoundedCornerShape(6.dp),
        avatarShape = RoundedCornerShape(50),
        bottomSheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        dialogShape = RoundedCornerShape(12.dp),
        imageShape = RoundedCornerShape(6.dp),
        radiusXs = 4.dp,
        radiusSm = 6.dp,
        radiusMd = 8.dp,
        radiusLg = 12.dp,
        radiusXl = 16.dp
    ),
    motion = ThemeMotion(
        durationFast = 100,
        durationMedium = 200,
        durationSlow = 350,
        easingStandard = "easeOut",
        easingEmphasized = "easeInOut",
        useSpringAnimations = false,
        springStiffness = 300f,
        springDamping = 20f,
        enableHaptics = false,
        enableAnimations = true
    ),
    componentStyles = ComponentStyles(
        buttonStyle = ButtonStyleConfig(
            useShadow = false,
            useGradient = false,
            iconSpacing = 8.dp,
            contentPadding = 16.dp,
            textAllCaps = false
        ),
        cardStyle = CardStyleConfig(
            useShadow = false,
            useBorder = true,
            useOverlay = false,
            imageAspectRatio = 1.6f,
            contentSpacing = 12.dp
        ),
        inputStyle = InputStyleConfig(
            useBorder = true,
            useBackground = false,
            labelPosition = LabelPosition.Above,
            helperTextVisible = true
        ),
        navigationStyle = NavigationStyleConfig(
            showLabels = true,
            useIndicator = true,
            indicatorStyle = IndicatorStyle.Underline,
            iconStyle = IconStyle.Outlined
        )
    )
)

/**
 * Cozy Theme - Warm, soft, comfortable
 * Creates a welcoming, family-friendly atmosphere
 */
val CozyTheme = ThemeConfig(
    name = "Cozy",
    colors = KhColors(
        p6 = Color(0xFF8B5A3C),        // Warm brown
        p7 = Color(0xFF6B4530),
        p5 = Color(0xFFA67C5B),
        p50 = Color(0xFFFAF5F0),
        p100 = Color(0xFFF0E6DC),
        pb = Color(0xFFE0D0C0),
        ac = Color(0xFFD4A373),         // Warm tan
        ac7 = Color(0xFFB8895A),
        ac50 = Color(0xFFFDF6EE),
        ac100 = Color(0xFFF9EBD9),
        sun = Color(0xFFE8B339),
        sun50 = Color(0xFFFDF8EA),
        sun100 = Color(0xFFF9EDD0),
        succ = Color(0xFF6B8E5A),       // Sage green
        succ50 = Color(0xFFEDF3E8),
        warn = Color(0xFFCC8844),
        warn50 = Color(0xFFFDF3E4),
        error = Color(0xFFB84A3C),
        errorBg = Color(0xFFFAEBE8),
        errorBorder = Color(0xFFF0D0CC),
        tx = Color(0xFF3D3329),
        tx2 = Color(0xFF5C5046),
        tx3 = Color(0xFF9A8E82),
        tx3b = Color(0xFF7A6E62),
        bd = Color(0xFFE8DED4),
        bg = Color(0xFFFCF9F5),
        surface = Color(0xFFFFFDF9),
        applePay = Color(0xFF1B1712)
    ),
    typography = KhTypography(
        display = TextStyle(fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 42.sp),
        h1 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 36.sp),
        h2 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
        h3 = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp, lineHeight = 24.sp),
        bodyLg = TextStyle(fontWeight = FontWeight.Normal, fontSize = 17.sp, lineHeight = 28.sp),
        body = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 24.sp),
        small = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
        xs = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp),
        label = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
        button = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 22.sp)
    ),
    dimensions = ThemeDimensions(
        spacingXs = 6.dp,
        spacingSm = 10.dp,
        spacingMd = 16.dp,
        spacingLg = 24.dp,
        spacingXl = 36.dp,
        spacingXxl = 52.dp,
        screenPadding = 20.dp,
        cardPadding = 18.dp,
        buttonHeight = 50.dp,
        buttonHeightSmall = 38.dp,
        inputHeight = 50.dp,
        iconSizeSm = 18.dp,
        iconSizeMd = 22.dp,
        iconSizeLg = 26.dp,
        touchTarget = 48.dp,
        avatarSm = 36.dp,
        avatarMd = 48.dp,
        avatarLg = 68.dp,
        cardElevation = 4.dp,
        cardImageHeight = 200.dp,
        bottomNavHeight = 76.dp,
        sidebarWidth = 250.dp,
        sidebarCollapsedWidth = 76.dp,
        dividerThickness = 1.dp,
        borderWidth = 1.dp,
        borderWidthFocused = 2.dp
    ),
    shapes = ThemeShapes(
        buttonShape = RoundedCornerShape(12.dp),
        cardShape = RoundedCornerShape(16.dp),
        inputShape = RoundedCornerShape(12.dp),
        chipShape = RoundedCornerShape(20.dp),
        avatarShape = RoundedCornerShape(50),
        bottomSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dialogShape = RoundedCornerShape(20.dp),
        imageShape = RoundedCornerShape(12.dp),
        radiusXs = 6.dp,
        radiusSm = 10.dp,
        radiusMd = 12.dp,
        radiusLg = 16.dp,
        radiusXl = 24.dp
    ),
    motion = ThemeMotion(
        durationFast = 180,
        durationMedium = 320,
        durationSlow = 480,
        easingStandard = "easeInOut",
        easingEmphasized = "easeInOut",
        useSpringAnimations = true,
        springStiffness = 280f,
        springDamping = 22f,
        enableHaptics = true,
        enableAnimations = true
    ),
    componentStyles = ComponentStyles(
        buttonStyle = ButtonStyleConfig(
            useShadow = true,
            useGradient = false,
            iconSpacing = 10.dp,
            contentPadding = 18.dp,
            textAllCaps = false
        ),
        cardStyle = CardStyleConfig(
            useShadow = true,
            useBorder = false,
            useOverlay = false,
            imageAspectRatio = 1.4f,
            contentSpacing = 14.dp
        ),
        inputStyle = InputStyleConfig(
            useBorder = true,
            useBackground = true,
            labelPosition = LabelPosition.Above,
            helperTextVisible = true
        ),
        navigationStyle = NavigationStyleConfig(
            showLabels = true,
            useIndicator = true,
            indicatorStyle = IndicatorStyle.Background,
            iconStyle = IconStyle.Filled
        )
    )
)

/**
 * Modern Theme - High contrast, sharp, minimalist
 * Contemporary, tech-forward design
 */
val ModernTheme = ThemeConfig(
    name = "Modern",
    colors = KhColors(
        p6 = Color(0xFF18181B),        // Near black
        p7 = Color(0xFF09090B),
        p5 = Color(0xFF3F3F46),
        p50 = Color(0xFFF4F4F5),
        p100 = Color(0xFFE4E4E7),
        pb = Color(0xFFD4D4D8),
        ac = Color(0xFF8B5CF6),         // Vibrant purple
        ac7 = Color(0xFF7C3AED),
        ac50 = Color(0xFFF5F3FF),
        ac100 = Color(0xFFEDE9FE),
        sun = Color(0xFFFACC15),
        sun50 = Color(0xFFFEFCE8),
        sun100 = Color(0xFFFEF9C3),
        succ = Color(0xFF22C55E),
        succ50 = Color(0xFFDCFCE7),
        warn = Color(0xFFF97316),
        warn50 = Color(0xFFFFF7ED),
        error = Color(0xFFEF4444),
        errorBg = Color(0xFFFEF2F2),
        errorBorder = Color(0xFFFECACA),
        tx = Color(0xFF09090B),
        tx2 = Color(0xFF52525B),
        tx3 = Color(0xFFA1A1AA),
        tx3b = Color(0xFF71717A),
        bd = Color(0xFFE4E4E7),
        bg = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        applePay = Color(0xFF1B1712)
    ),
    typography = KhTypography(
        display = TextStyle(fontWeight = FontWeight.Black, fontSize = 36.sp, lineHeight = 40.sp),
        h1 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 32.sp),
        h2 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 24.sp),
        h3 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 20.sp),
        bodyLg = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        body = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
        small = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
        xs = TextStyle(fontWeight = FontWeight.Medium, fontSize = 10.sp, lineHeight = 12.sp),
        label = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 14.sp),
        button = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp)
    ),
    dimensions = ThemeDimensions(
        spacingXs = 4.dp,
        spacingSm = 8.dp,
        spacingMd = 12.dp,
        spacingLg = 16.dp,
        spacingXl = 24.dp,
        spacingXxl = 32.dp,
        screenPadding = 16.dp,
        cardPadding = 12.dp,
        buttonHeight = 44.dp,
        buttonHeightSmall = 32.dp,
        inputHeight = 44.dp,
        iconSizeSm = 16.dp,
        iconSizeMd = 20.dp,
        iconSizeLg = 24.dp,
        touchTarget = 44.dp,
        avatarSm = 28.dp,
        avatarMd = 36.dp,
        avatarLg = 52.dp,
        cardElevation = 0.dp,
        cardImageHeight = 160.dp,
        bottomNavHeight = 60.dp,
        sidebarWidth = 200.dp,
        sidebarCollapsedWidth = 60.dp,
        dividerThickness = 1.dp,
        borderWidth = 1.dp,
        borderWidthFocused = 2.dp
    ),
    shapes = ThemeShapes(
        buttonShape = RoundedCornerShape(6.dp),
        cardShape = RoundedCornerShape(8.dp),
        inputShape = RoundedCornerShape(6.dp),
        chipShape = RoundedCornerShape(4.dp),
        avatarShape = RoundedCornerShape(6.dp),
        bottomSheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        dialogShape = RoundedCornerShape(12.dp),
        imageShape = RoundedCornerShape(6.dp),
        radiusXs = 2.dp,
        radiusSm = 4.dp,
        radiusMd = 6.dp,
        radiusLg = 8.dp,
        radiusXl = 12.dp
    ),
    motion = ThemeMotion(
        durationFast = 100,
        durationMedium = 180,
        durationSlow = 280,
        easingStandard = "linear",
        easingEmphasized = "easeOut",
        useSpringAnimations = false,
        springStiffness = 500f,
        springDamping = 25f,
        enableHaptics = false,
        enableAnimations = true
    ),
    componentStyles = ComponentStyles(
        buttonStyle = ButtonStyleConfig(
            useShadow = false,
            useGradient = false,
            iconSpacing = 6.dp,
            contentPadding = 14.dp,
            textAllCaps = true
        ),
        cardStyle = CardStyleConfig(
            useShadow = false,
            useBorder = true,
            useOverlay = true,
            imageAspectRatio = 1.78f,
            contentSpacing = 10.dp
        ),
        inputStyle = InputStyleConfig(
            useBorder = true,
            useBackground = false,
            labelPosition = LabelPosition.Inside,
            helperTextVisible = false
        ),
        navigationStyle = NavigationStyleConfig(
            showLabels = false,
            useIndicator = true,
            indicatorStyle = IndicatorStyle.Underline,
            iconStyle = IconStyle.Outlined
        )
    )
)

/**
 * Accessible Theme - High contrast, larger targets, clear typography
 * Optimized for users who need enhanced visibility and touch targets
 */
val AccessibleTheme = ThemeConfig(
    name = "Accessible",
    colors = KhColors(
        p6 = Color(0xFF0052CC),        // High contrast blue
        p7 = Color(0xFF003D99),
        p5 = Color(0xFF4C9AFF),
        p50 = Color(0xFFEBF3FF),
        p100 = Color(0xFFCCE0FF),
        pb = Color(0xFF99C2FF),
        ac = Color(0xFF6554C0),         // Accessible purple
        ac7 = Color(0xFF403294),
        ac50 = Color(0xFFF4F3FF),
        ac100 = Color(0xFFE6E4F5),
        sun = Color(0xFFFF991F),
        sun50 = Color(0xFFFFF4E5),
        sun100 = Color(0xFFFFE2B8),
        succ = Color(0xFF006644),
        succ50 = Color(0xFFE3FCEF),
        warn = Color(0xFFFF8B00),
        warn50 = Color(0xFFFFF4E5),
        error = Color(0xFFBF2600),
        errorBg = Color(0xFFFFEBE6),
        errorBorder = Color(0xFFFFBDAD),
        tx = Color(0xFF172B4D),
        tx2 = Color(0xFF344563),
        tx3 = Color(0xFF6B778C),
        tx3b = Color(0xFF505F79),
        bd = Color(0xFFDFE1E6),
        bg = Color(0xFFFAFBFC),
        surface = Color(0xFFFFFFFF),
        applePay = Color(0xFF1B1712)
    ),
    typography = KhTypography(
        display = TextStyle(fontWeight = FontWeight.Bold, fontSize = 38.sp, lineHeight = 48.sp),
        h1 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 42.sp),
        h2 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 26.sp, lineHeight = 34.sp),
        h3 = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 30.sp),
        bodyLg = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp, lineHeight = 32.sp),
        body = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, lineHeight = 28.sp),
        small = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        xs = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
        label = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp),
        button = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 26.sp)
    ),
    dimensions = ThemeDimensions(
        spacingXs = 8.dp,
        spacingSm = 12.dp,
        spacingMd = 20.dp,
        spacingLg = 28.dp,
        spacingXl = 40.dp,
        spacingXxl = 56.dp,
        screenPadding = 24.dp,
        cardPadding = 20.dp,
        buttonHeight = 56.dp,
        buttonHeightSmall = 44.dp,
        inputHeight = 56.dp,
        iconSizeSm = 22.dp,
        iconSizeMd = 28.dp,
        iconSizeLg = 32.dp,
        touchTarget = 56.dp,
        avatarSm = 44.dp,
        avatarMd = 56.dp,
        avatarLg = 80.dp,
        cardElevation = 6.dp,
        cardImageHeight = 220.dp,
        bottomNavHeight = 88.dp,
        sidebarWidth = 280.dp,
        sidebarCollapsedWidth = 88.dp,
        dividerThickness = 2.dp,
        borderWidth = 2.dp,
        borderWidthFocused = 4.dp
    ),
    shapes = ThemeShapes(
        buttonShape = RoundedCornerShape(10.dp),
        cardShape = RoundedCornerShape(14.dp),
        inputShape = RoundedCornerShape(10.dp),
        chipShape = RoundedCornerShape(8.dp),
        avatarShape = RoundedCornerShape(50),
        bottomSheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dialogShape = RoundedCornerShape(16.dp),
        imageShape = RoundedCornerShape(10.dp),
        radiusXs = 6.dp,
        radiusSm = 8.dp,
        radiusMd = 10.dp,
        radiusLg = 14.dp,
        radiusXl = 20.dp
    ),
    motion = ThemeMotion(
        durationFast = 200,
        durationMedium = 400,
        durationSlow = 600,
        easingStandard = "easeInOut",
        easingEmphasized = "easeInOut",
        useSpringAnimations = false,
        springStiffness = 200f,
        springDamping = 30f,
        enableHaptics = true,
        enableAnimations = true
    ),
    componentStyles = ComponentStyles(
        buttonStyle = ButtonStyleConfig(
            useShadow = true,
            useGradient = false,
            iconSpacing = 12.dp,
            contentPadding = 22.dp,
            textAllCaps = false
        ),
        cardStyle = CardStyleConfig(
            useShadow = true,
            useBorder = true,
            useOverlay = false,
            imageAspectRatio = 1.4f,
            contentSpacing = 16.dp
        ),
        inputStyle = InputStyleConfig(
            useBorder = true,
            useBackground = true,
            labelPosition = LabelPosition.Above,
            helperTextVisible = true
        ),
        navigationStyle = NavigationStyleConfig(
            showLabels = true,
            useIndicator = true,
            indicatorStyle = IndicatorStyle.Background,
            iconStyle = IconStyle.Filled
        )
    )
)

// Composition locals
val LocalThemeConfig = staticCompositionLocalOf { PlayfulTheme }
val LocalThemeDimensions = staticCompositionLocalOf { PlayfulTheme.dimensions }
val LocalThemeShapes = staticCompositionLocalOf { PlayfulTheme.shapes }
val LocalThemeMotion = staticCompositionLocalOf { PlayfulTheme.motion }
val LocalComponentStyles = staticCompositionLocalOf { PlayfulTheme.componentStyles }

/**
 * Get ThemeConfig for an AppTheme
 */
fun getThemeConfig(theme: AppTheme): ThemeConfig = when (theme) {
    AppTheme.Playful -> PlayfulTheme
    AppTheme.Professional -> ProfessionalTheme
    AppTheme.Cozy -> CozyTheme
    AppTheme.Modern -> ModernTheme
    AppTheme.Accessible -> AccessibleTheme
}
