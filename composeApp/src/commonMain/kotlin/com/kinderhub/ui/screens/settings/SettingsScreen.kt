package com.kinderhub.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.components.IconBell
import com.kinderhub.ui.components.IconChevronRight
import com.kinderhub.ui.components.IconGlobe
import com.kinderhub.ui.components.IconHelpCircle
import com.kinderhub.ui.components.IconPalette
import com.kinderhub.ui.components.IconShield
import com.kinderhub.ui.theme.AppTheme
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.ThemeManager

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes

    val currentTheme by ThemeManager.currentTheme.collectAsState()
    val themeInfo = ThemeManager.getThemeInfo(currentTheme)

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
                text = "Settings",
                style = typography.h2,
                color = colors.tx
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = dimensions.spacingMd)
        ) {
            // Appearance section
            item {
                SectionHeader(title = "Appearance")
            }

            item {
                SettingsItem(
                    icon = { color, size -> IconPalette(color = color, size = size) },
                    title = "Theme",
                    subtitle = "${themeInfo.emoji} ${themeInfo.displayName}",
                    onClick = onThemeClick,
                    showPreview = true,
                    previewColors = listOf(
                        themeInfo.config.colors.p6,
                        themeInfo.config.colors.ac
                    )
                )
            }

            item {
                SettingsItem(
                    icon = { color, size -> IconGlobe(color = color, size = size) },
                    title = "Language",
                    subtitle = "English",
                    onClick = onLanguageClick
                )
            }

            // Notifications section
            item {
                Spacer(modifier = Modifier.height(dimensions.spacingLg))
                SectionHeader(title = "Notifications")
            }

            item {
                SettingsItem(
                    icon = { color, size -> IconBell(color = color, size = size) },
                    title = "Push Notifications",
                    subtitle = "Manage notification preferences",
                    onClick = onNotificationsClick
                )
            }

            // Privacy section
            item {
                Spacer(modifier = Modifier.height(dimensions.spacingLg))
                SectionHeader(title = "Privacy & Security")
            }

            item {
                SettingsItem(
                    icon = { color, size -> IconShield(color = color, size = size) },
                    title = "Privacy Settings",
                    subtitle = "Control your data and privacy",
                    onClick = onPrivacyClick
                )
            }

            // Help section
            item {
                Spacer(modifier = Modifier.height(dimensions.spacingLg))
                SectionHeader(title = "Support")
            }

            item {
                SettingsItem(
                    icon = { color, size -> IconHelpCircle(color = color, size = size) },
                    title = "Help & FAQ",
                    subtitle = "Get help and find answers",
                    onClick = onHelpClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(dimensions.spacingXxl))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions

    Text(
        text = title.uppercase(),
        style = typography.label,
        color = colors.tx3,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(
            horizontal = dimensions.screenPadding,
            vertical = dimensions.spacingSm
        )
    )
}

@Composable
private fun SettingsItem(
    icon: @Composable (color: Color, size: Dp) -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showPreview: Boolean = false,
    previewColors: List<Color> = emptyList()
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .clickable { onClick() }
            .padding(
                horizontal = dimensions.screenPadding,
                vertical = dimensions.spacingMd
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(dimensions.touchTarget)
                .clip(shapes.buttonShape)
                .background(colors.p50),
            contentAlignment = Alignment.Center
        ) {
            icon(colors.p6, dimensions.iconSizeMd)
        }

        Spacer(modifier = Modifier.width(dimensions.spacingMd))

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = typography.body,
                color = colors.tx,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(dimensions.spacingXs))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Color preview dots
                if (showPreview && previewColors.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-4).dp)
                    ) {
                        previewColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(dimensions.iconSizeSm)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(dimensions.spacingSm))
                }
                Text(
                    text = subtitle,
                    style = typography.small,
                    color = colors.tx3
                )
            }
        }

        // Chevron
        IconChevronRight(color = colors.tx3, size = dimensions.iconSizeMd)
    }
}
