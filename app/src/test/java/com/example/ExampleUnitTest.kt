package com.example

import com.example.data.model.TriggerEntry
import com.example.domain.CulturalWisdom
import com.example.domain.PredictivePatternAnalyzer
import com.example.domain.RiskLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    @Test
    fun culturalWisdom_containsProverbs() {
        val proverbs = CulturalWisdom.proverbs
        assertTrue(proverbs.isNotEmpty())
        val random = CulturalWisdom.getRandomProverb()
        assertNotNull(random.moore)
        assertNotNull(random.french)
    }

    @Test
    fun predictivePatternAnalyzer_detectsHighStress() {
        val daytimeCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 0)
        }
        val triggers = listOf(
            TriggerEntry(timestamp = daytimeCal.timeInMillis, stressLevel = 8, mood = "Stressé", location = "Salon"),
            TriggerEntry(timestamp = daytimeCal.timeInMillis, stressLevel = 9, mood = "Stressé", location = "Bureau")
        )
        val insight = PredictivePatternAnalyzer.analyze(triggers)
        assertTrue(insight.hasRiskPattern)
        assertEquals(RiskLevel.MODERATE, insight.riskLevel)
    }

    @Test
    fun predictivePatternAnalyzer_detectsNightVulnerability() {
        val nightCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 30)
        }
        val triggers = listOf(
            TriggerEntry(timestamp = nightCal.timeInMillis, stressLevel = 5, mood = "Fatigué", location = "Salon"),
            TriggerEntry(timestamp = nightCal.timeInMillis, stressLevel = 6, mood = "Fatigué", location = "Chambre")
        )
        val insight = PredictivePatternAnalyzer.analyze(triggers)
        assertTrue(insight.hasRiskPattern)
        assertEquals(RiskLevel.HIGH, insight.riskLevel)
    }

    @Test
    fun predictivePatternAnalyzer_emptyIsLowRisk() {
        val insight = PredictivePatternAnalyzer.analyze(emptyList())
        assertEquals(false, insight.hasRiskPattern)
        assertEquals(RiskLevel.LOW, insight.riskLevel)
    }
}
