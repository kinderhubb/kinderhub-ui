package com.kinderhub.ui.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Message
import com.kinderhub.ui.data.model.MessageThread
import com.kinderhub.ui.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ThreadScreenState {
    Loading,
    Success,
    Error
}

data class ThreadUiState(
    val screenState: ThreadScreenState = ThreadScreenState.Loading,
    val thread: MessageThread? = null,
    val messages: List<Message> = emptyList(),
    val messageInput: String = "",
    val isSending: Boolean = false,
)

class ThreadViewModel(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThreadUiState())
    val uiState: StateFlow<ThreadUiState> = _uiState.asStateFlow()

    private var currentThreadId: String? = null

    fun loadThread(threadId: String) {
        currentThreadId = threadId

        viewModelScope.launch {
            _uiState.update { it.copy(screenState = ThreadScreenState.Loading) }

            try {
                val threads = messageRepository.getThreads()
                val thread = threads.find { it.id == threadId }
                val messages = messageRepository.getMessages(threadId)

                // Mark as read
                messageRepository.markThreadAsRead(threadId)

                if (thread != null) {
                    _uiState.update {
                        it.copy(
                            screenState = ThreadScreenState.Success,
                            thread = thread,
                            messages = messages
                        )
                    }
                } else {
                    _uiState.update { it.copy(screenState = ThreadScreenState.Error) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = ThreadScreenState.Error) }
            }
        }
    }

    fun updateMessageInput(input: String) {
        _uiState.update { it.copy(messageInput = input) }
    }

    fun sendMessage() {
        val threadId = currentThreadId ?: return
        val content = _uiState.value.messageInput.trim()
        if (content.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, messageInput = "") }

            try {
                val newMessage = messageRepository.sendMessage(threadId, content)
                _uiState.update {
                    it.copy(
                        messages = it.messages + newMessage,
                        isSending = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSending = false,
                        messageInput = content // Restore input on error
                    )
                }
            }
        }
    }
}
