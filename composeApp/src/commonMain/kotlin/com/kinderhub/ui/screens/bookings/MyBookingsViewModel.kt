package com.kinderhub.ui.screens.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Booking
import com.kinderhub.ui.data.model.BookingStatus
import com.kinderhub.ui.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class BookingsScreenState {
    Loading,
    Success,
    Empty,
    Error
}

enum class BookingsTab {
    Upcoming,
    Past
}

data class MyBookingsUiState(
    val screenState: BookingsScreenState = BookingsScreenState.Loading,
    val selectedTab: BookingsTab = BookingsTab.Upcoming,
    val upcomingBookings: List<Booking> = emptyList(),
    val pastBookings: List<Booking> = emptyList(),
)

class MyBookingsViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyBookingsUiState())
    val uiState: StateFlow<MyBookingsUiState> = _uiState.asStateFlow()

    fun loadBookings() {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = BookingsScreenState.Loading) }

            try {
                val allBookings = bookingRepository.getUserBookings()

                val upcoming = allBookings.filter { booking ->
                    booking.status == BookingStatus.Confirmed || booking.status == BookingStatus.Pending
                }

                val past = allBookings.filter { booking ->
                    booking.status == BookingStatus.Completed || booking.status == BookingStatus.Cancelled
                }

                val state = if (upcoming.isEmpty() && past.isEmpty()) {
                    BookingsScreenState.Empty
                } else {
                    BookingsScreenState.Success
                }

                _uiState.update {
                    it.copy(
                        screenState = state,
                        upcomingBookings = upcoming,
                        pastBookings = past
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = BookingsScreenState.Error) }
            }
        }
    }

    fun selectTab(tab: BookingsTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun getCurrentBookings(): List<Booking> {
        return when (_uiState.value.selectedTab) {
            BookingsTab.Upcoming -> _uiState.value.upcomingBookings
            BookingsTab.Past -> _uiState.value.pastBookings
        }
    }
}
