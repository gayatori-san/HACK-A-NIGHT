package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.UserData
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.GlassSurfaceHigh
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PhonePeAccentPurple
import com.example.ui.theme.PhonePeHeroGradient
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState

/**
 * KYC & Behavioral Identity Hub Modal.
 * Allows users to inspect their KYC verification tier, view their calibrated
 * behavioral financial fingerprint, and complete or retake the deep behavioral
 * questionnaire anytime.
 */
@Composable
fun KycBehavioralModal(
    visible: Boolean,
    uiState: WalletUiState,
    onClose: () -> Unit,
    onSaveKyc: (UserData) -> Unit,
    modifier: Modifier = Modifier
) {
    // Sub-mode: "dashboard" = view status & fingerprint, "survey" = interactive questionnaire
    var mode by remember { mutableStateOf("dashboard") }

    // Survey state
    var surveyStep by remember { mutableIntStateOf(1) }
    var trigger by remember(uiState.userData) {
        mutableStateOf(uiState.userData.vulnerabilityTrigger)
    }
    var risk by remember(uiState.userData) {
        mutableStateOf(uiState.userData.riskArchetype)
    }
    var delivery by remember(uiState.userData) {
        mutableStateOf(uiState.userData.deliveryFrequency)
    }
    var intervention by remember(uiState.userData) {
        mutableStateOf(uiState.userData.interventionStyle)
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xF008080E))
                .padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (mode == "survey") {
                            IconButton(
                                onClick = { mode = "dashboard" },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (mode == "survey") "BEHAVIORAL SURVEY" else "KYC IDENTITY & DNA",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                GlassBadge(
                                    text = if (uiState.userData.isBehavioralKycCompleted) "TIER 2 VERIFIED" else "TIER 1 BASIC",
                                    textColor = if (uiState.userData.isBehavioralKycCompleted) EmeraldGreen else NeonLime,
                                    borderColor = (if (uiState.userData.isBehavioralKycCompleted) EmeraldGreen else NeonLime).copy(alpha = 0.5f),
                                    backgroundColor = (if (uiState.userData.isBehavioralKycCompleted) EmeraldGreen else NeonLime).copy(alpha = 0.15f)
                                )
                            }
                            Text(
                                text = "Financial Psychology & Behavioral Risk Calibration",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .testTag("kyc_modal_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (mode == "dashboard") {
                    // KYC Dashboard & Fingerprint View
                    KycDashboardView(
                        userData = uiState.userData,
                        onStartSurvey = {
                            surveyStep = 1
                            mode = "survey"
                        }
                    )
                } else {
                    // Interactive Survey Questionnaire Inside Modal
                    KycSurveyInteractiveView(
                        currentStep = surveyStep,
                        trigger = trigger,
                        onTriggerChange = { trigger = it },
                        risk = risk,
                        onRiskChange = { risk = it },
                        delivery = delivery,
                        onDeliveryChange = { delivery = it },
                        intervention = intervention,
                        onInterventionChange = { intervention = it },
                        onNextStep = {
                            if (surveyStep < 4) {
                                surveyStep += 1
                            } else {
                                // Compute Archetype and Score
                                val computedArchetype = computeArchetype(trigger, risk)
                                val computedResilience = computeResilienceScore(risk, delivery, intervention)

                                val updatedData = uiState.userData.copy(
                                    isBehavioralKycCompleted = true,
                                    vulnerabilityTrigger = trigger,
                                    riskArchetype = risk,
                                    deliveryFrequency = delivery,
                                    interventionStyle = intervention,
                                    behavioralArchetype = computedArchetype,
                                    resilienceScore = computedResilience
                                )
                                onSaveKyc(updatedData)
                                mode = "dashboard"
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun KycDashboardView(
    userData: UserData,
    onStartSurvey: () -> Unit
) {
    // 1. Tier Verification Status Card
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = Color(0x331C1138),
        borderBrush = Brush.linearGradient(
            listOf(Color(0x60FFFFFF), PhonePeLightPurple, Color(0x20FFFFFF))
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                if (userData.isBehavioralKycCompleted) Color(0x2E00D09C) else Color(0x228B5CF6)
                            )
                            .border(
                                1.dp,
                                if (userData.isBehavioralKycCompleted) EmeraldGreen else PhonePeAccentPurple,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (userData.isBehavioralKycCompleted) Icons.Default.VerifiedUser else Icons.Default.Badge,
                            contentDescription = "KYC Shield",
                            tint = if (userData.isBehavioralKycCompleted) EmeraldGreen else PhonePeAccentPurple,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = if (userData.isBehavioralKycCompleted) "KYC Tier 2 Calibrated" else "KYC Tier 1: Basic Baseline",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (userData.isBehavioralKycCompleted) "100% Behavioral Financial DNA Verified" else "Complete deep survey for +250 XP & high AI fidelity",
                            fontFamily = HelveticaFontFamily,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Progress Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tier 1 Check
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x2200D09C))
                        .border(1.dp, EmeraldGreen.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Done",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tier 1: Basic",
                                fontFamily = HelveticaFontFamily,
                                color = EmeraldGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Allowance & Goals",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                // Tier 2 Check
                val isTier2 = userData.isBehavioralKycCompleted
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isTier2) Color(0x2200D09C) else Color(0x1A8B5CF6))
                        .border(
                            1.dp,
                            if (isTier2) EmeraldGreen.copy(alpha = 0.5f) else PhonePeAccentPurple.copy(alpha = 0.5f),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isTier2) Icons.Default.CheckCircle else Icons.Default.AutoAwesome,
                                contentDescription = "Tier 2",
                                tint = if (isTier2) EmeraldGreen else PhonePeAccentPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isTier2) "Tier 2: Done" else "Tier 2: Survey",
                                fontFamily = HelveticaFontFamily,
                                color = if (isTier2) EmeraldGreen else SoftLavender,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Psychology & Risk",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 2. Behavioral Archetype & Resilience Score Card
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0x241E1A2F)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BEHAVIORAL ARCHETYPE",
                        fontFamily = HelveticaFontFamily,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userData.behavioralArchetype,
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Resilience Score Dial
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x336366F1))
                        .border(1.dp, ElectricPurple, RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${userData.resilienceScore}%",
                            fontFamily = HelveticaFontFamily,
                            color = NeonLime,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Resilience",
                            fontFamily = HelveticaFontFamily,
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Behavioral Dimensions Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                KycDimensionRow(
                    label = "Vulnerability Trigger",
                    value = userData.vulnerabilityTrigger,
                    icon = "⚡"
                )
                KycDimensionRow(
                    label = "Risk DNA Persona",
                    value = userData.riskArchetype,
                    icon = "⚖️"
                )
                KycDimensionRow(
                    label = "Convenience Friction",
                    value = userData.deliveryFrequency,
                    icon = "🛵"
                )
                KycDimensionRow(
                    label = "Impulse Intervention",
                    value = userData.interventionStyle,
                    icon = "🛡️"
                )
                KycDimensionRow(
                    label = "Accountability Mode",
                    value = if (userData.mode == "strict") "Strict Drill Sergeant" else "Empathetic CFO",
                    icon = "📊"
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // 3. CTA: Retake or Complete Deep Survey
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (userData.isBehavioralKycCompleted) Color(0x336366F1) else PhonePePurple)
            .border(
                1.dp,
                if (userData.isBehavioralKycCompleted) ElectricPurple else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable { onStartSurvey() }
            .padding(vertical = 15.dp)
            .testTag("kyc_start_survey_button"),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "Survey",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (userData.isBehavioralKycCompleted) "Re-calibrate Behavioral Survey" else "Complete Deep Behavioral Survey (+250 XP)",
                fontFamily = HelveticaFontFamily,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun KycDimensionRow(
    label: String,
    value: String,
    icon: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x14FFFFFF))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontFamily = HelveticaFontFamily,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
        Text(
            text = value,
            fontFamily = HelveticaFontFamily,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun KycSurveyInteractiveView(
    currentStep: Int,
    trigger: String,
    onTriggerChange: (String) -> Unit,
    risk: String,
    onRiskChange: (String) -> Unit,
    delivery: String,
    onDeliveryChange: (String) -> Unit,
    intervention: String,
    onInterventionChange: (String) -> Unit,
    onNextStep: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Step Indicator (4 steps)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            (1..4).forEach { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (currentStep >= i) PhonePeLightPurple else Color(0x24FFFFFF))
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        when (currentStep) {
            1 -> {
                Text(
                    text = "Vulnerability Triggers.",
                    fontFamily = HelveticaFontFamily,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "When does your discipline break down most frequently?",
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
                    val isSelected = trigger.startsWith(option.take(6)) || trigger == option
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
                            .clickable { onTriggerChange(option) }
                            .padding(16.dp)
                            .testTag("kyc_trigger_${option.take(4)}"),
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

            2 -> {
                Text(
                    text = "Money Psychology & Risk.",
                    fontFamily = HelveticaFontFamily,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
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
                    val isSelected = risk.startsWith(option.take(6)) || risk == option
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
                            .clickable { onRiskChange(option) }
                            .padding(16.dp)
                            .testTag("kyc_risk_${option.take(4)}"),
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

            3 -> {
                Text(
                    text = "Convenience & Dining Velocity.",
                    fontFamily = HelveticaFontFamily,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "How often does food delivery or eating out replace cooking?",
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
                    val isSelected = delivery.startsWith(option.take(6)) || delivery == option
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
                            .clickable { onDeliveryChange(option) }
                            .padding(16.dp)
                            .testTag("kyc_delivery_${option.take(4)}"),
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

            4 -> {
                Text(
                    text = "Impulse Intervention Style.",
                    fontFamily = HelveticaFontFamily,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "When Habit-Wallet detects an impulse spend, how should it intervene?",
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
                    val isSelected = intervention.startsWith(option.take(6)) || intervention == option
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
                            .clickable { onInterventionChange(option) }
                            .padding(16.dp)
                            .testTag("kyc_intervene_${option.take(4)}"),
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
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Next / Finalize Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(PhonePePurple)
                .clickable { onNextStep() }
                .padding(vertical = 15.dp)
                .testTag("kyc_survey_continue_button"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (currentStep == 4) "Calibrate Financial DNA (+250 XP)" else "Continue",
                fontFamily = HelveticaFontFamily,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun computeArchetype(trigger: String, risk: String): String {
    return when {
        risk.contains("Guardian") -> "The Vigilant Guardian"
        risk.contains("Maverick") -> "The High-Growth Maverick"
        risk.contains("Stoic") -> "The Mindful Stoic"
        trigger.contains("Late Night") -> "The Dopamine Optimizer"
        trigger.contains("Social") -> "The Social Strategist"
        else -> "The Strategic Builder"
    }
}

private fun computeResilienceScore(risk: String, delivery: String, intervention: String): Int {
    var score = 65
    if (risk.contains("Guardian") || risk.contains("Stoic")) score += 15
    if (delivery.contains("Rarely")) score += 15
    else if (delivery.contains("Weekends")) score += 10
    else if (delivery.contains("Daily")) score -= 10

    if (intervention.contains("Cooling") || intervention.contains("Penalty")) score += 10
    return score.coerceIn(40, 98)
}
