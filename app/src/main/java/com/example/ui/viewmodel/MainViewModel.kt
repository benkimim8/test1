package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.CounselingSessionEntity
import com.example.data.local.DailyCheckinEntity
import com.example.data.local.PersonalDiagnosisEntity
import com.example.data.repository.CounselingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CounselingRepository

    val chatMessages: StateFlow<List<ChatMessageEntity>>
    val allCheckins: StateFlow<List<DailyCheckinEntity>>
    val counselingSessions: StateFlow<List<CounselingSessionEntity>>
    val personalDiagnoses: StateFlow<List<PersonalDiagnosisEntity>>
    val latestDiagnosis: StateFlow<PersonalDiagnosisEntity?>

    private val _isCounselorThinking = MutableStateFlow(false)
    val isCounselorThinking: StateFlow<Boolean> = _isCounselorThinking.asStateFlow()

    private val _isSummarizingSession = MutableStateFlow(false)
    val isSummarizingSession: StateFlow<Boolean> = _isSummarizingSession.asStateFlow()

    private val _isDiagnosing = MutableStateFlow(false)
    val isDiagnosing: StateFlow<Boolean> = _isDiagnosing.asStateFlow()

    private val _currentInput = MutableStateFlow("")
    val currentInput: StateFlow<String> = _currentInput.asStateFlow()

    // Profile questionnaire fields for personal career diagnosis
    val userAgeGroup = MutableStateFlow("40대 초반")
    val userGapPeriod = MutableStateFlow("1년 ~ 2년")
    val userCareerField = MutableStateFlow("일반사무 / 관리실무")
    val userUrgentHurdle = MutableStateFlow("나이 장벽과 반복된 서류 탈락")

    val todayDateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

    val todayCheckin: StateFlow<DailyCheckinEntity?>
    val roadmapCompletionMap: StateFlow<Map<String, Boolean>>
    val roadmapNotesMap: StateFlow<Map<String, String>>

    init {
        val database = AppDatabase.getInstance(application)
        repository = CounselingRepository(database)

        chatMessages = repository.chatMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        counselingSessions = repository.counselingSessions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        personalDiagnoses = repository.personalDiagnoses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        latestDiagnosis = repository.latestDiagnosis.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        allCheckins = repository.dailyCheckins.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        todayCheckin = repository.dailyCheckins.map { list ->
            list.firstOrNull { it.dateString == todayDateString }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        roadmapCompletionMap = repository.roadmapProgress.map { list ->
            list.associate { it.stepId to it.isCompleted }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

        roadmapNotesMap = repository.roadmapProgress.map { list ->
            list.associate { it.stepId to it.userNote }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

        viewModelScope.launch {
            repository.checkAndSeedInitialWelcome()
        }
    }

    fun onInputChanged(text: String) {
        _currentInput.value = text
    }

    fun sendMessage(text: String = _currentInput.value, topicTag: String? = null) {
        val trimmed = text.trim()
        if (trimmed.isBlank() || _isCounselorThinking.value) return

        _currentInput.value = ""
        _isCounselorThinking.value = true

        viewModelScope.launch {
            try {
                repository.sendUserMessage(trimmed, topicTag)
            } finally {
                _isCounselorThinking.value = false
            }
        }
    }

    fun summarizeCurrentSession(onComplete: ((CounselingSessionEntity) -> Unit)? = null) {
        if (_isSummarizingSession.value) return
        _isSummarizingSession.value = true

        viewModelScope.launch {
            try {
                val session = repository.summarizeCurrentSession()
                onComplete?.invoke(session)
            } finally {
                _isSummarizingSession.value = false
            }
        }
    }

    fun generatePersonalDiagnosis(onComplete: ((PersonalDiagnosisEntity) -> Unit)? = null) {
        if (_isDiagnosing.value) return
        _isDiagnosing.value = true

        viewModelScope.launch {
            try {
                val pastNotes = allCheckins.value.take(3).joinToString("; ") {
                    "${it.dateString} (${it.mood}): ${it.selfCompassionNote}"
                }
                val recentMood = todayCheckin.value?.mood ?: "불안/지침"

                val diagnosis = repository.generateAndSavePersonalDiagnosis(
                    ageGroup = userAgeGroup.value,
                    gapPeriod = userGapPeriod.value,
                    careerField = userCareerField.value,
                    urgentHurdle = userUrgentHurdle.value,
                    recentMood = recentMood,
                    pastNotes = pastNotes
                )
                onComplete?.invoke(diagnosis)
            } finally {
                _isDiagnosing.value = false
            }
        }
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    fun saveMood(mood: String, note: String) {
        val current = todayCheckin.value ?: DailyCheckinEntity(
            dateString = todayDateString,
            mood = mood,
            selfCompassionNote = note
        )

        val updated = current.copy(
            mood = mood,
            selfCompassionNote = note,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.saveCheckin(updated)
        }
    }

    fun toggleHabit(habitType: String) {
        val current = todayCheckin.value ?: DailyCheckinEntity(
            dateString = todayDateString,
            mood = "CALM"
        )

        val updated = when (habitType) {
            "walk" -> current.copy(habitWalkDone = !current.habitWalkDone)
            "water" -> current.copy(habitWaterDone = !current.habitWaterDone)
            "onestep" -> current.copy(habitOneStepDone = !current.habitOneStepDone)
            else -> current
        }

        viewModelScope.launch {
            repository.saveCheckin(updated)
        }
    }

    fun toggleRoadmapStep(stepId: String) {
        val currentCompleted = roadmapCompletionMap.value[stepId] ?: false
        val currentNote = roadmapNotesMap.value[stepId] ?: ""
        viewModelScope.launch {
            repository.toggleRoadmapStep(stepId, !currentCompleted, currentNote)
        }
    }

    fun saveRoadmapNote(stepId: String, note: String) {
        val currentCompleted = roadmapCompletionMap.value[stepId] ?: false
        viewModelScope.launch {
            repository.toggleRoadmapStep(stepId, currentCompleted, note)
        }
    }
}
