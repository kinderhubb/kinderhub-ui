package com.kinderhub.ui.data.auth

import kotlinx.datetime.Clock

/**
 * Platform-agnostic Auth0 service implementation.
 * This is a mock implementation for development/testing.
 *
 * For production:
 * - Android: Use Auth0 Android SDK
 * - iOS: Use Auth0.swift SDK
 * - Web: Use auth0-spa-js
 *
 * Create expect/actual declarations for platform-specific implementations.
 */
class MockAuth0Service(
    private val config: Auth0Config = DefaultAuth0Config
) : Auth0Service {

    private var storedCredentials: Auth0Credentials? = null
    private var storedUser: Auth0User? = null

    override suspend fun login(): AuthResult {
        // Simulate Auth0 Universal Login
        kotlinx.coroutines.delay(1500)

        val credentials = Auth0Credentials(
            accessToken = "mock_access_token_${Clock.System.now().toEpochMilliseconds()}",
            idToken = "mock_id_token",
            refreshToken = "mock_refresh_token",
            expiresAt = Clock.System.now().toEpochMilliseconds() + 86400000 // 24 hours
        )

        val user = Auth0User(
            sub = "auth0|user123",
            email = "sarah@example.com",
            emailVerified = true,
            name = "Sarah Bennett",
            givenName = "Sarah",
            familyName = "Bennett",
            picture = null
        )

        storedCredentials = credentials
        storedUser = user

        return AuthResult.Success(credentials, user)
    }

    override suspend fun loginWithConnection(connection: String): AuthResult {
        kotlinx.coroutines.delay(1000)

        val providerName = when (connection) {
            "apple" -> "Apple"
            "google-oauth2" -> "Google"
            else -> connection
        }

        val credentials = Auth0Credentials(
            accessToken = "mock_${connection}_token_${Clock.System.now().toEpochMilliseconds()}",
            idToken = "mock_id_token",
            refreshToken = "mock_refresh_token",
            expiresAt = Clock.System.now().toEpochMilliseconds() + 86400000
        )

        val user = Auth0User(
            sub = "$connection|user123",
            email = "sarah@example.com",
            emailVerified = true,
            name = "Sarah Bennett",
            givenName = "Sarah",
            familyName = "Bennett",
            picture = null
        )

        storedCredentials = credentials
        storedUser = user

        return AuthResult.Success(credentials, user)
    }

    override suspend fun signUp(email: String, password: String, name: String): AuthResult {
        kotlinx.coroutines.delay(1500)

        val nameParts = name.split(" ", limit = 2)

        val credentials = Auth0Credentials(
            accessToken = "mock_signup_token_${Clock.System.now().toEpochMilliseconds()}",
            idToken = "mock_id_token",
            refreshToken = "mock_refresh_token",
            expiresAt = Clock.System.now().toEpochMilliseconds() + 86400000
        )

        val user = Auth0User(
            sub = "auth0|${Clock.System.now().toEpochMilliseconds()}",
            email = email,
            emailVerified = false, // Needs verification
            name = name,
            givenName = nameParts.firstOrNull(),
            familyName = nameParts.getOrNull(1),
            picture = null
        )

        storedCredentials = credentials
        storedUser = user

        return AuthResult.Success(credentials, user)
    }

    override suspend fun logout(): Boolean {
        kotlinx.coroutines.delay(300)
        storedCredentials = null
        storedUser = null
        return true
    }

    override suspend fun getCredentials(): Auth0Credentials? {
        val creds = storedCredentials ?: return null

        // Check if expired
        if (creds.expiresAt < Clock.System.now().toEpochMilliseconds()) {
            return null
        }

        return creds
    }

    override suspend fun refreshToken(): AuthResult {
        val currentCreds = storedCredentials
            ?: return AuthResult.Error("no_credentials", "No stored credentials")

        kotlinx.coroutines.delay(500)

        val newCredentials = currentCreds.copy(
            accessToken = "mock_refreshed_token_${Clock.System.now().toEpochMilliseconds()}",
            expiresAt = Clock.System.now().toEpochMilliseconds() + 86400000
        )

        storedCredentials = newCredentials

        return AuthResult.Success(newCredentials, storedUser)
    }

    override suspend fun getUserProfile(): Auth0User? {
        return storedUser
    }

    override suspend fun isAuthenticated(): Boolean {
        return getCredentials() != null
    }
}
