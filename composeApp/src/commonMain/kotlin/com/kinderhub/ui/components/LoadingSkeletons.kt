package com.kinderhub.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space

@Composable
fun LoadingSkeletons(
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors

    val shimmerColors = listOf(
        c.bd.copy(alpha = 0.6f),
        c.bd.copy(alpha = 0.2f),
        c.bd.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(c.bg)
            .padding(horizontal = Space.screenPadding)
            .padding(top = Space.s48)
    ) {
        // Header skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                SkeletonBox(brush, Modifier.size(190.dp, 24.dp))
                Spacer(modifier = Modifier.height(Space.s8))
                SkeletonBox(brush, Modifier.size(120.dp, 14.dp))
            }
            SkeletonBox(brush, Modifier.size(44.dp).clip(Shapes.pill))
        }

        Spacer(modifier = Modifier.height(Space.s14))

        // Child selector skeletons
        Row(horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
            SkeletonBox(brush, Modifier.size(100.dp, 36.dp).clip(Shapes.pill))
            SkeletonBox(brush, Modifier.size(78.dp, 36.dp).clip(Shapes.pill))
            SkeletonBox(brush, Modifier.size(78.dp, 36.dp).clip(Shapes.pill))
        }

        Spacer(modifier = Modifier.height(Space.s20))

        // Search skeleton with gradient border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(Shapes.lg)
                .background(KhTheme.aiGradient())
                .padding(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                    .background(c.surface)
                    .padding(Space.s14)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconSparkles(color = c.p6, size = 20.dp)
                    Spacer(modifier = Modifier.width(Space.s12))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Space.s8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Animated dots
                        repeat(3) { index ->
                            val dotTransition = rememberInfiniteTransition()
                            val dotAlpha by dotTransition.animateFloat(
                                initialValue = 0.3f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(500, delayMillis = index * 150),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )
                            val dotColor = when (index) {
                                0 -> c.p6
                                1 -> c.sun
                                else -> c.ac
                            }
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(dotColor.copy(alpha = dotAlpha), Shapes.pill)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Space.s18))

        // Category skeletons
        Row(horizontalArrangement = Arrangement.spacedBy(Space.s16)) {
            repeat(4) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SkeletonBox(brush, Modifier.size(56.dp).clip(RoundedCornerShape(18.dp)))
                    Spacer(modifier = Modifier.height(Space.s8))
                    SkeletonBox(brush, Modifier.size(44.dp, 11.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(Space.s22))

        // Header skeleton
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonBox(brush, Modifier.size(130.dp, 20.dp))
            SkeletonBox(brush, Modifier.size(74.dp, 34.dp).clip(Shapes.md))
        }

        Spacer(modifier = Modifier.height(Space.s14))

        // Card skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.lg)
                .background(c.surface)
        ) {
            Column {
                SkeletonBox(brush, Modifier.fillMaxWidth().height(150.dp))
                Column(modifier = Modifier.padding(Space.s14)) {
                    SkeletonBox(brush, Modifier.size(200.dp, 18.dp))
                    Spacer(modifier = Modifier.height(Space.s10))
                    SkeletonBox(brush, Modifier.size(150.dp, 13.dp))
                    Spacer(modifier = Modifier.height(Space.s14))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SkeletonBox(brush, Modifier.size(64.dp, 14.dp))
                        SkeletonBox(brush, Modifier.size(54.dp, 18.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Space.s14))

        // Second card skeleton (partial)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.lg)
                .background(c.surface)
        ) {
            SkeletonBox(brush, Modifier.fillMaxWidth().height(96.dp))
        }
    }
}

@Composable
private fun SkeletonBox(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(Shapes.sm)
            .background(brush)
    )
}
