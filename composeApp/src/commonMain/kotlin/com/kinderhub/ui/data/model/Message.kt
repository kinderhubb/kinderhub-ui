package com.kinderhub.ui.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageThread(
    val id: String,
    val providerName: String,
    val providerInitials: String,
    val activityTitle: String? = null,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isProviderVerified: Boolean = true,
)

@Serializable
data class Message(
    val id: String,
    val threadId: String,
    val content: String,
    val timestamp: String,
    val isFromUser: Boolean,
    val isRead: Boolean = true,
)

@Serializable
data class SendMessageRequest(
    val threadId: String,
    val content: String,
)
