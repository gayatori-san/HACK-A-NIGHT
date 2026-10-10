package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.models.StatementAnalysisReport
import com.example.ui.theme.AmberCashback
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

/**
 * High-end pop-up dialog that arrives after AI analyzes the uploaded CSV/Excel statement:
 * - Bankruptcy meter with smooth pulsing animations
 * - Month-end behavioral analysis & hilarious psychological roast
 * - Compound Dividend Growth Opportunity Cost Chart (smooth animated curves & projection bars)
 * - Psychological drive away from baseless purchases
 */
@Composable
fun StatementAnalysisReportPopup(
    report: StatementAnalysisReport,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Subtle pulsating glow animation for bankruptcy meter
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Animated reveal progress for chart
    val chartAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        chartAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF1E1235),
                            Color(0xFF130A24),
                            Color(0xFF0D061A)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0x998B5CF6),
                            Color(0x5500D09C),
                            Color(0x33FF4757)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(20.dp)
                .testTag("statement_analysis_popup")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x2800D09C))
                                .border(1.dp, Color(0x6000D09C), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "RUNWAY XAI STATEMENT AUDIT",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.6.sp
                            )
                            Text(
                                text = "AI Behavioral Telemetry & Month-End Risk",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 1. BANKRUPTCY METER CARD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0x33FF4757),
                                    Color(0x221B1033)
                                )
                            )
                        )
                        .border(1.dp, Color(0x55FF4757), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🚨", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "BANKRUPTCY RISK METER",
                                    fontFamily = HelveticaFontFamily,
                                    color = CoralRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CoralRed.copy(alpha = 0.2f))
                                    .border(1.dp, CoralRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "${report.bankruptcyRiskPercent}% HAZARD",
                                    fontFamily = HelveticaFontFamily,
                                    color = CoralRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bankruptcy Bar Indicator
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(report.bankruptcyRiskPercent / 100f)
                                    .height(10.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(AmberCashback, CoralRed)
                                        )
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Liquidity Horizon: ${"%.1f".format(report.daysRemaining)} Days",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Monthly Leak: ₹${report.monthlyBurn.roundToInt()}",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. PSYCHOLOGICAL BEHAVIORAL ROAST (Fun & Driving user away from baseless purchases)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0x331C1235))
                        .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔥", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "XAI BEHAVIORAL ROAST",
                                fontFamily = HelveticaFontFamily,
                                color = AmberCashback,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${report.funnyRoast}\"",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 ${report.psychologicalNudge}",
                            fontFamily = HelveticaFontFamily,
                            color = EmeraldGreen,
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. IF THIS MONEY WAS INVESTED: COMPOUND DIVIDEND ANIMATION & CHART
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF141F2C))
                        .border(1.dp, Color(0x4038BDF8), RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ShowChart,
                                    contentDescription = null,
                                    tint = Color(0xFF38BDF8),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DIVIDEND & WEALTH LOSS CHART",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color(0xFF38BDF8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                text = "8% Div Yield",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 10.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "If ₹${report.totalWastedOnImpulse.toInt()} spent on impulse was invested in dividend assets:",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Smooth animated canvas chart showing wealth divergence
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                        ) {
                            val w = size.width
                            val h = size.height
                            val progress = chartAnim.value

                            // Baseline: Spent money disappears (slopes to 0)
                            val spentPath = Path().apply {
                                moveTo(0f, h * 0.35f)
                                cubicTo(
                                    w * 0.3f, h * 0.45f,
                                    w * 0.6f, h * 0.85f,
                                    w * progress, h * 0.95f
                                )
                            }
                            drawPath(
                                path = spentPath,
                                color = CoralRed.copy(alpha = 0.8f),
                                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Growth line: Compounded dividends + asset value (exponential curve upwards)
                            val investPath = Path().apply {
                                moveTo(0f, h * 0.7f)
                                cubicTo(
                                    w * 0.3f, h * 0.65f,
                                    w * 0.6f, h * 0.35f,
                                    w * progress, h * 0.12f
                                )
                            }
                            drawPath(
                                path = investPath,
                                color = EmeraldGreen,
                                style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                            )

                            // Animated endpoint pulse
                            if (progress > 0.8f) {
                                drawCircle(
                                    color = EmeraldGreen,
                                    radius = 5.dp.toPx(),
                                    center = Offset(w * progress, h * 0.12f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats Grid (Dividends & 5-year wealth projection)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x3300D09C))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "ANNUAL DIVIDENDS", color = SoftLavender, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "+₹${report.projectedDividendsYearly.roundToInt()} / yr", color = EmeraldGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x3338BDF8))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "5-YR COMPOUND", color = SoftLavender, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "₹${report.projectedWealth5Yrs.roundToInt()}", color = Color(0xFF38BDF8), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CTA Button: Dismiss & commit to discipline
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_statement_report_button")
                ) {
                    Text(
                        text = "I Commit: No Baseless Spends This Month (+30 XP)",
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
