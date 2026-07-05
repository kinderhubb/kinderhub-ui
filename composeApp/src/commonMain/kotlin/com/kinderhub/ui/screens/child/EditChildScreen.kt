package com.kinderhub.ui.screens.child

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.components.IconCheck
import com.kinderhub.ui.components.IconTrash
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.components.KhTextField
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space

@Composable
fun EditChildScreen(
    child: Child?,
    onSave: (Child) -> Unit,
    onDelete: (String) -> Unit,
    onBack: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    // Form state
    var firstName by remember(child) { mutableStateOf(child?.firstName ?: "") }
    var day by remember(child) { mutableStateOf(child?.dateOfBirth?.split("-")?.getOrNull(2) ?: "") }
    var month by remember(child) { mutableStateOf(child?.dateOfBirth?.split("-")?.getOrNull(1) ?: "") }
    var year by remember(child) { mutableStateOf(child?.dateOfBirth?.split("-")?.getOrNull(0) ?: "") }
    var avatarColor by remember(child) { mutableStateOf(child?.avatarColor ?: AvatarColor.Accent) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.surface)
                    .padding(horizontal = Space.screenPadding)
                    .padding(top = Space.s48, bottom = Space.s16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    IconArrowLeft(color = c.tx, size = 24.dp)
                }

                Spacer(modifier = Modifier.width(Space.s12))

                Text(
                    text = if (child != null) "Edit Child" else "Add Child",
                    style = typography.h2,
                    color = c.tx,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(Space.s24))

            // Avatar preview
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space.screenPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val avatarBgColor = when (avatarColor) {
                    AvatarColor.Accent -> c.ac
                    AvatarColor.Sunshine -> c.sun
                    AvatarColor.Primary -> c.p6
                    AvatarColor.Success -> c.succ
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(avatarBgColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = firstName.firstOrNull()?.uppercase() ?: "?",
                        style = typography.h1,
                        color = avatarBgColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(Space.s8))

                Text(
                    text = if (firstName.isNotBlank()) firstName else "Child's name",
                    style = typography.body,
                    color = if (firstName.isNotBlank()) c.tx else c.tx3
                )
            }

            Spacer(modifier = Modifier.height(Space.s24))

            // Form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.surface)
                    .padding(Space.screenPadding)
            ) {
                // Name field
                Text(
                    text = "First name",
                    style = typography.small,
                    color = c.tx2,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(Space.s8))
                KhTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "e.g. Ella"
                )

                Spacer(modifier = Modifier.height(Space.s20))

                // Date of birth
                Text(
                    text = "Date of birth",
                    style = typography.small,
                    color = c.tx2,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(Space.s8))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Space.s12)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        KhTextField(
                            value = day,
                            onValueChange = { if (it.length <= 2) day = it.filter { c -> c.isDigit() } },
                            placeholder = "DD"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        KhTextField(
                            value = month,
                            onValueChange = { if (it.length <= 2) month = it.filter { c -> c.isDigit() } },
                            placeholder = "MM"
                        )
                    }
                    Box(modifier = Modifier.weight(1.5f)) {
                        KhTextField(
                            value = year,
                            onValueChange = { if (it.length <= 4) year = it.filter { c -> c.isDigit() } },
                            placeholder = "YYYY"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Space.s20))

                // Avatar color
                Text(
                    text = "Avatar color",
                    style = typography.small,
                    color = c.tx2,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(Space.s12))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Space.s12)
                ) {
                    AvatarColor.entries.forEach { color ->
                        val bgColor = when (color) {
                            AvatarColor.Accent -> c.ac
                            AvatarColor.Sunshine -> c.sun
                            AvatarColor.Primary -> c.p6
                            AvatarColor.Success -> c.succ
                        }
                        val isSelected = avatarColor == color

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(bgColor.copy(alpha = 0.15f))
                                .then(
                                    if (isSelected) Modifier.border(2.dp, bgColor, CircleShape)
                                    else Modifier
                                )
                                .clickable { avatarColor = color },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                IconCheck(color = bgColor, size = 20.dp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Space.s24))

            // Save button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space.screenPadding)
            ) {
                KhButton(
                    text = "Save Changes",
                    onClick = {
                        if (firstName.isNotBlank() && day.isNotBlank() && month.isNotBlank() && year.isNotBlank()) {
                            val dateOfBirth = "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
                            val age = calculateAge(year.toIntOrNull() ?: 2020)
                            val updatedChild = Child(
                                id = child?.id ?: "new-${kotlin.random.Random.nextLong()}",
                                firstName = firstName,
                                dateOfBirth = dateOfBirth,
                                age = age,
                                avatarColor = avatarColor
                            )
                            onSave(updatedChild)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Delete button (only for existing children)
            if (child != null) {
                Spacer(modifier = Modifier.height(Space.s16))

                if (!showDeleteConfirm) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Space.screenPadding)
                    ) {
                        KhButton(
                            text = "Remove Child",
                            onClick = { showDeleteConfirm = true },
                            variant = KhButtonVariant.Destructive,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Space.screenPadding)
                            .clip(Shapes.lg)
                            .background(c.error.copy(alpha = 0.1f))
                            .padding(Space.s16)
                    ) {
                        Text(
                            text = "Are you sure you want to remove ${child.firstName}?",
                            style = typography.body,
                            color = c.error,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(Space.s12))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Space.s12)
                        ) {
                            KhButton(
                                text = "Cancel",
                                onClick = { showDeleteConfirm = false },
                                variant = KhButtonVariant.Secondary,
                                modifier = Modifier.weight(1f)
                            )
                            KhButton(
                                text = "Remove",
                                onClick = { onDelete(child.id) },
                                variant = KhButtonVariant.Destructive,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Space.s40))
        }
    }
}

private fun calculateAge(birthYear: Int): Int {
    val currentYear = 2026 // Simplified - in real app use actual current year
    return currentYear - birthYear
}
