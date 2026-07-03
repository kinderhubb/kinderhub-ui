package com.kinderhub.ui.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class Auth0Config(
    val domain: String,
    val clientId: String,
    val audience: String? = null,
    val scheme: String = "kinderhub",
)

// Default config - replace with your Auth0 credentials
val DefaultAuth0Config = Auth0Config(
    domain = "your-tenant.uk.auth0.com",
    clientId = "your-client-id",
    audience = "https://api.kinderhub.co.uk",
    scheme = "kinderhub"
)

@Serializable
data class Auth0Credentials(
    val accessToken: String,
    val idToken: String? = null,
    val refreshToken: String? = null,
    val expiresAt: Long,
    val tokenType: String = "Bearer",
)

@Serializable
data class Auth0User(
    val sub: String,           // Auth0 user ID
    val email: String?,
    val emailVerified: Boolean = false,
    val name: String?,
    val givenName: String?,
    val familyName: String?,
    val picture: String?,
)
