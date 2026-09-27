package com.example.data.model

data class RoadmapStage(
    val id: String,
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val counselorInsight: String,
    val realisticAction: String,
    val checklist: List<RoadmapCheckItem>,
    val tips: List<String>
)

data class RoadmapCheckItem(
    val id: String,
    val title: String,
    val description: String
)

object RoadmapDataProvider {
    val stages = listOf(
        RoadmapStage(
            id = "stage_1_recovery",
            stepNumber = 1,
            title = "마음과 일상 복구",
            subtitle = "자책감 멈추기 & 최소 생체 리듬 회복",
            counselorInsight = "장기 실업 상태에서 가장 무서운 적은 게으름이 아니라 '스스로를 향한 가혹한 비난'과 '망가진 수면 리듬'입니다. 몸이 방전되었는데 억지로 이력서를 쓰려 하면 더 깊은 자괴감에 빠집니다. 우선 하루 30분 햇볕과 일정한 기상 시간부터 지켜내며 뇌에 안전 신호를 보내야 합니다.",
            realisticAction = "알람을 맞춰 매일 오전 8시에는 무조건 일어나 커튼을 걷고 따뜻한 물을 마시세요. 이것만 해내도 오늘의 절반은 성공한 것입니다.",
            checklist = listOf(
                RoadmapCheckItem("c1_1", "기상 시간 고정하기", "밤에 늦게 자더라도 아침 8시 이전에는 기상하여 불을 켜기"),
                RoadmapCheckItem("c1_2", "햇볕 쬐며 20분 걷기", "세로토닌 분비를 위해 낮 시간 가벼운 동네 산책"),
                RoadmapCheckItem("c1_3", "자책 멈추기 훈련", "'난 실패자야'라는 생각이 들 때 '지금은 재정비 중이다'로 고쳐 말하기")
            ),
            tips = listOf(
                "침대에 하루 종일 누워있지 마세요. 낮에는 거실이나 도서관으로 장소를 분리하는 것만으로도 무기력이 크게 줄어듭니다.",
                "가족과의 마찰이 잦다면, 낮 시간 동안 공공도서관을 거점으로 삼으세요."
            )
        ),
        RoadmapStage(
            id = "stage_2_safetynet",
            stepNumber = 2,
            title = "생계 안전망 확보",
            subtitle = "국민취업지원제도 & 최소 생계비 방어",
            counselorInsight = "통장에 잔고가 마르면 사람은 장기적인 이력서나 면접을 준비할 심리적 여유를 완전히 상실합니다. '터널 시야'에 갇혀 더 조급해지고 나쁜 선택을 하게 됩니다. 국가가 마련한 구직 안전망을 이용하는 것은 부끄러운 일이 아닌 대한민국 국민의 정당한 권리입니다.",
            realisticAction = "고용센터 1350에 전화하거나 웹사이트를 통해 '국민취업지원제도' 수급 자격을 즉시 조회하세요.",
            checklist = listOf(
                RoadmapCheckItem("c2_1", "국민취업지원제도 신청", "월 50만원씩 최대 6개월간 지원되는 구직촉진수당 자격 확인 및 신청"),
                RoadmapCheckItem("c2_2", "국민내일배움카드 발급", "연간 300~500만원 한도의 국비지원 훈련 카드 신청"),
                RoadmapCheckItem("c2_3", "월 고정 지출 긴급 다이어트", "불필요한 구독 서비스 해지, 통신비 알뜰폰 전환으로 최소 생계 방어선 구축")
            ),
            tips = listOf(
                "만 35세~69세 중장년층도 중위소득 요건 충족 시 1유형(구직촉진수당)을 받을 수 있습니다.",
                "국민취업지원제도 참여 시 담당 전담 직업상담사가 1:1로 매칭되어 큰 심리적 의지가 됩니다."
            )
        ),
        RoadmapStage(
            id = "stage_3_reframing",
            stepNumber = 3,
            title = "나이와 공백기 재정의",
            subtitle = "청년 스펙 경쟁 탈피 & 관록의 언어로 전환",
            counselorInsight = "20대 신입 공채의 잣대로 자신을 재면 나이는 단점처럼 보입니다. 하지만 기업 입장에서 나이 있는 인재에게 기대하는 것은 화려한 자격증이 아니라 '근태의 성실함', '조직 내 묵묵한 적응력', '작은 일에도 불평하지 않는 책임감'입니다. 공백기는 숨길 것이 아니라 솔직하게 재정비 기간으로 소명하면 됩니다.",
            realisticAction = "이력서의 경력기술서를 단순 나열식이 아니라 '어떤 문제를 끈기 있게 해결했는지' 역량 중심으로 1페이지로 재작성해 보세요.",
            checklist = listOf(
                RoadmapCheckItem("c3_1", "공백기 소명 멘트 준비", "'건강 회복과 함께 앞으로 오래 일할 수 있는 직무를 신중히 탐색한 전환기'로 답변 준비"),
                RoadmapCheckItem("c3_2", "나의 관록/강점 3가지 정리", "갈등 조율, 근면 성실, 돌발 상황 대처 등 연륜에서 오는 실무 강점 메모"),
                RoadmapCheckItem("c3_3", "높은 눈높이와 선입견 내려놓기", "과거의 직급과 연봉에 얽매이지 않고 '다시 시작할 발판'으로서의 직무 기준 설정")
            ),
            tips = listOf(
                "면접관이 공백기를 물었을 때 위축되어 변명하지 마세요. '솔직히 재충전이 필요했던 시기였고, 지금은 완전히 회복되어 현업에서 오래 기여할 준비가 끝났습니다'라고 당당히 밝히는 편이 훨씬 신뢰를 줍니다."
            )
        ),
        RoadmapStage(
            id = "stage_4_targeting",
            stepNumber = 4,
            title = "현실적 타깃 직무 탐색",
            subtitle = "진입 장벽이 낮고 정년이 긴 현실적 분야",
            counselorInsight = "막연히 '아무 일이나 해야지' 하면 번번이 떨어지고 자존감만 상합니다. 나이를 덜 따지거나 중장년 채용 수요가 높은 특화 영역을 집중 공략해야 합니다. 공공기관 공무직, 시설안전관리, 물류운영, 사회복지/돌봄 케어, 중소기업 총무·관리 실무 등 현실적 진입로가 분명히 열려 있습니다.",
            realisticAction = "중장년내일센터(전국 31개소) 및 지자체 일자리포털에서 '신중년/중장년 우대' 필터를 걸고 공고 3곳을 스크랩하세요.",
            checklist = listOf(
                RoadmapCheckItem("c4_1", "공공기관 공무직/기간제 탐색", "나이 블라인드 채용을 시행하는 공공기관 환경/시설/사무보조 공고 확인"),
                RoadmapCheckItem("c4_2", "알짜 실속 자격증 검토", "전기기능사, 소방안전관리자, 지게차운전기능사, 사회복지사 등 평생 일자리 자격증 확인"),
                RoadmapCheckItem("c4_3", "중장년내일센터 상담 예약", "정부 지원 1:1 맞춤형 전직 지원 서비스 신청")
            ),
            tips = listOf(
                "소방안전관리자 2급이나 지게차운전기능사는 1~2개월 내 취득 가능하며 아파트/빌딩/물류센터 취업에 매우 실질적인 힘이 됩니다."
            )
        ),
        RoadmapStage(
            id = "stage_5_execution",
            stepNumber = 5,
            title = "지속 가능한 실천 & 멘탈 유지",
            subtitle = "주 2~3회 지원 습관 & 탈락 둔감력 기르기",
            counselorInsight = "취업은 능력 순이 아니라 '버티는 타이밍'입니다. 서류에서 떨어지는 것은 내가 부족해서가 아니라 단지 그 회사의 시기와 조건이 맞지 않았을 뿐입니다. 탈락 하나하나에 감정을 다 쓰면 지쳐 나가떨어집니다. 지원은 '우체통에 편지 넣듯 담담하게' 하고, 내 일상을 굳건히 지키는 것이 승리의 비결입니다.",
            realisticAction = "화요일과 목요일 오전을 '지원하는 시간'으로 고정하고, 지원 후에는 좋아하는 커피 한 잔이나 산책으로 스스로에게 즉시 보상해 주세요.",
            checklist = listOf(
                RoadmapCheckItem("c5_1", "구직 요일 루틴 만들기", "매일 지원하기보다 주 2~3회 집중 지원일 정하기"),
                RoadmapCheckItem("c5_2", "탈락에 무뎌지기 훈련", "'회사 사정상 다른 사람을 뽑았을 뿐, 내 가치와는 상관없다' 되뇌기"),
                RoadmapCheckItem("c5_3", "면접 1회 경험 만들기", "결과에 연연하지 않고 실전 감각을 되살리는 디딤돌로 면접 응시하기")
            ),
            tips = listOf(
                "10군데 넣어 9군데 떨어지는 것은 대한민국 모든 구직자의 평균입니다. 당연한 과정으로 받아들이고 다음 지원서에 집중하세요."
            )
        )
    )
}
