package com.kinderhub.ui.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatarInitials: String = "${firstName.firstOrNull() ?: ""}${lastName.firstOrNull() ?: ""}",
    val isEmailVerified: Boolean = false,
    val children: List<Child> = emptyList(),
    val location: UserLocation? = null,
    val savedPaymentMethods: List<PaymentMethod> = emptyList(),
)

@Serializable
data class Child(
    val id: String,
    val firstName: String,
    val dateOfBirth: String, // ISO format: "2019-03-15"
    val age: Int,
    val avatarColor: AvatarColor = AvatarColor.Accent,
)

@Serializable
enum class AvatarColor {
    Accent,     // For first child (e.g., Ella - terracotta)
    Sunshine,   // For second child (e.g., Max - gold)
    Primary,
    Success
}

@Serializable
data class UserLocation(
    val postcode: String,
    val city: String = "London",
    val isFromGps: Boolean = false,
)

@Serializable
data class PaymentMethod(
    val id: String,
    val type: PaymentMethodType,
    val last4: String? = null,
    val brand: String? = null, // "Visa", "Mastercard", etc.
    val isDefault: Boolean = false,
)

@Serializable
enum class PaymentMethodType {
    ApplePay,
    GooglePay,
    Card
}

@Serializable
data class AuthState(
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val accessToken: String? = null,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val user: User? = null,
    val accessToken: String? = null,
    val error: AuthError? = null,
)

@Serializable
data class AuthError(
    val code: String,
    val message: String,
)
