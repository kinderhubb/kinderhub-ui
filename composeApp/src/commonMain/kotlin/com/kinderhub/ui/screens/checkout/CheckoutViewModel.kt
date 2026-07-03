package com.kinderhub.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinderhub.ui.data.model.BookingConfirmation
import com.kinderhub.ui.data.model.BookingRequest
import com.kinderhub.ui.data.model.CheckoutSummary
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.PaymentMethodType
import com.kinderhub.ui.data.model.PromoCode
import com.kinderhub.ui.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class CheckoutScreenState {
    Loading,
    Ready,
    Processing,
    Error
}

data class CheckoutUiState(
    val screenState: CheckoutScreenState = CheckoutScreenState.Loading,
    val summary: CheckoutSummary? = null,
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val selectedPaymentMethod: PaymentMethod? = null,
    val promoCode: String = "",
    val appliedPromo: PromoCode? = null,
    val promoError: String? = null,
    val isApplyingPromo: Boolean = false,
    val totalAfterDiscount: Double = 0.0,
    val discount: Double = 0.0,
    val errorMessage: String? = null,
)

class CheckoutViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private var bookingParams: Triple<String, String, String>? = null

    // Mock payment methods - in real app from user profile
    private val mockPaymentMethods = listOf(
        PaymentMethod(
            id = "pm_apple",
            type = PaymentMethodType.ApplePay,
            isDefault = true
        ),
        PaymentMethod(
            id = "pm_visa",
            type = PaymentMethodType.Card,
            last4 = "4242",
            brand = "Visa",
            isDefault = false
        ),
        PaymentMethod(
            id = "pm_mc",
            type = PaymentMethodType.Card,
            last4 = "8888",
            brand = "Mastercard",
            isDefault = false
        )
    )

    fun loadCheckout(activityId: String, sessionId: String, childId: String) {
        bookingParams = Triple(activityId, sessionId, childId)

        viewModelScope.launch {
            _uiState.update { it.copy(screenState = CheckoutScreenState.Loading) }

            try {
                val summary = bookingRepository.getCheckoutSummary(activityId, sessionId, childId)

                if (summary != null) {
                    val defaultPayment = mockPaymentMethods.find { it.isDefault }
                        ?: mockPaymentMethods.firstOrNull()

                    _uiState.update {
                        it.copy(
                            screenState = CheckoutScreenState.Ready,
                            summary = summary,
                            paymentMethods = mockPaymentMethods,
                            selectedPaymentMethod = defaultPayment,
                            totalAfterDiscount = summary.total
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            screenState = CheckoutScreenState.Error,
                            errorMessage = "Unable to load checkout details"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        screenState = CheckoutScreenState.Error,
                        errorMessage = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun updatePromoCode(code: String) {
        _uiState.update {
            it.copy(
                promoCode = code,
                promoError = null
            )
        }
    }

    fun applyPromoCode() {
        val code = _uiState.value.promoCode.trim()
        if (code.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isApplyingPromo = true, promoError = null) }

            val promo = bookingRepository.validatePromoCode(code)

            if (promo != null && promo.isValid) {
                val summary = _uiState.value.summary ?: return@launch
                val discount = calculateDiscount(promo, summary.total)
                val newTotal = summary.total - discount

                _uiState.update {
                    it.copy(
                        appliedPromo = promo,
                        discount = discount,
                        totalAfterDiscount = newTotal.coerceAtLeast(0.0),
                        isApplyingPromo = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        promoError = "Invalid promo code",
                        isApplyingPromo = false
                    )
                }
            }
        }
    }

    fun removePromoCode() {
        val summary = _uiState.value.summary ?: return
        _uiState.update {
            it.copy(
                appliedPromo = null,
                promoCode = "",
                discount = 0.0,
                totalAfterDiscount = summary.total,
                promoError = null
            )
        }
    }

    fun processPayment(onSuccess: (bookingId: String) -> Unit) {
        val params = bookingParams ?: return
        val paymentMethod = _uiState.value.selectedPaymentMethod ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(screenState = CheckoutScreenState.Processing) }

            val request = BookingRequest(
                activityId = params.first,
                sessionId = params.second,
                childId = params.third,
                paymentMethodId = paymentMethod.id
            )

            val result = bookingRepository.createBooking(request)

            result.fold(
                onSuccess = { confirmation ->
                    onSuccess(confirmation.booking.id)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            screenState = CheckoutScreenState.Ready,
                            errorMessage = error.message ?: "Payment failed"
                        )
                    }
                }
            )
        }
    }

    private fun calculateDiscount(promo: PromoCode, total: Double): Double {
        return when (promo.discountType) {
            com.kinderhub.ui.data.model.DiscountType.Percentage -> total * (promo.discount / 100)
            com.kinderhub.ui.data.model.DiscountType.FixedAmount -> promo.discount
        }
    }

    fun canProceed(): Boolean {
        val state = _uiState.value
        return state.screenState == CheckoutScreenState.Ready &&
                state.selectedPaymentMethod != null &&
                state.summary != null
    }
}
