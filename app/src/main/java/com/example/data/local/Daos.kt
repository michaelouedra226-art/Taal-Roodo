package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ChallengeItem
import com.example.data.model.ForumPost
import com.example.data.model.TriggerEntry
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface TriggerDao {
    @Query("SELECT * FROM triggers ORDER BY timestamp DESC")
    fun getAllTriggers(): Flow<List<TriggerEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrigger(entry: TriggerEntry): Long

    @Query("DELETE FROM triggers WHERE id = :id")
    suspend fun deleteTrigger(id: Long)
}

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges ORDER BY id ASC")
    fun getAllChallenges(): Flow<List<ChallengeItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ChallengeItem>)

    @Update
    suspend fun updateChallenge(item: ChallengeItem)
}

@Dao
interface ForumDao {
    @Query("SELECT * FROM forum_posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<ForumPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ForumPost): Long

    @Query("UPDATE forum_posts SET likesCount = likesCount + 1 WHERE id = :id")
    suspend fun incrementLikes(id: Long)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfile)
}
