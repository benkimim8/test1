package com.example.data.model

data class GovernmentSupportProgram(
    val title: String,
    val organization: String,
    val summary: String,
    val benefits: String,
    val targetAudience: String,
    val contactNumber: String,
    val websiteUrl: String,
    val tag: String
)

data class EmergencyHotline(
    val title: String,
    val description: String,
    val phoneNumber: String,
    val operatingHours: String,
    val isCrisis: Boolean = false
)

data class SurvivalTip(
    val title: String,
    val content: String
)

object SafetyNetDataProvider {
    val programs = listOf(
        GovernmentSupportProgram(
            title = "국민취업지원제도 (구직촉진수당)",
            organization = "고용노동부",
            summary = "저소득 구직자에게 매월 50만원씩 최대 6개월간 총 300만원의 구직촉진수당과 1:1 맞춤 취업지원서비스를 제공합니다.",
            benefits = "월 50만원 × 6개월 (최대 300만원) + 취업성공수당 최대 150만원",
            targetAudience = "만 15세~69세 중위소득 60% 이하 구직자 (중장년층 1유형/2유형 포함)",
            contactNumber = "1350",
            websiteUrl = "https://www.kua.go.kr",
            tag = "생계/수당"
        ),
        GovernmentSupportProgram(
            title = "국민내일배움카드",
            organization = "고용노동부 / HRD-Net",
            summary = "일자리를 구하는 국민 누구나 직무 역량 개발을 위해 5년간 300만~500만원의 국비 훈련비를 지원합니다.",
            benefits = "훈련비의 45~85% (취약계층 최대 100%) 국비 지원 + 훈련장려금 월 최대 11.6만원",
            targetAudience = "실업자, 재직자, 자영업자 누구나 (공무원/사립학교 교직원 등 제외)",
            contactNumber = "1350",
            websiteUrl = "https://www.hrd.go.kr",
            tag = "직업훈련"
        ),
        GovernmentSupportProgram(
            title = "중장년내일센터 (전직지원 서비스)",
            organization = "노사발전재단 / 고용노동부",
            summary = "만 40세 이상 중장년을 대상으로 생애경력설계, 1:1 맞춤형 전직지원, 재도약 취업지원 프로그램을 무료로 제공합니다.",
            benefits = "무료 1:1 심층 취업컨설팅 + 적합 일자리 알선 + 면접 코칭",
            targetAudience = "만 40세 이상의 중장년 구직자 및 퇴직 예정자",
            contactNumber = "02-6021-1100",
            websiteUrl = "https://www.work.go.kr/senior",
            tag = "중장년특화"
        ),
        GovernmentSupportProgram(
            title = "긴급복지지원제도 (생계지원)",
            organization = "보건복지부 / 지자체",
            summary = "실직, 휴·폐업 등으로 생계유지가 갑자기 곤란해진 위기 가구에 생계비, 의료비, 주거비를 신속히 지원합니다.",
            benefits = "생계지원금 (1인 가구 기준 월 약 71만원 지원)",
            targetAudience = "주소득자의 실직이나 사업 실패 등으로 위기 상황에 처한 가구",
            contactNumber = "129",
            websiteUrl = "https://www.bokjiro.go.kr",
            tag = "긴급복지"
        ),
        GovernmentSupportProgram(
            title = "청년·중장년 마음건강지원사업 (심리지원 바우처)",
            organization = "보건복지부",
            summary = "우울, 불안, 구직 스트레스를 겪는 국민에게 전문 심리상담 서비스를 이용할 수 있는 바우처를 지원합니다.",
            benefits = "총 8회기 전문 심리상담 지원 (정부지원금 회당 5~8만원 상당)",
            targetAudience = "심리적 어려움을 겪는 구직자 및 국민",
            contactNumber = "129",
            websiteUrl = "https://www.bokjiro.go.kr",
            tag = "심리상담"
        )
    )

    val hotlines = listOf(
        EmergencyHotline(
            title = "고용노동부 상담센터",
            description = "국민취업지원제도, 실업급여, 내일배움카드, 노동법 상담",
            phoneNumber = "1350",
            operatingHours = "평일 09:00 ~ 18:00"
        ),
        EmergencyHotline(
            title = "보건복지상담센터",
            description = "긴급 생계비 지원, 기초생활보장, 복지 멤버십 문의",
            phoneNumber = "129",
            operatingHours = "365일 24시간"
        ),
        EmergencyHotline(
            title = "정신건강 위기상담전화",
            description = "극심한 우울, 고립감, 불안으로 잠 못 들 때 전문 상담사 연결",
            phoneNumber = "1577-0199",
            operatingHours = "365일 24시간",
            isCrisis = true
        ),
        EmergencyHotline(
            title = "자살예방 상담전화",
            description = "모든 게 끝난 것 같고 너무 힘들 때, 당신의 곁에 있습니다",
            phoneNumber = "109",
            operatingHours = "365일 24시간",
            isCrisis = true
        )
    )

    val practicalSurvivalTips = listOf(
        SurvivalTip(
            title = "통장 잔고 방어: 신용회복위원회 채무조정",
            content = "대출이자나 카드 대금이 밀리기 시작할 때 절대 사채나 고금리 대출로 막지 마세요. 신용회복위원회(1600-0114) '신속채무조정'이나 '프리워크아웃'을 신청하면 즉시 원금 상환 유예와 이자 감면을 받을 수 있습니다."
        ),
        SurvivalTip(
            title = "고정지출 다이어트: 알뜰폰 & 통신비 감면",
            content = "통신비를 알뜰폰(월 1~2만원대)으로 바꾸고, 장기 미취업자나 저소득층인 경우 주민센터에서 이동통신요금 감면 신청을 할 수 있습니다. 공공도서관을 낮 동안 적극 활용하여 냉난방비와 커피값을 절약하세요."
        ),
        SurvivalTip(
            title = "중장년 추천 취업 자격증 TOP 4",
            content = "1) 소방안전관리자 2급 (건물 필수 선임)\n2) 전기기능사 (정년 없는 평생 기술)\n3) 지게차운전기능사 (물류/제조업 즉시 투입)\n4) 사회복지사 2급 (돌봄/노인복지 안정 수요)"
        )
    )
}
