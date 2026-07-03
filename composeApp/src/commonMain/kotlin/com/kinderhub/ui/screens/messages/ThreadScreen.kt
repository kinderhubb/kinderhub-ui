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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.components.IconBadgeCheck
import com.kinderhub.ui.components.IconSend
import com.kinderhub.ui.data.model.Message
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ThreadScreen(
    threadId: String,
    onBack: () -> Unit,
    viewModel: ThreadViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val listState = rememberLazyListState()

    LaunchedEffect(threadId) {
        viewModel.loadThread(threadId)
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
            .imePadding()
    ) {
        // Header
        ThreadHeader(
            providerName = uiState.thread?.providerName ?: "",
            providerInitials = uiState.thread?.providerInitials ?: "",
            isVerified = uiState.thread?.isProviderVerified ?: false,
            onBack = onBack
        )

        when (uiState.screenState) {
            ThreadScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = c.p6)
                }
            }
            ThreadScreenState.Error -> {
                val strings = Strings.current
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(strings.commonError, color = c.tx2)
                }
            }
            ThreadScreenState.Success -> {
                // Messages
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = Space.screenPadding,
                        vertical = Space.s16
                    ),
                    verticalArrangement = Arrangement.spacedBy(Space.s10)
                ) {
                    items(uiState.messages) { message ->
                        MessageBubble(message = message)
                    }
                }

                // Input
                val strings = Strings.current
                MessageInput(
                    value = uiState.messageInput,
                    placeholder = strings.messagesTypeMessage,
                    onValueChange = viewModel::updateMessageInput,
                    onSend = viewModel::sendMessage,
                    isSending = uiState.isSending
                )
            }
        }
    }
}

@Composable
private fun ThreadHeader(
    providerName: String,
    providerInitials: String,
    isVerified: Boolean,
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

        Spacer(modifier = Modifier.width(Space.s14))

        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(c.p50),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = providerInitials,
                style = typography.body,
                color = c.p7,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(Space.s12))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = providerName,
                style = typography.body,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
            if (isVerified) {
                Spacer(modifier = Modifier.width(Space.s6))
                IconBadgeCheck(color = c.p6, size = 16.dp)
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val alignment = if (message.isFromUser) Alignment.End else Alignment.Start
    val bgColor = if (message.isFromUser) c.p6 else c.surface
    val textColor = if (message.isFromUser) Color.White else c.tx

    val bubbleShape = if (message.isFromUser) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 4.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 4.dp,
            bottomEnd = 16.dp
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .background(bgColor)
                .padding(horizontal = Space.s14, vertical = Space.s10)
        ) {
            Text(
                text = message.content,
                style = typography.body,
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = message.timestamp,
            style = typography.xs,
            color = c.tx3
        )
    }
}

@Composable
private fun MessageInput(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    isSending: Boolean
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(horizontal = Space.screenPadding, vertical = Space.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(Shapes.pill)
                .background(c.bg)
                .padding(horizontal = Space.s16, vertical = Space.s12)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = typography.body,
                    color = c.tx3
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = typography.body.copy(color = c.tx),
                cursorBrush = SolidColor(c.p6),
                singleLine = false,
                maxLines = 4
            )
        }

        Spacer(modifier = Modifier.width(Space.s12))

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (value.isNotBlank() && !isSending) c.p6 else c.p50)
                .clickable(enabled = value.isNotBlank() && !isSending) { onSend() },
            contentAlignment = Alignment.Center
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    color = c.p6,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                IconSend(
                    color = if (value.isNotBlank()) Color.White else c.p6,
                    size = 20.dp
                )
            }
        }
    }
}
