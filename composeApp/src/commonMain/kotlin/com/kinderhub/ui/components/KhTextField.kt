package com.kinderhub.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Size
import com.kinderhub.ui.theme.Space

@Composable
fun KhTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        when {
            isError -> c.error
            isFocused -> c.p5
            else -> c.bd
        }
    )

    val borderWidth = if (isFocused || isError) 2.dp else 1.dp

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = typography.label,
                color = c.tx2,
                modifier = Modifier.padding(bottom = Space.s8)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Size.inputHeight)
                .clip(Shapes.md)
                .background(c.surface)
                .border(borderWidth, borderColor, Shapes.md)
                .then(
                    if (isFocused && !isError) {
                        Modifier.border(
                            width = 4.dp,
                            color = c.p5.copy(alpha = 0.14f),
                            shape = Shapes.md
                        )
                    } else Modifier
                )
                .padding(horizontal = Space.s14),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isFocused) c.p6 else c.tx3,
                        modifier = Modifier.size(Size.iconMd)
                    )
                    Spacer(modifier = Modifier.width(Space.s10))
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = typography.body,
                            color = c.tx3
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { isFocused = it.isFocused },
                        textStyle = typography.body.copy(color = c.tx),
                        cursorBrush = SolidColor(c.p6),
                        singleLine = singleLine,
                        enabled = enabled,
                        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                    )
                }

                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(Space.s10))
                    trailingIcon()
                }
            }
        }

        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(Space.s8))
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Alert triangle icon placeholder
                Text(
                    text = "⚠ $errorMessage",
                    style = typography.small,
                    color = c.error
                )
            }
        }
    }
}
