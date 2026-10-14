package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ChallengeItem
import com.example.data.model.ForumPost
import com.example.data.model.TriggerEntry
import com.example.data.model.UserProfile

@Database(
    entities = [
        TriggerEntry::class,
        ChallengeItem::class,
        ForumPost::class,
        UserProfile::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun triggerDao(): TriggerDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun forumDao(): ForumDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "taal_roodo_db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
