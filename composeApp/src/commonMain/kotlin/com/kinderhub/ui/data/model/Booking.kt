package com.kinderhub.ui.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val id: String,
    val activityId: String,
    val activityTitle: String,
    val providerName: String,
    val sessionId: String,
    val childId: String,
    val childName: String,
    val sessionDate: String, // e.g., "Saturday, 15 Feb"
    val sessionTime: String, // e.g., "9:00 AM"
    val sessionDuration: String, // e.g., "45 mins"
    val status: BookingStatus,
    val price: Double,
    val currency: String = "£",
    val paymentMethodId: String? = null,
    val createdAt: String,
    val locationAddress: String = "",
    val providerImageUrl: String? = null,
)

@Serializable
enum class BookingStatus {
    Pending,
    Confirmed,
    Cancelled,
    Completed
}

@Serializable
data class BookingRequest(
    val activityId: String,
    val sessionId: String,
    val childId: String,
    val paymentMethodId: String,
    val isExpressBooking: Boolean = false,
)

@Serializable
data class BookingConfirmation(
    val booking: Booking,
    val confirmationNumber: String,
    val addToCalendarUrl: String? = null,
    val directionsUrl: String? = null,
)

@Serializable
data class CheckoutSummary(
    val activityTitle: String,
    val providerName: String,
    val sessionDate: String,
    val sessionTime: String,
    val sessionDuration: String,
    val childName: String,
    val subtotal: Double,
    val serviceFee: Double,
    val total: Double,
    val currency: String = "£",
)

@Serializable
data class PromoCode(
    val code: String,
    val discount: Double,
    val discountType: DiscountType,
    val isValid: Boolean,
)

@Serializable
enum class DiscountType {
    Percentage,
    FixedAmount
}
