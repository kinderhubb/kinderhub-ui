package com.kinderhub.ui.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinderhub.ui.components.ChildSelector
import com.kinderhub.ui.components.IconApple
import com.kinderhub.ui.components.IconBadgeCheck
import com.kinderhub.ui.components.IconCheck
import com.kinderhub.ui.components.IconChevronDown
import com.kinderhub.ui.components.IconHeart
import com.kinderhub.ui.components.IconMapPin
import com.kinderhub.ui.components.IconMessageCircle
import com.kinderhub.ui.components.IconStar
import com.kinderhub.ui.components.IconX
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.components.LoadingSkeletons
import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.data.model.ActivitySession
import com.kinderhub.ui.util.formatDistance
import com.kinderhub.ui.util.formatRating
import com.kinderhub.ui.data.model.CategoryColorTint
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.PaymentMethodType
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActivityDetailScreen(
    activityId: String,
    onBook: (activityId: String, sessionId: String, childId: String) -> Unit,
    onExpressBook: (activityId: String, sessionId: String, childId: String) -> Unit,
    onAskQuestion: (activityId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: ActivityDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors

    LaunchedEffect(activityId) {
        viewModel.loadActivity(activityId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        when (uiState.screenState) {
            ActivityDetailState.Loading -> {
                LoadingSkeletons()
            }
            ActivityDetailState.Loaded -> {
                uiState.activity?.let { activity ->
                    ActivityDetailContent(
                        activity = activity,
                        uiState = uiState,
                        onSessionSelect = viewModel::selectSession,
                        onChildSelect = viewModel::selectChild,
                        onToggleFavorite = viewModel::toggleFavorite,
                        onShowAllSessions = viewModel::toggleShowAllSessions,
                        onAskQuestion = { onAskQuestion(activity.id) },
                        onBook = {
                            val sessionId = uiState.selectedSessionId ?: return@ActivityDetailContent
                            val childId = uiState.selectedChildId ?: return@ActivityDetailContent
                            onBook(activity.id, sessionId, childId)
                        },
                        onExpressBook = {
                            val sessionId = uiState.selectedSessionId ?: return@ActivityDetailContent
                            val childId = uiState.selectedChildId ?: return@ActivityDetailContent
                            onExpressBook(activity.id, sessionId, childId)
                        },
                        onBack = onBack
                    )
                }
            }
            ActivityDetailState.Error -> {
                val strings = Strings.current
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(strings.commonError, color = c.tx2)
                        Spacer(modifier = Modifier.height(Space.s16))
                        KhButton(
                            text = strings.commonGoBack,
                            onClick = onBack,
                            variant = KhButtonVariant.Secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityDetailContent(
    activity: Activity,
    uiState: ActivityDetailUiState,
    onSessionSelect: (String) -> Unit,
    onChildSelect: (String) -> Unit,
    onToggleFavorite: () -> Unit,
    onShowAllSessions: () -> Unit,
    onAskQuestion: () -> Unit,
    onBook: () -> Unit,
    onExpressBook: () -> Unit,
    onBack: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val placeholderColor = when (activity.category.colorTint) {
        CategoryColorTint.Primary -> c.p100 to c.p50
        CategoryColorTint.Success -> c.succ50 to c.succ50
        CategoryColorTint.Accent -> c.ac50 to c.ac100
        CategoryColorTint.Sunshine -> c.sun50 to c.sun100
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {
            // Hero image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(placeholderColor.first, placeholderColor.second)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = activity.imagePlaceholder,
                        style = typography.small.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        ),
                        color = c.p6.copy(alpha = 0.7f),
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.7f), Shapes.sm)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )

                    // Back button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(Space.s16)
                            .padding(top = Space.s32)
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.9f), Shapes.pill)
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        IconX(color = c.tx, size = 20.dp)
                    }

                    // Favorite button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(Space.s16)
                            .padding(top = Space.s32)
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.9f), Shapes.pill)
                            .clickable { onToggleFavorite() },
                        contentAlignment = Alignment.Center
                    ) {
                        IconHeart(
                            color = if (uiState.isFavorite) c.ac else c.tx3,
                            filled = uiState.isFavorite,
                            size = 20.dp
                        )
                    }
                }
            }

            // Content
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Space.screenPadding)
                        .padding(top = Space.s20)
                ) {
                    // Category badge
                    Box(
                        modifier = Modifier
                            .clip(Shapes.pill)
                            .background(c.p50)
                            .padding(horizontal = Space.s10, vertical = Space.s4)
                    ) {
                        Text(
                            text = activity.category.name,
                            style = typography.small,
                            color = c.p7,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(Space.s12))

                    // Title with verified
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = activity.title,
                            style = typography.h1,
                            color = c.tx,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (activity.isVerified) {
                            Spacer(modifier = Modifier.width(Space.s8))
                            IconBadgeCheck(color = c.p6, size = 22.dp)
                        }
                    }

                    Spacer(modifier = Modifier.height(Space.s12))

                    // Rating, age, distance
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Space.s16)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconStar(color = c.sun, filled = true, size = 16.dp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatRating(activity.rating),
                                style = typography.body,
                                color = c.tx,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = " (${activity.reviewCount})",
                                style = typography.body,
                                color = c.tx3
                            )
                        }

                        Text(
                            text = "Ages ${activity.ageMin}–${activity.ageMax}",
                            style = typography.body,
                            color = c.tx2
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconMapPin(color = c.tx3, size = 14.dp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${formatDistance(activity.distance)} mi",
                                style = typography.body,
                                color = c.tx2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Space.s20))

                    // Description
                    Text(
                        text = activity.description,
                        style = typography.body,
                        color = c.tx2,
                        lineHeight = 24.sp
                    )

                    // What's included
                    if (activity.whatIncluded.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(Space.s24))

                        Text(
                            text = "What's included",
                            style = typography.h3,
                            color = c.tx,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(Space.s12))

                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(Space.s8)
                        ) {
                            activity.whatIncluded.forEach { item ->
                                TrustChip(text = item)
                            }
                        }
                    }

                    // Sessions
                    Spacer(modifier = Modifier.height(Space.s24))

                    Text(
                        text = "Available sessions",
                        style = typography.h3,
                        color = c.tx,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(Space.s12))

                    val sessionsToShow = if (uiState.showAllSessions) {
                        activity.sessions
                    } else {
                        activity.sessions.take(3)
                    }

                    sessionsToShow.forEach { session ->
                        SessionRow(
                            session = session,
                            isSelected = uiState.selectedSessionId == session.id,
                            onSelect = { onSessionSelect(session.id) }
                        )
                        Spacer(modifier = Modifier.height(Space.s10))
                    }

                    if (activity.sessions.size > 3 && !uiState.showAllSessions) {
                        Text(
                            text = "Show all ${activity.sessions.size} sessions",
                            style = typography.body,
                            color = c.p6,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .clickable { onShowAllSessions() }
                                .padding(vertical = Space.s8)
                        )
                    }

                    // Cancellation policy
                    Spacer(modifier = Modifier.height(Space.s20))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(Shapes.md)
                            .background(c.succ50)
                            .padding(Space.s14),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconCheck(color = c.succ, size = 18.dp)
                        Spacer(modifier = Modifier.width(Space.s10))
                        Text(
                            text = activity.cancellationPolicy,
                            style = typography.small,
                            color = c.succ,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Ask a question
                    Spacer(modifier = Modifier.height(Space.s16))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(Shapes.md)
                            .border(1.dp, c.bd, Shapes.md)
                            .clickable { onAskQuestion() }
                            .padding(Space.s14),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconMessageCircle(color = c.p6, size = 20.dp)
                        Spacer(modifier = Modifier.width(Space.s12))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ask a question",
                                style = typography.body,
                                color = c.tx,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Message ${activity.providerName} before booking",
                                style = typography.small,
                                color = c.tx3
                            )
                        }
                        IconChevronDown(color = c.tx3, size = 18.dp)
                    }
                }
            }
        }

        // Sticky booking footer
        BookingFooter(
            activity = activity,
            selectedSession = activity.sessions.find { it.id == uiState.selectedSessionId },
            children = uiState.children,
            selectedChildId = uiState.selectedChildId,
            hasExpressBooking = uiState.hasExpressBooking,
            paymentMethod = uiState.defaultPaymentMethod,
            onChildSelect = onChildSelect,
            onBook = onBook,
            onExpressBook = onExpressBook,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TrustChip(text: String) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .clip(Shapes.pill)
            .background(c.succ50)
            .padding(horizontal = Space.s12, vertical = Space.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconCheck(color = c.succ, size = 14.dp)
        Spacer(modifier = Modifier.width(Space.s8))
        Text(
            text = text,
            style = typography.small,
            color = c.succ,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun SessionRow(
    session: ActivitySession,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val isAvailable = session.spotsLeft > 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.md)
            .background(if (isSelected) c.p50 else c.surface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) c.p6 else c.bd,
                shape = Shapes.md
            )
            .clickable(enabled = isAvailable) { onSelect() }
            .padding(Space.s14),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${session.dayOfWeek} ${session.time}",
                style = typography.body,
                color = if (isAvailable) c.tx else c.tx3,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = session.duration,
                style = typography.small,
                color = c.tx3
            )
        }

        // Spots indicator
        when {
            session.spotsLeft == 0 -> {
                Box(
                    modifier = Modifier
                        .clip(Shapes.pill)
                        .background(c.errorBg)
                        .padding(horizontal = Space.s10, vertical = Space.s4)
                ) {
                    Text(
                        text = "Fully booked",
                        style = typography.small,
                        color = c.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            session.spotsLeft <= 3 -> {
                Box(
                    modifier = Modifier
                        .clip(Shapes.pill)
                        .background(c.warn50)
                        .padding(horizontal = Space.s10, vertical = Space.s4)
                ) {
                    Text(
                        text = "Only ${session.spotsLeft} left",
                        style = typography.small,
                        color = c.warn,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            else -> {
                Text(
                    text = "${session.spotsLeft} spots",
                    style = typography.small,
                    color = c.tx3
                )
            }
        }
    }
}

@Composable
private fun BookingFooter(
    activity: Activity,
    selectedSession: ActivitySession?,
    children: List<Child>,
    selectedChildId: String?,
    hasExpressBooking: Boolean,
    paymentMethod: PaymentMethod?,
    onChildSelect: (String) -> Unit,
    onBook: () -> Unit,
    onExpressBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(c.surface)
            .padding(Space.screenPadding)
    ) {
        // Price
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "£${selectedSession?.pricePerSession?.toInt() ?: activity.pricePerSession.toInt()}",
                    style = typography.h1,
                    color = c.tx,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " /session",
                    style = typography.body,
                    color = c.tx3
                )
            }

            // Child selector
            if (children.isNotEmpty()) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(Space.s8)
                ) {
                    children.forEach { child ->
                        ChildSelector(
                            child = child,
                            isSelected = selectedChildId == child.id,
                            onClick = { onChildSelect(child.id) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Space.s14))

        // Booking buttons
        if (hasExpressBooking && paymentMethod != null && selectedChildId != null) {
            // Express book with Apple Pay
            val childName = children.find { it.id == selectedChildId }?.firstName ?: ""
            KhButton(
                text = "Book now · $childName",
                onClick = onExpressBook,
                variant = KhButtonVariant.Wallet,
                icon = {
                    if (paymentMethod.type == PaymentMethodType.ApplePay) {
                        IconApple(color = Color.White, size = 18.dp)
                    }
                }
            )
        } else {
            KhButton(
                text = "Continue to book",
                onClick = onBook,
                enabled = selectedChildId != null && selectedSession?.spotsLeft ?: 0 > 0
            )
        }
    }
}
