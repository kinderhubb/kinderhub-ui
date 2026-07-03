package com.kinderhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.data.model.Category
import com.kinderhub.ui.data.model.CategoryColorTint
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Space

@Composable
fun CategoryTile(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val (bgColor, iconColor) = when (category.colorTint) {
        CategoryColorTint.Primary -> c.p50 to c.p6
        CategoryColorTint.Success -> c.succ50 to c.succ
        CategoryColorTint.Accent -> c.ac50 to c.ac7
        CategoryColorTint.Sunshine -> c.sun50 to c.warn
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(60.dp)
            .clickable { onClick() }
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            CategoryIcon(iconName = category.icon, color = iconColor)
        }

        Spacer(modifier = Modifier.height(Space.s8))

        Text(
            text = category.name,
            style = typography.small,
            color = c.tx2,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CategoryIcon(
    iconName: String,
    color: Color,
    size: androidx.compose.ui.unit.Dp = 26.dp
) {
    when (iconName) {
        "waves" -> IconWaves(color = color, size = size)
        "flask-conical" -> IconFlask(color = color, size = size)
        "trophy" -> IconTrophy(color = color, size = size)
        "music" -> IconMusic(color = color, size = size)
        "graduation-cap" -> IconGraduationCap(color = color, size = size)
        else -> IconSparkles(color = color, size = size)
    }
}

// Additional category icons

@Composable
fun IconWaves(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(size)) {
        val scale = size.toPx() / 24f
        val stroke = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 2f * scale,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
            join = androidx.compose.ui.graphics.StrokeJoin.Round
        )

        // Wave 1
        val path1 = androidx.compose.ui.graphics.Path().apply {
            moveTo(2f * scale, 6f * scale)
            cubicTo(4f * scale, 4f * scale, 6f * scale, 8f * scale, 8f * scale, 6f * scale)
            cubicTo(10f * scale, 4f * scale, 12f * scale, 8f * scale, 14f * scale, 6f * scale)
            cubicTo(16f * scale, 4f * scale, 18f * scale, 8f * scale, 20f * scale, 6f * scale)
            cubicTo(22f * scale, 4f * scale, 22f * scale, 6f * scale, 22f * scale, 6f * scale)
        }
        drawPath(path1, color, style = stroke)

        // Wave 2
        val path2 = androidx.compose.ui.graphics.Path().apply {
            moveTo(2f * scale, 12f * scale)
            cubicTo(4f * scale, 10f * scale, 6f * scale, 14f * scale, 8f * scale, 12f * scale)
            cubicTo(10f * scale, 10f * scale, 12f * scale, 14f * scale, 14f * scale, 12f * scale)
            cubicTo(16f * scale, 10f * scale, 18f * scale, 14f * scale, 20f * scale, 12f * scale)
            cubicTo(22f * scale, 10f * scale, 22f * scale, 12f * scale, 22f * scale, 12f * scale)
        }
        drawPath(path2, color, style = stroke)

        // Wave 3
        val path3 = androidx.compose.ui.graphics.Path().apply {
            moveTo(2f * scale, 18f * scale)
            cubicTo(4f * scale, 16f * scale, 6f * scale, 20f * scale, 8f * scale, 18f * scale)
            cubicTo(10f * scale, 16f * scale, 12f * scale, 20f * scale, 14f * scale, 18f * scale)
            cubicTo(16f * scale, 16f * scale, 18f * scale, 20f * scale, 20f * scale, 18f * scale)
            cubicTo(22f * scale, 16f * scale, 22f * scale, 18f * scale, 22f * scale, 18f * scale)
        }
        drawPath(path3, color, style = stroke)
    }
}

@Composable
fun IconFlask(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(size)) {
        val scale = size.toPx() / 24f
        val stroke = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 2f * scale,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
            join = androidx.compose.ui.graphics.StrokeJoin.Round
        )

        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(9f * scale, 2f * scale)
            lineTo(15f * scale, 2f * scale)
            moveTo(10f * scale, 2f * scale)
            lineTo(10f * scale, 8f * scale)
            lineTo(4f * scale, 18f * scale)
            cubicTo(3f * scale, 20f * scale, 4f * scale, 22f * scale, 6f * scale, 22f * scale)
            lineTo(18f * scale, 22f * scale)
            cubicTo(20f * scale, 22f * scale, 21f * scale, 20f * scale, 20f * scale, 18f * scale)
            lineTo(14f * scale, 8f * scale)
            lineTo(14f * scale, 2f * scale)
        }
        drawPath(path, color, style = stroke)
    }
}

@Composable
fun IconTrophy(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(size)) {
        val scale = size.toPx() / 24f
        val stroke = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 2f * scale,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
            join = androidx.compose.ui.graphics.StrokeJoin.Round
        )

        // Cup body
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(6f * scale, 3f * scale)
            lineTo(6f * scale, 9f * scale)
            cubicTo(6f * scale, 13f * scale, 9f * scale, 15f * scale, 12f * scale, 15f * scale)
            cubicTo(15f * scale, 15f * scale, 18f * scale, 13f * scale, 18f * scale, 9f * scale)
            lineTo(18f * scale, 3f * scale)
            close()
        }
        drawPath(path, color, style = stroke)

        // Handles
        val leftHandle = androidx.compose.ui.graphics.Path().apply {
            moveTo(6f * scale, 5f * scale)
            cubicTo(3f * scale, 5f * scale, 2f * scale, 7f * scale, 2f * scale, 9f * scale)
            cubicTo(2f * scale, 11f * scale, 4f * scale, 12f * scale, 6f * scale, 11f * scale)
        }
        drawPath(leftHandle, color, style = stroke)

        val rightHandle = androidx.compose.ui.graphics.Path().apply {
            moveTo(18f * scale, 5f * scale)
            cubicTo(21f * scale, 5f * scale, 22f * scale, 7f * scale, 22f * scale, 9f * scale)
            cubicTo(22f * scale, 11f * scale, 20f * scale, 12f * scale, 18f * scale, 11f * scale)
        }
        drawPath(rightHandle, color, style = stroke)

        // Stand
        drawLine(color, androidx.compose.ui.geometry.Offset(12f * scale, 15f * scale), androidx.compose.ui.geometry.Offset(12f * scale, 18f * scale), strokeWidth = 2f * scale, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        drawLine(color, androidx.compose.ui.geometry.Offset(8f * scale, 21f * scale), androidx.compose.ui.geometry.Offset(16f * scale, 21f * scale), strokeWidth = 2f * scale, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        drawLine(color, androidx.compose.ui.geometry.Offset(12f * scale, 18f * scale), androidx.compose.ui.geometry.Offset(12f * scale, 21f * scale), strokeWidth = 2f * scale, cap = androidx.compose.ui.graphics.StrokeCap.Round)
    }
}

@Composable
fun IconMusic(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(size)) {
        val scale = size.toPx() / 24f
        val stroke = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 2f * scale,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
            join = androidx.compose.ui.graphics.StrokeJoin.Round
        )

        // Notes
        drawCircle(color, 3f * scale, androidx.compose.ui.geometry.Offset(6f * scale, 18f * scale), style = stroke)
        drawCircle(color, 3f * scale, androidx.compose.ui.geometry.Offset(18f * scale, 16f * scale), style = stroke)

        // Stems
        drawLine(color, androidx.compose.ui.geometry.Offset(9f * scale, 18f * scale), androidx.compose.ui.geometry.Offset(9f * scale, 5f * scale), strokeWidth = 2f * scale, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        drawLine(color, androidx.compose.ui.geometry.Offset(21f * scale, 16f * scale), androidx.compose.ui.geometry.Offset(21f * scale, 3f * scale), strokeWidth = 2f * scale, cap = androidx.compose.ui.graphics.StrokeCap.Round)

        // Beam
        drawLine(color, androidx.compose.ui.geometry.Offset(9f * scale, 5f * scale), androidx.compose.ui.geometry.Offset(21f * scale, 3f * scale), strokeWidth = 2f * scale, cap = androidx.compose.ui.graphics.StrokeCap.Round)
    }
}

@Composable
fun IconGraduationCap(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: androidx.compose.ui.unit.Dp = 24.dp
) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(size)) {
        val scale = size.toPx() / 24f
        val stroke = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 2f * scale,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
            join = androidx.compose.ui.graphics.StrokeJoin.Round
        )

        // Cap
        val capPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(12f * scale, 3f * scale)
            lineTo(22f * scale, 8f * scale)
            lineTo(12f * scale, 13f * scale)
            lineTo(2f * scale, 8f * scale)
            close()
        }
        drawPath(capPath, color, style = stroke)

        // Left side
        val leftPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(6f * scale, 10f * scale)
            lineTo(6f * scale, 16f * scale)
            cubicTo(6f * scale, 18f * scale, 9f * scale, 20f * scale, 12f * scale, 20f * scale)
            cubicTo(15f * scale, 20f * scale, 18f * scale, 18f * scale, 18f * scale, 16f * scale)
            lineTo(18f * scale, 10f * scale)
        }
        drawPath(leftPath, color, style = stroke)

        // Tassel
        drawLine(color, androidx.compose.ui.geometry.Offset(22f * scale, 8f * scale), androidx.compose.ui.geometry.Offset(22f * scale, 14f * scale), strokeWidth = 2f * scale, cap = androidx.compose.ui.graphics.StrokeCap.Round)
    }
}
