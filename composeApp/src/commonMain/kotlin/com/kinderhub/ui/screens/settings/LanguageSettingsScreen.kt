package com.kinderhub.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.components.IconCheck
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.AppLanguage
import com.kinderhub.ui.util.LocalLocalization
import com.kinderhub.ui.util.Strings

@Composable
fun LanguageSettingsScreen(
    onBack: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val strings = Strings.current
    val localization = LocalLocalization.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(horizontal = Space.s8, vertical = Space.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                IconArrowLeft(color = c.tx, size = 24.dp)
            }

            Spacer(modifier = Modifier.width(Space.s8))

            Text(
                text = "Language", // This one stays as-is since it's the language selector
                style = typography.h2,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(Space.s16))

        // Language options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
        ) {
            AppLanguage.entries.forEachIndexed { index, language ->
                LanguageRow(
                    language = language,
                    isSelected = localization.currentLanguage == language,
                    onClick = { localization.setLanguage(language) }
                )

                if (index < AppLanguage.entries.lastIndex) {
                    HorizontalDivider(
                        color = c.bd,
                        thickness = 1.dp,
                        modifier = Modifier.padding(start = Space.screenPadding)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Space.s16))

        // Info text
        Text(
            text = "The app language will change immediately when you select a new language.",
            style = typography.small,
            color = c.tx3,
            modifier = Modifier.padding(horizontal = Space.screenPadding)
        )
    }
}

@Composable
private fun LanguageRow(
    language: AppLanguage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Space.screenPadding, vertical = Space.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = language.nativeName,
                style = typography.body,
                color = c.tx,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
            Text(
                text = language.displayName,
                style = typography.small,
                color = c.tx2
            )
        }

        if (isSelected) {
            IconCheck(color = c.p6, size = 20.dp)
        }
    }
}
