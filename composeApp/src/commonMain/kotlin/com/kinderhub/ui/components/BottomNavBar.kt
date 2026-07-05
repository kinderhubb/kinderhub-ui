package com.kinderhub.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.IndicatorStyle
import com.kinderhub.ui.theme.KhTheme

@Composable
fun BottomNavBar(
    selectedTab: Int,
    unreadCount: Int = 0,
    onDiscoverClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KhTheme.colors
    val dimensions = KhTheme.dimensions
    val navStyle = KhTheme.componentStyles.navigationStyle

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.bottomNavHeight)
            .background(colors.surface)
            .drawBehind {
                drawLine(
                    color = colors.bd,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = dimensions.dividerThickness.toPx()
                )
            }
            .padding(
                horizontal = dimensions.spacingMd,
                vertical = dimensions.spacingSm
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavItem(
                icon = { color -> IconCompass(color = color, size = dimensions.iconSizeLg) },
                label = "Discover",
                isSelected = selectedTab == 0,
                onClick = onDiscoverClick
            )

            NavItem(
                icon = { color -> IconCalendarCheck(color = color, size = dimensions.iconSizeLg) },
                label = "Bookings",
                isSelected = selectedTab == 1,
                onClick = onBookingsClick
            )

            NavItem(
                icon = { color -> IconMessageCircle(color = color, size = dimensions.iconSizeLg) },
                label = "Messages",
                isSelected = selectedTab == 2,
                onClick = onMessagesClick,
                badgeCount = unreadCount
            )

            NavItem(
                icon = { color -> IconUser(color = color, size = dimensions.iconSizeLg) },
                label = "Account",
                isSelected = selectedTab == 3,
                onClick = onAccountClick
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: @Composable (Color) -> Unit,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int = 0
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes
    val motion = KhTheme.motion
    val navStyle = KhTheme.componentStyles.navigationStyle

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Theme-aware animations
    val scale by animateFloatAsState(
        targetValue = if (isPressed && motion.enableAnimations) 0.9f else 1f,
        animationSpec = if (motion.useSpringAnimations) {
            spring(stiffness = motion.springStiffness)
        } else {
            tween(durationMillis = motion.durationFast)
        }
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) colors.p6 else colors.tx3,
        animationSpec = tween(durationMillis = motion.durationMedium)
    )

    val labelColor by animateColorAsState(
        targetValue = if (isSelected) colors.p6 else colors.tx3,
        animationSpec = tween(durationMillis = motion.durationMedium)
    )

    // Indicator background color
    val indicatorColor by animateColorAsState(
        targetValue = if (isSelected && navStyle.useIndicator) colors.p50 else Color.Transparent,
        animationSpec = tween(durationMillis = motion.durationMedium)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = dimensions.spacingSm)
    ) {
        // Icon with indicator
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .then(
                    when (navStyle.indicatorStyle) {
                        IndicatorStyle.Pill -> Modifier
                            .clip(shapes.chipShape)
                            .background(indicatorColor)
                            .padding(
                                horizontal = dimensions.spacingMd,
                                vertical = dimensions.spacingXs
                            )
                        IndicatorStyle.Background -> Modifier
                            .clip(shapes.buttonShape)
                            .background(indicatorColor)
                            .padding(dimensions.spacingSm)
                        else -> Modifier
                    }
                )
        ) {
            icon(iconColor)

            // Badge
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(dimensions.spacingSm)
                        .offset(
                            x = dimensions.iconSizeMd / 2,
                            y = -(dimensions.spacingXs)
                        )
                        .background(colors.ac, shapes.avatarShape)
                        .align(Alignment.TopEnd)
                )
            }
        }

        // Underline indicator
        if (navStyle.indicatorStyle == IndicatorStyle.Underline && isSelected && navStyle.useIndicator) {
            Box(
                modifier = Modifier
                    .padding(top = dimensions.spacingXs)
                    .size(width = dimensions.iconSizeLg, height = dimensions.borderWidthFocused)
                    .background(colors.p6, shapes.chipShape)
            )
        }

        // Label (if enabled in theme)
        if (navStyle.showLabels) {
            Text(
                text = label,
                style = typography.xs.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = labelColor,
                modifier = Modifier.padding(top = dimensions.spacingXs)
            )
        }
    }
}
