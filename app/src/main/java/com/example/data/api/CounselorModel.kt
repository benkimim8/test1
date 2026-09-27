package com.example.data.api

data class CounselorReply(
    val fullResponse: String,
    val emotionalComfort: String,
    val practicalAdvice: String
)

object CounselorTextParser {
    fun parse(text: String): CounselorReply {
        var comfort = ""
        var practical = ""

        val comfortMarker = "🌱 [마음 처방]"
        val practicalMarker = "🪜 [현실의 한 걸음]"

        if (text.contains(comfortMarker) && text.contains(practicalMarker)) {
            val parts = text.split(practicalMarker)
            val firstPart = parts.getOrNull(0) ?: ""
            practical = parts.getOrNull(1)?.trim() ?: ""

            val subParts = firstPart.split(comfortMarker)
            comfort = subParts.getOrNull(1)?.trim() ?: ""
        } else if (text.contains("[마음 처방]") && text.contains("[현실의 한 걸음]")) {
            val parts = text.split("[현실의 한 걸음]")
            val firstPart = parts.getOrNull(0) ?: ""
            practical = parts.getOrNull(1)?.replace(":", "")?.trim() ?: ""

            val subParts = firstPart.split("[마음 처방]")
            comfort = subParts.getOrNull(1)?.replace(":", "")?.trim() ?: ""
        }

        if (comfort.isEmpty()) {
            comfort = "지금 느끼시는 그 지친 마음은 결코 잘못된 게 아닙니다. 지금까지 홀로 무거운 시간을 견뎌오시느라 참 애쓰셨습니다."
        }
        if (practical.isEmpty()) {
            practical = "오늘 하루는 스스로를 자책하지 말고, 창문을 열어 맑은 공기를 마시며 20분간 가볍게 동네를 산책해 보세요."
        }

        return CounselorReply(
            fullResponse = text,
            emotionalComfort = comfort,
            practicalAdvice = practical
        )
    }
}
