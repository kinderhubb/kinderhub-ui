package com.kinderhub.ui.screens.payment

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.components.IconApple
import com.kinderhub.ui.components.IconArrowLeft
import com.kinderhub.ui.components.IconCreditCard
import com.kinderhub.ui.components.KhButton
import com.kinderhub.ui.components.KhButtonVariant
import com.kinderhub.ui.data.model.PaymentMethod
import com.kinderhub.ui.data.model.PaymentMethodType
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space

@Composable
fun EditPaymentScreen(
    paymentMethod: PaymentMethod?,
    onSetDefault: (String) -> Unit,
    onDelete: (String) -> Unit,
    onBack: () -> Unit
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    var isDefault by remember(paymentMethod) { mutableStateOf(paymentMethod?.isDefault ?: false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (paymentMethod == null) {
        // No payment method found
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(c.bg),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Payment method not found",
                    style = typography.body,
                    color = c.tx2
                )
                Spacer(modifier = Modifier.height(Space.s16))
                KhButton(
                    text = "Go Back",
                    onClick = onBack,
                    variant = KhButtonVariant.Secondary
                )
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
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
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    IconArrowLeft(color = c.tx, size = 24.dp)
                }

                Spacer(modifier = Modifier.width(Space.s12))

                Text(
                    text = "Payment Method",
                    style = typography.h2,
                    color = c.tx,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(Space.s24))

            // Payment method card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.surface)
                    .padding(Space.screenPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(Shapes.md)
                            .background(c.bg),
                        contentAlignment = Alignment.Center
                    ) {
                        when (paymentMethod.type) {
                            PaymentMethodType.ApplePay -> IconApple(color = c.tx, size = 28.dp)
                            else -> IconCreditCard(color = c.tx, size = 28.dp)
                        }
                    }

                    Spacer(modifier = Modifier.width(Space.s16))

                    // Details
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (paymentMethod.type) {
                                PaymentMethodType.ApplePay -> "Apple Pay"
                                PaymentMethodType.GooglePay -> "Google Pay"
                                PaymentMethodType.Card -> "${paymentMethod.brand ?: "Card"}"
                            },
                            style = typography.h3,
                            color = c.tx,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (paymentMethod.type == PaymentMethodType.Card && paymentMethod.last4 != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "•••• •••• •••• ${paymentMethod.last4}",
                                style = typography.body,
                                color = c.tx2
                            )
                        }

                        if (paymentMethod.isDefault) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Default payment method",
                                style = typography.small,
                                color = c.p6,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Space.s24))

            // Settings
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.surface)
            ) {
                // Set as default toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!isDefault) {
                                isDefault = true
                                onSetDefault(paymentMethod.id)
                            }
                        }
                        .padding(horizontal = Space.screenPadding, vertical = Space.s16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Set as default",
                            style = typography.body,
                            color = c.tx,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Use this for all future payments",
                            style = typography.small,
                            color = c.tx2
                        )
                    }

                    Switch(
                        checked = isDefault,
                        onCheckedChange = {
                            if (it && !isDefault) {
                                isDefault = true
                                onSetDefault(paymentMethod.id)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = c.surface,
                            checkedTrackColor = c.p6,
                            uncheckedThumbColor = c.surface,
                            uncheckedTrackColor = c.tx3
                        )
                    )
                }

                HorizontalDivider(color = c.bd)

                // Payment type info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Space.screenPadding, vertical = Space.s16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Type",
                        style = typography.body,
                        color = c.tx2
                    )
                    Text(
                        text = when (paymentMethod.type) {
                            PaymentMethodType.ApplePay -> "Digital Wallet"
                            PaymentMethodType.GooglePay -> "Digital Wallet"
                            PaymentMethodType.Card -> "Credit/Debit Card"
                        },
                        style = typography.body,
                        color = c.tx,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (paymentMethod.brand != null) {
                    HorizontalDivider(color = c.bd)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Space.screenPadding, vertical = Space.s16),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Card brand",
                            style = typography.body,
                            color = c.tx2
                        )
                        Text(
                            text = paymentMethod.brand,
                            style = typography.body,
                            color = c.tx,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Space.s24))

            // Delete button
            if (!showDeleteConfirm) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Space.screenPadding)
                ) {
                    KhButton(
                        text = "Remove Payment Method",
                        onClick = { showDeleteConfirm = true },
                        variant = KhButtonVariant.Destructive,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Space.screenPadding)
                        .clip(Shapes.lg)
                        .background(c.error.copy(alpha = 0.1f))
                        .padding(Space.s16)
                ) {
                    Text(
                        text = "Are you sure you want to remove this payment method?",
                        style = typography.body,
                        color = c.error,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(Space.s12))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Space.s12)
                    ) {
                        KhButton(
                            text = "Cancel",
                            onClick = { showDeleteConfirm = false },
                            variant = KhButtonVariant.Secondary,
                            modifier = Modifier.weight(1f)
                        )
                        KhButton(
                            text = "Remove",
                            onClick = { onDelete(paymentMethod.id) },
                            variant = KhButtonVariant.Destructive,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Space.s40))
        }
    }
}
