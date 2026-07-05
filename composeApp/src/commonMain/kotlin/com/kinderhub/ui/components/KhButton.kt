package com.kinderhub.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.kinderhub.ui.theme.KhTheme

enum class KhButtonVariant {
    Primary,
    Secondary,
    Outline,
    Destructive,
    Wallet,
    Ai
}

enum class KhButtonSize {
    Small,
    Medium,
    Large
}

@Composable
fun KhButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: KhButtonVariant = KhButtonVariant.Primary,
    size: KhButtonSize = KhButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingText: String? = null,
    fullWidth: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes
    val motion = KhTheme.motion
    val buttonStyle = KhTheme.componentStyles.buttonStyle

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Theme-aware button height based on size
    val buttonHeight = when (size) {
        KhButtonSize.Small -> dimensions.buttonHeightSmall
        KhButtonSize.Medium -> dimensions.buttonHeight
        KhButtonSize.Large -> dimensions.buttonHeight + dimensions.spacingMd
    }

    // Calculate colors based on variant
    val (backgroundColor, contentColor, borderColor) = when (variant) {
        KhButtonVariant.Primary -> Triple(
            if (isPressed) colors.p7 else colors.p6,
            Color.White,
            Color.Transparent
        )
        KhButtonVariant.Secondary -> Triple(
            if (isPressed) colors.p100 else colors.p50,
            colors.p7,
            Color.Transparent
        )
        KhButtonVariant.Outline -> Triple(
            if (isPressed) colors.p50 else Color.Transparent,
            colors.p7,
            colors.p6
        )
        KhButtonVariant.Destructive -> Triple(
            if (isPressed) Color(0xFFA03830) else colors.error,
            Color.White,
            Color.Transparent
        )
        KhButtonVariant.Wallet -> Triple(
            colors.applePay,
            Color.White,
            Color.Transparent
        )
        KhButtonVariant.Ai -> Triple(
            Color.Transparent,
            Color.White,
            Color.Transparent
        )
    }

    // Theme-aware animations
    val animationSpec = if (motion.useSpringAnimations) {
        spring<Float>(
            stiffness = motion.springStiffness,
            dampingRatio = motion.springDamping / 100f
        )
    } else {
        tween<Float>(durationMillis = motion.durationFast)
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && motion.enableAnimations) 0.97f else 1f,
        animationSpec = animationSpec
    )

    val animatedBg by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = motion.durationFast)
    )

    val alpha = if (enabled && !loading) 1f else 0.5f

    // Apply theme-aware text transformation
    val displayText = if (buttonStyle.textAllCaps) text.uppercase() else text

    // Theme-aware shadow
    val elevation = if (buttonStyle.useShadow && variant == KhButtonVariant.Primary) {
        dimensions.cardElevation / 2
    } else {
        dimensions.cardElevation * 0
    }

    Box(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .height(buttonHeight)
            .scale(scale)
            .alpha(alpha)
            .shadow(
                elevation = elevation,
                shape = shapes.buttonShape,
                clip = false
            )
            .clip(shapes.buttonShape)
            .then(
                if (variant == KhButtonVariant.Ai && buttonStyle.useGradient) {
                    Modifier.background(KhTheme.aiGradient())
                } else {
                    Modifier.background(animatedBg)
                }
            )
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(
                        width = dimensions.borderWidth,
                        color = borderColor,
                        shape = shapes.buttonShape
                    )
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !loading,
                onClick = onClick
            )
            .padding(horizontal = buttonStyle.contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensions.iconSizeMd),
                    color = contentColor,
                    strokeWidth = dimensions.borderWidth
                )
                if (loadingText != null) {
                    Spacer(modifier = Modifier.width(buttonStyle.iconSpacing))
                    Text(
                        text = if (buttonStyle.textAllCaps) loadingText.uppercase() else loadingText,
                        style = typography.button,
                        color = contentColor
                    )
                }
            } else {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(buttonStyle.iconSpacing))
                }
                Text(
                    text = displayText,
                    style = typography.button,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Icon-only button variant
 */
@Composable
fun KhIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: KhButtonVariant = KhButtonVariant.Secondary,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
) {
    val colors = KhTheme.colors
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes
    val motion = KhTheme.motion

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (backgroundColor, borderColor) = when (variant) {
        KhButtonVariant.Primary -> Pair(
            if (isPressed) colors.p7 else colors.p6,
            Color.Transparent
        )
        KhButtonVariant.Secondary -> Pair(
            if (isPressed) colors.p100 else colors.p50,
            Color.Transparent
        )
        KhButtonVariant.Outline -> Pair(
            if (isPressed) colors.p50 else Color.Transparent,
            colors.p6
        )
        else -> Pair(colors.p50, Color.Transparent)
    }

    val scale by animateFloatAsState(
        targetValue = if (isPressed && motion.enableAnimations) 0.92f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh)
    )

    Box(
        modifier = modifier
            .size(dimensions.touchTarget)
            .scale(scale)
            .clip(shapes.buttonShape)
            .background(backgroundColor)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(dimensions.borderWidth, borderColor, shapes.buttonShape)
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

/**
 * Text-only button (no background)
 */
@Composable
fun KhTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color? = null,
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val motion = KhTheme.motion
    val buttonStyle = KhTheme.componentStyles.buttonStyle

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val textColor = color ?: colors.p6
    val displayText = if (buttonStyle.textAllCaps) text.uppercase() else text

    val alpha by animateFloatAsState(
        targetValue = when {
            !enabled -> 0.5f
            isPressed -> 0.7f
            else -> 1f
        },
        animationSpec = tween(durationMillis = motion.durationFast)
    )

    Text(
        text = displayText,
        style = typography.button,
        color = textColor.copy(alpha = alpha),
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
    )
}
