package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.local.CounselingSessionEntity
import com.example.data.local.PersonalDiagnosisEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class GeminiCounselorService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val systemPrompt = """
당신은 15년 이상의 경력을 지닌 대한민국 공인 1급 직업상담사이자 임상심리상담사 '서진우'입니다.
대화 상대는 나이가 많고(30대 중후반, 40대, 50대 이상), 오랜 구직 실패나 비자발적 퇴직, 길어진 공백기로 인해 깊은 자책감과 번아웃, 경제적 불안, 가족과의 단절을 겪고 있는 지친 구직자입니다.

반드시 지켜야 할 상담 원칙:
1. [깊은 공감과 무조건적 수용]:
   '노력하면 다 된다'는 식의 무책임한 긍정, 훈계, 얕은 위로는 내담자에게 상처가 됩니다. 내담자가 홀로 짊어져온 침묵의 무게와 밤마다 겪었을 자책, 가족 앞에서의 작아짐을 깊이 공감하고 인정해 주세요. 정중하고 따뜻한 존댓말을 씁니다.
2. [개인의 탓이 아닌 구조적 시각 제공]:
   지금 겪는 어려움은 내담자가 게으르거나 무능해서가 아니라, 한국 노동시장의 경직성, 조기퇴직 압박, 경력 단절자를 배려하지 않는 채용 시장의 구조적 문제임을 객관적으로 짚어주어 자책을 덜어주세요.
3. [현실적이고 실행 가능한 단계적 조언]:
   - 공백기 대처: 공백기를 실패가 아닌 '생애 전환기', '재정비 기간'으로 재구성하는 현실적 이력서/면접 전략
   - 나이 극복: 청년 신입 공채와 다른 '경력직/중장년 채용 룰' 제시 (직무 성실성, 갈등 중재력, 위기대처 능력 강조)
   - 타겟 업종: 나이 제한이 비교적 덜하고 안정적인 현실적 분야 (공공기관 공무직/기간제, 중소·강소기업 관리/사무/운영직, 시설/안전관리, 물류운영, 사회복지/돌봄, 내일배움카드 기술 연계)
   - 생계 안전망: 국민취업지원제도(월 50만원 지원), 긴급생계지원, 무료 심리상담 연결
   - 일상 회복: 거창한 계획 대신 오늘 당장 할 수 있는 마이크로 루틴(아침 기상시간 지키기, 30분 산책, 햇볕 쬐기)
4. [답변의 필수 구성]:
   자연스러운 대화체로 답변하되, 글의 후반부에 명확히 구별되는 다음 두 섹션을 반드시 포함해 주세요:
   🌱 [마음 처방] : 지금의 자책감을 내려놓게 돕는 따뜻한 통찰과 위로의 한마디
   🪜 [현실의 한 걸음] : 오늘이나 이번 주에 당장 부담 없이 실천해 볼 수 있는 구체적인 행동 1가지
    """.trimIndent()

    suspend fun getCounselingResponse(userMessage: String): CounselorReply = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey.contains("TODO")) {
            Log.d("GeminiCounselorService", "Using rich fallback counseling engine (no API key)")
            return@withContext FallbackCounselorEngine.generateCounselingResponse(userMessage)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", userMessage)
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                val systemInstructionObj = JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemPrompt)
                        })
                    })
                }
                put("systemInstruction", systemInstructionObj)

                val generationConfig = JSONObject().apply {
                    put("temperature", 0.7)
                    put("topP", 0.95)
                }
                put("generationConfig", generationConfig)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.w("GeminiCounselorService", "Gemini API error code: ${response.code}")
                    return@withContext FallbackCounselorEngine.generateCounselingResponse(userMessage)
                }

                val responseString = response.body?.string() ?: ""
                val responseJson = JSONObject(responseString)
                val candidates = responseJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    return@withContext CounselorTextParser.parse(text)
                } else {
                    return@withContext FallbackCounselorEngine.generateCounselingResponse(userMessage)
                }
            }
        } catch (e: Exception) {
            Log.e("GeminiCounselorService", "Error calling Gemini API: ${e.message}", e)
            return@withContext FallbackCounselorEngine.generateCounselingResponse(userMessage)
        }
    }

    suspend fun summarizeSession(
        conversationText: String,
        recentUserTopic: String
    ): CounselingSessionEntity = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext FallbackCounselorEngine.generateSessionSummary(conversationText, recentUserTopic)
        }

        val prompt = """
다음은 나이 많고 지친 구직자와 직업심리상담사 서진우의 최근 상담 대화 내용입니다:
$conversationText

위 상담 내용을 바탕으로 다음 형식에 맞춰 요약해 주세요.
반드시 각 항목의 제목을 아래와 같이 명시해 주세요:
[제목]: (1줄 핵심 주제, 예: 길어진 공백기의 두려움과 현실적 소명 전략)
[핵심 고민]: (내담자가 호소한 가장 큰 두려움이나 아픔 1~2문장)
[초기 감정]: (불안, 무기력, 자책 등)
[최종 감정]: (담담함, 안도, 희망 등)
[상담 요약]: (상담에서 다룬 핵심 내용 2~3문장)
[상담사 통찰]: (내담자의 자책감을 덜어주는 심리학적 진단과 관점 전환)
[실행 약속 3가지]:
1. (구체적이고 부담 없는 1단계 행동)
2. (2단계 행동)
3. (3단계 행동)
        """.trimIndent()

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
            }
            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext FallbackCounselorEngine.generateSessionSummary(conversationText, recentUserTopic)
                }
                val raw = response.body?.string() ?: ""
                val text = JSONObject(raw).optJSONArray("candidates")
                    ?.optJSONObject(0)?.optJSONObject("content")
                    ?.optJSONArray("parts")?.optJSONObject(0)?.optString("text")

                if (text.isNullOrBlank()) {
                    return@withContext FallbackCounselorEngine.generateSessionSummary(conversationText, recentUserTopic)
                }

                val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
                val title = text.lineContaining("[제목]")?.removePrefix("[제목]")?.trim()?.replace(":", "")
                    ?: "마음의 무게 덜어내기와 현실적 구직 첫걸음"
                val concern = text.lineContaining("[핵심 고민]")?.removePrefix("[핵심 고민]")?.trim()?.replace(":", "")
                    ?: recentUserTopic
                val initMood = text.lineContaining("[초기 감정]")?.removePrefix("[초기 감정]")?.trim()?.replace(":", "")
                    ?: "불안/자책"
                val finMood = text.lineContaining("[최종 감정]")?.removePrefix("[최종 감정]")?.trim()?.replace(":", "")
                    ?: "담담함/안도"
                val summary = text.extractBetween("[상담 요약]", "[상담사 통찰]")
                    .ifBlank { "상담을 통해 자책의 고리를 끊고 현실적인 생계 안전망과 직무 트랙을 함께 정리했습니다." }
                val insight = text.extractBetween("[상담사 통찰]", "[실행 약속 3가지]")
                    .ifBlank { "내담자의 불안은 의지 박약이 아니라 지친 뇌의 정직한 신호이며, 속도를 늦추고 차근차근 디딤돌을 밟아야 합니다." }
                val actions = text.substringAfter("[실행 약속 3가지]").trim()
                    .ifBlank { "1. 기상 시간 고정하기\n2. 국민취업지원제도 1350 문의하기\n3. 햇볕 20분 쬐기" }

                return@withContext CounselingSessionEntity(
                    dateString = today,
                    title = title,
                    primaryConcern = concern,
                    initialMood = initMood,
                    finalMood = finMood,
                    coreSummary = summary.trim(),
                    counselorInsight = insight.trim(),
                    actionSteps = actions.trim(),
                    messageCount = 6,
                    timestamp = System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            Log.e("GeminiCounselorService", "Session summary error: ${e.message}", e)
            return@withContext FallbackCounselorEngine.generateSessionSummary(conversationText, recentUserTopic)
        }
    }

    suspend fun generatePersonalDiagnosis(
        ageGroup: String,
        gapPeriod: String,
        careerField: String,
        urgentHurdle: String,
        recentMood: String,
        pastNotes: String
    ): PersonalDiagnosisEntity = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext FallbackCounselorEngine.generatePersonalDiagnosis(
                ageGroup, gapPeriod, careerField, urgentHurdle, recentMood
            )
        }

        val prompt = """
당신은 직업심리상담사 서진우입니다.
내담자의 종합 프로필과 상태를 분석하여 [개인 맞춤 심층 진단 및 현실 재기 처방 리포트]를 작성해 주세요.

내담자 정보:
- 연령대: $ageGroup
- 공백기 기간: $gapPeriod
- 이전 직무/배경: $careerField
- 가장 시급한 어려움: $urgentHurdle
- 최근 마음 날씨: $recentMood
- 과거 상담 및 감정 기록: $pastNotes

다음 섹션을 명확히 구분하여 매우 현실적이고 구체적으로 작성해 주세요:
[종합 심리 분석]: (내담자가 겪는 자책과 번아웃의 메커니즘을 심리학적으로 공감하고 해석)
[나이와 경험의 강점 재발견]: (20대 신입과 차별화되는 성숙함, 인내력, 실무 관록 3가지)
[현실적 추천 직무 및 안전망]: (나이 장벽이 낮은 공공기관 공무직, 시설/안전관리, 물류운영, 사회복지 등과 국비지원 자격증, 국민취업지원제도 등 구체적 매칭)
[3단계 실행 계획]: (1주차 심신/생계 방어, 2주차 직무/자격 탐색, 3주차 규칙적 지원 루틴)
[상담사의 격려 한마디]: (가슴을 울리는 따뜻하고 단단한 1줄)
        """.trimIndent()

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
            }
            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext FallbackCounselorEngine.generatePersonalDiagnosis(
                        ageGroup, gapPeriod, careerField, urgentHurdle, recentMood
                    )
                }
                val raw = response.body?.string() ?: ""
                val text = JSONObject(raw).optJSONArray("candidates")
                    ?.optJSONObject(0)?.optJSONObject("content")
                    ?.optJSONArray("parts")?.optJSONObject(0)?.optString("text")

                if (text.isNullOrBlank()) {
                    return@withContext FallbackCounselorEngine.generatePersonalDiagnosis(
                        ageGroup, gapPeriod, careerField, urgentHurdle, recentMood
                    )
                }

                val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
                val psych = text.extractBetween("[종합 심리 분석]", "[나이와 경험의 강점 재발견]")
                    .ifBlank { "장기화된 고립으로 인한 심리적 방전 상태이며, 나만의 속도를 회복해야 합니다." }
                val strengths = text.extractBetween("[나이와 경험의 강점 재발견]", "[현실적 추천 직무 및 안전망]")
                    .ifBlank { "1. 묵묵한 인내력과 위기관리\n2. 갈등 중재와 조직 적응력\n3. 진중한 근태 신뢰성" }
                val jobs = text.extractBetween("[현실적 추천 직무 및 안전망]", "[3단계 실행 계획]")
                    .ifBlank { "• 공공기관 공무직(블라인드 채용)\n• 시설안전관리/지게차운전\n• 국민취업지원제도 구직촉진수당" }
                val plan = text.extractBetween("[3단계 실행 계획]", "[상담사의 격려 한마디]")
                    .ifBlank { "[1주차] 기상 고정 및 1350 상담\n[2주차] 국비 교육 탐색\n[3주차] 주 2회 지원 루틴" }
                val quote = text.substringAfter("[상담사의 격려 한마디]").trim()
                    .ifBlank { "당신의 세월은 헛되지 않았습니다. 당신만의 조용하고 단단한 봄이 곧 시작됩니다." }

                return@withContext PersonalDiagnosisEntity(
                    dateString = today,
                    userAgeGroup = ageGroup,
                    userGapPeriod = gapPeriod,
                    userCareerField = careerField,
                    userUrgentHurdle = urgentHurdle,
                    psychologicalAnalysis = psych.trim(),
                    reconstructedStrengths = strengths.trim(),
                    recommendedJobTracks = jobs.trim(),
                    threeStepActionPlan = plan.trim(),
                    encouragementQuote = quote.trim(),
                    timestamp = System.currentTimeMillis()
                )
            }
        } catch (e: Exception) {
            Log.e("GeminiCounselorService", "Diagnosis error: ${e.message}", e)
            return@withContext FallbackCounselorEngine.generatePersonalDiagnosis(
                ageGroup, gapPeriod, careerField, urgentHurdle, recentMood
            )
        }
    }

    private fun String.lineContaining(keyword: String): String? {
        return lines().firstOrNull { it.contains(keyword) }
    }

    private fun String.extractBetween(startMarker: String, endMarker: String): String {
        if (!contains(startMarker)) return ""
        val after = substringAfter(startMarker)
        return if (after.contains(endMarker)) {
            after.substringBefore(endMarker).trim()
        } else {
            after.trim()
        }
    }
}
