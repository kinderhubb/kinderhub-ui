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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconPlus
import com.kinderhub.ui.components.IconX
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.components.KhTextField
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddChildScreen(
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    viewModel: AddChildViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val strings = Strings.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Space.screenPadding)
                .padding(top = Space.s48, bottom = 100.dp)
        ) {
            // Progress indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.onboardingStep(uiState.currentStep, uiState.totalSteps),
                    style = typography.small,
                    color = c.tx3,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = strings.onboardingSkip,
                    style = typography.small,
                    color = c.p6,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onSkip() }
                )
            }

            // Progress bar
            Spacer(modifier = Modifier.height(Space.s12))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.s8)
            ) {
                repeat(uiState.totalSteps) { step ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(Shapes.pill)
                            .background(
                                if (step < uiState.currentStep) c.p6 else c.p100
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(Space.s32))

            // Title
            Text(
                text = strings.onboardingAddChildren,
                style = typography.h1,
                color = c.tx,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Space.s8))

            Text(
                text = strings.onboardingAddChildrenSubtitle,
                style = typography.body,
                color = c.tx2
            )

            Spacer(modifier = Modifier.height(Space.s24))

            // Existing children
            uiState.existingChildren.forEach { child ->
                ExistingChildRow(
                    child = child,
                    onEdit = { viewModel.editExistingChild(child.id) }
                )
                Spacer(modifier = Modifier.height(Space.s12))
            }

            // Child forms
            uiState.childForms.forEachIndexed { index, form ->
                if (index > 0) {
                    Spacer(modifier = Modifier.height(Space.s20))
                }

                ChildFormCard(
                    form = form,
                    showRemove = uiState.childForms.size > 1,
                    firstNameLabel = strings.onboardingFirstName,
                    firstNamePlaceholder = strings.onboardingFirstNamePlaceholder,
                    dateOfBirthLabel = strings.onboardingDateOfBirth,
                    onFirstNameChange = { viewModel.updateChildFirstName(form.id, it) },
                    onBirthDayChange = { viewModel.updateChildBirthDay(form.id, it) },
                    onBirthMonthChange = { viewModel.updateChildBirthMonth(form.id, it) },
                    onBirthYearChange = { viewModel.updateChildBirthYear(form.id, it) },
                    onRemove = { viewModel.removeChild(form.id) }
                )
            }

            Spacer(modifier = Modifier.height(Space.s20))

            // Add another child button (dashed)
            AddAnotherChildButton(
                label = strings.onboardingAddAnotherChild,
                onClick = { viewModel.addAnotherChild() }
            )
        }

        // Sticky bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            KhButton(
                text = strings.onboardingContinue,
                onClick = { viewModel.saveChildren(onComplete) },
                loading = uiState.isSaving,
                loadingText = strings.commonLoading
            )
        }
    }
}

@Composable
private fun ExistingChildRow(
    child: Child,
    onEdit: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val avatarColor = when (child.avatarColor) {
        AvatarColor.Accent -> c.ac
        AvatarColor.Sunshine -> c.sun
        AvatarColor.Primary -> c.p6
        AvatarColor.Success -> c.succ
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.lg)
            .background(c.surface)
            .border(1.dp, c.bd, Shapes.lg)
            .clickable { onEdit() }
            .padding(Space.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(Shapes.pill)
                .background(avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = child.firstName.first().uppercase(),
                style = typography.h3,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(Space.s12))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = child.firstName,
                style = typography.bodyLg,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Age ${child.age} · born ${formatDateOfBirth(child.dateOfBirth)}",
                style = typography.small,
                color = c.tx3b
            )
        }

        Text(
            text = "Edit",
            style = typography.small,
            color = c.p6,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ChildFormCard(
    form: ChildFormData,
    showRemove: Boolean,
    firstNameLabel: String,
    firstNamePlaceholder: String,
    dateOfBirthLabel: String,
    onFirstNameChange: (String) -> Unit,
    onBirthDayChange: (String) -> Unit,
    onBirthMonthChange: (String) -> Unit,
    onBirthYearChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.lg)
            .background(c.surface)
            .padding(Space.s16)
    ) {
        if (showRemove) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(Shapes.pill)
                        .background(c.p50)
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    IconX(color = c.tx3, size = 16.dp)
                }
            }
            Spacer(modifier = Modifier.height(Space.s8))
        }

        KhTextField(
            value = form.firstName,
            onValueChange = onFirstNameChange,
            label = firstNameLabel,
            placeholder = firstNamePlaceholder,
            isError = form.firstNameError != null,
            errorMessage = form.firstNameError
        )

        Spacer(modifier = Modifier.height(Space.s16))

        Text(
            text = dateOfBirthLabel,
            style = typography.label,
            color = c.tx2,
            modifier = Modifier.padding(bottom = Space.s8)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.s12)
        ) {
            DateFieldBox(
                value = form.birthDay,
                onValueChange = onBirthDayChange,
                placeholder = "DD",
                modifier = Modifier.weight(1f)
            )
            DateFieldBox(
                value = form.birthMonth,
                onValueChange = onBirthMonthChange,
                placeholder = "MM",
                modifier = Modifier.weight(1f)
            )
            DateFieldBox(
                value = form.birthYear,
                onValueChange = onBirthYearChange,
                placeholder = "YYYY",
                modifier = Modifier.weight(1.5f)
            )
        }

        if (form.dateError != null) {
            Spacer(modifier = Modifier.height(Space.s8))
            Text(
                text = "⚠ ${form.dateError}",
                style = typography.small,
                color = c.error
            )
        }
    }
}

@Composable
private fun DateFieldBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(Shapes.md)
            .background(c.bg)
            .border(1.dp, c.bd, Shapes.md)
            .padding(horizontal = Space.s12),
        contentAlignment = Alignment.Center
    ) {
        KhTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AddAnotherChildButton(
    label: String,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(Shapes.lg)
            .border(
                width = 2.dp,
                color = c.bd,
                shape = Shapes.lg
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconPlus(color = c.p6, size = 20.dp)
            Spacer(modifier = Modifier.width(Space.s8))
            Text(
                text = label,
                style = typography.button,
                color = c.p6
            )
        }
    }
}

private fun formatDateOfBirth(dateOfBirth: String): String {
    // dateOfBirth format: "2019-03-15"
    val parts = dateOfBirth.split("-")
    if (parts.size != 3) return dateOfBirth

    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    val month = parts[1].toIntOrNull()?.let { months.getOrNull(it - 1) } ?: parts[1]
    return "$month ${parts[0]}"
}
