package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.FallbackCounselorEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("다시봄", appName)
  }

  @Test
  fun `counselor generates realistic advice for gap year concern`() {
    val reply = FallbackCounselorEngine.generateCounselingResponse("공백기가 너무 길어져서 두려워요")
    assertNotNull(reply.emotionalComfort)
    assertNotNull(reply.practicalAdvice)
    assertTrue(reply.fullResponse.contains("서진우"))
  }

  @Test
  fun `counselor generates session summary and personal diagnosis`() {
    val summary = FallbackCounselorEngine.generateSessionSummary("나이 때문에 계속 탈락해요", "나이 장벽")
    assertNotNull(summary.title)
    assertTrue(summary.actionSteps.isNotBlank())

    val diagnosis = FallbackCounselorEngine.generatePersonalDiagnosis(
      ageGroup = "40대 초반",
      gapPeriod = "1년 ~ 2년",
      careerField = "일반사무",
      urgentHurdle = "생계비 부족",
      recentMood = "불안"
    )
    assertNotNull(diagnosis.psychologicalAnalysis)
    assertNotNull(diagnosis.recommendedJobTracks)
    assertNotNull(diagnosis.threeStepActionPlan)
  }
}
