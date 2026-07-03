package com.kinderhub.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.User
import com.kinderhub.ui.data.repository.AuthRepository
import com.kinderhub.ui.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountUiState(
    val user: User? = null,
    val children: List<Child> = emptyList(),
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val isLoggingOut: Boolean = false,
)

class AccountViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        observeUserData()
    }

    private fun observeUserData() {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
        viewModelScope.launch {
            userRepository.children.collect { children ->
                _uiState.update { it.copy(children = children) }
            }
        }
        viewModelScope.launch {
            userRepository.paymentMethods.collect { paymentMethods ->
                _uiState.update { it.copy(paymentMethods = paymentMethods) }
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true) }
            authRepository.logout()
            onLoggedOut()
        }
    }
}
