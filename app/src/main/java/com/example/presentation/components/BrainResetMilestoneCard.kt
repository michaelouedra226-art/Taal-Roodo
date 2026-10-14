package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPrimary

data class Milestone(
    val dayTarget: Int,
    val title: String,
    val symbol: String, // Mooré concept / Symbol
    val neurobiology: String,
    val mooreWisdom: String
)

@Composable
fun BrainResetMilestoneCard(
    currentStreakDays: Int,
    modifier: Modifier = Modifier
) {
    val milestones = listOf(
        Milestone(
            dayTarget = 7,
            title = "J7 — La Graine (Bĩisim)",
            symbol = "🌱",
            neurobiology = "Fin du sevrage aigu. Baisse de la dopamine superficielle, retour de la lucidité.",
            mooreWisdom = "Bi yaa yĩmde : la petite graine d'effort s'enracine."
        ),
        Milestone(
            dayTarget = 30,
            title = "J30 — Le Bourgeon (Yĩmde)",
            symbol = "🌿",
            neurobiology = "Sensibilité des récepteurs D2 restaurée. Réduction de l'anxiété et du brouillard mental.",
            mooreWisdom = "Ned yaa a menga ninsaal : le cerveau retrouve sa souveraineté."
        ),
        Milestone(
            dayTarget = 90,
            title = "J90 — Le Grand Baobab (Kãenkãe)",
            symbol = "🌳",
            neurobiology = "Réinitialisation neurochimique complète. Le cortex préfrontal contrôle les pulsions.",
            mooreWisdom = "Kãenkãe pa zoet sãbo ye : inébranlable face à la tempête."
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurfaceElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Réinitialisation Cérébrale (TR-Path)",
                    style = MaterialTheme.typography.titleMedium,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Neuroplasticité et restauration dopaminergique",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(GoldPrimary.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "$currentStreakDays / 90 Jours",
                    color = AmberSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress bar to 90 days
        val progress = (currentStreakDays.toFloat() / 90f).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = GoldPrimary,
            trackColor = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.height(18.dp))

        milestones.forEach { milestone ->
            val isReached = currentStreakDays >= milestone.dayTarget

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isReached) EmeraldSuccess.copy(alpha = 0.2f)
                            else Color(0xFF262626)
                        )
                        .border(
                            1.dp,
                            if (isReached) EmeraldSuccess else Color.DarkGray,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isReached) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Atteint",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Verrouillé",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = milestone.symbol,
                            fontSize = 16.sp
                        )
                        Text(
                            text = milestone.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isReached) GoldPrimary else Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = milestone.neurobiology,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "« ${milestone.mooreWisdom} »",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberSecondary,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }
        }
    }
}
