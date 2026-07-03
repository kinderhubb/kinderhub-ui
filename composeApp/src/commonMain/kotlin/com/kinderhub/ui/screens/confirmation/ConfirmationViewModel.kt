package com.kinderhub.ui.screens.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Booking
import com.kinderhub.ui.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ConfirmationScreenState {
    Loading,
    Success,
    Error
}

data class ConfirmationUiState(
    val screenState: ConfirmationScreenState = ConfirmationScreenState.Loading,
    val booking: Booking? = null,
    val confirmationNumber: String? = null,
)

class ConfirmationViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmationUiState())
    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    fun loadBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = ConfirmationScreenState.Loading) }

            try {
                val booking = bookingRepository.getBookingById(bookingId)

                if (booking != null) {
                    // Generate confirmation number from booking ID
                    val confirmationNumber = "KH${bookingId.filter { it.isDigit() }.takeLast(8)}"

                    _uiState.update {
                        it.copy(
                            screenState = ConfirmationScreenState.Success,
                            booking = booking,
                            confirmationNumber = confirmationNumber
                        )
                    }
                } else {
                    _uiState.update { it.copy(screenState = ConfirmationScreenState.Error) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = ConfirmationScreenState.Error) }
            }
        }
    }

    fun addToCalendar() {
        // In real app, would open calendar intent or deep link
        // Platform-specific implementation via expect/actual
    }

    fun getDirections() {
        // In real app, would open maps app with directions
        // Platform-specific implementation via expect/actual
    }

    fun shareBooking() {
        // In real app, would open share sheet
        // Platform-specific implementation via expect/actual
    }
}
