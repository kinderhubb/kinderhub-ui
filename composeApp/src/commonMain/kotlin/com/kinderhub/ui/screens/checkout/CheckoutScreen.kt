package com.kinderhub.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconApple
import com.kinderhub.ui.util.formatPrice
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.components.IconCalendar
import com.kinderhub.ui.components.IconClock
import com.kinderhub.ui.components.IconCreditCard
import com.kinderhub.ui.components.IconShield
import com.kinderhub.ui.components.IconTag
import com.kinderhub.ui.components.IconUsers
import com.kinderhub.ui.components.IconX
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.data.model.CheckoutSummary
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.PaymentMethodType
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space
import com.kinderhub.ui.util.Strings
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CheckoutScreen(
    activityId: String,
    sessionId: String,
    childId: String,
    onPaymentSuccess: (bookingId: String) -> Unit,
    onBack: () -> Unit,
    viewModel: CheckoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val strings = Strings.current

    LaunchedEffect(activityId, sessionId, childId) {
        viewModel.loadCheckout(activityId, sessionId, childId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        // Header
        CheckoutHeader(title = strings.bookingCheckout, onBack = onBack)

        when (uiState.screenState) {
            CheckoutScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = c.p6)
                }
            }
            CheckoutScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.errorMessage ?: strings.commonError,
                            color = c.tx2
                        )
                        Spacer(modifier = Modifier.height(Space.s16))
                        KhButton(
                            text = strings.commonGoBack,
                            onClick = onBack,
                            variant = KhButtonVariant.Secondary
                        )
                    }
                }
            }
            CheckoutScreenState.Processing -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = c.p6)
                        Spacer(modifier = Modifier.height(Space.s16))
                        Text(
                            text = strings.bookingProcessing,
                            style = typography.body,
                            color = c.tx2
                        )
                    }
                }
            }
            CheckoutScreenState.Ready -> {
                CheckoutContent(
                    uiState = uiState,
                    onPaymentMethodSelect = viewModel::selectPaymentMethod,
                    onPromoCodeChange = viewModel::updatePromoCode,
                    onApplyPromo = viewModel::applyPromoCode,
                    onRemovePromo = viewModel::removePromoCode,
                    onPay = { viewModel.processPayment(onPaymentSuccess) }
                )
            }
        }
    }
}

@Composable
private fun CheckoutHeader(
    title: String,
    onBack: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(horizontal = Space.screenPadding)
            .padding(top = Space.s48, bottom = Space.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(c.bg)
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            IconArrowLeft(color = c.tx, size = 20.dp)
        }

        Spacer(modifier = Modifier.width(Space.s16))

        Text(
            text = title,
            style = typography.h2,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CheckoutContent(
    uiState: CheckoutUiState,
    onPaymentMethodSelect: (PaymentMethod) -> Unit,
    onPromoCodeChange: (String) -> Unit,
    onApplyPromo: () -> Unit,
    onRemovePromo: () -> Unit,
    onPay: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    val summary = uiState.summary ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Booking summary
        BookingSummaryCard(summary = summary)

        Spacer(modifier = Modifier.height(Space.s20))

        // Payment methods
        PaymentMethodsSection(
            methods = uiState.paymentMethods,
            selectedMethod = uiState.selectedPaymentMethod,
            onSelect = onPaymentMethodSelect
        )

        Spacer(modifier = Modifier.height(Space.s20))

        // Promo code
        PromoCodeSection(
            code = uiState.promoCode,
            appliedPromo = uiState.appliedPromo,
            error = uiState.promoError,
            isApplying = uiState.isApplyingPromo,
            onCodeChange = onPromoCodeChange,
            onApply = onApplyPromo,
            onRemove = onRemovePromo
        )

        Spacer(modifier = Modifier.height(Space.s20))

        // Price breakdown
        PriceBreakdown(
            subtotal = summary.subtotal,
            serviceFee = summary.serviceFee,
            discount = uiState.discount,
            total = uiState.totalAfterDiscount,
            currency = summary.currency
        )

        Spacer(modifier = Modifier.height(Space.s24))

        // Pay button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.surface)
                .padding(Space.screenPadding)
        ) {
            Column {
                KhButton(
                    text = "Pay ${summary.currency}${formatPrice(uiState.totalAfterDiscount)}",
                    onClick = onPay,
                    modifier = Modifier.fillMaxWidth(),
                    variant = KhButtonVariant.Wallet
                )

                Spacer(modifier = Modifier.height(Space.s12))

                // Security note
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconShield(color = c.tx3, size = 14.dp)
                    Spacer(modifier = Modifier.width(Space.s6))
                    Text(
                        text = "Secure payment powered by Stripe",
                        style = typography.xs,
                        color = c.tx3
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Space.s24))
    }
}

@Composable
private fun BookingSummaryCard(summary: CheckoutSummary) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(Space.screenPadding)
    ) {
        Text(
            text = summary.activityTitle,
            style = typography.h3,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s4))

        Text(
            text = summary.providerName,
            style = typography.small,
            color = c.tx2
        )

        Spacer(modifier = Modifier.height(Space.s16))

        HorizontalDivider(color = c.bd, thickness = 1.dp)

        Spacer(modifier = Modifier.height(Space.s16))

        // Session details
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCalendar(color = c.p6, size = 16.dp)
            Spacer(modifier = Modifier.width(Space.s10))
            Text(
                text = summary.sessionDate,
                style = typography.body,
                color = c.tx
            )
        }

        Spacer(modifier = Modifier.height(Space.s10))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconClock(color = c.p6, size = 16.dp)
            Spacer(modifier = Modifier.width(Space.s10))
            Text(
                text = "${summary.sessionTime} · ${summary.sessionDuration}",
                style = typography.body,
                color = c.tx
            )
        }

        Spacer(modifier = Modifier.height(Space.s10))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconUsers(color = c.p6, size = 16.dp)
            Spacer(modifier = Modifier.width(Space.s10))
            Text(
                text = summary.childName,
                style = typography.body,
                color = c.tx
            )
        }
    }
}

@Composable
private fun PaymentMethodsSection(
    methods: List<PaymentMethod>,
    selectedMethod: PaymentMethod?,
    onSelect: (PaymentMethod) -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screenPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCreditCard(color = c.p6, size = 20.dp)
            Spacer(modifier = Modifier.width(Space.s10))
            Text(
                text = "Payment Method",
                style = typography.h3,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(Space.s12))

        Column(verticalArrangement = Arrangement.spacedBy(Space.s10)) {
            methods.forEach { method ->
                PaymentMethodCard(
                    method = method,
                    isSelected = method.id == selectedMethod?.id,
                    onClick = { onSelect(method) }
                )
            }
        }
    }
}

@Composable
private fun PaymentMethodCard(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val bgColor = if (isSelected) c.p50 else c.surface
    val borderColor = if (isSelected) c.pb else c.bd

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.lg)
            .background(bgColor)
            .border(1.5.dp, borderColor, Shapes.lg)
            .clickable { onClick() }
            .padding(Space.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(Shapes.md)
                .background(c.bg),
            contentAlignment = Alignment.Center
        ) {
            when (method.type) {
                PaymentMethodType.ApplePay -> IconApple(color = c.tx, size = 20.dp)
                PaymentMethodType.GooglePay -> IconCreditCard(color = c.tx, size = 20.dp)
                PaymentMethodType.Card -> IconCreditCard(color = c.tx, size = 20.dp)
            }
        }

        Spacer(modifier = Modifier.width(Space.s14))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = when (method.type) {
                    PaymentMethodType.ApplePay -> "Apple Pay"
                    PaymentMethodType.GooglePay -> "Google Pay"
                    PaymentMethodType.Card -> "${method.brand} •••• ${method.last4}"
                },
                style = typography.body,
                color = c.tx,
                fontWeight = FontWeight.Medium
            )
            if (method.isDefault) {
                Text(
                    text = "Default",
                    style = typography.xs,
                    color = c.tx3
                )
            }
        }

        // Selection indicator
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (isSelected) c.p6 else Color.Transparent)
                .border(2.dp, if (isSelected) c.p6 else c.bd, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
    }
}

@Composable
private fun PromoCodeSection(
    code: String,
    appliedPromo: com.kinderhub.ui.data.model.PromoCode?,
    error: String?,
    isApplying: Boolean,
    onCodeChange: (String) -> Unit,
    onApply: () -> Unit,
    onRemove: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.screenPadding)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconTag(color = c.p6, size = 20.dp)
            Spacer(modifier = Modifier.width(Space.s10))
            Text(
                text = "Promo Code",
                style = typography.h3,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(Space.s12))

        if (appliedPromo != null) {
            // Applied promo display
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(Shapes.lg)
                    .background(c.succ.copy(alpha = 0.1f))
                    .border(1.dp, c.succ.copy(alpha = 0.3f), Shapes.lg)
                    .padding(Space.s14),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = appliedPromo.code,
                        style = typography.body,
                        color = c.succ,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(Space.s8))
                    Text(
                        text = "Applied",
                        style = typography.small,
                        color = c.succ
                    )
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    IconX(color = c.succ, size = 16.dp)
                }
            }
        } else {
            // Input field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(Shapes.lg)
                        .background(c.surface)
                        .border(1.dp, if (error != null) c.error else c.bd, Shapes.lg)
                        .padding(Space.s14)
                ) {
                    if (code.isEmpty()) {
                        Text(
                            text = "Enter code",
                            style = typography.body,
                            color = c.tx3
                        )
                    }
                    BasicTextField(
                        value = code,
                        onValueChange = onCodeChange,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = typography.body.copy(color = c.tx),
                        cursorBrush = SolidColor(c.p6),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.width(Space.s12))

                KhButton(
                    text = if (isApplying) "..." else "Apply",
                    onClick = onApply,
                    variant = KhButtonVariant.Secondary,
                    enabled = code.isNotBlank() && !isApplying
                )
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(Space.s8))
                Text(
                    text = error,
                    style = typography.small,
                    color = c.error
                )
            }
        }
    }
}

@Composable
private fun PriceBreakdown(
    subtotal: Double,
    serviceFee: Double,
    discount: Double,
    total: Double,
    currency: String
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(c.surface)
            .padding(Space.screenPadding)
    ) {
        Text(
            text = "Price Details",
            style = typography.h3,
            color = c.tx,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Space.s16))

        PriceRow("Session", "$currency${formatPrice(subtotal)}")
        Spacer(modifier = Modifier.height(Space.s10))
        PriceRow("Service fee", "$currency${formatPrice(serviceFee)}")

        if (discount > 0) {
            Spacer(modifier = Modifier.height(Space.s10))
            PriceRow(
                label = "Discount",
                value = "-$currency${formatPrice(discount)}",
                valueColor = KhTheme.colors.succ
            )
        }

        Spacer(modifier = Modifier.height(Space.s16))
        HorizontalDivider(color = c.bd, thickness = 1.dp)
        Spacer(modifier = Modifier.height(Space.s16))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = typography.body,
                color = c.tx,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$currency${formatPrice(total)}",
                style = typography.h3,
                color = c.tx,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
    valueColor: Color = KhTheme.colors.tx2
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = typography.body,
            color = c.tx2
        )
        Text(
            text = value,
            style = typography.body,
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
    }
}
