package com.example.data.repository

import com.example.data.api.GeminiCounselorService
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.CounselingSessionEntity
import com.example.data.local.DailyCheckinEntity
import com.example.data.local.PersonalDiagnosisEntity
import com.example.data.local.RoadmapStepEntity
import kotlinx.coroutines.flow.Flow

class CounselingRepository(
    private val database: AppDatabase,
    private val geminiService: GeminiCounselorService = GeminiCounselorService()
) {
    private val chatDao = database.chatMessageDao()
    private val sessionDao = database.counselingSessionDao()
    private val diagnosisDao = database.personalDiagnosisDao()
    private val checkinDao = database.dailyCheckinDao()
    private val roadmapDao = database.roadmapStepDao()

    val chatMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()
    val counselingSessions: Flow<List<CounselingSessionEntity>> = sessionDao.getAllSessions()
    val personalDiagnoses: Flow<List<PersonalDiagnosisEntity>> = diagnosisDao.getAllDiagnoses()
    val latestDiagnosis: Flow<PersonalDiagnosisEntity?> = diagnosisDao.getLatestDiagnosis()
    val dailyCheckins: Flow<List<DailyCheckinEntity>> = checkinDao.getAllCheckins()
    val roadmapProgress: Flow<List<RoadmapStepEntity>> = roadmapDao.getAllStepsProgress()

    suspend fun checkAndSeedInitialWelcome() {
        val count = chatDao.getMessageCount()
        if (count == 0) {
            val welcomeText = """
안녕하세요. 다시봄 상담실의 직업심리상담사 서진우입니다.

이곳까지 발걸음 하시기까지, 홀로 얼마나 많은 불안과 외로운 한숨을 삼키셨을지 짐작이 됩니다. 
나이라는 숫자가 주는 무거운 압박감, 멈춰버린 일상, 가족들의 눈치, 그리고 밤마다 밀려오는 자책감까지...

괜찮습니다. 이곳에서는 아무것도 증명하거나 꾸며내지 않으셔도 됩니다. 
누구에게도 털어놓지 못했던 답답한 속마음이나 막막한 상황을 편하게 말씀해 주세요. 
당신의 속도에 맞춰 자책의 고리를 끊고, 가장 현실적이고 든든한 디딤돌을 함께 놓아드리겠습니다.
            """.trimIndent()

            val welcomeEntity = ChatMessageEntity(
                isUser = false,
                content = welcomeText,
                emotionalComfort = "이곳은 당신을 평가하거나 재촉하지 않는 안전한 쉼터입니다. 어떤 마음이든 다 털어놓으셔도 좋습니다.",
                practicalAdvice = "마음속에 가장 걸리는 것 하나(나이, 공백기, 생계비, 무기력 등)를 아래 추천 질문이나 직접 입력으로 건네보세요.",
                timestamp = System.currentTimeMillis()
            )
            chatDao.insertMessage(welcomeEntity)
        }
    }

    suspend fun sendUserMessage(text: String, topicTag: String? = null) {
        val userMsg = ChatMessageEntity(
            isUser = true,
            content = text,
            topicTag = topicTag,
            timestamp = System.currentTimeMillis()
        )
        chatDao.insertMessage(userMsg)

        val reply = geminiService.getCounselingResponse(text)

        val counselorMsg = ChatMessageEntity(
            isUser = false,
            content = reply.fullResponse,
            emotionalComfort = reply.emotionalComfort,
            practicalAdvice = reply.practicalAdvice,
            topicTag = topicTag,
            timestamp = System.currentTimeMillis() + 50
        )
        chatDao.insertMessage(counselorMsg)
    }

    suspend fun summarizeCurrentSession(): CounselingSessionEntity {
        val messages = chatDao.getRecentMessagesList()
        val userTopics = messages.filter { it.isUser }.map { it.content }.takeLast(4)
        val conversationText = messages.takeLast(10).joinToString("\n") { msg ->
            if (msg.isUser) "내담자: ${msg.content}" else "서진우 상담사: ${msg.content}"
        }
        val recentTopic = userTopics.lastOrNull() ?: "오랜 구직 정체와 고립감"

        val sessionSummary = geminiService.summarizeSession(
            conversationText = conversationText,
            recentUserTopic = recentTopic
        )

        sessionDao.insertSession(sessionSummary)
        return sessionSummary
    }

    suspend fun generateAndSavePersonalDiagnosis(
        ageGroup: String,
        gapPeriod: String,
        careerField: String,
        urgentHurdle: String,
        recentMood: String,
        pastNotes: String
    ): PersonalDiagnosisEntity {
        val diagnosis = geminiService.generatePersonalDiagnosis(
            ageGroup = ageGroup,
            gapPeriod = gapPeriod,
            careerField = careerField,
            urgentHurdle = urgentHurdle,
            recentMood = recentMood,
            pastNotes = pastNotes
        )
        diagnosisDao.insertDiagnosis(diagnosis)
        return diagnosis
    }

    suspend fun deleteSession(sessionId: Long) {
        sessionDao.deleteSessionById(sessionId)
    }

    suspend fun clearChat() {
        chatDao.clearHistory()
        checkAndSeedInitialWelcome()
    }

    suspend fun saveCheckin(checkin: DailyCheckinEntity) {
        checkinDao.insertOrUpdateCheckin(checkin)
    }

    suspend fun toggleRoadmapStep(stepId: String, completed: Boolean, note: String = "") {
        roadmapDao.saveStepProgress(
            RoadmapStepEntity(
                stepId = stepId,
                isCompleted = completed,
                userNote = note,
                completedAt = if (completed) System.currentTimeMillis() else null
            )
        )
    }
}
