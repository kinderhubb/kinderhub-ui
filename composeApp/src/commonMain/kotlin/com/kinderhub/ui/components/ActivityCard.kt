package com.kinderhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinderhub.ui.data.model.Activity
import com.kinderhub.ui.util.formatDistance
import com.kinderhub.ui.util.formatRating
import com.kinderhub.ui.data.model.CategoryColorTint
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space

@Composable
fun ActivityCard(
    activity: Activity,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    val placeholderColor = when (activity.category.colorTint) {
        CategoryColorTint.Primary -> c.p100 to c.p50
        CategoryColorTint.Success -> c.succ50 to c.succ50
        CategoryColorTint.Accent -> c.ac50 to c.ac100
        CategoryColorTint.Sunshine -> c.sun50 to c.sun100
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, Shapes.lg)
            .clip(Shapes.lg)
            .background(c.surface)
            .clickable { onClick() }
    ) {
        Column {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 100.dp else 150.dp)
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
                    color = c.p6.copy(alpha = 0.7f),
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.7f),
                            Shapes.sm
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )

                // AI Pick badge
                if (activity.isAiPick) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(Space.s12)
                            .background(
                                KhTheme.aiGradient(),
                                Shapes.pill
                            )
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconSparkles(color = Color.White, size = 12.dp)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "AI pick",
                                style = typography.small.copy(fontSize = 11.sp),
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
                                .padding(start = if (activity.isAiPick) 100.dp else Space.s12, top = Space.s12)
                                .background(
                                    if (spots == 0) c.errorBg else c.warn50,
                                    Shapes.pill
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (spots == 0) "Fully booked" else "$spots left",
                                style = typography.small.copy(fontSize = 11.sp),
                                color = if (spots == 0) c.error else c.warn,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Favorite button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Space.s12)
                        .size(34.dp)
                        .background(
                            Color.White.copy(alpha = 0.92f),
                            Shapes.pill
                        )
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    IconHeart(color = c.ac7, size = 18.dp)
                }
            }

            // Content
            Column(modifier = Modifier.padding(Space.s14)) {
                // Title with verified badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = activity.title,
                        style = typography.bodyLg,
                        color = c.tx,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (activity.isVerified) {
                        Spacer(modifier = Modifier.width(Space.s8))
                        IconBadgeCheck(color = c.p6, size = 17.dp)
                    }
                }

                Spacer(modifier = Modifier.height(Space.s4))

                // Meta info
                Text(
                    text = "${activity.nextSessionDay} ${activity.nextSessionTime} · ${formatDistance(activity.distance)} mi · Ages ${activity.ageMin}–${activity.ageMax}",
                    style = typography.small,
                    color = c.tx3b
                )

                Spacer(modifier = Modifier.height(Space.s12))

                // Rating and price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconStar(color = c.sun, filled = true, size = 15.dp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatRating(activity.rating),
                            style = typography.small,
                            color = c.tx2,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(${activity.reviewCount})",
                            style = typography.small,
                            color = c.tx3
                        )
                    }

                    // Price
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${activity.currency}${activity.pricePerSession.toInt()}",
                            style = typography.h3,
                            color = c.tx,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = " /session",
                            style = typography.small,
                            color = c.tx3
                        )
                    }
                }
            }
        }
    }
}
