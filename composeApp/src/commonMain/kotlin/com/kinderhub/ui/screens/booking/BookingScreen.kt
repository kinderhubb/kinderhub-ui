package com.kinderhub.ui.screens.booking

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.util.formatPrice
import com.kinderhub.ui.components.IconBadgeCheck
import com.kinderhub.ui.components.IconCalendar
import com.kinderhub.ui.components.IconClock
import com.kinderhub.ui.components.IconUsers
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.data.model.ActivitySession
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingScreen(
    activityId: String,
    sessionId: String?,
    childId: String?,
    onProceedToCheckout: (activityId: String, sessionId: String, childId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: BookingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val typography = KhTheme.typography

    LaunchedEffect(activityId) {
        viewModel.loadBookingData(activityId, sessionId, childId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        // Header
        BookingHeader(
            title = "Book Session",
            onBack = onBack
        )

        when (uiState.screenState) {
            BookingScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...", color = c.tx2)
                }
            }
            BookingScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Something went wrong", color = c.tx2)
                }
            }
            BookingScreenState.Ready -> {
                BookingContent(
                    uiState = uiState,
                    onSessionSelect = viewModel::selectSession,
                    onChildSelect = viewModel::selectChild,
                    onProceed = {
                        viewModel.getBookingParams()?.let { (actId, sessId, chId) ->
                            onProceedToCheckout(actId, sessId, chId)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BookingHeader(
    title: String,
    onBack: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

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
                .background(c.bg)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            IconArrowLeft(color = c.tx, size = 20.dp)
        }

        Spacer(modifier = Modifier.width(Space.s16))

        Text(
            text = title,
            style = typography.h2,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BookingContent(
    uiState: BookingUiState,
    onSessionSelect: (ActivitySession) -> Unit,
    onChildSelect: (Child) -> Unit,
    onProceed: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val activity = uiState.activity ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Activity summary card
        ActivitySummaryCard(
            title = activity.title,
            provider = activity.providerName,
            isVerified = activity.isVerified
        )

        Spacer(modifier = Modifier.height(Space.s20))

        // Session selection
        SectionHeader(
            icon = { IconCalendar(color = c.p6, size = 20.dp) },
            title = "Select Session"
        )

        Spacer(modifier = Modifier.height(Space.s12))

        Column(
            modifier = Modifier.padding(horizontal = Space.screenPadding),
            verticalArrangement = Arrangement.spacedBy(Space.s10)
        ) {
            uiState.availableSessions.forEach { session ->
                SessionCard(
                    session = session,
                    isSelected = session.id == uiState.selectedSession?.id,
                    onClick = { onSessionSelect(session) }
                )
            }
        }

        Spacer(modifier = Modifier.height(Space.s24))

        // Child selection
        SectionHeader(
            icon = { IconUsers(color = c.p6, size = 20.dp) },
            title = "Select Child"
        )

        Spacer(modifier = Modifier.height(Space.s12))

        Column(
            modifier = Modifier.padding(horizontal = Space.screenPadding),
            verticalArrangement = Arrangement.spacedBy(Space.s10)
        ) {
            uiState.eligibleChildren.forEach { child ->
                ChildCard(
                    child = child,
                    isSelected = child.id == uiState.selectedChild?.id,
                    onClick = { onChildSelect(child) }
                )
            }

            if (uiState.eligibleChildren.isEmpty()) {
                Text(
                    text = "No children match the age requirements (${activity.ageMin}-${activity.ageMax} years)",
                    style = typography.small,
                    color = c.tx3,
                    modifier = Modifier.padding(vertical = Space.s12)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(Space.s24))

        // CTA Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            KhButton(
                text = "Continue to Checkout",
                onClick = onProceed,
                enabled = uiState.canProceed,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ActivitySummaryCard(
    title: String,
    provider: String,
    isVerified: Boolean
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(Space.screenPadding)
    ) {
        Text(
            text = title,
            style = typography.h3,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s8))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = provider,
                style = typography.body,
                color = c.tx2
            )
            if (isVerified) {
                Spacer(modifier = Modifier.width(Space.s6))
                IconBadgeCheck(color = c.p6, size = 16.dp)
            }
        }
    }
}

@Composable
private fun SectionHeader(
    icon: @Composable () -> Unit,
    title: String
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier.padding(horizontal = Space.screenPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(Space.s10))
        Text(
            text = title,
            style = typography.h3,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SessionCard(
    session: ActivitySession,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val bgColor = if (isSelected) c.p50 else c.surface
    val borderColor = if (isSelected) c.pb else c.bd

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.lg)
            .background(bgColor)
            .border(1.5.dp, borderColor, Shapes.lg)
            .clickable { onClick() }
            .padding(Space.s16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = session.dayOfWeek,
                style = typography.body,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Space.s4))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconClock(color = c.tx3, size = 14.dp)
                Spacer(modifier = Modifier.width(Space.s6))
                Text(
                    text = "${session.time} · ${session.duration}",
                    style = typography.small,
                    color = c.tx2
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "£${formatPrice(session.pricePerSession)}",
                style = typography.body,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Space.s4))
            val spotsText = if (session.spotsLeft <= 3) {
                "${session.spotsLeft} spots left"
            } else {
                "${session.spotsLeft} available"
            }
            val spotsColor = if (session.spotsLeft <= 3) c.ac else c.tx3
            Text(
                text = spotsText,
                style = typography.xs,
                color = spotsColor,
                fontWeight = if (session.spotsLeft <= 3) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun ChildCard(
    child: Child,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val bgColor = if (isSelected) c.p50 else c.surface
    val borderColor = if (isSelected) c.pb else c.bd

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
            .background(bgColor)
            .border(1.5.dp, borderColor, Shapes.lg)
            .clickable { onClick() }
            .padding(Space.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(avatarColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = child.firstName.first().toString(),
                style = typography.h3,
                color = avatarColor,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(Space.s14))

        Column {
            Text(
                text = child.firstName,
                style = typography.body,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${child.age} years old",
                style = typography.small,
                color = c.tx2
            )
        }
    }
}
