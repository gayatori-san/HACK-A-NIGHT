package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.PatternAnalysisData
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PatternAnalysisModal(
    visible: Boolean,
    isAnalyzing: Boolean,
    insightsData: PatternAnalysisData?,
    handledHabits: Set<String>,
    onClose: () -> Unit,
    onAnalyze: () -> Unit,
    onHabitAction: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xD908080A))
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(32.dp, RoundedCornerShape(28.dp), spotColor = Color(0x66000000))
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(
                                Color(0xF5241445),
                                Color(0xF5161326),
                                Color(0xF50D0C14)
                            )
                        )
                    )
                    .border(1.dp, GlassBorderGradient, RoundedCornerShape(28.dp))
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Back Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0x1AFFFFFF))
                        .border(0.8.dp, Color(0x26FFFFFF), RoundedCornerShape(18.dp))
                        .clickable { onClose() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("pattern_back_button"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BACK",
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Header with Glowing Ring
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x26FFFFFF))
                            .border(1.dp, GlassBorderGradient, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(NeonLime)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pattern Analysis ✨",
                                fontFamily = HelveticaFontFamily,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = (-0.3).sp
                            )
                            Text(
                                text = "Just now",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Review your psychological spending habits. Adopt good habits and dump leakages to level up!",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action area
                if (insightsData == null && !isAnalyzing) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(com.example.ui.theme.PhonePePurple)
                            .clickable { onAnalyze() }
                            .padding(vertical = 15.dp)
                            .testTag("analyze_habits_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Analyze My Habits",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.2).sp
                        )
                    }
                }

                if (isAnalyzing) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x0FFFFFFF))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = com.example.ui.theme.PhonePeLightPurple,
                            modifier = Modifier.size(36.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "DECODING SPENDING HABITS...",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                if (insightsData != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                        // Strengths section
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "STRENGTHS",
                                    fontFamily = HelveticaFontFamily,
                                    color = EmeraldGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            insightsData.goodHabits.forEach { habit ->
                                if (!handledHabits.contains(habit)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0x1410B981))
                                            .border(1.dp, Color(0x2610B981), RoundedCornerShape(14.dp))
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "• $habit",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                            lineHeight = 16.sp,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(EmeraldGreen)
                                                .clickable { onHabitAction(habit, false) }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                                .testTag("adopt_habit_${habit.take(5)}"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "ADOPT ⚡",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.Black,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            if (insightsData.goodHabits.all { handledHabits.contains(it) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x0AFFFFFF))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "All adopted! Incredible discipline.",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Leakages section
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(CoralRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LEAKAGES",
                                    fontFamily = HelveticaFontFamily,
                                    color = CoralRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            insightsData.badHabits.forEach { habit ->
                                if (!handledHabits.contains(habit)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(Color(0x14F43F5E))
                                            .border(1.dp, Color(0x26F43F5E), RoundedCornerShape(14.dp))
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "• $habit",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                            lineHeight = 16.sp,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0x1FFFFFFF))
                                                .border(1.dp, Color(0x2BFFFFFF), RoundedCornerShape(8.dp))
                                                .clickable { onHabitAction(habit, true) }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                                .testTag("dump_habit_${habit.take(5)}"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "DUMP 🗑️",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            if (insightsData.badHabits.all { handledHabits.contains(it) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x0AFFFFFF))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "All dumped! You're unstoppable.",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
