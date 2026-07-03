package com.kinderhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Size
import com.kinderhub.ui.theme.Space

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
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(Size.bottomNavHeight)
            .background(c.surface)
            .drawBehind {
                drawLine(
                    color = c.bd,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(horizontal = Space.s12, vertical = Space.s10)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavItem(
                icon = { color -> IconCompass(color = color, size = 23.dp) },
                label = "Discover",
                isSelected = selectedTab == 0,
                onClick = onDiscoverClick
            )

            NavItem(
                icon = { color -> IconCalendarCheck(color = color, size = 23.dp) },
                label = "Bookings",
                isSelected = selectedTab == 1,
                onClick = onBookingsClick
            )

            NavItem(
                icon = { color -> IconMessageCircle(color = color, size = 23.dp) },
                label = "Messages",
                isSelected = selectedTab == 2,
                onClick = onMessagesClick,
                badgeCount = unreadCount
            )

            NavItem(
                icon = { color -> IconUser(color = color, size = 23.dp) },
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
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val color = if (isSelected) c.p6 else c.tx3

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = Space.s8)
    ) {
        Box {
            icon(color)

            // Badge
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 12.dp, y = (-2).dp)
                        .background(c.ac, Shapes.pill)
                        .align(Alignment.TopEnd)
                )
            }
        }

        Text(
            text = label,
            style = typography.small.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
