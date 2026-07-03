package com.kinderhub.ui.screens.bookings

import androidx.compose.foundation.background
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
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.util.formatPrice
import com.kinderhub.ui.components.IconCalendar
import com.kinderhub.ui.components.IconClock
import com.kinderhub.ui.components.IconMapPin
import com.kinderhub.ui.components.IconMessageCircle
import com.kinderhub.ui.components.IconNavigation
import com.kinderhub.ui.components.IconShare
import com.kinderhub.ui.components.IconUsers
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.data.model.Booking
import com.kinderhub.ui.data.model.BookingStatus
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingDetailScreen(
    bookingId: String,
    onMessageProvider: (String) -> Unit,
    onCancel: () -> Unit,
    onBack: () -> Unit,
    viewModel: BookingDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors

    LaunchedEffect(bookingId) {
        viewModel.loadBooking(bookingId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        // Header
        DetailHeader(onBack = onBack)

        when (uiState.screenState) {
            BookingDetailScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = c.p6)
                }
            }
            BookingDetailScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Couldn't load booking", color = c.tx2)
                }
            }
            BookingDetailScreenState.Success -> {
                uiState.booking?.let { booking ->
                    DetailContent(
                        booking = booking,
                        onMessageProvider = { onMessageProvider(booking.providerName) },
                        onGetDirections = viewModel::getDirections,
                        onAddToCalendar = viewModel::addToCalendar,
                        onShare = viewModel::shareBooking,
                        onCancel = {
                            viewModel.cancelBooking()
                            onCancel()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(onBack: () -> Unit) {
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
            text = "Booking Details",
            style = typography.h2,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DetailContent(
    booking: Booking,
    onMessageProvider: () -> Unit,
    onGetDirections: () -> Unit,
    onAddToCalendar: () -> Unit,
    onShare: () -> Unit,
    onCancel: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val statusColor = when (booking.status) {
        BookingStatus.Confirmed -> c.succ
        BookingStatus.Pending -> c.sun
        BookingStatus.Cancelled -> c.error
        BookingStatus.Completed -> c.tx3
    }

    val statusText = when (booking.status) {
        BookingStatus.Confirmed -> "Confirmed"
        BookingStatus.Pending -> "Pending"
        BookingStatus.Cancelled -> "Cancelled"
        BookingStatus.Completed -> "Completed"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Activity info card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            // Status
            Box(
                modifier = Modifier
                    .clip(Shapes.pill)
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = Space.s12, vertical = 6.dp)
            ) {
                Text(
                    text = statusText,
                    style = typography.small,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(Space.s16))

            Text(
                text = booking.activityTitle,
                style = typography.h2,
                color = c.tx,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Space.s6))

            Text(
                text = booking.providerName,
                style = typography.body,
                color = c.tx2
            )
        }

        Spacer(modifier = Modifier.height(Space.s16))

        // Session details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            Text(
                text = "Session Details",
                style = typography.h3,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(Space.s16))

            DetailRow(
                icon = { IconCalendar(color = c.p6, size = 20.dp) },
                label = "Date",
                value = booking.sessionDate
            )

            Spacer(modifier = Modifier.height(Space.s14))

            DetailRow(
                icon = { IconClock(color = c.p6, size = 20.dp) },
                label = "Time",
                value = "${booking.sessionTime} · ${booking.sessionDuration}"
            )

            Spacer(modifier = Modifier.height(Space.s14))

            DetailRow(
                icon = { IconUsers(color = c.p6, size = 20.dp) },
                label = "Child",
                value = booking.childName
            )

            if (booking.locationAddress.isNotBlank()) {
                Spacer(modifier = Modifier.height(Space.s14))

                DetailRow(
                    icon = { IconMapPin(color = c.p6, size = 20.dp) },
                    label = "Location",
                    value = booking.locationAddress
                )
            }
        }

        Spacer(modifier = Modifier.height(Space.s16))

        // Quick actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            Text(
                text = "Quick Actions",
                style = typography.h3,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(Space.s16))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.s12)
            ) {
                QuickActionItem(
                    icon = { IconNavigation(color = c.p6, size = 20.dp) },
                    label = "Directions",
                    onClick = onGetDirections,
                    modifier = Modifier.weight(1f)
                )

                QuickActionItem(
                    icon = { IconCalendar(color = c.p6, size = 20.dp) },
                    label = "Calendar",
                    onClick = onAddToCalendar,
                    modifier = Modifier.weight(1f)
                )

                QuickActionItem(
                    icon = { IconShare(color = c.p6, size = 20.dp) },
                    label = "Share",
                    onClick = onShare,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(Space.s16))

        // Payment info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            Text(
                text = "Payment",
                style = typography.h3,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(Space.s16))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Session fee",
                    style = typography.body,
                    color = c.tx2
                )
                Text(
                    text = "${booking.currency}${formatPrice(booking.price)}",
                    style = typography.body,
                    color = c.tx,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(Space.s24))

        // Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            KhButton(
                text = "Message Provider",
                onClick = onMessageProvider,
                modifier = Modifier.fillMaxWidth(),
                icon = { IconMessageCircle(color = Color.White, size = 18.dp) }
            )

            if (booking.status == BookingStatus.Confirmed || booking.status == BookingStatus.Pending) {
                Spacer(modifier = Modifier.height(Space.s12))

                KhButton(
                    text = "Cancel Booking",
                    onClick = onCancel,
                    variant = KhButtonVariant.Destructive,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(Space.s24))
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
private fun QuickActionItem(
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
            .background(c.bg)
            .clickable { onClick() }
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

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = label,
            style = typography.xs,
            color = c.tx2,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
