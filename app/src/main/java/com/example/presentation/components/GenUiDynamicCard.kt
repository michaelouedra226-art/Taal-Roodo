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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DynamicCardType
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldPrimary

@Composable
fun GenUiDynamicCard(
    type: DynamicCardType,
    onActionTriggered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (type == DynamicCardType.NONE) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceElevated)
            .border(1.5.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("genui_dynamic_card")
    ) {
        when (type) {
            DynamicCardType.BREATHING -> {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Air,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Intervention Rapide — Respiration Calibrée",
                            style = MaterialTheme.typography.titleSmall,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "5 secondes d'inspiration, 5 secondes d'expiration. Réduit immédiatement l'urgence dopaminergique.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Button(
                        onClick = { onActionTriggered("OPEN_BREATHING") },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("genui_action_breathing")
                    ) {
                        Text("Démarrer la Cohérence Cardiaque", fontWeight = FontWeight.Bold)
                    }
                }
            }

            DynamicCardType.PHYSICAL_CHALLENGE -> {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = AmberSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Redirection Physique Immédiate",
                            style = MaterialTheme.typography.titleSmall,
                            color = AmberSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Fais 20 pompes maintenant ou bois un grand verre d'eau fraîche pour briser la fixation visuelle.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Button(
                        onClick = { onActionTriggered("COMPLETE_PHYSICAL") },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberSecondary, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Défi Relevé (+15 Clarté)", fontWeight = FontWeight.Bold)
                    }
                }
            }

            DynamicCardType.MOORE_PROVERB -> {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ancrage Culturel & Sagesse Mooré",
                            style = MaterialTheme.typography.titleSmall,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "« Ned yaa a menga ninsaal. »\nChacun est l'artisan de sa propre dignité et de sa victoire.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    OutlinedButton(
                        onClick = { onActionTriggered("REFLECT_PROVERB") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Méditer cette parole", color = GoldPrimary)
                    }
                }
            }

            DynamicCardType.FORTRESS_PROPOSAL -> {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Activer le Mode Forteresse (TR-Shield)",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFFFF5252),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Verrouille les navigateurs et le filtrage VPN strict avec code PIN administrateur.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Button(
                        onClick = { onActionTriggered("ACTIVATE_FORTRESS") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252), contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Activer le Verrouillage Forteresse", fontWeight = FontWeight.Bold)
                    }
                }
            }

            else -> {}
        }
    }
}
