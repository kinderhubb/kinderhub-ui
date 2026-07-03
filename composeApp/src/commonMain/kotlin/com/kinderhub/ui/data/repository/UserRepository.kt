package com.kinderhub.ui.data.repository

import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.PaymentMethodType
import com.kinderhub.ui.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface UserRepository {
    val user: StateFlow<User?>
    val children: StateFlow<List<Child>>
    val paymentMethods: StateFlow<List<PaymentMethod>>

    fun getChildById(childId: String): Child?
    fun updateChild(child: Child)
    fun deleteChild(childId: String)
    fun addChild(child: Child)

    fun getPaymentMethodById(paymentId: String): PaymentMethod?
    fun setDefaultPaymentMethod(paymentId: String)
    fun deletePaymentMethod(paymentId: String)
    fun addPaymentMethod(paymentMethod: PaymentMethod)
}

class MockUserRepository : UserRepository {
    private val _user = MutableStateFlow<User?>(null)
    override val user: StateFlow<User?> = _user.asStateFlow()

    private val _children = MutableStateFlow<List<Child>>(emptyList())
    override val children: StateFlow<List<Child>> = _children.asStateFlow()

    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    override val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods.asStateFlow()

    init {
        // Initialize with mock data
        val mockUser = User(
            id = "user_001",
            email = "parent@example.com",
            firstName = "Sarah",
            lastName = "Johnson",
            isEmailVerified = true
        )

        val mockChildren = listOf(
            Child("child_001", "Ella", "2021-03-15", 5, AvatarColor.Accent),
            Child("child_002", "Max", "2018-07-22", 8, AvatarColor.Sunshine)
        )

        val mockPaymentMethods = listOf(
            PaymentMethod(
                id = "pm_apple",
                type = PaymentMethodType.ApplePay,
                isDefault = true
            ),
            PaymentMethod(
                id = "pm_visa",
                type = PaymentMethodType.Card,
                last4 = "4242",
                brand = "Visa"
            )
        )

        _user.value = mockUser
        _children.value = mockChildren
        _paymentMethods.value = mockPaymentMethods
    }

    override fun getChildById(childId: String): Child? {
        return _children.value.find { it.id == childId }
    }

    override fun updateChild(child: Child) {
        _children.update { currentList ->
            currentList.map { if (it.id == child.id) child else it }
        }
    }

    override fun deleteChild(childId: String) {
        _children.update { currentList ->
            currentList.filter { it.id != childId }
        }
    }

    override fun addChild(child: Child) {
        _children.update { it + child }
    }

    override fun getPaymentMethodById(paymentId: String): PaymentMethod? {
        return _paymentMethods.value.find { it.id == paymentId }
    }

    override fun setDefaultPaymentMethod(paymentId: String) {
        _paymentMethods.update { currentList ->
            currentList.map { it.copy(isDefault = it.id == paymentId) }
        }
    }

    override fun deletePaymentMethod(paymentId: String) {
        _paymentMethods.update { currentList ->
            currentList.filter { it.id != paymentId }
        }
    }

    override fun addPaymentMethod(paymentMethod: PaymentMethod) {
        _paymentMethods.update { it + paymentMethod }
    }
}
