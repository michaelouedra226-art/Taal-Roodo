package com.example.presentation.path

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TriggerEntry
import com.example.data.model.UserProfile
import com.example.domain.PatternInsight
import com.example.domain.PredictivePatternAnalyzer
import com.example.domain.RiskLevel
import com.example.presentation.components.BrainResetMilestoneCard
import com.example.presentation.components.GoldenStreakFlame
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PathScreen(
    userProfile: UserProfile,
    triggers: List<TriggerEntry>,
    onLogTrigger: (mood: String, stress: Int, location: String, notes: String, resisted: Boolean) -> Unit,
    onResetStreak: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var isJournalFormOpen by remember { mutableStateOf(false) }

    // Form inputs
    var selectedMood by remember { mutableStateOf("Stressé") }
    var stressSlider by remember { mutableFloatStateOf(6f) }
    var selectedLocation by remember { mutableStateOf("Chambre (Seul)") }
    var triggerNotes by remember { mutableStateOf("") }
    var resistedUrge by remember { mutableStateOf(true) }

    val moodOptions = listOf("Stressé", "Ennuyé", "Fatigué", "Solitaire", "Anxieux", "Calme")
    val locationOptions = listOf("Chambre (Seul)", "Lit avant sommeil", "Salon", "Transports", "Bureau")

    val patternInsight = remember(triggers) {
        PredictivePatternAnalyzer.analyze(triggers)
    }

    // Mental clarity score calculation (based on streak + triggers resisted)
    val baseScore = (userProfile.streakDays * 3 + 45).coerceIn(10, 100)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Clarity Score & Streak Overview Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(22.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GoldenStreakFlame(size = 40.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${userProfile.streakDays} Jours de Victoire",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                                Text(
                                    text = "Record : ${userProfile.bestStreakDays} jours consécutifs",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.LightGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Score de Clarté Mentale",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Dopamine naturelle stabilisée et cortex préfrontal réactif.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Circular progress score
                    Box(
                        modifier = Modifier.size(96.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val progressFraction = baseScore / 100f

                        Canvas(modifier = Modifier.size(96.dp)) {
                            drawCircle(
                                color = Color(0xFF262626),
                                style = Stroke(width = 8.dp.toPx())
                            )
                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(AmberSecondary, GoldPrimary, EmeraldSuccess)
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * progressFraction,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$baseScore%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = "Clarté",
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        }

        // Brain Reset Neurobiology Roadmap
        item {
            BrainResetMilestoneCard(currentStreakDays = userProfile.streakDays)
        }

        // Predictive Pattern Analyzer Banner
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when (patternInsight.riskLevel) {
                        RiskLevel.HIGH -> Color(0xFF2A1010)
                        RiskLevel.MODERATE -> Color(0xFF2A200A)
                        RiskLevel.LOW -> DarkSurfaceElevated
                    }
                ),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        when (patternInsight.riskLevel) {
                            RiskLevel.HIGH -> CrimsonAlert
                            RiskLevel.MODERATE -> AmberSecondary
                            RiskLevel.LOW -> DarkBorder
                        },
                        RoundedCornerShape(18.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (patternInsight.riskLevel) {
                                RiskLevel.HIGH -> Icons.Default.Warning
                                RiskLevel.MODERATE -> Icons.Default.Lightbulb
                                RiskLevel.LOW -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            tint = when (patternInsight.riskLevel) {
                                RiskLevel.HIGH -> CrimsonAlert
                                RiskLevel.MODERATE -> AmberSecondary
                                RiskLevel.LOW -> EmeraldSuccess
                            },
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = patternInsight.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = when (patternInsight.riskLevel) {
                                RiskLevel.HIGH -> CrimsonAlert
                                RiskLevel.MODERATE -> AmberSecondary
                                RiskLevel.LOW -> GoldPrimary
                            }
                        )
                    }

                    Text(
                        text = patternInsight.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = "Conseil de protection : ${patternInsight.recommendation}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Trigger Journal Form Toggle
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Journal des Déclencheurs (Patterns)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Button(
                    onClick = { isJournalFormOpen = !isJournalFormOpen },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.testTag("toggle_journal_form_button")
                ) {
                    Icon(
                        imageVector = if (isJournalFormOpen) Icons.Default.History else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isJournalFormOpen) "Fermer" else "Noter un déclencheur", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Expandable Trigger Form
        if (isJournalFormOpen) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Enregistrer une envie / situation à risque",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("État émotionnel :", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            moodOptions.take(3).forEach { mood ->
                                val selected = selectedMood == mood
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (selected) GoldPrimary else Color(0xFF242424))
                                        .clickable { selectedMood = mood }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = mood,
                                        color = if (selected) Color.Black else Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Niveau de stress / envie : ${stressSlider.toInt()} / 10",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Slider(
                            value = stressSlider,
                            onValueChange = { stressSlider = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = GoldPrimary,
                                activeTrackColor = GoldPrimary,
                                inactiveTrackColor = DarkBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Lieu / Contexte :", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            locationOptions.take(2).forEach { loc ->
                                val selected = selectedLocation == loc
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (selected) AmberSecondary else Color(0xFF242424))
                                        .clickable { selectedLocation = loc }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = loc,
                                        color = if (selected) Color.Black else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = triggerNotes,
                            onValueChange = { triggerNotes = it },
                            placeholder = { Text("Notes personnelles (pensée, déclencheur spécifique)...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1B1B1B),
                                unfocusedContainerColor = Color(0xFF1B1B1B),
                                focusedIndicatorColor = GoldPrimary,
                                unfocusedIndicatorColor = DarkBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (resistedUrge) "J'ai résisté avec succès !" else "Rechute constatée",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (resistedUrge) EmeraldSuccess else CrimsonAlert
                            )
                            Switch(
                                checked = resistedUrge,
                                onCheckedChange = { resistedUrge = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = EmeraldSuccess,
                                    checkedTrackColor = Color(0xFF003814),
                                    uncheckedThumbColor = CrimsonAlert,
                                    uncheckedTrackColor = Color(0xFF381010)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onLogTrigger(
                                    selectedMood,
                                    stressSlider.toInt(),
                                    selectedLocation,
                                    triggerNotes,
                                    resistedUrge
                                )
                                triggerNotes = ""
                                isJournalFormOpen = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_trigger_button")
                        ) {
                            Text("Enregistrer dans la mémoire sécurisée", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Recent entries list
        if (triggers.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Aucun déclencheur consigné.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = "Enregistre chaque envie pour nourrir l'algorithme prédictif et anticiper les pièges.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(triggers.take(5), key = { it.id }) { entry ->
                TriggerHistoryItem(entry = entry)
            }
        }

        // Streak Reset Option (Empathetic & educational, zero shame)
        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    onResetStreak()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reset_streak_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Déclarer une rechute sans honte (Apprentissage)", color = Color.Gray)
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun TriggerHistoryItem(entry: TriggerEntry) {
    val dateFormat = remember { SimpleDateFormat("dd MMM • HH:mm", Locale.FRENCH) }
    val formattedDate = remember(entry.timestamp) { dateFormat.format(Date(entry.timestamp)) }

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.mood,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF262626))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Stress ${entry.stressLevel}/10",
                            fontSize = 10.sp,
                            color = AmberSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Lieu : ${entry.location}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 2.dp)
                )
                if (entry.notes.isNotBlank()) {
                    Text(
                        text = "« ${entry.notes} »",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (entry.resisted) EmeraldSuccess.copy(alpha = 0.2f)
                            else CrimsonAlert.copy(alpha = 0.2f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (entry.resisted) "RÉSISTÉ" else "CRAQUÉ",
                        color = if (entry.resisted) EmeraldSuccess else CrimsonAlert,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
