package com.example.presentation.panic

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.domain.CulturalWisdom
import com.example.presentation.components.BreathingCircle
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldPrimary

@Composable
fun PanicDialog(
    onDismiss: () -> Unit,
    emergencyContactName: String,
    emergencyContactPhone: String,
    onUrgeResisted: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(1) } // 1: Respiration, 2: Physique, 3: IA, 4: SOS
    val context = LocalContext.current
    val randomProverb = remember { CulturalWisdom.getRandomProverb() }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp))
                .background(DarkBackground)
                .border(2.dp, CrimsonAlert, RoundedCornerShape(24.dp))
                .padding(20.dp)
                .testTag("panic_dialog_modal")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with alert badge & close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CrimsonAlert.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = CrimsonAlert,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PROTOCOLE D'URGENCE",
                                style = MaterialTheme.typography.titleMedium,
                                color = CrimsonAlert,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Étape $currentStep sur 4 — Redirection",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_panic_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = Color.LightGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Steps switcher
                AnimatedContent(
                    targetState = currentStep,
                    label = "panic_steps_anim"
                ) { step ->
                    when (step) {
                        1 -> {
                            // Step 1: Coherence cardiaque (5s / 5s)
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                BreathingCircle(
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { currentStep = 2 },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldPrimary,
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("panic_next_step_1")
                                ) {
                                    Text("J'ai respiré — Étape Suivante (Action Physique)", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        2 -> {
                            // Step 2: Redirection physique immédiate
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.FitnessCenter,
                                                contentDescription = null,
                                                tint = AmberSecondary,
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Action Physique Choc",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = AmberSecondary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "1. Fais 20 pompes immédiatement ou 30 squats sur place.\n\n2. Va te laver le visage à l'eau froide pour briser l'état hypnotique de l'écran.\n\n3. Bois un grand verre d'eau en pleine conscience.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { currentStep = 3 },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AmberSecondary,
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("panic_next_step_2")
                                ) {
                                    Text("Action effectuée — Soutien Mental Agnes", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        3 -> {
                            // Step 3: Message Agnes & Sagesse Mooré
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Psychology,
                                                contentDescription = null,
                                                tint = GoldPrimary,
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Sagesse & Ancrage Culturel",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = GoldPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "« ${randomProverb.moore} »",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = GoldPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = randomProverb.french,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Message d'Agnes : Ne sacrifie pas ton futur pour 5 secondes d'illusion artificielle. Tu es plus fort que ce réflexe.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { currentStep = 4 },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Appeler Partenaire", color = CrimsonAlert)
                                    }
                                    Button(
                                        onClick = {
                                            onUrgeResisted()
                                            onDismiss()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = GoldPrimary,
                                            contentColor = Color.Black
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("panic_victory_button")
                                    ) {
                                        Text("Victoire (+1 Victoire)", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        4 -> {
                            // Step 4: SOS Partenaire de Responsabilité
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = null,
                                                tint = CrimsonAlert,
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Partenaire de Responsabilité",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = CrimsonAlert,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        val hasContact = emergencyContactPhone.isNotBlank()
                                        Text(
                                            text = if (hasContact) "Contact : $emergencyContactName ($emergencyContactPhone)"
                                            else "Aucun contact SOS pré-défini. Le bouton ci-dessous ouvrira directement le clavier d'appel de votre téléphone.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Le secret est l'oxygène de la dépendance. Brise le secret en appelant un confident.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.LightGray,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        val uri = if (emergencyContactPhone.isNotBlank()) {
                                            Uri.parse("tel:$emergencyContactPhone")
                                        } else {
                                            Uri.parse("tel:")
                                        }
                                        val intent = Intent(Intent.ACTION_DIAL, uri)
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CrimsonAlert,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        if (emergencyContactPhone.isNotBlank()) "Appeler $emergencyContactName"
                                        else "Ouvrir l'application Téléphone d'urgence",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        onUrgeResisted()
                                        onDismiss()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("L'envie est passée, fermer", color = GoldPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
