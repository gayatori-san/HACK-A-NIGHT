package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserData
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PhonePeAccentPurple
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(
    onComplete: (UserData) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1-5: Basic Baseline KYC
    // 6: Intermission / Checkpoint Choice (Proceed to Deep KYC or Complete Later via KYC Icon)
    // 7-10: Extensive Behavioral Questions
    // 11: Behavioral Calibration Summary
    var step by remember { mutableIntStateOf(1) }

    // Basic KYC State
    var allowanceStr by remember { mutableStateOf("5000") }
    var weakness by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var healthGoal by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("strict") }

    // Deep Behavioral KYC State
    var trigger by remember { mutableStateOf("🌙 Late Night Cravings (11 PM - 2 AM)") }
    var risk by remember { mutableStateOf("⚖️ Balanced Builder") }
    var delivery by remember { mutableStateOf("🍕 3–4 Times / Week") }
    var intervention by remember { mutableStateOf("⏱️ 5-Second Cooling Timer") }

    val isStepValid = when (step) {
        1 -> allowanceStr.toIntOrNull() != null && allowanceStr.toInt() > 0
        2 -> weakness.isNotBlank()
        3 -> goal.isNotBlank()
        4 -> healthGoal.isNotBlank()
        5 -> mode.isNotBlank()
        else -> true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Minimalist Ambient glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x266366F1), Color.Transparent),
                        radius = 700f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                // KYC Phase Header Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (step >= 7) Icons.Default.Psychology else Icons.Default.Badge,
                            contentDescription = "KYC Phase",
                            tint = if (step >= 7) EmeraldGreen else NeonLime,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (step <= 5) "KYC TIER 1: BASIC BASELINE" else if (step == 6) "KYC CHECKPOINT" else "KYC TIER 2: BEHAVIORAL DNA",
                            fontFamily = HelveticaFontFamily,
                            color = if (step >= 7) EmeraldGreen else NeonLime,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }

                    Text(
                        text = if (step <= 5) "Step $step of 5" else if (step == 6) "Verified" else "Step ${step - 6} of 4",
                        fontFamily = HelveticaFontFamily,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Indicator
                val maxBars = if (step <= 6) 5 else 4
                val activeBarIndex = if (step <= 5) step else if (step == 6) 5 else (step - 6)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    (1..maxBars).forEach { i ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (activeBarIndex >= i) (if (step >= 7) EmeraldGreen else NeonLime) else Color(0x24FFFFFF)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Step Content
                AnimatedContent(
                    targetState = step,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "onboarding_step"
                ) { currentStep ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        when (currentStep) {
                            1 -> {
                                Text(
                                    text = "Monthly Capital.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.8).sp
                                )
                                Text(
                                    text = "What is your monthly allowance or income?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 6.dp, bottom = 22.dp)
                                )

                                OutlinedTextField(
                                    value = allowanceStr,
                                    onValueChange = { allowanceStr = it.filter { ch -> ch.isDigit() } },
                                    leadingIcon = {
                                        Text(
                                            text = "₹",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextSecondary,
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontFamily = HelveticaFontFamily,
                                        color = TextPrimary,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    placeholder = {
                                        Text(text = "5000", fontFamily = HelveticaFontFamily, color = TextMuted, fontSize = 30.sp)
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(18.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PhonePeLightPurple,
                                        unfocusedBorderColor = BorderMedium,
                                        focusedContainerColor = Color(0x14FFFFFF),
                                        unfocusedContainerColor = Color(0x0AFFFFFF)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("onboarding_allowance_input")
                                )
                            }

                            2 -> {
                                Text(
                                    text = "Expense Drain.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.8).sp
                                )
                                Text(
                                    text = "Where does your discretionary budget usually go?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 6.dp, bottom = 22.dp)
                                )

                                val options = listOf(
                                    "Dining Out / Delivery",
                                    "Covering Others' Expenses",
                                    "Impulse Purchases",
                                    "Daily Coffee / Snacks"
                                )
                                options.forEach { option ->
                                    val isSelected = weakness == option
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .clickable { weakness = option }
                                            .padding(18.dp)
                                            .testTag("onboarding_weakness_${option.take(4)}"),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = option,
                                            fontFamily = HelveticaFontFamily,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            3 -> {
                                Text(
                                    text = "The Target.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.8).sp
                                )
                                Text(
                                    text = "What is your primary financial objective?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 6.dp, bottom = 22.dp)
                                )

                                val options = listOf(
                                    "Tech / Gadget Upgrade",
                                    "Emergency Fund",
                                    "Travel Savings",
                                    "Investment Portfolio"
                                )
                                options.forEach { option ->
                                    val isSelected = goal == option
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .clickable { goal = option }
                                            .padding(18.dp)
                                            .testTag("onboarding_goal_${option.take(4)}"),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = option,
                                            fontFamily = HelveticaFontFamily,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            4 -> {
                                Text(
                                    text = "Health Goal.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.8).sp
                                )
                                Text(
                                    text = "What is your primary fitness target?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 6.dp, bottom = 22.dp)
                                )

                                val options = listOf("Fat Loss", "Muscle Gain", "Energy")
                                options.forEach { option ->
                                    val isSelected = healthGoal == option
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .clickable { healthGoal = option }
                                            .padding(18.dp)
                                            .testTag("onboarding_health_${option.take(4)}"),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = option,
                                            fontFamily = HelveticaFontFamily,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            5 -> {
                                Text(
                                    text = "Advisor Persona.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.8).sp
                                )
                                Text(
                                    text = "How should Habit-Wallet AI advise you?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 6.dp, bottom = 22.dp)
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    val isStrict = mode == "strict"
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(if (isStrict) Color(0x336366F1) else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isStrict) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(18.dp)
                                            )
                                            .clickable { mode = "strict" }
                                            .padding(18.dp)
                                            .testTag("persona_strict")
                                    ) {
                                        Column {
                                            Text(
                                                text = "📊 Strict Accountability",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Direct, firm, and highly professional feedback to keep you disciplined.",
                                                fontFamily = HelveticaFontFamily,
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }

                                    val isGentle = mode == "gentle"
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(if (isGentle) Color(0x336366F1) else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isGentle) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(18.dp)
                                            )
                                            .clickable { mode = "gentle" }
                                            .padding(18.dp)
                                            .testTag("persona_gentle")
                                    ) {
                                        Column {
                                            Text(
                                                text = "🌱 Supportive CFO",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = "Calm, encouraging, and empathetic financial guidance.",
                                                fontFamily = HelveticaFontFamily,
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // 6: Intermission / Checkpoint Choice
                            6 -> {
                                GlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    backgroundColor = Color(0x331C1138),
                                    borderBrush = Brush.linearGradient(
                                        listOf(Color(0x80FFFFFF), PhonePeLightPurple, Color(0x30FFFFFF))
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(22.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x2200D09C))
                                                .border(1.5.dp, EmeraldGreen, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Success",
                                                tint = EmeraldGreen,
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Text(
                                            text = "Basic KYC Verified!",
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Your core financial framework is logged (+250 XP earned). To unlock high-precision behavioral intervention and personalized AI roasts, complete the Deep Behavioral Analysis now or anytime via the KYC Icon.",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextSecondary,
                                            fontSize = 13.sp,
                                            lineHeight = 18.sp
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))

                                        // Badge highlighting KYC icon availability
                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0x1A8B5CF6))
                                                .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Badge,
                                                contentDescription = "KYC Icon",
                                                tint = PhonePeAccentPurple,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "A KYC Icon will be available on your Home Screen",
                                                fontFamily = HelveticaFontFamily,
                                                color = SoftLavender,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }

                            // 7: Spending Vulnerability Triggers
                            7 -> {
                                Text(
                                    text = "Vulnerability Triggers.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.6).sp
                                )
                                Text(
                                    text = "When does your financial and dietary discipline break down?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                                )

                                val triggers = listOf(
                                    "🌙 Late Night Cravings (11 PM - 2 AM)" to "Insomnia and boredom scrolling",
                                    "🎉 Weekend Social Splurges" to "Group bills and peer pressure",
                                    "💼 Post-Work Fatigue & Stress" to "Comfort retail therapy after long hours",
                                    "🏷️ Flash Sales & Limited-Time Deals" to "Fear of missing out on bargains"
                                )

                                triggers.forEach { (option, desc) ->
                                    val isSelected = trigger == option
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .clickable { trigger = option }
                                            .padding(16.dp)
                                            .testTag("onboarding_trigger_${option.take(4)}"),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Column {
                                            Text(
                                                text = option,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color.White else TextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = desc,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color(0xCCFFFFFF) else TextMuted,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // 8: Risk & Money Psychology
                            8 -> {
                                Text(
                                    text = "Money Psychology & Risk.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.6).sp
                                )
                                Text(
                                    text = "How do you naturally approach capital allocation & growth?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                                )

                                val risks = listOf(
                                    "🛡️ Capital Guardian" to "Prioritizes emergency buffers, safety, and zero loss.",
                                    "⚖️ Balanced Builder" to "Systematic compounding, SIPs, and disciplined balance.",
                                    "🚀 Growth Maverick" to "High risk tolerance, momentum investments & equity bets.",
                                    "🧘 Mindful Stoic" to "Minimalist lifestyle, intentional spending & high savings."
                                )

                                risks.forEach { (option, desc) ->
                                    val isSelected = risk == option
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .clickable { risk = option }
                                            .padding(16.dp)
                                            .testTag("onboarding_risk_${option.take(4)}"),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Column {
                                            Text(
                                                text = option,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color.White else TextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = desc,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color(0xCCFFFFFF) else TextMuted,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // 9: Dining & Convenience Velocity
                            9 -> {
                                Text(
                                    text = "Convenience & Dining Velocity.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.6).sp
                                )
                                Text(
                                    text = "How often does food delivery or eating out replace home cooking?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                                )

                                val frequencies = listOf(
                                    "🛵 Daily Habit (Heavy Swiggy/Zomato)" to "High convenience dependency, eats ~40% of allowance.",
                                    "🍕 3–4 Times / Week" to "Moderate splurges during busy workdays.",
                                    "☕ Weekends Only" to "Disciplined weekdays, relaxed leisure weekends.",
                                    "🥗 Rarely / Disciplined Meal Prepper" to "Cooks at home, strict dietary control."
                                )

                                frequencies.forEach { (option, desc) ->
                                    val isSelected = delivery == option
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .clickable { delivery = option }
                                            .padding(16.dp)
                                            .testTag("onboarding_delivery_${option.take(4)}"),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Column {
                                            Text(
                                                text = option,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color.White else TextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = desc,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color(0xCCFFFFFF) else TextMuted,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // 10: Impulse Intervention Style
                            10 -> {
                                Text(
                                    text = "Impulse Intervention Style.",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.6).sp
                                )
                                Text(
                                    text = "When Habit-Wallet detects an impulse spend, what stops you?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                                )

                                val interventions = listOf(
                                    "⏱️ 5-Second Cooling Timer" to "Adds cognitive friction before payment authorization.",
                                    "🌶️ Savage AI Roast" to "Humorous, brutal habit roast highlighting your runway loss.",
                                    "🔒 Auto-Save Penalty (15% Lockbox)" to "Diverts a matching percentage into your investment vault.",
                                    "📊 Runway Depletion Gauge" to "Visualizes immediate days lost until zero-cash."
                                )

                                interventions.forEach { (option, desc) ->
                                    val isSelected = intervention == option
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) ElectricPurple else Color(0x0DFFFFFF))
                                            .border(
                                                1.dp,
                                                if (isSelected) SolidColor(ElectricPurple) else GlassBorderGradient,
                                                RoundedCornerShape(16.dp)
                                            )
                                            .clickable { intervention = option }
                                            .padding(16.dp)
                                            .testTag("onboarding_intervene_${option.take(4)}"),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Column {
                                            Text(
                                                text = option,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color.White else TextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = desc,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) Color(0xCCFFFFFF) else TextMuted,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // 11: Calibration Summary Card
                            11 -> {
                                val computedArchetype = when {
                                    risk.contains("Guardian") -> "The Vigilant Guardian"
                                    risk.contains("Maverick") -> "The High-Growth Maverick"
                                    risk.contains("Stoic") -> "The Mindful Stoic"
                                    trigger.contains("Late Night") -> "The Dopamine Optimizer"
                                    trigger.contains("Social") -> "The Social Strategist"
                                    else -> "The Strategic Builder"
                                }

                                GlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    backgroundColor = Color(0x331C1138),
                                    borderBrush = Brush.linearGradient(
                                        listOf(Color(0x80FFFFFF), EmeraldGreen, Color(0x30FFFFFF))
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "DNA CALIBRATION RESULT",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = TextMuted,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 0.8.sp
                                                )
                                                Text(
                                                    text = computedArchetype,
                                                    fontFamily = HelveticaFontFamily,
                                                    color = Color.White,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            GlassBadge(
                                                text = "100% VERIFIED",
                                                textColor = EmeraldGreen,
                                                borderColor = EmeraldGreen.copy(alpha = 0.5f),
                                                backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Summary Dimensions
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            SummaryItem(label = "Vulnerability", value = trigger.take(24))
                                            SummaryItem(label = "Risk DNA", value = risk.take(22))
                                            SummaryItem(label = "Dining Velocity", value = delivery.take(22))
                                            SummaryItem(label = "Intervention", value = intervention.take(24))
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "✨ Your Habit-Wallet AI is calibrated with custom friction rules and personalized roasts (+500 XP total awarded).",
                                            fontFamily = HelveticaFontFamily,
                                            color = EmeraldGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Buttons
            Column {
                Spacer(modifier = Modifier.height(20.dp))

                if (step == 6) {
                    // Two clear buttons at checkpoint
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(PhonePePurple)
                            .clickable { step = 7 }
                            .padding(vertical = 16.dp)
                            .testTag("onboarding_proceed_deep_kyc"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Deep KYC",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Proceed to Deep Behavioral Analysis (+250 XP)",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x1AFFFFFF))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                            .clickable {
                                val finalAllowance = allowanceStr.toIntOrNull() ?: 5000
                                onComplete(
                                    UserData(
                                        allowance = finalAllowance,
                                        weakness = weakness.ifBlank { "Dining Out / Delivery" },
                                        goal = goal.ifBlank { "Tech / Gadget Upgrade" },
                                        healthGoal = healthGoal.ifBlank { "Fat Loss" },
                                        mode = mode,
                                        isBehavioralKycCompleted = false
                                    )
                                )
                            }
                            .padding(vertical = 14.dp)
                            .testTag("onboarding_complete_later_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Enter Dashboard (Complete Behavioral Survey Later)",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    // Standard Continue or Finalize Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isStepValid) PhonePePurple else Color(0x288B5CF6))
                            .clickable(enabled = isStepValid) {
                                when {
                                    step < 5 -> step += 1
                                    step == 5 -> step = 6 // Show checkpoint choice
                                    step in 7..10 -> step += 1
                                    step == 11 -> {
                                        val finalAllowance = allowanceStr.toIntOrNull() ?: 5000
                                        val computedArchetype = when {
                                            risk.contains("Guardian") -> "The Vigilant Guardian"
                                            risk.contains("Maverick") -> "The High-Growth Maverick"
                                            risk.contains("Stoic") -> "The Mindful Stoic"
                                            trigger.contains("Late Night") -> "The Dopamine Optimizer"
                                            trigger.contains("Social") -> "The Social Strategist"
                                            else -> "The Strategic Builder"
                                        }
                                        var score = 65
                                        if (risk.contains("Guardian") || risk.contains("Stoic")) score += 15
                                        if (delivery.contains("Rarely")) score += 15
                                        else if (delivery.contains("Weekends")) score += 10
                                        else if (delivery.contains("Daily")) score -= 10
                                        if (intervention.contains("Cooling") || intervention.contains("Penalty")) score += 10
                                        val finalScore = score.coerceIn(40, 98)

                                        onComplete(
                                            UserData(
                                                allowance = finalAllowance,
                                                weakness = weakness.ifBlank { "Dining Out / Delivery" },
                                                goal = goal.ifBlank { "Tech / Gadget Upgrade" },
                                                healthGoal = healthGoal.ifBlank { "Fat Loss" },
                                                mode = mode,
                                                isBehavioralKycCompleted = true,
                                                vulnerabilityTrigger = trigger,
                                                riskArchetype = risk,
                                                deliveryFrequency = delivery,
                                                interventionStyle = intervention,
                                                behavioralArchetype = computedArchetype,
                                                resilienceScore = finalScore
                                            )
                                        )
                                    }
                                }
                            }
                            .padding(vertical = 16.dp)
                            .testTag("onboarding_continue_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (step == 11) "Initialize Fully Verified AI Profile" else "Continue",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.2).sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x14FFFFFF))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontFamily = HelveticaFontFamily, color = TextSecondary, fontSize = 11.sp)
        Text(text = value, fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}
