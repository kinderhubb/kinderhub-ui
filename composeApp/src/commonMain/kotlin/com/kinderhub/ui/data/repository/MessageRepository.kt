package com.kinderhub.ui.data.repository

import com.kinderhub.ui.data.model.Message
import com.kinderhub.ui.data.model.MessageThread
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

interface MessageRepository {
    suspend fun getThreads(): List<MessageThread>
    suspend fun getMessages(threadId: String): List<Message>
    suspend fun sendMessage(threadId: String, content: String): Message
    suspend fun markThreadAsRead(threadId: String)
    suspend fun getUnreadCount(): Int
}

class MockMessageRepository : MessageRepository {

    private val threads = mutableListOf(
        MessageThread(
            id = "thread_001",
            providerName = "AquaKids Academy",
            providerInitials = "AK",
            activityTitle = "Little Swimmers – Beginner",
            lastMessage = "Looking forward to seeing Ella on Saturday!",
            lastMessageTime = "2h ago",
            unreadCount = 1
        ),
        MessageThread(
            id = "thread_002",
            providerName = "STEAM Kids London",
            providerInitials = "SK",
            activityTitle = "Mini Scientists Lab",
            lastMessage = "The lab coats are provided. Just bring enthusiasm!",
            lastMessageTime = "Yesterday",
            unreadCount = 0
        ),
        MessageThread(
            id = "thread_003",
            providerName = "Junior Football Academy",
            providerInitials = "JF",
            activityTitle = "Football Skills Camp",
            lastMessage = "Thank you for your booking confirmation.",
            lastMessageTime = "3 days ago",
            unreadCount = 0
        )
    )

    private val messages = mutableMapOf(
        "thread_001" to mutableListOf(
            Message(
                id = "msg_001",
                threadId = "thread_001",
                content = "Hi! I have a question about the swimming lessons.",
                timestamp = "10:30 AM",
                isFromUser = true
            ),
            Message(
                id = "msg_002",
                threadId = "thread_001",
                content = "Hello! Of course, happy to help. What would you like to know?",
                timestamp = "10:45 AM",
                isFromUser = false
            ),
            Message(
                id = "msg_003",
                threadId = "thread_001",
                content = "Does Ella need to bring her own goggles?",
                timestamp = "10:47 AM",
                isFromUser = true
            ),
            Message(
                id = "msg_004",
                threadId = "thread_001",
                content = "We provide all equipment including goggles, swim caps, and floatation devices. Just bring a towel and swimwear!",
                timestamp = "11:00 AM",
                isFromUser = false
            ),
            Message(
                id = "msg_005",
                threadId = "thread_001",
                content = "Looking forward to seeing Ella on Saturday!",
                timestamp = "11:02 AM",
                isFromUser = false,
                isRead = false
            )
        ),
        "thread_002" to mutableListOf(
            Message(
                id = "msg_006",
                threadId = "thread_002",
                content = "Hi, does Max need to bring anything for the science class?",
                timestamp = "Yesterday",
                isFromUser = true
            ),
            Message(
                id = "msg_007",
                threadId = "thread_002",
                content = "The lab coats are provided. Just bring enthusiasm!",
                timestamp = "Yesterday",
                isFromUser = false
            )
        ),
        "thread_003" to mutableListOf(
            Message(
                id = "msg_008",
                threadId = "thread_003",
                content = "Your booking for Football Skills Camp has been confirmed.",
                timestamp = "3 days ago",
                isFromUser = false
            ),
            Message(
                id = "msg_009",
                threadId = "thread_003",
                content = "Thank you for your booking confirmation.",
                timestamp = "3 days ago",
                isFromUser = false
            )
        )
    )

    override suspend fun getThreads(): List<MessageThread> {
        delay(300)
        return threads.sortedByDescending { it.unreadCount }
    }

    override suspend fun getMessages(threadId: String): List<Message> {
        delay(200)
        return messages[threadId] ?: emptyList()
    }

    override suspend fun sendMessage(threadId: String, content: String): Message {
        delay(300)
        val newMessage = Message(
            id = "msg_${Clock.System.now().toEpochMilliseconds()}",
            threadId = threadId,
            content = content,
            timestamp = "Just now",
            isFromUser = true
        )
        messages.getOrPut(threadId) { mutableListOf() }.add(newMessage)

        // Update thread's last message
        val threadIndex = threads.indexOfFirst { it.id == threadId }
        if (threadIndex >= 0) {
            threads[threadIndex] = threads[threadIndex].copy(
                lastMessage = content,
                lastMessageTime = "Just now"
            )
        }

        return newMessage
    }

    override suspend fun markThreadAsRead(threadId: String) {
        delay(100)
        val threadIndex = threads.indexOfFirst { it.id == threadId }
        if (threadIndex >= 0) {
            threads[threadIndex] = threads[threadIndex].copy(unreadCount = 0)
        }
        messages[threadId]?.let { msgs ->
            for (i in msgs.indices) {
                msgs[i] = msgs[i].copy(isRead = true)
            }
        }
    }

    override suspend fun getUnreadCount(): Int {
        return threads.sumOf { it.unreadCount }
    }
}
