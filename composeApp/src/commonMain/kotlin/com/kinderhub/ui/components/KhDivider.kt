package com.kinderhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Space

@Composable
fun KhDivider(
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(c.bd)
    )
}

@Composable
fun KhDividerWithText(
    text: String,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(c.bd)
        )
        Text(
            text = text,
            style = typography.small,
            color = c.tx3,
            modifier = Modifier.padding(horizontal = Space.s16)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(c.bd)
        )
    }
}
