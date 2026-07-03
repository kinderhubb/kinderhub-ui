package com.kinderhub.ui.screens.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.AppShell
import com.kinderhub.ui.components.IconCalendar
import com.kinderhub.ui.components.IconChevronRight
import com.kinderhub.ui.components.IconClock
import com.kinderhub.ui.components.IconCompass
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.components.LayoutMode
import com.kinderhub.ui.data.model.Booking
import com.kinderhub.ui.data.model.BookingStatus
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.AppStrings
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyBookingsScreen(
    onBookingClick: (String) -> Unit,
    onExplore: () -> Unit,
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    viewModel: MyBookingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val strings = Strings.current

    LaunchedEffect(Unit) {
        viewModel.loadBookings()
    }

    AppShell(
        selectedTab = 1,
        onDiscoverClick = onNavigateToDiscover,
        onBookingsClick = { /* Already here */ },
        onMessagesClick = onNavigateToMessages,
        onAccountClick = onNavigateToAccount
    ) { layoutMode ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(c.bg)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                BookingsHeader(title = strings.navMyBookings)

                // Tab bar
                TabBar(
                    selectedTab = uiState.selectedTab,
                    upcomingCount = uiState.upcomingBookings.size,
                    pastCount = uiState.pastBookings.size,
                    upcomingLabel = strings.bookingsUpcoming,
                    pastLabel = strings.bookingsPast,
                    onTabSelect = viewModel::selectTab
                )

                when (uiState.screenState) {
                    BookingsScreenState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = c.p6)
                        }
                    }
                    BookingsScreenState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(strings.commonError, color = c.tx2)
                        }
                    }
                    BookingsScreenState.Empty -> {
                        EmptyBookingsState(
                            noBookingsText = strings.bookingsNoUpcoming,
                            onExplore = onExplore
                        )
                    }
                    BookingsScreenState.Success -> {
                        BookingsList(
                            bookings = viewModel.getCurrentBookings(),
                            noBookingsText = if (uiState.selectedTab == BookingsTab.Upcoming)
                                strings.bookingsNoUpcoming else strings.bookingsNoPast,
                            onBookingClick = onBookingClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingsHeader(title: String) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(horizontal = Space.screenPadding)
            .padding(top = Space.s48, bottom = Space.s16)
    ) {
        Text(
            text = title,
            style = typography.h1,
            color = c.tx,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TabBar(
    selectedTab: BookingsTab,
    upcomingCount: Int,
    pastCount: Int,
    upcomingLabel: String,
    pastLabel: String,
    onTabSelect: (BookingsTab) -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(horizontal = Space.screenPadding)
            .padding(bottom = Space.s12),
        horizontalArrangement = Arrangement.spacedBy(Space.s8)
    ) {
        TabChip(
            label = upcomingLabel,
            count = upcomingCount,
            isSelected = selectedTab == BookingsTab.Upcoming,
            onClick = { onTabSelect(BookingsTab.Upcoming) },
            modifier = Modifier.weight(1f)
        )

        TabChip(
            label = pastLabel,
            count = pastCount,
            isSelected = selectedTab == BookingsTab.Past,
            onClick = { onTabSelect(BookingsTab.Past) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabChip(
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val bgColor = if (isSelected) c.p50 else c.bg
    val borderColor = if (isSelected) c.pb else c.bd
    val textColor = if (isSelected) c.p7 else c.tx2

    Box(
        modifier = modifier
            .clip(Shapes.pill)
            .background(bgColor)
            .border(1.dp, borderColor, Shapes.pill)
            .clickable { onClick() }
            .padding(vertical = Space.s12),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = typography.body,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
            if (count > 0) {
                Spacer(modifier = Modifier.width(Space.s8))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) c.p6 else c.tx3),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        style = typography.xs,
                        color = c.surface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingsList(
    bookings: List<Booking>,
    noBookingsText: String,
    onBookingClick: (String) -> Unit
) {
    if (bookings.isEmpty()) {
        EmptyTabState(message = noBookingsText)
    } else {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = Space.screenPadding,
                vertical = Space.s16
            ),
            verticalArrangement = Arrangement.spacedBy(Space.s12)
        ) {
            items(bookings) { booking ->
                BookingCard(
                    booking = booking,
                    onClick = { onBookingClick(booking.id) }
                )
            }
        }
    }
}

@Composable
private fun BookingCard(
    booking: Booking,
    onClick: () -> Unit
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.xl)
            .background(c.surface)
            .clickable { onClick() }
            .padding(Space.s16),
        verticalAlignment = Alignment.Top
    ) {
        // Activity image placeholder
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(Shapes.lg)
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = booking.activityTitle.first().toString(),
                style = typography.h2,
                color = c.p6,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(Space.s14))

        Column(modifier = Modifier.weight(1f)) {
            // Status badge
            Box(
                modifier = Modifier
                    .clip(Shapes.pill)
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = Space.s10, vertical = 4.dp)
            ) {
                Text(
                    text = statusText,
                    style = typography.xs,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(Space.s8))

            Text(
                text = booking.activityTitle,
                style = typography.body,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(Space.s4))

            Text(
                text = booking.providerName,
                style = typography.small,
                color = c.tx2
            )

            Spacer(modifier = Modifier.height(Space.s10))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconCalendar(color = c.tx3, size = 14.dp)
                Spacer(modifier = Modifier.width(Space.s6))
                Text(
                    text = booking.sessionDate,
                    style = typography.small,
                    color = c.tx2
                )
            }

            Spacer(modifier = Modifier.height(Space.s4))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconClock(color = c.tx3, size = 14.dp)
                Spacer(modifier = Modifier.width(Space.s6))
                Text(
                    text = "${booking.sessionTime} · ${booking.childName}",
                    style = typography.small,
                    color = c.tx2
                )
            }
        }

        IconChevronRight(color = c.tx3, size = 20.dp)
    }
}

@Composable
private fun EmptyTabState(message: String) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Space.screenPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = typography.body,
            color = c.tx3,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyBookingsState(
    noBookingsText: String,
    onExplore: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val strings = Strings.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Space.screenPadding)
            .padding(top = Space.s40),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            IconCalendar(color = c.p6, size = 44.dp)
        }

        Spacer(modifier = Modifier.height(Space.s20))

        Text(
            text = noBookingsText,
            style = typography.h2,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = "Explore activities and book your first session",
            style = typography.body,
            color = c.tx2,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Space.s24))

        KhButton(
            text = strings.navDiscover,
            onClick = onExplore,
            icon = { IconCompass(color = c.surface, size = 18.dp) }
        )
    }
}
