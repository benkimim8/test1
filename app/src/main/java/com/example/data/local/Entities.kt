package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val isUser: Boolean,
    val content: String,
    val emotionalComfort: String? = null,
    val practicalAdvice: String? = null,
    val topicTag: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "counseling_sessions")
data class CounselingSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dateString: String, // e.g. "2026-09-23"
    val title: String,
    val primaryConcern: String,
    val initialMood: String = "불안",
    val finalMood: String = "담담함",
    val coreSummary: String,
    val counselorInsight: String,
    val actionSteps: String,
    val messageCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "personal_diagnoses")
data class PersonalDiagnosisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dateString: String,
    val userAgeGroup: String,
    val userGapPeriod: String,
    val userCareerField: String,
    val userUrgentHurdle: String,
    val psychologicalAnalysis: String,
    val reconstructedStrengths: String,
    val recommendedJobTracks: String,
    val threeStepActionPlan: String,
    val encouragementQuote: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_checkins")
data class DailyCheckinEntity(
    @PrimaryKey
    val dateString: String, // e.g. "2026-09-23"
    val mood: String, // "TIRED", "ANXIOUS", "NUMB", "CALM", "HOPEFUL"
    val moodScore: Int = 3, // 1 to 5
    val selfCompassionNote: String = "",
    val habitWalkDone: Boolean = false,
    val habitWaterDone: Boolean = false,
    val habitOneStepDone: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "roadmap_progress")
data class RoadmapStepEntity(
    @PrimaryKey
    val stepId: String,
    val isCompleted: Boolean = false,
    val userNote: String = "",
    val completedAt: Long? = null
)
