package com.example.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.GoldPrimary

@Composable
fun GoldenStreakFlame(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flame_flicker")

    val flicker by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale"
    )

    val innerSway by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_sway"
    )

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(GoldPrimary.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.75f)) {
            val w = this.size.width
            val h = this.size.height

            // Outer golden glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GoldPrimary.copy(alpha = 0.35f), Color.Transparent),
                    center = Offset(w / 2, h * 0.65f),
                    radius = w * 0.6f * flicker
                )
            )

            // Outer flame teardrop path
            val outerFlame = Path().apply {
                moveTo(w * 0.5f + innerSway, 0f)
                cubicTo(
                    w * 0.85f, h * 0.35f,
                    w * 0.95f, h * 0.75f,
                    w * 0.5f, h
                )
                cubicTo(
                    w * 0.05f, h * 0.75f,
                    w * 0.15f, h * 0.35f,
                    w * 0.5f + innerSway, 0f
                )
                close()
            }

            drawPath(
                path = outerFlame,
                brush = Brush.verticalGradient(
                    colors = listOf(GoldPrimary, AmberSecondary, Color(0xFFFF6D00))
                )
            )

            // Inner intense white-gold core
            val innerFlame = Path().apply {
                moveTo(w * 0.5f, h * 0.28f)
                cubicTo(
                    w * 0.75f, h * 0.5f,
                    w * 0.8f, h * 0.85f,
                    w * 0.5f, h * 0.95f
                )
                cubicTo(
                    w * 0.2f, h * 0.85f,
                    w * 0.25f, h * 0.5f,
                    w * 0.5f, h * 0.28f
                )
                close()
            }

            drawPath(
                path = innerFlame,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, GoldPrimary)
                )
            )
        }
    }
}
