package com.example.domain

import com.example.data.model.TriggerEntry
import java.util.Calendar

data class PatternInsight(
    val hasRiskPattern: Boolean,
    val title: String,
    val description: String,
    val recommendation: String,
    val riskLevel: RiskLevel
)

enum class RiskLevel { LOW, MODERATE, HIGH }

object PredictivePatternAnalyzer {

    fun analyze(triggers: List<TriggerEntry>): PatternInsight {
        if (triggers.isEmpty()) {
            return PatternInsight(
                hasRiskPattern = false,
                title = "Données en cours d'apprentissage",
                description = "Enregistre tes moments de doute ou d'envie pour identifier tes schémas inconscients.",
                recommendation = "Continue de maintenir ton bouclier TR-Shield actif.",
                riskLevel = RiskLevel.LOW
            )
        }

        val highStressCount = triggers.count { it.stressLevel >= 7 }
        val nightTriggers = triggers.count { entry ->
            val cal = Calendar.getInstance().apply { timeInMillis = entry.timestamp }
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            hour >= 22 || hour <= 4
        }
        val bedroomTriggers = triggers.count { it.location.contains("chambre", ignoreCase = true) || it.location.contains("lit", ignoreCase = true) }

        return when {
            nightTriggers >= 2 -> {
                PatternInsight(
                    hasRiskPattern = true,
                    title = "Vulnérabilité Nocturne Détectée",
                    description = "Tu as noté plusieurs envies entre 22h et 4h du matin. La fatigue diminue les freins du cortex préfrontal.",
                    recommendation = "Activer le Mode Forteresse dès 21h30 et laisser le téléphone hors de la chambre.",
                    riskLevel = RiskLevel.HIGH
                )
            }
            highStressCount >= 2 -> {
                PatternInsight(
                    hasRiskPattern = true,
                    title = "Déclencheur lié au Stress Élevé",
                    description = "Ton cerveau associe la pression et le stress intense à une échappatoire dopaminergique rapide.",
                    recommendation = "Pratique 5 minutes de cohérence cardiaque (Bouton Panique) dès les premiers signes de tension.",
                    riskLevel = RiskLevel.MODERATE
                )
            }
            bedroomTriggers >= 2 -> {
                PatternInsight(
                    hasRiskPattern = true,
                    title = "Conditionnement Spatial (Chambre / Lit)",
                    description = "L'isolement dans la chambre est ton environnement à plus haut risque.",
                    recommendation = "Redirige immédiatement ton énergie en changeant de pièce ou en effectuant 20 pompes.",
                    riskLevel = RiskLevel.MODERATE
                )
            }
            else -> {
                PatternInsight(
                    hasRiskPattern = false,
                    title = "Vigilance Stable",
                    description = "Aucun schéma critique dominant pour le moment. Tes déclencheurs restent sous contrôle.",
                    recommendation = "Reste vigilant lors des moments de solitude et garde contact avec ton partenaire.",
                    riskLevel = RiskLevel.LOW
                )
            }
        }
    }
}
