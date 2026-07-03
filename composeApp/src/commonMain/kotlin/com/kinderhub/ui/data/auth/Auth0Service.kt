package com.kinderhub.ui.data.auth

import com.kinderhub.ui.data.model.AuthResponse
import com.kinderhub.ui.data.model.User

/**
 * Auth0 authentication service interface.
 * Platform-specific implementations handle the actual Auth0 SDK integration.
 */
interface Auth0Service {
    /**
     * Start the Auth0 Universal Login flow
     */
    suspend fun login(): AuthResult

    /**
     * Start Auth0 login with specific connection (e.g., "apple", "google-oauth2")
     */
    suspend fun loginWithConnection(connection: String): AuthResult

    /**
     * Sign up with email/password
     */
    suspend fun signUp(email: String, password: String, name: String): AuthResult

    /**
     * Log out and clear credentials
     */
    suspend fun logout(): Boolean

    /**
     * Get stored credentials if valid
     */
    suspend fun getCredentials(): Auth0Credentials?

    /**
     * Refresh the access token
     */
    suspend fun refreshToken(): AuthResult

    /**
     * Get the current user profile from Auth0
     */
    suspend fun getUserProfile(): Auth0User?

    /**
     * Check if user is currently authenticated
     */
    suspend fun isAuthenticated(): Boolean
}

sealed class AuthResult {
    data class Success(
        val credentials: Auth0Credentials,
        val user: Auth0User?
    ) : AuthResult()

    data class Error(
        val code: String,
        val message: String,
        val cause: Throwable? = null
    ) : AuthResult()

    data object Cancelled : AuthResult()
}

/**
 * Convert Auth0User to app User model
 */
fun Auth0User.toUser(): User {
    return User(
        id = sub,
        email = email ?: "",
        firstName = givenName ?: name?.split(" ")?.firstOrNull() ?: "",
        lastName = familyName ?: name?.split(" ")?.drop(1)?.joinToString(" ") ?: "",
        isEmailVerified = emailVerified,
        children = emptyList(), // Will be loaded from backend
        location = null,
        savedPaymentMethods = emptyList()
    )
}
