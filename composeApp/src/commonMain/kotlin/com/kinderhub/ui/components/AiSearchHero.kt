package com.kinderhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kinderhub.ui.theme.KhTheme
import com.kinderhub.ui.theme.Shapes
import com.kinderhub.ui.theme.Space

@Composable
fun AiSearchHero(
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = KhTheme.colors
    val typography = KhTheme.typography

    // Outer gradient border
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.lg)
            .background(KhTheme.aiGradient())
            .padding(2.dp)
    ) {
        // Inner white content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(c.surface)
                .clickable { onClick() }
                .padding(horizontal = Space.s14, vertical = Space.s14)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconSparkles(color = c.p6, size = 20.dp)

                Spacer(modifier = Modifier.width(Space.s12))

                Text(
                    text = placeholder,
                    style = typography.small.copy(lineHeight = typography.body.lineHeight),
                    color = c.tx3,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(Space.s12))

                // Mic button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(c.p50),
                    contentAlignment = Alignment.Center
                ) {
                    IconMic(color = c.p6, size = 19.dp)
                }
            }
        }
    }
}
