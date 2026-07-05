package com.kinderhub.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.data.model.CategoryColorTint
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.util.formatDistance
import com.kinderhub.ui.util.formatRating

@Composable
fun ActivityCard(
    activity: Activity,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes
    val motion = KhTheme.motion
    val cardStyle = KhTheme.componentStyles.cardStyle

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Theme-aware press animation
    val scale by animateFloatAsState(
        targetValue = if (isPressed && motion.enableAnimations) 0.98f else 1f,
        animationSpec = if (motion.useSpringAnimations) {
            spring(stiffness = motion.springStiffness, dampingRatio = motion.springDamping / 100f)
        } else {
            tween(durationMillis = motion.durationFast)
        }
    )

    // Category-based placeholder colors
    val placeholderColor = when (activity.category.colorTint) {
        CategoryColorTint.Primary -> colors.p100 to colors.p50
        CategoryColorTint.Success -> colors.succ50 to colors.succ50
        CategoryColorTint.Accent -> colors.ac50 to colors.ac100
        CategoryColorTint.Sunshine -> colors.sun50 to colors.sun100
    }

    // Theme-aware image height
    val imageHeight = if (isCompact) {
        dimensions.cardImageHeight * 0.6f
    } else {
        dimensions.cardImageHeight
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .then(
                if (cardStyle.useShadow) {
                    Modifier.shadow(
                        elevation = dimensions.cardElevation,
                        shape = shapes.cardShape,
                        clip = false
                    )
                } else Modifier
            )
            .clip(shapes.cardShape)
            .background(colors.surface)
            .then(
                if (cardStyle.useBorder) {
                    Modifier.border(
                        width = dimensions.borderWidth,
                        color = colors.bd,
                        shape = shapes.cardShape
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Column {
            // Image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(shapes.imageShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(placeholderColor.first, placeholderColor.second),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder text
                Text(
                    text = activity.imagePlaceholder,
                    style = typography.small.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    ),
                    color = colors.p6.copy(alpha = 0.7f),
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.7f),
                            shapes.chipShape
                        )
                        .padding(horizontal = dimensions.spacingSm, vertical = dimensions.spacingXs)
                )

                // Overlay gradient (if theme uses it)
                if (cardStyle.useOverlay) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                }

                // AI Pick badge
                if (activity.isAiPick) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(dimensions.spacingMd)
                            .background(
                                KhTheme.aiGradient(),
                                shapes.chipShape
                            )
                            .padding(
                                horizontal = dimensions.spacingSm,
                                vertical = dimensions.spacingXs
                            )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconSparkles(color = Color.White, size = dimensions.iconSizeSm)
                            Spacer(modifier = Modifier.width(dimensions.spacingXs))
                            Text(
                                text = "AI pick",
                                style = typography.xs,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Spots badge
                activity.spotsLeft?.let { spots ->
                    if (spots <= 3) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(
                                    start = if (activity.isAiPick) {
                                        dimensions.spacingMd + 85.dp
                                    } else dimensions.spacingMd,
                                    top = dimensions.spacingMd
                                )
                                .background(
                                    if (spots == 0) colors.errorBg else colors.warn50,
                                    shapes.chipShape
                                )
                                .padding(
                                    horizontal = dimensions.spacingSm,
                                    vertical = dimensions.spacingXs
                                )
                        ) {
                            Text(
                                text = if (spots == 0) "Fully booked" else "$spots left",
                                style = typography.xs,
                                color = if (spots == 0) colors.error else colors.warn,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Favorite button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(dimensions.spacingMd)
                        .size(dimensions.touchTarget * 0.8f)
                        .background(
                            Color.White.copy(alpha = 0.92f),
                            shapes.avatarShape
                        )
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    IconHeart(
                        color = colors.ac7,
                        size = dimensions.iconSizeMd
                    )
                }
            }

            // Content area
            Column(
                modifier = Modifier.padding(dimensions.cardPadding)
            ) {
                // Title with verified badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = activity.title,
                        style = typography.bodyLg,
                        color = colors.tx,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (activity.isVerified) {
                        Spacer(modifier = Modifier.width(dimensions.spacingSm))
                        IconBadgeCheck(
                            color = colors.p6,
                            size = dimensions.iconSizeMd
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensions.spacingXs))

                // Meta info
                Text(
                    text = "${activity.nextSessionDay} ${activity.nextSessionTime} · ${formatDistance(activity.distance)} mi · Ages ${activity.ageMin}–${activity.ageMax}",
                    style = typography.small,
                    color = colors.tx3b
                )

                Spacer(modifier = Modifier.height(cardStyle.contentSpacing))

                // Rating and price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconStar(
                            color = colors.sun,
                            filled = true,
                            size = dimensions.iconSizeSm
                        )
                        Spacer(modifier = Modifier.width(dimensions.spacingXs))
                        Text(
                            text = formatRating(activity.rating),
                            style = typography.small,
                            color = colors.tx2,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(dimensions.spacingXs))
                        Text(
                            text = "(${activity.reviewCount})",
                            style = typography.small,
                            color = colors.tx3
                        )
                    }

                    // Price
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${activity.currency}${activity.pricePerSession.toInt()}",
                            style = typography.h3,
                            color = colors.tx,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " /session",
                            style = typography.small,
                            color = colors.tx3
                        )
                    }
                }
            }
        }
    }
}

/**
 * Horizontal compact activity card for lists
 */
@Composable
fun ActivityCardHorizontal(
    activity: Activity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = KhTheme.colors
    val typography = KhTheme.typography
    val dimensions = KhTheme.dimensions
    val shapes = KhTheme.shapes
    val cardStyle = KhTheme.componentStyles.cardStyle

    val placeholderColor = when (activity.category.colorTint) {
        CategoryColorTint.Primary -> colors.p100 to colors.p50
        CategoryColorTint.Success -> colors.succ50 to colors.succ50
        CategoryColorTint.Accent -> colors.ac50 to colors.ac100
        CategoryColorTint.Sunshine -> colors.sun50 to colors.sun100
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (cardStyle.useShadow) {
                    Modifier.shadow(dimensions.cardElevation / 2, shapes.cardShape)
                } else Modifier
            )
            .clip(shapes.cardShape)
            .background(colors.surface)
            .then(
                if (cardStyle.useBorder) {
                    Modifier.border(dimensions.borderWidth, colors.bd, shapes.cardShape)
                } else Modifier
            )
            .clickable { onClick() }
            .padding(dimensions.spacingSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(dimensions.avatarLg)
                .clip(shapes.imageShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(placeholderColor.first, placeholderColor.second)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = activity.category.icon,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(dimensions.spacingMd))

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                style = typography.body,
                color = colors.tx,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(dimensions.spacingXs))
            Text(
                text = "${activity.nextSessionDay} · ${formatDistance(activity.distance)} mi",
                style = typography.small,
                color = colors.tx3
            )
        }

        // Price
        Text(
            text = "${activity.currency}${activity.pricePerSession.toInt()}",
            style = typography.h3,
            color = colors.p6,
            fontWeight = FontWeight.Bold
        )
    }
}
