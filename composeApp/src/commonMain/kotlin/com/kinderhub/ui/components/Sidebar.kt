package com.kinderhub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Size
import com.kinderhub.ui.theme.Space

/**
 * Collapsible sidebar for web/desktop layout
 * Expanded: 240dp with logo, labels, and children panel
 * Collapsed: 76dp icon rail
 */
@Composable
fun Sidebar(
    selectedTab: Int,
    isCollapsed: Boolean,
    unreadCount: Int = 0,
    children: List<Child> = emptyList(),
    onToggleCollapse: () -> Unit,
    onDiscoverClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onMessagesClick: () -> Unit,
    onAccountClick: () -> Unit,
    onAddChildClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val sidebarWidth by animateDpAsState(
        targetValue = if (isCollapsed) Size.sidebarCollapsed else Size.sidebarExpanded,
        animationSpec = tween(durationMillis = 200)
    )

    Column(
        modifier = modifier
            .width(sidebarWidth)
            .fillMaxHeight()
            .background(c.surface)
            .drawBehind {
                // Right border
                drawLine(
                    color = c.bd,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(vertical = Space.s22, horizontal = if (isCollapsed) Space.s12 else Space.s16)
    ) {
        // Logo
        LogoSection(isCollapsed = isCollapsed)

        Spacer(modifier = Modifier.height(Space.s32))

        // Navigation items
        Column(verticalArrangement = Arrangement.spacedBy(Space.s4)) {
            NavItem(
                icon = { color -> IconCompass(color = color, size = 20.dp) },
                label = "Discover",
                isSelected = selectedTab == 0,
                isCollapsed = isCollapsed,
                onClick = onDiscoverClick
            )

            NavItem(
                icon = { color -> IconCalendarCheck(color = color, size = 20.dp) },
                label = "My Bookings",
                isSelected = selectedTab == 1,
                isCollapsed = isCollapsed,
                onClick = onBookingsClick
            )

            NavItem(
                icon = { color -> IconMessageCircle(color = color, size = 20.dp) },
                label = "Messages",
                isSelected = selectedTab == 2,
                isCollapsed = isCollapsed,
                badgeCount = unreadCount,
                onClick = onMessagesClick
            )

            NavItem(
                icon = { color -> IconUser(color = color, size = 20.dp) },
                label = "Account",
                isSelected = selectedTab == 3,
                isCollapsed = isCollapsed,
                onClick = onAccountClick
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Children panel (only on Discover when expanded)
        if (!isCollapsed && selectedTab == 0 && children.isNotEmpty()) {
            ChildrenPanel(
                children = children,
                onAddChild = onAddChildClick
            )
            Spacer(modifier = Modifier.height(Space.s16))
        }

        // Collapse toggle
        CollapseToggle(
            isCollapsed = isCollapsed,
            onClick = onToggleCollapse
        )
    }
}

@Composable
private fun LogoSection(isCollapsed: Boolean) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = Space.s4)
    ) {
        // Logo icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(Shapes.md)
                .background(c.p6),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "K",
                style = typography.h3,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        AnimatedVisibility(
            visible = !isCollapsed,
            enter = fadeIn(animationSpec = tween(150)),
            exit = fadeOut(animationSpec = tween(100))
        ) {
            Row {
                Spacer(modifier = Modifier.width(Space.s12))
                Text(
                    text = "KinderHub",
                    style = typography.h3,
                    color = c.tx,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: @Composable (Color) -> Unit,
    label: String,
    isSelected: Boolean,
    isCollapsed: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val bgColor = if (isSelected) c.p50 else Color.Transparent
    val textColor = if (isSelected) c.p7 else c.tx3

    Box(
        modifier = Modifier
            .clip(Shapes.md)
            .background(bgColor)
            .clickable { onClick() }
            .padding(
                horizontal = if (isCollapsed) Space.s12 else Space.s14,
                vertical = Space.s12
            ),
        contentAlignment = if (isCollapsed) Alignment.Center else Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box {
                icon(textColor)

                // Unread badge
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.TopEnd)
                            .background(c.ac, CircleShape)
                    )
                }
            }

            AnimatedVisibility(
                visible = !isCollapsed,
                enter = fadeIn(animationSpec = tween(150)),
                exit = fadeOut(animationSpec = tween(100))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(Space.s14))
                    Text(
                        text = label,
                        style = typography.body,
                        color = textColor,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                    )

                    if (badgeCount > 0) {
                        Spacer(modifier = Modifier.width(Space.s8))
                        Box(
                            modifier = Modifier
                                .clip(Shapes.pill)
                                .background(c.ac)
                                .padding(horizontal = Space.s8, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeCount.toString(),
                                style = typography.xs,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChildrenPanel(
    children: List<Child>,
    onAddChild: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .clip(Shapes.lg)
            .background(c.bg)
            .padding(Space.s14)
    ) {
        Text(
            text = "Children",
            style = typography.small,
            color = c.tx3,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(Space.s12))

        Row(horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
            children.forEach { child ->
                val avatarColor = when (child.avatarColor) {
                    AvatarColor.Accent -> c.ac
                    AvatarColor.Sunshine -> c.sun
                    AvatarColor.Primary -> c.p6
                    AvatarColor.Success -> c.succ
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(avatarColor.copy(alpha = 0.15f))
                        .clickable { /* Select child */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = child.firstName.first().toString(),
                        style = typography.body,
                        color = avatarColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Add child button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(c.p50)
                    .clickable { onAddChild() },
                contentAlignment = Alignment.Center
            ) {
                IconPlus(color = c.p6, size = 18.dp)
            }
        }
    }
}

@Composable
private fun CollapseToggle(
    isCollapsed: Boolean,
    onClick: () -> Unit
) {
    val c = KhTheme.colors

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(c.bg)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isCollapsed) {
            IconPanelLeftOpen(color = c.tx3, size = 18.dp)
        } else {
            IconPanelLeftClose(color = c.tx3, size = 18.dp)
        }
    }
}

// Space.s22 extension
private val Space.s22 get() = 22.dp
private val Space.s28 get() = 28.dp
