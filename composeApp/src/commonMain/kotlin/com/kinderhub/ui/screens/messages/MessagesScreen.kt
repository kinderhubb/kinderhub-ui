package com.kinderhub.ui.screens.messages

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.AppShell
import com.kinderhub.ui.components.IconBadgeCheck
import com.kinderhub.ui.components.IconMessageCircle
import com.kinderhub.ui.components.LayoutMode
import com.kinderhub.ui.data.model.MessageThread
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessagesScreen(
    onThreadClick: (String) -> Unit,
    onNavigateToDiscover: () -> Unit = {},
    onNavigateToBookings: () -> Unit = {},
    onNavigateToAccount: () -> Unit = {},
    viewModel: MessagesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val strings = Strings.current

    LaunchedEffect(Unit) {
        viewModel.loadThreads()
    }

    AppShell(
        selectedTab = 2,
        unreadCount = uiState.unreadCount,
        onDiscoverClick = onNavigateToDiscover,
        onBookingsClick = onNavigateToBookings,
        onMessagesClick = { /* Already here */ },
        onAccountClick = onNavigateToAccount
    ) { layoutMode ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(c.bg)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                MessagesHeader(
                    title = strings.messagesTitle,
                    unreadCount = uiState.unreadCount
                )

                when (uiState.screenState) {
                    MessagesScreenState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = c.p6)
                        }
                    }
                    MessagesScreenState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(strings.commonError, color = c.tx2)
                        }
                    }
                    MessagesScreenState.Empty -> {
                        EmptyMessagesState(noMessagesText = strings.messagesNoMessages)
                    }
                    MessagesScreenState.Success -> {
                        ThreadsList(
                            threads = uiState.threads,
                            onThreadClick = onThreadClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessagesHeader(
    title: String,
    unreadCount: Int
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
        Text(
            text = title,
            style = typography.h1,
            color = c.tx,
            fontWeight = FontWeight.Bold
        )

        if (unreadCount > 0) {
            Spacer(modifier = Modifier.width(Space.s10))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(c.ac)
                    .padding(horizontal = Space.s10, vertical = 4.dp)
            ) {
                Text(
                    text = "$unreadCount",
                    style = typography.xs,
                    color = c.surface,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ThreadsList(
    threads: List<MessageThread>,
    onThreadClick: (String) -> Unit
) {
    val c = KhTheme.colors

    LazyColumn(
        contentPadding = PaddingValues(vertical = Space.s8)
    ) {
        items(threads) { thread ->
            ThreadItem(
                thread = thread,
                onClick = { onThreadClick(thread.id) }
            )
            HorizontalDivider(
                color = c.bd,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = Space.screenPadding)
            )
        }
    }
}

@Composable
private fun ThreadItem(
    thread: MessageThread,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Space.screenPadding, vertical = Space.s14),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = thread.providerInitials,
                style = typography.h3,
                color = c.p7,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(Space.s14))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = thread.providerName,
                        style = typography.body,
                        color = c.tx,
                        fontWeight = if (thread.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (thread.isProviderVerified) {
                        Spacer(modifier = Modifier.width(Space.s6))
                        IconBadgeCheck(color = c.p6, size = 16.dp)
                    }
                }

                Text(
                    text = thread.lastMessageTime,
                    style = typography.xs,
                    color = c.tx3
                )
            }

            if (thread.activityTitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = thread.activityTitle,
                    style = typography.small,
                    color = c.p6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(Space.s6))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = thread.lastMessage,
                    style = typography.small,
                    color = if (thread.unreadCount > 0) c.tx else c.tx2,
                    fontWeight = if (thread.unreadCount > 0) FontWeight.Medium else FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (thread.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(Space.s10))
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(c.ac),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = thread.unreadCount.toString(),
                            style = typography.xs,
                            color = c.surface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMessagesState(noMessagesText: String) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

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
            IconMessageCircle(color = c.p6, size = 44.dp)
        }

        Spacer(modifier = Modifier.height(Space.s20))

        Text(
            text = noMessagesText,
            style = typography.h2,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = "When you message a provider or receive booking updates, they'll appear here",
            style = typography.body,
            color = c.tx2,
            textAlign = TextAlign.Center
        )
    }
}
