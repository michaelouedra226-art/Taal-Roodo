package com.example.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldPrimary

@Composable
fun BreathingCircle(
    modifier: Modifier = Modifier,
    cycleDurationMs: Int = 10000 // 5s in, 5s out
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing_transition")

    // Progress from 0f (empty) to 1f (full) and back to 0f
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = cycleDurationMs / 2, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_scale"
    )

    val isInhaling = breathScale >= 0.77f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurfaceElevated)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cohérence Cardiaque — Respiration 5s/5s",
            style = MaterialTheme.typography.titleMedium,
            color = GoldPrimary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Régule ton système nerveux et élimine le pic d'urgence",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .testTag("breathing_circle_container"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.minDimension / 2f * 0.85f

                // Outer ambient aura
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GoldPrimary.copy(alpha = 0.25f * breathScale),
                            AmberSecondary.copy(alpha = 0.08f * breathScale),
                            Color.Transparent
                        ),
                        center = center,
                        radius = baseRadius * breathScale * 1.15f
                    ),
                    radius = baseRadius * breathScale * 1.15f,
                    center = center
                )

                // Background track circle
                drawCircle(
                    color = Color.DarkGray.copy(alpha = 0.35f),
                    radius = baseRadius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Expanding breathing pulse circle
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GoldPrimary.copy(alpha = 0.85f),
                            AmberSecondary.copy(alpha = 0.65f),
                            Color(0x33FFB300)
                        ),
                        center = center,
                        radius = baseRadius * breathScale
                    ),
                    radius = baseRadius * breathScale,
                    center = center
                )

                // Accent contour ring
                drawCircle(
                    color = GoldPrimary,
                    radius = baseRadius * breathScale,
                    center = center,
                    style = Stroke(width = 2.5.dp.toPx())
                )
            }

            // Phase and timer text in center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isInhaling) "INSPIRER" else "EXPIRER",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = if (isInhaling) "Remplis tes poumons..." else "Relâche la pression...",
                    fontSize = 11.sp,
                    color = Color(0xFF1E1E1E),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(GoldPrimary.copy(alpha = 0.15f))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Cycle régulier : 6 respirations / minute",
                style = MaterialTheme.typography.labelSmall,
                color = AmberSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
