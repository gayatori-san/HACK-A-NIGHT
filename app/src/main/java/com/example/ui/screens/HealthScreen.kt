package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.HealthTip
import com.example.data.models.SwapTip
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralRed
import com.example.ui.theme.DeepPurple1
import com.example.ui.theme.DeepPurple2
import com.example.ui.theme.DeepPurple3
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState
import kotlin.math.roundToInt

@Composable
fun HealthScreen(
    uiState: WalletUiState,
    swapList: List<SwapTip>,
    healthTips: List<HealthTip>,
    onClaimSwap: (SwapTip) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentSwap = swapList.getOrNull(uiState.swapIndex) ?: swapList.firstOrNull()
    val totalMinutesLost = ((uiState.junkFoodSpend / 50.0) * 12.0).roundToInt()
    val hoursLost = totalMinutesLost / 60
    val minutesLost = totalMinutesLost % 60

    val veggieLabel = when (uiState.veggieLevel) {
        0 -> "Low"
        1 -> "Good"
        2 -> "High"
        else -> "Max"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("health_screen_lazy_column")
    ) {
        // 1. Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Health-Spending",
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.8).sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "❤️", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Track your nutritional ROI and avoid fast-food traps.",
                    fontFamily = HelveticaFontFamily,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // 2. Veggie & Protein Score Dual Hero Cards
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(DeepPurple1, DeepPurple2, DeepPurple3)
                        )
                    )
                    .border(1.dp, GlassBorderGradient, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Veggie Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0x24FFFFFF))
                            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(18.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "VEGGIE INTAKE",
                                fontFamily = HelveticaFontFamily,
                                color = Color(0xCCFFFFFF),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = veggieLabel,
                                    fontFamily = HelveticaFontFamily,
                                    color = if (uiState.veggieLevel > 0) EmeraldGreen else Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "🥬", fontSize = 18.sp)
                            }
                        }
                    }

                    // Protein Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0x24FFFFFF))
                            .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(18.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "PROTEIN SCORE",
                                fontFamily = HelveticaFontFamily,
                                color = Color(0xCCFFFFFF),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${uiState.proteinScore}/10",
                                    fontFamily = HelveticaFontFamily,
                                    color = if (uiState.proteinScore >= 8) EmeraldGreen else NeonLime,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(text = "🥚", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }

        // 3. Nutritional Gap Warning
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xD9171418))
                    .border(1.dp, Color(0x26F43F5E), RoundedCornerShape(22.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CoralRed.copy(alpha = 0.2f))
                                .border(1.dp, CoralRed.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "!", color = CoralRed, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Nutritional Gap Warning",
                                fontFamily = HelveticaFontFamily,
                                color = CoralRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "You've spent ${uiState.junkPercentage}% of your food budget on junk this week.",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color(0x26F43F5E))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(uiState.junkPercentage / 100f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(CoralRed)
                        )
                    }
                }
            }
        }

        // 4. Life Lost to Junk Interactive Mortality Graph
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xD9131317))
                    .border(1.dp, GlassBorderGradient, RoundedCornerShape(22.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Life Lost to Junk 💀",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = (-0.2).sp
                        )
                        Text(
                            text = "-${hoursLost}h ${minutesLost}m",
                            fontFamily = HelveticaFontFamily,
                            color = CoralRed,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp
                        )
                    }

                    Text(
                        text = "SCIENTIFIC MORTALITY GRAPH",
                        fontFamily = HelveticaFontFamily,
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                    )

                    // 7-day Bar Graph
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(96.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val days = listOf("M", "T", "W", "T", "F", "S", "Now")
                        val barRatios = listOf(0.40f, 0.75f, 0.45f, 0.90f, 0.60f, 0.85f, 0.55f)

                        days.forEachIndexed { i, day ->
                            val ratio = barRatios[i]
                            val isDanger = ratio > 0.75f
                            val isWarn = ratio > 0.50f
                            val barColor = if (isDanger) CoralRed else if (isWarn) NeonLime else EmeraldGreen

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(ratio)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(barColor)
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = day,
                                    fontFamily = HelveticaFontFamily,
                                    color = if (i == 6) NeonLime else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val commentary = when {
                        uiState.junkFoodSpend > 2000 -> "Bro is doing a speedrun to the afterlife."
                        uiState.junkFoodSpend > 1000 -> "Your doctor would like a word."
                        uiState.junkFoodSpend > 400 -> "Your arteries are slightly annoyed."
                        else -> "You're mostly surviving. Good job."
                    }

                    Text(
                        text = commentary,
                        fontFamily = HelveticaFontFamily,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        // 5. Smart Nutrition Swap Card
        item {
            if (currentSwap != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Goal: ${uiState.userData.healthGoal}",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = (-0.2).sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x14FFFFFF))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${uiState.xp} / 500 XP",
                                fontFamily = HelveticaFontFamily,
                                color = NeonLime,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xD914141A))
                            .border(1.dp, GlassBorderGradient, RoundedCornerShape(22.dp))
                            .padding(16.dp)
                            .testTag("health_swap_card")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                    Text(text = currentSwap.badIcon, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = currentSwap.bad,
                                        fontFamily = HelveticaFontFamily,
                                        color = TextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }

                                Text(text = "➡️", fontSize = 18.sp, color = TextMuted)

                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                    Text(text = currentSwap.goodIcon, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = currentSwap.good,
                                        fontFamily = HelveticaFontFamily,
                                        color = EmeraldGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "SAVE: ${currentSwap.save}",
                                        fontFamily = HelveticaFontFamily,
                                        color = EmeraldGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "BENEFIT: ${currentSwap.benefit}",
                                        fontFamily = HelveticaFontFamily,
                                        color = ElectricPurple,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(NeonLime)
                                        .clickable { onClaimSwap(currentSwap) }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                        .testTag("claim_swap_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Swap & Claim",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.Black,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Cheap Macro Index
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Cheap Macro Index",
                    fontFamily = HelveticaFontFamily,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.2).sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val items = listOf(
                        Triple("🥚", "Eggs", "₹7 / 6g Protein"),
                        Triple("🥜", "Soya Chunks", "₹10 / 52g Protein"),
                        Triple("🍌", "Bananas", "₹5 / Energy")
                    )

                    items.forEach { (icon, name, detail) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x0DFFFFFF))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Column {
                                Text(text = icon, fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = name, fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = detail, fontFamily = HelveticaFontFamily, color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // 7. Condition Budgets
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Text(
                    text = "Condition Budgets",
                    fontFamily = HelveticaFontFamily,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.2).sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                healthTips.forEach { tip ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0x0DFFFFFF))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = tip.icon, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = tip.title, fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x14FFFFFF))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "💰 ${tip.budget}", fontFamily = HelveticaFontFamily, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x14FFFFFF))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = "🔥 ${tip.cal}", fontFamily = HelveticaFontFamily, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                tip.foods.forEach { food ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x1A000000))
                                            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = food, fontFamily = HelveticaFontFamily, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Normal)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
