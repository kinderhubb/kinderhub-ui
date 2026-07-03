package com.kinderhub.ui.screens.activity

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

enum class ActivityDetailState {
    Loading,
    Loaded,
    Error
}

data class ActivityDetailUiState(
    val screenState: ActivityDetailState = ActivityDetailState.Loading,
    val activity: Activity? = null,

    // Booking
    val selectedSessionId: String? = null,
    val selectedChildId: String? = null,
    val children: List<Child> = emptyList(),

    // Express booking
    val hasExpressBooking: Boolean = false,
    val defaultPaymentMethod: PaymentMethod? = null,

    // UI
    val isFavorite: Boolean = false,
    val showAllSessions: Boolean = false,
    val showAskQuestion: Boolean = false,
)

class ActivityDetailViewModel(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityDetailUiState())
    val uiState: StateFlow<ActivityDetailUiState> = _uiState.asStateFlow()

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

    fun loadActivity(activityId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = ActivityDetailState.Loading) }

            try {
                val activity = activityRepository.getActivityById(activityId)

                if (activity != null) {
                    // Auto-select first available session
                    val firstAvailable = activity.sessions.firstOrNull { it.spotsLeft > 0 }

                    // Filter children by age range
                    val eligibleChildren = mockChildren.filter { child ->
                        child.age >= activity.ageMin && child.age <= activity.ageMax
                    }

                    // Auto-select first eligible child
                    val selectedChild = eligibleChildren.firstOrNull()

                    _uiState.update {
                        it.copy(
                            screenState = ActivityDetailState.Loaded,
                            activity = activity,
                            selectedSessionId = firstAvailable?.id,
                            selectedChildId = selectedChild?.id,
                            children = eligibleChildren,
                            hasExpressBooking = selectedChild != null && mockPaymentMethod != null,
                            defaultPaymentMethod = mockPaymentMethod
                        )
                    }
                } else {
                    _uiState.update { it.copy(screenState = ActivityDetailState.Error) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(screenState = ActivityDetailState.Error) }
            }
        }
    }

    fun selectSession(sessionId: String) {
        _uiState.update { it.copy(selectedSessionId = sessionId) }
    }

    fun selectChild(childId: String) {
        _uiState.update {
            it.copy(
                selectedChildId = childId,
                hasExpressBooking = it.defaultPaymentMethod != null
            )
        }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    fun toggleShowAllSessions() {
        _uiState.update { it.copy(showAllSessions = !it.showAllSessions) }
    }

    fun showAskQuestion() {
        _uiState.update { it.copy(showAskQuestion = true) }
    }

    fun hideAskQuestion() {
        _uiState.update { it.copy(showAskQuestion = false) }
    }

    fun getSelectedSession(): ActivitySession? {
        return _uiState.value.activity?.sessions?.find {
            it.id == _uiState.value.selectedSessionId
        }
    }

    fun getSelectedChild(): Child? {
        return _uiState.value.children.find {
            it.id == _uiState.value.selectedChildId
        }
    }

    fun canBook(): Boolean {
        val state = _uiState.value
        val session = getSelectedSession()
        return state.selectedChildId != null &&
                session != null &&
                session.spotsLeft > 0
    }
}
