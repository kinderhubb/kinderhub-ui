package com.kinderhub.ui.screens.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Booking
import com.kinderhub.ui.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class BookingDetailScreenState {
    Loading,
    Success,
    Error
}

data class BookingDetailUiState(
    val screenState: BookingDetailScreenState = BookingDetailScreenState.Loading,
    val booking: Booking? = null,
)

class BookingDetailViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingDetailUiState())
    val uiState: StateFlow<BookingDetailUiState> = _uiState.asStateFlow()

    private var currentBookingId: String? = null

    fun loadBooking(bookingId: String) {
        currentBookingId = bookingId

        viewModelScope.launch {
            _uiState.update { it.copy(screenState = BookingDetailScreenState.Loading) }

            try {
                val booking = bookingRepository.getBookingById(bookingId)

                if (booking != null) {
                    _uiState.update {
                        it.copy(
                            screenState = BookingDetailScreenState.Success,
                            booking = booking
                        )
                    }
                } else {
                    _uiState.update { it.copy(screenState = BookingDetailScreenState.Error) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = BookingDetailScreenState.Error) }
            }
        }
    }

    fun cancelBooking() {
        val bookingId = currentBookingId ?: return

        viewModelScope.launch {
            bookingRepository.cancelBooking(bookingId)
        }
    }

    fun getDirections() {
        // Platform-specific implementation via expect/actual
    }

    fun addToCalendar() {
        // Platform-specific implementation via expect/actual
    }

    fun shareBooking() {
        // Platform-specific implementation via expect/actual
    }
}
