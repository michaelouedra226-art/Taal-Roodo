package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "triggers")
data class TriggerEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: String, // Calme, Anxieux, Solitaire, Ennuyé, Fatigué, Stressé
    val stressLevel: Int, // 1..10
    val location: String, // Chambre, Salon, Soir au lit, Bureau, etc.
    val notes: String = "",
    val resisted: Boolean = true
)

@Entity(tableName = "challenges")
data class ChallengeItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Physique, Mental, Social, Spirituel
    val description: String,
    val durationMinutes: Int,
    val points: Int,
    val isCompleted: Boolean = false,
    val dateDay: String // Format YYYY-MM-DD
)

@Entity(tableName = "forum_posts")
data class ForumPost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorAlias: String, // e.g. "Guerrier_226", "Frère_Ouaga", "Bao_Bobo"
    val daysClean: Int,
    val content: String,
    val likesCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val tag: String = "Victoire" // Victoire, Soutien, Témoignage
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val streakDays: Int = 14,
    val bestStreakDays: Int = 28,
    val lastCheckInDate: Long = System.currentTimeMillis(),
    val emergencyContactName: String = "Grand Frère / Ami de confiance",
    val emergencyContactPhone: String = "+226 70 00 00 00",
    val isFortressActive: Boolean = false,
    val fortressPin: String = "1234",
    val preferredLanguage: String = "FR", // "FR" ou "MOORE"
    val customAgnesApiKey: String = ""
)
