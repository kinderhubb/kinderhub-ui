package com.kinderhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.data.model.AvatarColor
import com.kinderhub.ui.data.model.Child
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space

@Composable
fun ChildSelector(
    label: String = "All children",
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    Box(
        modifier = modifier
            .clip(Shapes.pill)
            .background(if (isSelected) c.p50 else c.surface)
            .border(1.dp, if (isSelected) c.pb else c.bd, Shapes.pill)
            .clickable { onClick() }
            .padding(start = 5.dp, end = 12.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // All avatar
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(Color(0xFFF3F4F6), Shapes.pill),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "All",
                    style = typography.small.copy(fontSize = 11.dp.value.sp),
                    color = c.tx3,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(Space.s8))

            Text(
                text = label,
                style = typography.small,
                color = if (isSelected) c.p7 else c.tx2,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun ChildSelector(
    child: Child,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val avatarColor = when (child.avatarColor) {
        AvatarColor.Accent -> c.ac
        AvatarColor.Sunshine -> c.sun
        AvatarColor.Primary -> c.p6
        AvatarColor.Success -> c.succ
    }

    val avatarTextColor = when (child.avatarColor) {
        AvatarColor.Accent -> Color.White
        AvatarColor.Sunshine -> Color(0xFF7C4A03) // Dark gold
        AvatarColor.Primary -> Color.White
        AvatarColor.Success -> Color.White
    }

    Box(
        modifier = modifier
            .clip(Shapes.pill)
            .background(if (isSelected) c.p50 else c.surface)
            .border(1.dp, if (isSelected) c.pb else c.bd, Shapes.pill)
            .clickable { onClick() }
            .padding(start = 5.dp, end = 12.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Child avatar
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(avatarColor, Shapes.pill),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = child.firstName.first().uppercase(),
                    style = typography.small.copy(fontSize = 11.dp.value.sp),
                    color = avatarTextColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(Space.s8))

            Text(
                text = "${child.firstName} · ${child.age}",
                style = typography.small,
                color = if (isSelected) c.p7 else c.tx2,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

private val Int.sp: androidx.compose.ui.unit.TextUnit
    get() = androidx.compose.ui.unit.TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)

private val Float.sp: androidx.compose.ui.unit.TextUnit
    get() = androidx.compose.ui.unit.TextUnit(this, androidx.compose.ui.unit.TextUnitType.Sp)
