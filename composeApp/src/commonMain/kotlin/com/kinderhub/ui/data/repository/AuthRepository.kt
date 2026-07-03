package com.kinderhub.ui.data.repository

import com.kinderhub.ui.data.auth.Auth0Service
import com.kinderhub.ui.data.auth.AuthResult
import com.kinderhub.ui.data.auth.MockAuth0Service
import com.kinderhub.ui.data.auth.toUser
import com.kinderhub.ui.data.model.AuthError
import com.kinderhub.ui.data.model.AuthResponse
import com.kinderhub.ui.data.model.LoginRequest
import com.kinderhub.ui.data.model.SignUpRequest
import com.kinderhub.ui.data.model.User

interface AuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun signUp(request: SignUpRequest): AuthResponse
    suspend fun loginWithApple(): AuthResponse
    suspend fun loginWithGoogle(): AuthResponse
    suspend fun logout()
    suspend fun resendVerificationEmail(email: String): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun isAuthenticated(): Boolean
}

/**
 * Auth repository implementation using Auth0
 */
class Auth0AuthRepository(
    private val auth0Service: Auth0Service = MockAuth0Service()
) : AuthRepository {

    override suspend fun login(request: LoginRequest): AuthResponse {
        // For email/password, we use Auth0 Resource Owner Password flow
        // In production, this would use auth0Service.login() for Universal Login
        return when (val result = auth0Service.login()) {
            is AuthResult.Success -> {
                val user = result.user?.toUser()
                AuthResponse(
                    success = true,
                    user = user,
                    accessToken = result.credentials.accessToken
                )
            }
            is AuthResult.Error -> {
                AuthResponse(
                    success = false,
                    error = AuthError(result.code, result.message)
                )
            }
            is AuthResult.Cancelled -> {
                AuthResponse(
                    success = false,
                    error = AuthError("cancelled", "Login was cancelled")
                )
            }
        }
    }

    override suspend fun signUp(request: SignUpRequest): AuthResponse {
        val fullName = "${request.firstName} ${request.lastName}"

        return when (val result = auth0Service.signUp(request.email, request.password, fullName)) {
            is AuthResult.Success -> {
                val user = result.user?.toUser()
                AuthResponse(
                    success = true,
                    user = user,
                    accessToken = if (user?.isEmailVerified == true) result.credentials.accessToken else null
                )
            }
            is AuthResult.Error -> {
                AuthResponse(
                    success = false,
                    error = AuthError(result.code, result.message)
                )
            }
            is AuthResult.Cancelled -> {
                AuthResponse(
                    success = false,
                    error = AuthError("cancelled", "Sign up was cancelled")
                )
            }
        }
    }

    override suspend fun loginWithApple(): AuthResponse {
        return when (val result = auth0Service.loginWithConnection("apple")) {
            is AuthResult.Success -> {
                val user = result.user?.toUser()
                AuthResponse(
                    success = true,
                    user = user,
                    accessToken = result.credentials.accessToken
                )
            }
            is AuthResult.Error -> {
                AuthResponse(
                    success = false,
                    error = AuthError(result.code, result.message)
                )
            }
            is AuthResult.Cancelled -> {
                AuthResponse(
                    success = false,
                    error = AuthError("cancelled", "Apple login was cancelled")
                )
            }
        }
    }

    override suspend fun loginWithGoogle(): AuthResponse {
        return when (val result = auth0Service.loginWithConnection("google-oauth2")) {
            is AuthResult.Success -> {
                val user = result.user?.toUser()
                AuthResponse(
                    success = true,
                    user = user,
                    accessToken = result.credentials.accessToken
                )
            }
            is AuthResult.Error -> {
                AuthResponse(
                    success = false,
                    error = AuthError(result.code, result.message)
                )
            }
            is AuthResult.Cancelled -> {
                AuthResponse(
                    success = false,
                    error = AuthError("cancelled", "Google login was cancelled")
                )
            }
        }
    }

    override suspend fun logout() {
        auth0Service.logout()
    }

    override suspend fun resendVerificationEmail(email: String): Boolean {
        // In production, this would call Auth0 Management API
        // POST /api/v2/jobs/verification-email
        kotlinx.coroutines.delay(1000)
        return true
    }

    override suspend fun getCurrentUser(): User? {
        val auth0User = auth0Service.getUserProfile() ?: return null
        return auth0User.toUser()
    }

    override suspend fun isAuthenticated(): Boolean {
        return auth0Service.isAuthenticated()
    }
}

// Keep MockAuthRepository for testing without Auth0
class MockAuthRepository : AuthRepository {
    private val auth0Repository = Auth0AuthRepository()

    override suspend fun login(request: LoginRequest) = auth0Repository.login(request)
    override suspend fun signUp(request: SignUpRequest) = auth0Repository.signUp(request)
    override suspend fun loginWithApple() = auth0Repository.loginWithApple()
    override suspend fun loginWithGoogle() = auth0Repository.loginWithGoogle()
    override suspend fun logout() = auth0Repository.logout()
    override suspend fun resendVerificationEmail(email: String) = auth0Repository.resendVerificationEmail(email)
    override suspend fun getCurrentUser() = auth0Repository.getCurrentUser()
    override suspend fun isAuthenticated() = auth0Repository.isAuthenticated()
}
