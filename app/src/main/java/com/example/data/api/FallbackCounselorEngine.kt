package com.example.data.api

import com.example.data.local.CounselingSessionEntity
import com.example.data.local.PersonalDiagnosisEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FallbackCounselorEngine {

    fun generateCounselingResponse(userInput: String): CounselorReply {
        val trimmed = userInput.trim()

        return when {
            trimmed.contains("공백기") || trimmed.contains("쉬었") || trimmed.contains("오랜만") || trimmed.contains("경력단절") -> {
                val full = """
안녕하세요. 서진우 상담사입니다.
공백기가 길어지면 이력서에 적을 날짜 하나하나가 무거운 짐처럼 느껴지고, 마우스 클릭 한 번조차 거대한 벽처럼 다가오는 법입니다. 그 망설임과 두려움은 지극히 자연스러운 감정입니다.

하지만 먼저 꼭 기억해 주세요. 공백기는 당신의 '실패'가 아니라, 치열하게 달리다 멈춰 서서 삶을 지키기 위해 버텨낸 '생존의 시간'이었습니다. 채용 시장에서도 최근에는 무조건적인 흠집으로 보기보다, 그 시간 동안 무엇을 돌아보고 어떤 마음가짐으로 현장에 복귀하려 하는지를 훨씬 중요하게 봅니다.

이력서나 면접에서는 "몸과 마음을 재정비하고 새로운 직무 역량을 다지는 전환기였습니다"라고 당당하게 프레이밍할 수 있습니다. 

🌱 [마음 처방]
"빈 칸에 짓눌리지 마세요. 이력서의 공백은 내 삶의 공백이 아니라, 다음 도약을 위해 숨을 고른 자리입니다."

🪜 [현실의 한 걸음]
완벽한 이력서를 쓰려고 하지 마세요. 오늘 당장은 이력서 양식을 열어보지 않아도 좋습니다. 대신 메모장에 '내가 지난 세월 동안 잘해냈던 사소한 일 3가지'만 적어보세요. 그 작은 불씨부터 살려봅시다.
                """.trimIndent()
                CounselorReply(
                    fullResponse = full,
                    emotionalComfort = "빈 칸에 짓눌리지 마세요. 이력서의 공백은 내 삶의 공백이 아니라, 다음 도약을 위해 숨을 고른 자리입니다.",
                    practicalAdvice = "오늘 당장은 이력서 양식을 열지 않아도 좋습니다. 메모장에 '내가 과거에 잘 해냈던 일 3가지'만 간단히 적어보세요."
                )
            }

            trimmed.contains("나이") || trimmed.contains("늦") || trimmed.contains("탈락") || trimmed.contains("자존감") -> {
                val full = """
서진우 상담사입니다. 보내주신 글에서 전해지는 허탈함과 씁쓸함에 가슴이 참 먹먹합니다.
서류 지원 버튼을 누르고 '탈락'이라는 글자를 마주할 때마다, 마치 내 모든 인생이 부정당하는 것 같고 "나이만 먹고 난 뭘 했나" 하는 자책이 파도처럼 밀려오셨을 겁니다.

하지만 냉정하게 말씀드리면, 지금의 탈락은 당신의 인간적 가치가 모자라서가 아닙니다. 20대 신입 공채 기준의 채용 시장 잣대에 당신을 억지로 끼워 맞췄기 때문일 가능성이 매우 높습니다. 

나이가 있다는 것은 단점이 아니라, '조직 내 갈등을 겪어본 관록', '돌발 상황에 흔들리지 않는 인내력', '젊은 세대가 채우기 힘든 진중한 책임감'이라는 강력한 무기입니다. 신입 채용 시장이 아닌, 중장년층을 우대하거나 나이에 구애받지 않는 공공기관 공무직, 시설·안전관리, 중소·강소기업의 실무 운영직 등으로 시야를 돌리면 당신의 연륜은 신뢰가 됩니다.

🌱 [마음 처방]
"당신의 세월은 낭비된 시간이 아니라, 그 어떤 청년도 가지지 못한 삶의 지혜와 굳은살입니다. 나이 때문에 스스로를 깎아내리지 마세요."

🪜 [현실의 한 걸음]
잡코리아/사람인 대신, 정부가 운영하는 '중장년 일자리 희망센터' 또는 '워크넷 신중년 특화관'에 접속해 보세요. 나이 제한 없이 성실성과 책임감을 최우선으로 보는 채용 공고 3개만 가볍게 읽어보시길 권합니다.
                """.trimIndent()
                CounselorReply(
                    fullResponse = full,
                    emotionalComfort = "당신의 세월은 낭비된 시간이 아니라, 그 어떤 청년도 가지지 못한 삶의 지혜와 굳은살입니다.",
                    practicalAdvice = "워크넷 '신중년 일자리' 코너나 '중장년내일센터'에 방문하여 나이 제한이 없는 현실적인 직무 공고 3곳을 살펴보세요."
                )
            }

            trimmed.contains("가족") || trimmed.contains("눈치") || trimmed.contains("방") || trimmed.contains("집") || trimmed.contains("숨") -> {
                val full = """
안녕하세요, 내담자님. 서진우 상담사입니다.
방문 밖에서 들리는 가족들의 발소리, 숟가락 부딪히는 소리마저 죄스럽고 숨이 턱 막히는 그 심정... 제가 수많은 내담자분들을 만나며 가장 가슴 아프게 듣는 이야기 중 하나입니다.

가족들을 사랑하고 미안하기 때문에 더 숨고 싶고, "언제까지 저러고 있을 거냐"는 무언의 시선이 비수처럼 꽂히셨을 겁니다.
하지만 가족들도 당신을 미워해서가 아니라, 어떻게 도와주어야 할지 몰라 서툴러서 그런 침묵이 생기는 경우가 많습니다.

무엇보다 가장 위험한 것은 '자발적 고립'입니다. 방 안에만 갇혀 있으면 뇌는 끝없는 자책 루프를 돌며 자존감을 갉아먹습니다. 밥 한 끼라도 당당하게 드셔도 됩니다. 당신은 죄를 지은 게 아니라 구직의 긴 터널을 지나고 있을 뿐입니다.

🌱 [마음 처방]
"가족에게 미안한 마음이 들 때마다, 그 마음을 나를 채찍질하는 데 쓰지 말고 '오늘을 견뎌낼 온기'로 바꿔 안아주세요."

🪜 [현실의 한 걸음]
내일 오전, 가족들이 활동하는 시간이나 아침 일찍 집 근처 도서관이나 산책로로 나가보세요. 단 1시간이라도 바깥 공기를 마시며 '나만의 안전한 피난처 공간'을 확보하는 것부터 시작합시다.
                """.trimIndent()
                CounselorReply(
                    fullResponse = full,
                    emotionalComfort = "가족에게 미안한 마음이 들 때마다, 그 마음을 나를 채찍질하는 데 쓰지 말고 '오늘을 견뎌낼 온기'로 바꿔 안아주세요.",
                    practicalAdvice = "내일 아침에는 집 근처 공공도서관이나 동네 산책로로 나가 1시간 동안 햇볕을 쬐며 나만의 공간을 가져보세요."
                )
            }

            trimmed.contains("돈") || trimmed.contains("생계") || trimmed.contains("생활비") || trimmed.contains("잔고") || trimmed.contains("빚") -> {
                val full = """
서진우 상담사입니다.
통장 잔고가 줄어드는 것을 볼 때 찾아오는 공포는 말로 다 표현할 수 없지요. 당장 다음 달 공과금, 식비가 위태로워지면 사람은 장기적인 이력서나 미래를 생각할 마음의 여유가 완전히 사라집니다. '터널 시야'에 빠져 불안감에 압도되는 게 당연합니다.

이럴 때일수록 거창한 꿈보다 '생계의 최소 안전판'을 먼저 만드는 게 최우선 순위입니다.
많은 분들이 모르고 지나치시는데, 고용노동부의 '국민취업지원제도'에 참여하면 매월 50만 원씩 최대 6개월(총 300만 원) 동안 구직촉진수당을 지원받을 수 있습니다. 또한 당장 생계가 곤란한 경우 지자체 긴급복지지원이나 공공근로(희망근로)를 통해 최소한의 캐시플로우를 확보할 수 있습니다.

돈이 떨어져 가는 것은 부끄러운 일이 아니며, 국가가 마련해둔 사회 안전망을 적극적으로 딛고 올라서는 것은 국민의 당당한 권리입니다.

🌱 [마음 처방]
"경제적 궁핍이 당신의 인격이나 능력의 결함이 아닙니다. 지금은 비상 상황이고, 긴급 구호 물품을 받듯 제도의 도움을 받아도 괜찮습니다."

🪜 [현실의 한 걸음]
지금 바로 고용노동부 고객상담센터(국번없이 1350)에 전화하거나, '국민취업지원제도' 홈페이지에서 내가 수당 지급 대상(1유형/2유형)인지 모의 진단을 확인해 보세요.
                """.trimIndent()
                CounselorReply(
                    fullResponse = full,
                    emotionalComfort = "경제적 궁핍이 당신의 인격이나 능력의 결함이 아닙니다. 지금은 국가의 안전망을 딛고 올라설 권리가 있습니다.",
                    practicalAdvice = "고용노동부 1350번으로 전화해 '국민취업지원제도 구직촉진수당' 자격 요건을 문의해 보세요."
                )
            }

            trimmed.contains("직무") || trimmed.contains("일자리") || trimmed.contains("자격증") || trimmed.contains("추천") || trimmed.contains("어떤 일") -> {
                val full = """
안녕하세요, 내담자님. 현실적인 진로를 함께 고민해 드리는 서진우 상담사입니다.
나이가 든 상태에서 새로운 일을 찾을 때 가장 중요한 원칙은 두 가지입니다:
첫째, '청년들과 같은 트랙에서 스펙으로 경쟁하지 않는다.'
둘째, '경험의 성숙도와 신뢰성이 우선시되는 시장을 타깃팅한다.'

현실적으로 30대 후반~50대에서 재진입 및 안착 성공률이 높은 분야를 짚어드리겠습니다:
1. **공공기관·지자체 공무직/기간제 근로자**: 나이 블라인드 평가가 철저하며 정년 보장 또는 비교적 안정적인 근무 환경이 주어집니다 (사무행정보조, 시설관리, 공원/환경 관리 등).
2. **시설 및 안전 관리 분야**: '전기기능사', '소방안전관리자 2급', '에너지관리기능사' 등은 나이와 상관없이 정년 이후까지 수요가 꾸준한 대표적 알짜 자격증입니다.
3. **물류·유통·생산 관리**: 단순 노무가 아닌 현장 반장/자재 관리/출하 검수 등은 성실함과 위기대처 능력을 지닌 중장년층을 선호합니다.
4. **사회복지 및 돌봄 케어**: '사회복지사 2급'이나 '요양보호사' 취득 후 복지관, 주간보호센터, 노인/장애인 기관에서 60대 이후까지 꾸준히 일할 수 있습니다.

국비 지원 '내일배움카드'를 발급받으면 학원비의 80~100%를 지원받으며 훈련수당까지 받으며 배울 수 있습니다.

🌱 [마음 처방]
"화려한 간판 대신, 내 삶을 단단하게 지탱해 줄 튼튼한 흙길을 찾는 과정입니다. 그 어떤 정직한 노동도 귀하지 않은 것은 없습니다."

🪜 [현실의 한 걸음]
직업훈련포털(HRD-Net)에서 거주 지역의 '국비지원 훈련 과정'을 검색해 보세요. '전기', '조경', '사회복지', '물류' 중 마음에 닿는 키워드 하나를 검색해 보는 것부터 시작합시다.
                """.trimIndent()
                CounselorReply(
                    fullResponse = full,
                    emotionalComfort = "화려한 간판 대신, 내 삶을 단단하게 지탱해 줄 튼튼한 흙길을 찾는 과정입니다. 정직한 노동은 모두 귀합니다.",
                    practicalAdvice = "직업훈련포털(HRD-Net)에 접속하여 거주지 인근의 내일배움카드 무료 교육 과정을 1개 찾아보세요."
                )
            }

            trimmed.contains("무기력") || trimmed.contains("우울") || trimmed.contains("자책") || trimmed.contains("아무것도") || trimmed.contains("힘들") || trimmed.contains("지쳤") -> {
                val full = """
안녕하세요, 내담자님. 서진우 상담사입니다.
글자를 읽어 내려가는데 당신의 깊은 한숨과 무거운 어깨가 그대로 느껴져 가슴이 뭉클합니다.
"오늘도 아무것도 못하고 침대에만 누워있었어", "난 왜 이 모양일까..." 하며 스스로에게 가장 모진 말을 퍼붓고 계셨지요?

하지만 제가 단언컨대 말씀드립니다. 당신이 지금 무기력한 것은 게을러서가 아니라, 당신의 뇌와 몸이 "더 이상은 버틸 에너지가 바닥났다"며 보내는 긴급 구조 신호(SOS)입니다.
스마트폰 배터리가 1%일 때 무거운 게임을 돌릴 수 없듯이, 방전된 상태에서는 이력서 한 줄도 쓸 수 없는 게 과학적으로 지극히 정상입니다.

오늘 하루 아무것도 하지 못했어도 괜찮습니다. 당신은 오늘 하루 동안에도 '살아내는 것'만으로 엄청난 에너지를 썼습니다. 스스로를 처벌하는 법관이 되지 마시고, 칭찬해 주는 든든한 친구가 되어주세요.

🌱 [마음 처방]
"지치는 것은 죄가 아닙니다. 너무 오랫동안 혼자서 너무 무거운 짐을 지고 오셨기에 잠시 주저앉은 것뿐입니다. 쉬어가도 괜찮습니다."

🪜 [현실의 한 걸음]
오늘은 구직 생각, 미래 걱정을 서랍 속에 완전히 넣어두세요. 대신 따뜻한 물을 한 컵 천천히 마시고, 어깨를 가볍게 털어내며 "오늘도 견뎌내느라 애썼다"고 소리 내어 자신에게 말해주세요.
                """.trimIndent()
                CounselorReply(
                    fullResponse = full,
                    emotionalComfort = "지치는 것은 죄가 아닙니다. 너무 오랫동안 혼자 무거운 짐을 지고 오셨기에 방전된 것뿐입니다.",
                    practicalAdvice = "따뜻한 물 한 잔을 천천히 마시고, 거울을 보며 '오늘도 잘 버텼다'고 나 자신에게 따뜻하게 한마디 건네보세요."
                )
            }

            else -> {
                val full = """
서진우 상담사입니다. 속마음을 털어놓아 주셔서 고맙습니다.
인생의 긴 여정 속에서 지금 마주한 정체기는 참으로 아프고 시린 시간입니다. 남들은 다 앞으로 달려가는 것 같은데 나만 뒤처져 멈춰 서 있는 것 같은 고독감이 드셨을 겁니다.

하지만 심리학에서는 이를 '잠복기(Incubation)'라고 부릅니다. 씨앗이 땅속 어둠 속에서 썩어가는 것처럼 보여도, 실은 땅속 깊이 뿌리를 내리는 시간이듯 말입니다. 
우리는 조급함을 조금 내려놓고, 하나씩 현실적인 단계를 밟아갈 것입니다. 저 서진우가 당신의 곁에서 현실적인 나침반이 되어 드리겠습니다.

🌱 [마음 처방]
"당신의 인생 시계는 고장 난 것이 아니라, 당신만의 계절을 맞이하기 위해 시간을 맞추고 있는 중입니다."

🪜 [현실의 한 걸음]
지금 머릿속에 맴도는 막연한 걱정거리 중 '내가 지금 당장 바꿀 수 없는 것'과 '지금 당장 할 수 있는 사소한 일'을 종이에 한 줄씩 나누어 적어보세요.
                """.trimIndent()
                CounselorReply(
                    fullResponse = full,
                    emotionalComfort = "당신의 인생 시계는 고장 난 것이 아니라, 당신만의 계절을 맞이하기 위해 시간을 맞추고 있는 중입니다.",
                    practicalAdvice = "머릿속 걱정 중 '지금 당장 내가 할 수 있는 작은 일' 한 가지만 골라 적어보세요."
                )
            }
        }
    }

    fun generateSessionSummary(
        conversationText: String,
        recentUserTopic: String
    ): CounselingSessionEntity {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

        val isAgeTopic = conversationText.contains("나이") || recentUserTopic.contains("나이")
        val isGapTopic = conversationText.contains("공백") || recentUserTopic.contains("공백")
        val isMoneyTopic = conversationText.contains("돈") || conversationText.contains("생계")
        val isLethargy = conversationText.contains("무기력") || conversationText.contains("자책")

        val title = when {
            isAgeTopic -> "나이라는 장벽에 대한 불안과 관록의 재발견"
            isGapTopic -> "길어진 공백기의 자책감과 현실적 소명 전략"
            isMoneyTopic -> "줄어드는 생계비에 대한 공포와 제도적 안전망 점검"
            isLethargy -> "방전된 심신에 대한 수용과 무기력 탈출"
            else -> "마음의 무게 덜어내기와 현실적 구직 첫걸음"
        }

        val primaryConcern = when {
            isAgeTopic -> "나이 때문에 계속 서류에서 탈락하여 인생 전체가 부정당하는 기분"
            isGapTopic -> "공백기가 길어져 이력서 제출 자체가 두렵고 사회 복귀가 막막함"
            isMoneyTopic -> "통장 잔고가 줄어들어 당장 다음 달 생계비와 미래가 극도로 불안함"
            isLethargy -> "하루 종일 침대에 누워 자책만 반복하며 의욕이 상실된 상태"
            else -> "오랜 구직 정체기로 인한 고립감과 자존감 저하"
        }

        val summary = """
오랜 시간 고립된 상태에서 쌓여온 불안과 자책감을 솔직하게 표현하며, 문제의 원인을 개인의 무능이 아닌 구조적 채용 시장의 현실과 연결지어 이해하기 시작했습니다. 
무리하게 서두르기보다 당장의 심리적 방전을 회복하고, 나이에 맞는 현실적인 진입 트랙을 탐색하기로 합의했습니다.
        """.trimIndent()

        val insight = """
내담자는 완벽주의와 주변의 기대에 짓눌려 스스로를 가혹하게 비판하고 있었습니다. 
하지만 이는 에너지가 고갈된 상태에서의 자연스러운 뇌의 방어 기제이며, 실패가 아닌 '생애 재정비기'로 프레이밍을 전환할 때 비로소 실행력이 생겨납니다.
        """.trimIndent()

        val actionSteps = """
1. 매일 오전 8시 기상 후 따뜻한 물 한 잔과 20분 햇볕 산책 실천하기
2. 고용노동부 국민취업지원제도(1350)를 통해 월 50만원 구직촉진수당 자격 확인하기
3. 완벽한 이력서 대신, 내가 과거에 끈기 있게 해냈던 사소한 경험 3가지를 메모장에 적어보기
        """.trimIndent()

        return CounselingSessionEntity(
            dateString = today,
            title = title,
            primaryConcern = primaryConcern,
            initialMood = if (isLethargy) "무기력" else "불안/자책",
            finalMood = "담담함/안도",
            coreSummary = summary,
            counselorInsight = insight,
            actionSteps = actionSteps,
            messageCount = 6,
            timestamp = System.currentTimeMillis()
        )
    }

    fun generatePersonalDiagnosis(
        ageGroup: String,
        gapPeriod: String,
        careerField: String,
        urgentHurdle: String,
        recentMood: String
    ): PersonalDiagnosisEntity {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())

        val psychAnalysis = """
내담자님은 현재 [$ageGroup]의 연령대에서 [$gapPeriod] 동안 누적된 고립감과 [$urgentHurdle] 문제로 인해 높은 수준의 심리적 번아웃을 겪고 계십니다. 
특히 최근 마음 날씨가 [$recentMood] 상태로 나타나는 것은, 오랜 기간 타인과의 비교 및 자책으로 인해 자아존중감이 심각하게 위축되어 있음을 보여줍니다. 
하지만 결코 의지 박약이 아니며, 지친 뇌와 신체가 휴식을 갈망하는 정직한 생리적 신호입니다.
        """.trimIndent()

        val strengths = """
1. **위기관리 및 묵묵한 지구력**: 과거 [$careerField] 분야를 포함해 인생의 여러 굴곡을 버텨낸 저력은 20대 신입들이 흉내 낼 수 없는 강력한 연륜입니다.
2. **조직 융화 및 갈등 둔감력**: 산전수전을 겪으며 다져진 감정 조절 능력은 관리자 및 실무 운영진에게 깊은 안정감을 제공합니다.
3. **진중한 근태 신뢰성**: 단기 이직이 잦은 젊은 층에 비해, 안정적인 터전을 원하는 책임감 있는 태도는 중장년 우대 직무에서 가장 높게 평가받는 자산입니다.
        """.trimIndent()

        val recommendedJobs = """
• **1순위 공공/준정부 영역**: 공공기관 공무직(블라인드 채용), 지자체 시설/환경 관리, 공공근로 및 신중년 디딤돌 일자리
• **2순위 현실적 알짜 자격 트랙**: 국비지원(내일배움카드)을 통한 [소방안전관리자 2급], [전기기능사], [지게차운전], [사회복지사 2급]
• **3순위 민간 안정 직무**: 강소기업 총무·자재관리, 물류 거점 센터 운영 검수, 시니어/케어 서비스 코디네이터
• **긴급 생계 안전망**: 고용노동부 국민취업지원제도 1유형(구직촉진수당 월 50만원×6개월) 즉시 신청 권장
        """.trimIndent()

        val threeStepPlan = """
[1주차 : 심신 회복 및 생계 방어선 구축]
- 밤낮 바뀐 생활 청산: 기상 알람 고정 & 매일 낮 30분 산책
- 고용노동부(1350) 연락하여 국민취업지원제도 수당 수급 자격 심사 접수

[2주차 : 나이/공백기 재구성 및 직무 압축]
- 공백기를 '가족 간병/자기정비 및 직무 전환 준비기'로 소명하는 1문장 작성
- HRD-Net 직업훈련포털에서 거주지 근처 국비 무료 교육 과정 2개 찜하기

[3주차 : 담담한 지원 루틴 만들기]
- 워크넷 '신중년/중장년 우대 채용관'에서 나이 제한 없는 공고 3곳 선별
- 결과에 상처받지 않는 마음가짐으로 주 2회 화/목 규칙적 지원 습관 정착
        """.trimIndent()

        val quote = "당신의 세월은 헛되지 않았습니다. 잠시 겨울을 지나고 있을 뿐, 당신만의 단단한 봄이 곧 시작됩니다."

        return PersonalDiagnosisEntity(
            dateString = today,
            userAgeGroup = ageGroup,
            userGapPeriod = gapPeriod,
            userCareerField = careerField,
            userUrgentHurdle = urgentHurdle,
            psychologicalAnalysis = psychAnalysis,
            reconstructedStrengths = strengths,
            recommendedJobTracks = recommendedJobs,
            threeStepActionPlan = threeStepPlan,
            encouragementQuote = quote,
            timestamp = System.currentTimeMillis()
        )
    }
}
