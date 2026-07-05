package com.kinderhub.ui.screens.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.components.IconCheck
import com.kinderhub.ui.theme.AppTheme
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.ThemeInfo
import com.kinderhub.ui.theme.ThemeManager
import com.kinderhub.ui.theme.getThemeConfig
import com.kinderhub.ui.theme.preview

@Composable
fun ThemeSettingsScreen(
    onBack: () -> Unit
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes

    val currentTheme by ThemeManager.currentTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface)
                .padding(dimensions.screenPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(dimensions.touchTarget)
                    .clip(shapes.buttonShape)
                    .background(colors.p50)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                IconArrowLeft(color = colors.p6, size = dimensions.iconSizeMd)
            }

            Spacer(modifier = Modifier.width(dimensions.spacingMd))

            Text(
                text = "Choose Theme",
                style = typography.h2,
                color = colors.tx
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(dimensions.screenPadding),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacingMd)
        ) {
            item {
                Text(
                    text = "Select a theme that matches your style",
                    style = typography.body,
                    color = colors.tx2
                )
                Spacer(modifier = Modifier.height(dimensions.spacingLg))
            }

            items(ThemeManager.availableThemes) { themeInfo ->
                ThemeCard(
                    themeInfo = themeInfo,
                    isSelected = currentTheme == themeInfo.theme,
                    onClick = { ThemeManager.setTheme(themeInfo.theme) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spacingXl))

                // Preview section
                Text(
                    text = "Theme Preview",
                    style = typography.h3,
                    color = colors.tx
                )

                Spacer(modifier = Modifier.height(dimensions.spacingMd))

                ThemePreviewCard(currentTheme)
            }
        }
    }
}

@Composable
private fun ThemeCard(
    themeInfo: ThemeInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes
    val motion = KhTheme.motion

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = motion.springStiffness)
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.p6 else colors.bd
    )

    val preview = themeInfo.theme.preview()
    val config = themeInfo.config

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (isSelected) dimensions.cardElevation else dimensions.cardElevation / 2,
                shape = shapes.cardShape
            )
            .clip(shapes.cardShape)
            .background(colors.surface)
            .border(
                width = if (isSelected) dimensions.borderWidthFocused else dimensions.borderWidth,
                color = borderColor,
                shape = shapes.cardShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(dimensions.cardPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color preview circles
            Row(
                horizontalArrangement = Arrangement.spacedBy((-8).dp)
            ) {
                ColorCircle(color = preview.primaryColor, size = 36.dp)
                ColorCircle(color = preview.accentColor, size = 36.dp)
                ColorCircle(color = preview.backgroundColor, size = 36.dp, borderColor = colors.bd)
            }

            Spacer(modifier = Modifier.width(dimensions.spacingMd))

            // Theme info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = themeInfo.emoji,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(dimensions.spacingSm))
                    Text(
                        text = themeInfo.displayName,
                        style = typography.bodyLg,
                        color = colors.tx,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(dimensions.spacingXs))
                Text(
                    text = themeInfo.description,
                    style = typography.small,
                    color = colors.tx3
                )
            }

            // Selection indicator
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(dimensions.iconSizeLg)
                        .background(colors.p6, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconCheck(color = Color.White, size = dimensions.iconSizeMd)
                }
            }
        }
    }
}

@Composable
private fun ColorCircle(
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    borderColor: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .background(color)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(1.dp, borderColor, CircleShape)
                } else Modifier
            )
    )
}

@Composable
private fun ThemePreviewCard(theme: AppTheme) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes
    val cardStyle = KhTheme.componentStyles.cardStyle
    val buttonStyle = KhTheme.componentStyles.buttonStyle

    val config = getThemeConfig(theme)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (cardStyle.useShadow) {
                    Modifier.shadow(dimensions.cardElevation, shapes.cardShape)
                } else Modifier
            )
            .clip(shapes.cardShape)
            .background(colors.surface)
            .then(
                if (cardStyle.useBorder) {
                    Modifier.border(dimensions.borderWidth, colors.bd, shapes.cardShape)
                } else Modifier
            )
            .padding(dimensions.cardPadding)
    ) {
        Text(
            text = "Sample Activity",
            style = typography.h3,
            color = colors.tx
        )

        Spacer(modifier = Modifier.height(dimensions.spacingSm))

        Text(
            text = "This is how content looks with the ${config.name} theme",
            style = typography.body,
            color = colors.tx2
        )

        Spacer(modifier = Modifier.height(dimensions.spacingMd))

        // Sample button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.buttonHeight)
                .then(
                    if (buttonStyle.useShadow) {
                        Modifier.shadow(dimensions.cardElevation / 2, shapes.buttonShape)
                    } else Modifier
                )
                .clip(shapes.buttonShape)
                .then(
                    if (buttonStyle.useGradient) {
                        Modifier.background(
                            Brush.horizontalGradient(listOf(colors.p6, colors.ac))
                        )
                    } else {
                        Modifier.background(colors.p6)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (buttonStyle.textAllCaps) "BOOK NOW" else "Book Now",
                style = typography.button,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(dimensions.spacingMd))

        // Sample chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacingSm)
        ) {
            listOf("Sports", "Arts", "Music").forEach { label ->
                Box(
                    modifier = Modifier
                        .clip(shapes.chipShape)
                        .background(colors.p50)
                        .padding(
                            horizontal = dimensions.spacingMd,
                            vertical = dimensions.spacingSm
                        )
                ) {
                    Text(
                        text = label,
                        style = typography.label,
                        color = colors.p7
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensions.spacingMd))

        // Theme details
        Column {
            ThemeDetailRow("Spacing", config.dimensions.spacingMd.toString())
            ThemeDetailRow("Button Height", config.dimensions.buttonHeight.toString())
            ThemeDetailRow("Animations", if (config.motion.enableAnimations) "Enabled" else "Disabled")
            ThemeDetailRow("Spring Animations", if (config.motion.useSpringAnimations) "Yes" else "No")
        }
    }
}

@Composable
private fun ThemeDetailRow(label: String, value: String) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.spacingXs),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = typography.small,
            color = colors.tx3
        )
        Text(
            text = value,
            style = typography.small,
            color = colors.tx2,
            fontWeight = FontWeight.Medium
        )
    }
}
