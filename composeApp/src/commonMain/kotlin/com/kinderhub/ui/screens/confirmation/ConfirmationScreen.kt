package com.kinderhub.ui.screens.confirmation

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconCalendar
import com.kinderhub.ui.components.IconCheck
import com.kinderhub.ui.components.IconClock
import com.kinderhub.ui.components.IconMapPin
import com.kinderhub.ui.components.IconNavigation
import com.kinderhub.ui.components.IconShare
import com.kinderhub.ui.components.IconUsers
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.data.model.Booking
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.AppStrings
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConfirmationScreen(
    bookingId: String,
    onViewBookings: () -> Unit,
    onBackToDiscover: () -> Unit,
    viewModel: ConfirmationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val strings = Strings.current

    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        when (uiState.screenState) {
            ConfirmationScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = c.p6)
                }
            }
            ConfirmationScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(strings.commonError, color = c.tx2)
                        Spacer(modifier = Modifier.height(Space.s16))
                        KhButton(
                            text = strings.navDiscover,
                            onClick = onBackToDiscover,
                            variant = KhButtonVariant.Secondary
                        )
                    }
                }
            }
            ConfirmationScreenState.Success -> {
                uiState.booking?.let { booking ->
                    ConfirmationContent(
                        booking = booking,
                        confirmationNumber = uiState.confirmationNumber,
                        strings = strings,
                        onAddToCalendar = viewModel::addToCalendar,
                        onGetDirections = viewModel::getDirections,
                        onShare = viewModel::shareBooking,
                        onViewBookings = onViewBookings,
                        onBackToDiscover = onBackToDiscover
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmationContent(
    booking: Booking,
    confirmationNumber: String?,
    strings: AppStrings,
    onAddToCalendar: () -> Unit,
    onGetDirections: () -> Unit,
    onShare: () -> Unit,
    onViewBookings: () -> Unit,
    onBackToDiscover: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(Space.s48))

        // Success header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Space.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success checkmark
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(c.succ.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(c.succ),
                    contentAlignment = Alignment.Center
                ) {
                    IconCheck(color = Color.White, size = 32.dp)
                }
            }

            Spacer(modifier = Modifier.height(Space.s20))

            Text(
                text = strings.confirmationBookingConfirmed,
                style = typography.h1,
                color = c.tx,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Space.s8))

            Text(
                text = "Your spot is reserved. See you there!",
                style = typography.body,
                color = c.tx2,
                textAlign = TextAlign.Center
            )

            if (confirmationNumber != null) {
                Spacer(modifier = Modifier.height(Space.s12))
                Text(
                    text = strings.confirmationNumber(confirmationNumber),
                    style = typography.small,
                    color = c.tx3,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(Space.s24))

        // Booking details card
        BookingDetailsCard(booking = booking)

        Spacer(modifier = Modifier.height(Space.s20))

        // Quick actions
        QuickActionsRow(
            onAddToCalendar = onAddToCalendar,
            onGetDirections = onGetDirections,
            onShare = onShare
        )

        Spacer(modifier = Modifier.height(Space.s24))

        // What's next section
        WhatsNextSection()

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(Space.s24))

        // CTAs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            KhButton(
                text = strings.confirmationViewBookings,
                onClick = onViewBookings,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Space.s12))

            KhButton(
                text = strings.confirmationBackToDiscover,
                onClick = onBackToDiscover,
                variant = KhButtonVariant.Outline,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(Space.s24))
    }
}

@Composable
private fun BookingDetailsCard(booking: Booking) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screenPadding)
            .clip(Shapes.xl)
            .background(c.surface)
            .padding(Space.s20)
    ) {
        Text(
            text = booking.activityTitle,
            style = typography.h3,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s4))

        Text(
            text = booking.providerName,
            style = typography.small,
            color = c.tx2
        )

        Spacer(modifier = Modifier.height(Space.s16))
        HorizontalDivider(color = c.bd, thickness = 1.dp)
        Spacer(modifier = Modifier.height(Space.s16))

        // Details grid
        DetailRow(
            icon = { IconCalendar(color = c.p6, size = 18.dp) },
            label = "Date",
            value = booking.sessionDate
        )

        Spacer(modifier = Modifier.height(Space.s14))

        DetailRow(
            icon = { IconClock(color = c.p6, size = 18.dp) },
            label = "Time",
            value = "${booking.sessionTime} · ${booking.sessionDuration}"
        )

        Spacer(modifier = Modifier.height(Space.s14))

        DetailRow(
            icon = { IconUsers(color = c.p6, size = 18.dp) },
            label = "Child",
            value = booking.childName
        )

        if (booking.locationAddress.isNotBlank()) {
            Spacer(modifier = Modifier.height(Space.s14))

            DetailRow(
                icon = { IconMapPin(color = c.p6, size = 18.dp) },
                label = "Location",
                value = booking.locationAddress
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        icon()
        Spacer(modifier = Modifier.width(Space.s12))
        Column {
            Text(
                text = label,
                style = typography.xs,
                color = c.tx3,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = typography.body,
                color = c.tx
            )
        }
    }
}

@Composable
private fun QuickActionsRow(
    onAddToCalendar: () -> Unit,
    onGetDirections: () -> Unit,
    onShare: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screenPadding),
        horizontalArrangement = Arrangement.spacedBy(Space.s12)
    ) {
        QuickActionButton(
            icon = { IconCalendar(color = c.p6, size = 20.dp) },
            label = "Add to Calendar",
            onClick = onAddToCalendar,
            modifier = Modifier.weight(1f)
        )

        QuickActionButton(
            icon = { IconNavigation(color = c.p6, size = 20.dp) },
            label = "Directions",
            onClick = onGetDirections,
            modifier = Modifier.weight(1f)
        )

        QuickActionButton(
            icon = { IconShare(color = c.p6, size = 20.dp) },
            label = "Share",
            onClick = onShare,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = modifier
            .clip(Shapes.lg)
            .background(c.surface)
            .padding(Space.s14),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.height(Space.s10))

        Text(
            text = label,
            style = typography.xs,
            color = c.tx2,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun WhatsNextSection() {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screenPadding)
    ) {
        Text(
            text = "What's Next",
            style = typography.h3,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s16))

        val steps = listOf(
            "You'll receive a confirmation email with all details",
            "Arrive 10 minutes early for check-in",
            "Bring any required equipment mentioned in the activity"
        )

        steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(c.p50),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        style = typography.small,
                        color = c.p7,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(Space.s12))

                Text(
                    text = step,
                    style = typography.body,
                    color = c.tx2,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            if (index < steps.lastIndex) {
                Spacer(modifier = Modifier.height(Space.s14))
            }
        }
    }
}
