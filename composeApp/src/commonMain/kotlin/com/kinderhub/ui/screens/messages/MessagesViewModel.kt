package com.kinderhub.ui.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.MessageThread
import com.kinderhub.ui.data.repository.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class MessagesScreenState {
    Loading,
    Success,
    Empty,
    Error
}

data class MessagesUiState(
    val screenState: MessagesScreenState = MessagesScreenState.Loading,
    val threads: List<MessageThread> = emptyList(),
    val unreadCount: Int = 0,
)

class MessagesViewModel(
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    fun loadThreads() {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = MessagesScreenState.Loading) }

            try {
                val threads = messageRepository.getThreads()
                val unreadCount = messageRepository.getUnreadCount()

                val state = if (threads.isEmpty()) {
                    MessagesScreenState.Empty
                } else {
                    MessagesScreenState.Success
                }

                _uiState.update {
                    it.copy(
                        screenState = state,
                        threads = threads,
                        unreadCount = unreadCount
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = MessagesScreenState.Error) }
            }
        }
    }
}
