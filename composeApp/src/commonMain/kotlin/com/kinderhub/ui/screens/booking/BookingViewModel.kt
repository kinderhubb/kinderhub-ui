package com.kinderhub.ui.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.data.model.ActivitySession
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.PaymentMethodType
import com.kinderhub.ui.data.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class BookingScreenState {
    Loading,
    Ready,
    Error
}

data class BookingUiState(
    val screenState: BookingScreenState = BookingScreenState.Loading,
    val activity: Activity? = null,
    val selectedSession: ActivitySession? = null,
    val selectedChild: Child? = null,
    val availableSessions: List<ActivitySession> = emptyList(),
    val eligibleChildren: List<Child> = emptyList(),
    val defaultPaymentMethod: PaymentMethod? = null,
    val canProceed: Boolean = false,
)

class BookingViewModel(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    // Mock data - in real app from user profile
    private val mockChildren = listOf(
        Child("child_001", "Ella", "2019-03-15", 5, AvatarColor.Accent),
        Child("child_002", "Max", "2016-07-22", 8, AvatarColor.Sunshine),
    )

    private val mockPaymentMethod = PaymentMethod(
        id = "pm_001",
        type = PaymentMethodType.ApplePay,
        isDefault = true
    )

    fun loadBookingData(activityId: String, sessionId: String?, childId: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = BookingScreenState.Loading) }

            try {
                val activity = activityRepository.getActivityById(activityId)

                if (activity != null) {
                    val availableSessions = activity.sessions.filter { it.spotsLeft > 0 }

                    // Filter children by age range
                    val eligibleChildren = mockChildren.filter { child ->
                        child.age >= activity.ageMin && child.age <= activity.ageMax
                    }

                    // Pre-select session if provided, else first available
                    val selectedSession = sessionId?.let { id ->
                        availableSessions.find { it.id == id }
                    } ?: availableSessions.firstOrNull()

                    // Pre-select child if provided, else first eligible
                    val selectedChild = childId?.let { id ->
                        eligibleChildren.find { it.id == id }
                    } ?: eligibleChildren.firstOrNull()

                    _uiState.update {
                        it.copy(
                            screenState = BookingScreenState.Ready,
                            activity = activity,
                            selectedSession = selectedSession,
                            selectedChild = selectedChild,
                            availableSessions = availableSessions,
                            eligibleChildren = eligibleChildren,
                            defaultPaymentMethod = mockPaymentMethod,
                            canProceed = selectedSession != null && selectedChild != null
                        )
                    }
                } else {
                    _uiState.update { it.copy(screenState = BookingScreenState.Error) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = BookingScreenState.Error) }
            }
        }
    }

    fun selectSession(session: ActivitySession) {
        _uiState.update {
            it.copy(
                selectedSession = session,
                canProceed = it.selectedChild != null
            )
        }
    }

    fun selectChild(child: Child) {
        _uiState.update {
            it.copy(
                selectedChild = child,
                canProceed = it.selectedSession != null
            )
        }
    }

    fun getBookingParams(): Triple<String, String, String>? {
        val state = _uiState.value
        val activityId = state.activity?.id ?: return null
        val sessionId = state.selectedSession?.id ?: return null
        val childId = state.selectedChild?.id ?: return null
        return Triple(activityId, sessionId, childId)
    }
}
