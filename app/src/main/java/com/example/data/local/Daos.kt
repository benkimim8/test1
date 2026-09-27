package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    suspend fun getRecentMessagesList(): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()

    @Query("SELECT COUNT(*) FROM chat_messages")
    suspend fun getMessageCount(): Int
}

@Dao
interface CounselingSessionDao {
    @Query("SELECT * FROM counseling_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<CounselingSessionEntity>>

    @Query("SELECT * FROM counseling_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: Long): CounselingSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: CounselingSessionEntity): Long

    @Query("DELETE FROM counseling_sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: Long)
}

@Dao
interface PersonalDiagnosisDao {
    @Query("SELECT * FROM personal_diagnoses ORDER BY timestamp DESC")
    fun getAllDiagnoses(): Flow<List<PersonalDiagnosisEntity>>

    @Query("SELECT * FROM personal_diagnoses ORDER BY timestamp DESC LIMIT 1")
    fun getLatestDiagnosis(): Flow<PersonalDiagnosisEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosis(diagnosis: PersonalDiagnosisEntity): Long
}

@Dao
interface DailyCheckinDao {
    @Query("SELECT * FROM daily_checkins ORDER BY dateString DESC")
    fun getAllCheckins(): Flow<List<DailyCheckinEntity>>

    @Query("SELECT * FROM daily_checkins WHERE dateString = :dateString LIMIT 1")
    suspend fun getCheckinByDate(dateString: String): DailyCheckinEntity?

    @Query("SELECT * FROM daily_checkins WHERE dateString = :dateString LIMIT 1")
    fun observeCheckinByDate(dateString: String): Flow<DailyCheckinEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCheckin(checkin: DailyCheckinEntity)
}

@Dao
interface RoadmapStepDao {
    @Query("SELECT * FROM roadmap_progress")
    fun getAllStepsProgress(): Flow<List<RoadmapStepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStepProgress(step: RoadmapStepEntity)

    @Update
    suspend fun updateStepProgress(step: RoadmapStepEntity)
}
