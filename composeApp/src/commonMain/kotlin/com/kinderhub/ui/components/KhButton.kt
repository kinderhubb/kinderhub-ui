package com.kinderhub.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Size
import com.kinderhub.ui.theme.Space

enum class KhButtonVariant {
    Primary,
    Secondary,
    Outline,
    Destructive,
    Wallet,
    Ai
}

@Composable
fun KhButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: KhButtonVariant = KhButtonVariant.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingText: String? = null,
    icon: @Composable (() -> Unit)? = null,
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (backgroundColor, contentColor, borderColor) = when (variant) {
        KhButtonVariant.Primary -> Triple(
            if (isPressed) c.p7 else c.p6,
            Color.White,
            Color.Transparent
        )
        KhButtonVariant.Secondary -> Triple(
            if (isPressed) c.p100 else c.p50,
            c.p7,
            Color.Transparent
        )
        KhButtonVariant.Outline -> Triple(
            if (isPressed) c.p50 else Color.Transparent,
            c.p7,
            c.p6
        )
        KhButtonVariant.Destructive -> Triple(
            if (isPressed) Color(0xFFA03830) else c.error,
            Color.White,
            Color.Transparent
        )
        KhButtonVariant.Wallet -> Triple(
            c.applePay,
            Color.White,
            Color.Transparent
        )
        KhButtonVariant.Ai -> Triple(
            Color.Transparent, // Will use gradient
            Color.White,
            Color.Transparent
        )
    }

    val animatedBg by animateColorAsState(backgroundColor)
    val alpha = if (enabled && !loading) 1f else 0.5f

    Box(
        modifier = modifier
            .height(Size.buttonHeight)
            .fillMaxWidth()
            .alpha(alpha)
            .clip(Shapes.md)
            .then(
                if (variant == KhButtonVariant.Ai) {
                    Modifier.background(KhTheme.aiGradient())
                } else {
                    Modifier.background(animatedBg)
                }
            )
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(1.dp, borderColor, Shapes.md)
                } else {
                    Modifier
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled && !loading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
                if (loadingText != null) {
                    Spacer(modifier = Modifier.width(Space.s8))
                    Text(
                        text = loadingText,
                        style = typography.button,
                        color = contentColor
                    )
                }
            } else {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(Space.s8))
                }
                Text(
                    text = text,
                    style = typography.button,
                    color = contentColor
                )
            }
        }
    }
}
