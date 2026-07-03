package com.kinderhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space

/**
 * Data class representing a menu item
 */
data class MenuItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val icon: @Composable (color: Color, size: Dp) -> Unit,
    val iconBackgroundColor: Color? = null,
    val titleColor: Color? = null,
    val showChevron: Boolean = true,
    val badge: String? = null
)

/**
 * Data class for menu section with header and items
 */
data class MenuSection(
    val title: String? = null,
    val items: List<MenuItem>
)

/**
 * Renders a list of menu sections
 */
@Composable
fun MenuSections(
    sections: List<MenuSection>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        sections.forEachIndexed { index, section ->
            if (section.title != null) {
                MenuSectionHeader(title = section.title)
            }
            MenuItemsList(
                items = section.items,
                onItemClick = onItemClick
            )
            if (index < sections.lastIndex) {
                Spacer(modifier = Modifier.padding(vertical = Space.s10))
            }
        }
    }
}

/**
 * Section header text
 */
@Composable
fun MenuSectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Text(
        text = title,
        style = typography.small,
        color = c.tx3,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.padding(horizontal = Space.screenPadding, vertical = Space.s10)
    )
}

/**
 * Renders a list of menu items with dividers
 */
@Composable
fun MenuItemsList(
    items: List<MenuItem>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(c.surface)
    ) {
        items.forEachIndexed { index, item ->
            MenuItemRow(
                item = item,
                onClick = { onItemClick(item.id) }
            )
            if (index < items.lastIndex) {
                HorizontalDivider(
                    color = c.bd,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 70.dp)
                )
            }
        }
    }
}

/**
 * Single menu item row
 */
@Composable
fun MenuItemRow(
    item: MenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val titleColor = item.titleColor ?: c.tx
    val iconBgColor = item.iconBackgroundColor ?: c.bg

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Space.screenPadding, vertical = Space.s14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with background
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(if (item.iconBackgroundColor != null) CircleShape else Shapes.md)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            item.icon(if (item.titleColor != null) item.titleColor else c.tx2, 20.dp)
        }

        Spacer(modifier = Modifier.width(Space.s14))

        // Title and subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = typography.body,
                color = titleColor,
                fontWeight = FontWeight.Medium
            )
            if (item.subtitle != null) {
                Text(
                    text = item.subtitle,
                    style = typography.small,
                    color = c.tx2
                )
            }
        }

        // Badge
        if (item.badge != null) {
            Box(
                modifier = Modifier
                    .clip(Shapes.pill)
                    .background(c.ac)
                    .padding(horizontal = Space.s8, vertical = 2.dp)
            ) {
                Text(
                    text = item.badge,
                    style = typography.xs,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.width(Space.s8))
        }

        // Chevron
        if (item.showChevron) {
            IconChevronRight(color = c.tx3, size = 18.dp)
        }
    }
}

/**
 * Action row (like Add Child, Add Payment) with accent color
 */
@Composable
fun MenuActionRow(
    title: String,
    icon: @Composable (color: Color, size: Dp) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Space.screenPadding, vertical = Space.s14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            icon(c.p6, 20.dp)
        }

        Spacer(modifier = Modifier.width(Space.s14))

        Text(
            text = title,
            style = typography.body,
            color = c.p6,
            fontWeight = FontWeight.Medium
        )
    }
}
