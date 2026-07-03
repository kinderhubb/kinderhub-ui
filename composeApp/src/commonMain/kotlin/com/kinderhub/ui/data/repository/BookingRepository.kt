package com.kinderhub.ui.data.repository

import com.kinderhub.ui.data.model.Booking
import com.kinderhub.ui.data.model.BookingConfirmation
import com.kinderhub.ui.data.model.BookingRequest
import com.kinderhub.ui.data.model.BookingStatus
import com.kinderhub.ui.data.model.CheckoutSummary
import com.kinderhub.ui.data.model.PromoCode
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

interface BookingRepository {
    suspend fun createBooking(request: BookingRequest): Result<BookingConfirmation>
    suspend fun getBookingById(bookingId: String): Booking?
    suspend fun getUserBookings(): List<Booking>
    suspend fun cancelBooking(bookingId: String): Result<Unit>
    suspend fun getCheckoutSummary(
        activityId: String,
        sessionId: String,
        childId: String
    ): CheckoutSummary?
    suspend fun validatePromoCode(code: String): PromoCode?
}

class MockBookingRepository(
    private val activityRepository: ActivityRepository
) : BookingRepository {

    private val bookings = mutableListOf<Booking>()
    private var bookingCounter = 1000

    override suspend fun createBooking(request: BookingRequest): Result<BookingConfirmation> {
        delay(1500) // Simulate network

        val activity = activityRepository.getActivityById(request.activityId)
            ?: return Result.failure(Exception("Activity not found"))

        val session = activity.sessions.find { it.id == request.sessionId }
            ?: return Result.failure(Exception("Session not found"))

        // Mock child name lookup
        val childName = when (request.childId) {
            "child_001" -> "Ella"
            "child_002" -> "Max"
            else -> "Child"
        }

        val bookingId = "BKG${++bookingCounter}"
        val confirmationNumber = "KH${Clock.System.now().toEpochMilliseconds().toString().takeLast(8)}"

        val booking = Booking(
            id = bookingId,
            activityId = activity.id,
            activityTitle = activity.title,
            providerName = activity.providerName,
            sessionId = session.id,
            childId = request.childId,
            childName = childName,
            sessionDate = "Saturday, 15 Feb",
            sessionTime = session.time,
            sessionDuration = session.duration,
            status = BookingStatus.Confirmed,
            price = session.pricePerSession,
            paymentMethodId = request.paymentMethodId,
            createdAt = "2025-02-15T09:00:00Z",
            locationAddress = "123 Pool Lane, London SW1 1AA"
        )

        bookings.add(booking)

        return Result.success(
            BookingConfirmation(
                booking = booking,
                confirmationNumber = confirmationNumber,
                addToCalendarUrl = "https://calendar.google.com/event?...",
                directionsUrl = "https://maps.google.com/?q=123+Pool+Lane+London"
            )
        )
    }

    override suspend fun getBookingById(bookingId: String): Booking? {
        delay(300)
        return bookings.find { it.id == bookingId } ?: getMockBookings().find { it.id == bookingId }
    }

    override suspend fun getUserBookings(): List<Booking> {
        delay(500)
        return bookings + getMockBookings()
    }

    override suspend fun cancelBooking(bookingId: String): Result<Unit> {
        delay(800)
        val index = bookings.indexOfFirst { it.id == bookingId }
        if (index >= 0) {
            bookings[index] = bookings[index].copy(status = BookingStatus.Cancelled)
            return Result.success(Unit)
        }
        return Result.failure(Exception("Booking not found"))
    }

    override suspend fun getCheckoutSummary(
        activityId: String,
        sessionId: String,
        childId: String
    ): CheckoutSummary? {
        delay(300)

        val activity = activityRepository.getActivityById(activityId) ?: return null
        val session = activity.sessions.find { it.id == sessionId } ?: return null

        val childName = when (childId) {
            "child_001" -> "Ella"
            "child_002" -> "Max"
            else -> "Child"
        }

        val subtotal = session.pricePerSession
        val serviceFee = subtotal * 0.05 // 5% service fee

        return CheckoutSummary(
            activityTitle = activity.title,
            providerName = activity.providerName,
            sessionDate = "Saturday, 15 Feb",
            sessionTime = session.time,
            sessionDuration = session.duration,
            childName = childName,
            subtotal = subtotal,
            serviceFee = serviceFee,
            total = subtotal + serviceFee
        )
    }

    override suspend fun validatePromoCode(code: String): PromoCode? {
        delay(500)
        return when (code.uppercase()) {
            "FIRST10" -> PromoCode(
                code = "FIRST10",
                discount = 10.0,
                discountType = com.kinderhub.ui.data.model.DiscountType.Percentage,
                isValid = true
            )
            "SAVE5" -> PromoCode(
                code = "SAVE5",
                discount = 5.0,
                discountType = com.kinderhub.ui.data.model.DiscountType.FixedAmount,
                isValid = true
            )
            else -> null
        }
    }

    private fun getMockBookings(): List<Booking> = listOf(
        Booking(
            id = "BKG001",
            activityId = "act_001",
            activityTitle = "Little Swimmers – Beginner",
            providerName = "AquaKids Academy",
            sessionId = "sess_001",
            childId = "child_001",
            childName = "Ella",
            sessionDate = "Saturday, 22 Feb",
            sessionTime = "9:00 AM",
            sessionDuration = "45 mins",
            status = BookingStatus.Confirmed,
            price = 18.0,
            createdAt = "2025-02-10T10:00:00Z",
            locationAddress = "123 Pool Lane, London SW1 1AA"
        ),
        Booking(
            id = "BKG002",
            activityId = "act_002",
            activityTitle = "Mini Scientists Lab",
            providerName = "STEAM Kids London",
            sessionId = "sess_002",
            childId = "child_002",
            childName = "Max",
            sessionDate = "Sunday, 23 Feb",
            sessionTime = "10:00 AM",
            sessionDuration = "1 hour",
            status = BookingStatus.Confirmed,
            price = 22.0,
            createdAt = "2025-02-08T14:30:00Z",
            locationAddress = "45 Science Road, London E1 2AB"
        ),
        Booking(
            id = "BKG003",
            activityId = "act_003",
            activityTitle = "Football Skills Camp",
            providerName = "Junior Football Academy",
            sessionId = "sess_003",
            childId = "child_002",
            childName = "Max",
            sessionDate = "Saturday, 8 Feb",
            sessionTime = "2:00 PM",
            sessionDuration = "1.5 hours",
            status = BookingStatus.Completed,
            price = 15.0,
            createdAt = "2025-01-25T09:00:00Z",
            locationAddress = "Sports Park, London N1 3CD"
        )
    )
}
