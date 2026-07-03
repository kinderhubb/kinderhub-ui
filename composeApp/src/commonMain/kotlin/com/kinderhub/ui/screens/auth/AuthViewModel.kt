package com.kinderhub.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.AuthResponse
import com.kinderhub.ui.data.model.LoginRequest
import com.kinderhub.ui.data.model.SignUpRequest
import com.kinderhub.ui.data.model.User
import com.kinderhub.ui.data.repository.AuthRepository
import com.kinderhub.ui.data.repository.MockAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthMode {
    Login,
    SignUp
}

enum class AuthScreenState {
    Default,
    Loading,
    Error,
    VerificationPending,
    Success
}

data class AuthUiState(
    val mode: AuthMode = AuthMode.Login,
    val screenState: AuthScreenState = AuthScreenState.Default,

    // Form fields
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val showPassword: Boolean = false,

    // Validation
    val emailError: String? = null,
    val passwordError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,

    // General error (banner)
    val errorMessage: String? = null,

    // Loading text
    val loadingText: String = "Logging in...",

    // Authenticated user
    val user: User? = null,
)

class AuthViewModel(
    private val authRepository: AuthRepository = MockAuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                errorMessage = null
            )
        }
    }

    fun updateFirstName(firstName: String) {
        _uiState.update {
            it.copy(
                firstName = firstName,
                firstNameError = null,
                errorMessage = null
            )
        }
    }

    fun updateLastName(lastName: String) {
        _uiState.update {
            it.copy(
                lastName = lastName,
                lastNameError = null,
                errorMessage = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(showPassword = !it.showPassword) }
    }

    fun toggleMode() {
        _uiState.update {
            it.copy(
                mode = if (it.mode == AuthMode.Login) AuthMode.SignUp else AuthMode.Login,
                emailError = null,
                passwordError = null,
                firstNameError = null,
                lastNameError = null,
                errorMessage = null,
                screenState = AuthScreenState.Default
            )
        }
    }

    fun login() {
        val state = _uiState.value

        // Validate
        var hasError = false

        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email is required") }
            hasError = true
        } else if (!isValidEmail(state.email)) {
            _uiState.update { it.copy(emailError = "Please enter a valid email") }
            hasError = true
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.Loading,
                    loadingText = "Logging in..."
                )
            }

            val response = authRepository.login(
                LoginRequest(
                    email = state.email.trim(),
                    password = state.password
                )
            )

            handleAuthResponse(response)
        }
    }

    fun signUp() {
        val state = _uiState.value

        // Validate
        var hasError = false

        if (state.firstName.isBlank()) {
            _uiState.update { it.copy(firstNameError = "First name is required") }
            hasError = true
        }

        if (state.lastName.isBlank()) {
            _uiState.update { it.copy(lastNameError = "Last name is required") }
            hasError = true
        }

        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email is required") }
            hasError = true
        } else if (!isValidEmail(state.email)) {
            _uiState.update { it.copy(emailError = "Please enter a valid email") }
            hasError = true
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password is required") }
            hasError = true
        } else if (state.password.length < 8) {
            _uiState.update { it.copy(passwordError = "Password must be at least 8 characters") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.Loading,
                    loadingText = "Creating account..."
                )
            }

            val response = authRepository.signUp(
                SignUpRequest(
                    email = state.email.trim(),
                    password = state.password,
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim()
                )
            )

            if (response.success && response.user != null && !response.user.isEmailVerified) {
                _uiState.update {
                    it.copy(
                        screenState = AuthScreenState.VerificationPending,
                        user = response.user
                    )
                }
            } else {
                handleAuthResponse(response)
            }
        }
    }

    fun loginWithApple() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.Loading,
                    loadingText = "Connecting to Apple..."
                )
            }

            val response = authRepository.loginWithApple()
            handleAuthResponse(response)
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.Loading,
                    loadingText = "Connecting to Google..."
                )
            }

            val response = authRepository.loginWithGoogle()
            handleAuthResponse(response)
        }
    }

    fun resendVerificationEmail() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.Loading,
                    loadingText = "Sending email..."
                )
            }

            val success = authRepository.resendVerificationEmail(state.email)

            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.VerificationPending,
                    errorMessage = if (success) null else "Failed to send verification email"
                )
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                screenState = if (it.screenState == AuthScreenState.Error) AuthScreenState.Default else it.screenState
            )
        }
    }

    private fun handleAuthResponse(response: AuthResponse) {
        if (response.success && response.user != null) {
            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.Success,
                    user = response.user,
                    errorMessage = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    screenState = AuthScreenState.Error,
                    errorMessage = response.error?.message ?: "Something went wrong. Please try again."
                )
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
