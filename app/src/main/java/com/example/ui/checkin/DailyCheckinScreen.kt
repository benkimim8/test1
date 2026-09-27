package com.example.ui.checkin

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.NordicWalking
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.MainViewModel

data class MoodOption(
    val key: String,
    val label: String,
    val emoji: String,
    val score: Int
)

@Composable
fun DailyCheckinScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val todayCheckin by viewModel.todayCheckin.collectAsStateWithLifecycle()
    val allCheckins by viewModel.allCheckins.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val moodOptions = remember {
        listOf(
            MoodOption("TIRED", "무기력", "🌫️", 1),
            MoodOption("ANXIOUS", "깊은 불안", "🌧️", 2),
            MoodOption("NUMB", "답답함", "☁️", 3),
            MoodOption("CALM", "담담함", "🍃", 4),
            MoodOption("HOPEFUL", "한 줄기 희망", "☀️", 5)
        )
    }

    var selectedMoodKey by remember(todayCheckin) {
        mutableStateOf(todayCheckin?.mood ?: "ANXIOUS")
    }

    var noteText by remember(todayCheckin) {
        mutableStateOf(todayCheckin?.selfCompassionNote ?: "")
    }

    val selfCompassionSuggestions = remember {
        listOf(
            "오늘 하루도 버텨낸 나 자신 수고했다.",
            "서류 탈락은 내 탓이 아닌 시장 상황일 뿐이다.",
            "조급해 말고 하루 한 걸음씩만 나아가자.",
            "맛있는 밥 챙겨먹고 햇볕 쬐며 산책하자."
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(
                        text = "오늘의 마음 날씨 & 작은 한 걸음",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "거창한 목표 대신, 오늘 하루 나를 지켜내는 3가지 최소한의 실천",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Mood Weather Selector
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "오늘 내 마음의 날씨는 어떤가요?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        moodOptions.forEach { option ->
                            val isSelected = selectedMoodKey == option.key
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedMoodKey = option.key
                                        viewModel.saveMood(option.key, noteText)
                                    }
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 12.dp)
                                    .testTag("mood_${option.key}")
                            ) {
                                Text(text = option.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = option.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3 Micro Habits
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "오늘의 생존 마이크로 루틴 (3가지)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "도파민과 신체 리듬을 회복하여 무기력의 늪에서 건져올립니다",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val walkDone = todayCheckin?.habitWalkDone == true
                    val waterDone = todayCheckin?.habitWaterDone == true
                    val oneStepDone = todayCheckin?.habitOneStepDone == true

                    HabitItemRow(
                        icon = Icons.Default.WbSunny,
                        title = "햇볕 쬐며 20분 이상 산책하기",
                        subtitle = "세로토닌 분비 촉진 & 뇌의 자책 회로 일시 정지",
                        isDone = walkDone,
                        onToggle = { viewModel.toggleHabit("walk") },
                        testTag = "habit_walk"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HabitItemRow(
                        icon = Icons.Default.LocalDrink,
                        title = "기상 후 따뜻한 물 한 잔 천천히 마시기",
                        subtitle = "밤새 굳어있던 몸을 깨우고 자율신경계 안정",
                        isDone = waterDone,
                        onToggle = { viewModel.toggleHabit("water") },
                        testTag = "habit_water"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HabitItemRow(
                        icon = Icons.Default.Favorite,
                        title = "자책 대신 '오늘도 잘 버텼다' 자신에게 말하기",
                        subtitle = "스스로를 향한 비난을 멈추고 든든한 내 편 되어주기",
                        isDone = oneStepDone,
                        onToggle = { viewModel.toggleHabit("onestep") },
                        testTag = "habit_onestep"
                    )
                }
            }
        }

        // Self Compassion Note
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "나를 위한 마음 토닥임 노트",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "누구에게도 말하지 못한 속마음이나, 오늘 스스로에게 해주고 싶은 말을 적어보세요",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick self-compassion chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(selfCompassionSuggestions) { phrase ->
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable { noteText = phrase }
                            ) {
                                Text(
                                    text = phrase,
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = {
                            Text(
                                "예: 오늘도 서류 탈락을 봤지만 너무 좌절하지 말자. 내 잘못이 아니라 시장 상황일 뿐이다. 맛있는 밥 한 끼 챙겨먹자.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("self_compassion_input"),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.saveMood(selectedMoodKey, noteText)
                            Toast.makeText(context, "오늘의 마음 체크가 기록되었습니다", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_checkin_button")
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("오늘 기록 저장하기", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Recent Checkins
        if (allCheckins.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "내가 묵묵히 버텨온 날들",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(allCheckins.take(5), key = { it.dateString }) { checkin ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = checkin.dateString,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        val emoji = when (checkin.mood) {
                            "TIRED" -> "🌫️ 무기력"
                            "ANXIOUS" -> "🌧️ 깊은 불안"
                            "NUMB" -> "☁️ 답답함"
                            "CALM" -> "🍃 담담함"
                            "HOPEFUL" -> "☀️ 희망"
                            else -> "🍃 담담함"
                        }

                        Text(text = emoji, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = checkin.selfCompassionNote.ifBlank { "루틴 실천 완료" },
                            fontSize = 12.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isDone: Boolean,
    onToggle: () -> Unit,
    testTag: String
) {
    Surface(
        color = if (isDone) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onToggle() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDone) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isDone) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Checkbox(
                checked = isDone,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
