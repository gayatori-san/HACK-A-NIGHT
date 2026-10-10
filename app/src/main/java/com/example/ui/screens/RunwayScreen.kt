package com.example.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PhonePeAccentPurple
import com.example.ui.theme.PhonePeDarkHeader
import com.example.ui.theme.PhonePeHeroGradient
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState
import kotlin.math.roundToInt

class AndroidRunwayBridge(
    private val onApplyRescue: () -> Unit
) {
    @JavascriptInterface
    fun onApplyRescuePlan() {
        onApplyRescue()
    }
}

@Composable
fun RunwayScreen(
    uiState: WalletUiState,
    onBack: () -> Unit,
    onApplyRescuePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mode toggle: 0 = "HTML XAI Studio", 1 = "Native XAI & Runway"
    var viewMode by remember { mutableStateOf(0) }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("runway_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PhonePeDarkHeader)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "RUNWAY",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        GlassBadge(
                            text = "XAI ENGINE",
                            textColor = EmeraldGreen,
                            borderColor = EmeraldGreen.copy(alpha = 0.4f),
                            backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                        )
                    }
                    Text(
                        text = "Autonomous Finance & Explainable Decision Trees",
                        fontFamily = HelveticaFontFamily,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            GlassBadge(
                text = "ACTIVE XAI",
                textColor = EmeraldGreen,
                borderColor = EmeraldGreen.copy(alpha = 0.3f),
                backgroundColor = EmeraldGreen.copy(alpha = 0.12f),
                dotColor = EmeraldGreen
            )
        }

        // Clean, Spacious Mode Switcher Segmented Control
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF1B132B))
                .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(14.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .then(
                        if (viewMode == 0) Modifier.background(Brush.horizontalGradient(listOf(PhonePeLightPurple, PhonePePurple)))
                        else Modifier.background(Color.Transparent)
                    )
                    .clickable { viewMode = 0 }
                    .padding(vertical = 10.dp)
                    .testTag("runway_tab_html"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (viewMode == 0) Color.White else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Interactive HTML Studio",
                        fontFamily = HelveticaFontFamily,
                        color = if (viewMode == 0) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (viewMode == 0) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .then(
                        if (viewMode == 1) Modifier.background(Brush.horizontalGradient(listOf(PhonePeLightPurple, PhonePePurple)))
                        else Modifier.background(Color.Transparent)
                    )
                    .clickable { viewMode = 1 }
                    .padding(vertical = 10.dp)
                    .testTag("runway_tab_native"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = null,
                        tint = if (viewMode == 1) Color.White else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Native XAI Engine",
                        fontFamily = HelveticaFontFamily,
                        color = if (viewMode == 1) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (viewMode == 1) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Content Area
        Box(modifier = Modifier.fillMaxSize()) {
            if (viewMode == 0) {
                RunwayHtmlWebView(
                    uiState = uiState,
                    onApplyRescuePlan = onApplyRescuePlan,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                RunwayNativeDashboard(
                    uiState = uiState,
                    onApplyRescuePlan = onApplyRescuePlan,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RunwayHtmlWebView(
    uiState: WalletUiState,
    onApplyRescuePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        allowFileAccess = true
                        cacheMode = WebSettings.LOAD_NO_CACHE
                    }
                    setBackgroundColor(0x00000000)

                    // Add JavaScript Interface for Two-Way Bridge
                    addJavascriptInterface(
                        AndroidRunwayBridge(onApplyRescuePlan),
                        "AndroidBridge"
                    )

                    webChromeClient = WebChromeClient()
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                            // Feed latest live telemetry from Kotlin StateFlow
                            view?.evaluateJavascript(
                                "if (typeof updateDataFromAndroid === 'function') { updateDataFromAndroid(${uiState.balance}, ${uiState.totalSpent}, ${uiState.userData.allowance}); }",
                                null
                            )
                        }
                    }

                    loadUrl("file:///android_asset/runway_engine.html")
                    webViewRef = this
                }
            },
            update = { webView ->
                webView.evaluateJavascript(
                    "if (typeof updateDataFromAndroid === 'function') { updateDataFromAndroid(${uiState.balance}, ${uiState.totalSpent}, ${uiState.userData.allowance}); }",
                    null
                )
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ObsidianBlack.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = NeonLime, strokeWidth = 3.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Loading Runway XAI Engine...",
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun RunwayNativeDashboard(
    uiState: WalletUiState,
    onApplyRescuePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var cutImpulseSlider by remember { mutableFloatStateOf(20f) }
    var curfewSlider by remember { mutableFloatStateOf(3f) }
    var activeSubTab by remember { mutableStateOf("xai") } // "xai", "roast", "openbanking"

    val baseRunway = if (uiState.currentOutflowDay > 0) {
        (uiState.balance / uiState.currentOutflowDay.toDouble()).coerceAtLeast(0.0)
    } else 24.5

    val extraDaysFromCut = (cutImpulseSlider / 10f) * 1.8f
    val extraDaysFromCurfew = curfewSlider * 0.9f
    val totalExtraDays = (extraDaysFromCut + extraDaysFromCurfew)
    val projectedRunwayDays = (baseRunway + totalExtraDays)

    val dailyBurn = (uiState.currentOutflowDay * (1f - (cutImpulseSlider * 0.007f))).roundToInt().coerceAtLeast(200)

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Hero Projected Runway Gauge Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                backgroundColor = Color(0x331C1138),
                borderBrush = Brush.linearGradient(
                    listOf(Color(0x80FFFFFF), PhonePeLightPurple, Color(0x30FFFFFF))
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "PROJECTED RUNWAY",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "%.1f".format(projectedRunwayDays),
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-1).sp
                                )
                                Text(
                                    text = " Days",
                                    fontFamily = HelveticaFontFamily,
                                    color = NeonLime,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                                )
                            }
                        }

                        val isCritical = dailyBurn > uiState.safeLimitDay
                        GlassBadge(
                            text = "₹$dailyBurn / day",
                            textColor = if (isCritical) CoralRed else EmeraldGreen,
                            borderColor = (if (isCritical) CoralRed else EmeraldGreen).copy(alpha = 0.5f),
                            backgroundColor = (if (isCritical) CoralRed else EmeraldGreen).copy(alpha = 0.15f),
                            dotColor = if (isCritical) CoralRed else EmeraldGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulated Liquidity Curve Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                    ) {
                        val progress = (projectedRunwayDays / 30.0).coerceIn(0.0, 1.0).toFloat()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(EmeraldGreen, PhonePeAccentPurple, PhonePeLightPurple)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Current Balance: ₹${uiState.balance.toInt()}",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Depletion Horizon: Day ${(projectedRunwayDays.toInt())}",
                            fontFamily = HelveticaFontFamily,
                            color = if (projectedRunwayDays < 15) CoralRed else EmeraldGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // 2. Interactive What-If Simulator Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = Color(0x261E1A2F)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎛️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "What-If Runway Simulator",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        GlassBadge(
                            text = "+${"%.1f".format(totalExtraDays)} Days Unlocked",
                            textColor = EmeraldGreen,
                            borderColor = Color(0x6600D09C),
                            backgroundColor = Color(0x2200D09C)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Slider 1: Cut Impulse Food Spends
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Cut Impulse Food Spends",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${cutImpulseSlider.roundToInt()}%",
                            fontFamily = HelveticaFontFamily,
                            color = SoftLavender,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = cutImpulseSlider,
                        onValueChange = { cutImpulseSlider = it },
                        valueRange = 0f..60f,
                        steps = 6,
                        colors = SliderDefaults.colors(
                            thumbColor = PhonePeLightPurple,
                            activeTrackColor = PhonePeLightPurple,
                            inactiveTrackColor = Color(0x22FFFFFF)
                        )
                    )

                    // Slider 2: Extend Weekend Curfew
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Extend Weekend Curfew",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "+${curfewSlider.roundToInt()} Days",
                            fontFamily = HelveticaFontFamily,
                            color = SoftLavender,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = curfewSlider,
                        onValueChange = { curfewSlider = it },
                        valueRange = 0f..10f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = PhonePeLightPurple,
                            activeTrackColor = PhonePeLightPurple,
                            inactiveTrackColor = Color(0x22FFFFFF)
                        )
                    )
                }
            }
        }

        // 3. Engine Navigation Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x33000000))
                    .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val tabs = listOf(
                    Triple("xai", "XAI Tree", Icons.Default.Insights),
                    Triple("roast", "Roast & Rescue", Icons.Default.ElectricBolt),
                    Triple("openbanking", "Open Banking", Icons.Default.AccountBalance),
                    Triple("training", "AI Training", Icons.Default.Psychology)
                )

                tabs.forEach { (key, label, icon) ->
                    val isSelected = activeSubTab == key
                    val bgModifier = if (isSelected) {
                        Modifier.background(
                            Brush.linearGradient(
                                listOf(PhonePePurple, Color(0xFF4C1D95))
                            )
                        )
                    } else {
                        Modifier.background(Color.Transparent)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .then(bgModifier)
                            .clickable { activeSubTab = key }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Color.White else TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = label,
                                fontFamily = HelveticaFontFamily,
                                color = if (isSelected) Color.White else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Tab Content
        when (activeSubTab) {
            "xai" -> {
                // Visual Explainable AI Tree Nodes
                item {
                    XaiLogicTreeNode(
                        step = "1. Ingested Stream",
                        title = "Transaction: ₹340 @ Swiggy Midnight",
                        subtitle = "11:42 PM • Category: Dining • Context: Boredom late-night scroll",
                        badge = "100% Raw Telemetry",
                        badgeColor = Color(0xFF38BDF8)
                    )
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "▼ Behavioral Pattern Engine", color = PhonePeLightPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                item {
                    XaiLogicTreeNode(
                        step = "2. Vulnerability Classifier",
                        title = "Dopamine-Craving Impulse Flagged",
                        subtitle = "Historical Correlation: 88% of late-night orders trigger regret next morning",
                        badge = "94% Confidence",
                        badgeColor = Color(0xFFF472B6)
                    )
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "▼ Explainable Runway Weights", color = PhonePeLightPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                item {
                    XaiLogicTreeNode(
                        step = "3. Liquidity Horizon Impact",
                        title = "Burn Acceleration: +₹340 / week multiplier",
                        subtitle = "Reduces month-end buffer by 6.8%. Directly threatens gadget upgrade milestone.",
                        badge = "-14 Hrs Runway",
                        badgeColor = Color(0xFFFACC15)
                    )
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "▼ Autonomous Directive", color = PhonePeLightPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                item {
                    XaiLogicTreeNode(
                        step = "4. Autonomous Intervention",
                        title = "Health-Swap Shield Active & ₹150 Micro-Save Triggered",
                        subtitle = "Suggest Greek yogurt/water alternate + auto-deposit saved differential into Liquid Buffer.",
                        badge = "Active Shield",
                        badgeColor = NeonLime,
                        isHighlighted = true
                    )
                }
            }

            "roast" -> {
                // Roast Card
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0x29FF4757),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x80FF4757), Color(0x30FF4757))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🔥", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "THE XAI FINANCIAL ROAST",
                                    fontFamily = HelveticaFontFamily,
                                    color = CoralRed,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "\"You logged 4 midnight bubble teas this week and wondered why your runway is choking. Your bank account is running an endurance marathon with a sprained ankle, but hey, at least your boba pearls had premium brown sugar!\"",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Burn Impact: -1.8 Days Runway Consumed",
                                fontFamily = HelveticaFontFamily,
                                color = CoralRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Rescue Card
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0x2400D09C),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x8000D09C), Color(0x3000D09C))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🛡️", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AUTONOMOUS RESCUE PROTOCOL",
                                    fontFamily = HelveticaFontFamily,
                                    color = EmeraldGreen,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "1. Freeze discretionary food ordering until Friday night.\n2. Auto-divert ₹450 from weekend leisure to re-anchor the 30-day runway target.\n3. Earn +80 XP bonus by logging zero junk spends for 48 hours.",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(NeonLime, Color(0xFF8CE000))
                                        )
                                    )
                                    .clickable { onApplyRescuePlan() }
                                    .padding(vertical = 12.dp)
                                    .testTag("apply_rescue_plan_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = "Execute",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "EXECUTE RESCUE ACTION (+50 XP)",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            "openbanking" -> {
                // Open Banking Ingestion
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0x261E1A2F)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Connected Banking Feeds",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                GlassBadge(
                                    text = "2 ACTIVE",
                                    textColor = EmeraldGreen,
                                    borderColor = Color(0x6600D09C),
                                    backgroundColor = Color(0x2200D09C)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Primary: HDFC Bank •••• 4091 (UPI ID: user@okhdfcbank)",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Secondary: PhonePe Wallet (₹1,450 Reserve)",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0x261E1A2F)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Financial Fingerprint Health",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Liquidity Buffer Index", color = TextSecondary, fontSize = 12.sp)
                                Text(text = "7.8 / 10", color = EmeraldGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Impulse Vulnerability Ratio", color = TextSecondary, fontSize = 12.sp)
                                Text(text = "High (Fri-Sat)", color = CoralRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Autonomous Savings Velocity", color = TextSecondary, fontSize = 12.sp)
                                Text(text = "₹180 / day", color = EmeraldGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            "training" -> {
                // Free-Tier AI Model Training, Fine-Tuning & Quantization Guide & Playground
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = Color(0xFF161124),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x508B5CF6), PhonePeLightPurple, Color(0x20FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🧠", fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Free-Tier AI Engine Training",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                GlassBadge(
                                    text = "100% FREE",
                                    textColor = EmeraldGreen,
                                    borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                    backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "How to train & run financial XAI intelligence models without expensive GPU cloud bills using Google AI Studio, Colab, Kaggle, and on-device quantization.",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Method 1: Google AI Studio Gemini API Free Tier & System Prompts
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF181329),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x408B5CF6), Color(0x205F259F), Color(0x20FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "1. Few-Shot In-Context Tuning",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                GlassBadge(
                                    text = "Google AI Studio",
                                    textColor = Color(0xFF38BDF8),
                                    borderColor = Color(0x4038BDF8),
                                    backgroundColor = Color(0x1A38BDF8)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Cost: $0.00 / month\n" +
                                        "• Model: Gemini 2.5 Flash / Flash Lite (Free tier: 15 RPM, 1M TPM)\n" +
                                        "• Strategy: No weight retraining needed! Feed 30-50 structured JSON input/output financial reasoning pairs in the System Instruction. Gemini learns your XAI logic tree rules instantaneously at runtime without spending a cent on compute.",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                // Method 2: Google Colab T4 / Kaggle P100 LoRA Fine-Tuning
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF181329),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x408B5CF6), Color(0x205F259F), Color(0x20FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "2. QLoRA on Colab / Kaggle Free GPU",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                GlassBadge(
                                    text = "Colab T4 Free",
                                    textColor = Color(0xFFF472B6),
                                    borderColor = Color(0x40F472B6),
                                    backgroundColor = Color(0x1AF472B6)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Compute: Google Colab (Free T4 GPU, 12GB VRAM) or Kaggle (30 hrs/week P100 GPU)\n" +
                                        "• Stack: Unsloth / Hugging Face PEFT + BitsAndBytes (4-bit quantization)\n" +
                                        "• Base Models: Gemma-2-2B-it or Llama-3.2-1B-Instruct\n" +
                                        "• Dataset: 500-1,000 synthetic transaction records with categorization, regret rating, and liquidity horizon delta.\n" +
                                        "• Training time: ~15 to 25 minutes on Free T4 using Unsloth 4-bit QLoRA.",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                // Method 3: On-Device Edge Deployment (Zero Backend Cost)
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF181329),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x408B5CF6), Color(0x205F259F), Color(0x20FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "3. On-Device Edge GGUF / MediaPipe",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                GlassBadge(
                                    text = "Edge Native",
                                    textColor = EmeraldGreen,
                                    borderColor = Color(0x4010B981),
                                    backgroundColor = Color(0x1A10B981)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Cost: $0 forever (runs 100% on the user's Android phone)\n" +
                                        "• Format: Convert trained model to GGUF (Q4_K_M) or MediaPipe LLM format (Gemini Nano)\n" +
                                        "• Privacy & Speed: Zero server latency, works completely offline in airplane mode, no API rate limits.",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                // Method 4: Synthetic Financial Data Generation Prompt
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF140F22),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x33FFFFFF), Color(0x228B5CF6))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🛠️ Recommended Step-by-Step Training Recipe",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "1. Generate 500 samples using Gemini Free Tier: Ask Gemini to generate JSON pairs of Indian UPI transactions with regret tags and runway scores.\n" +
                                        "2. Open Google Colab (Free T4) and load 'unsloth/gemma-2-2b-it-bnb-4bit'.\n" +
                                        "3. Fine-tune for 3 epochs with LoRA r=16, alpha=16.\n" +
                                        "4. Export LoRA weights and merge into 4-bit GGUF.\n" +
                                        "5. Test decision tree inferences in Runway XAI Engine!",
                                fontFamily = HelveticaFontFamily,
                                color = TextPrimary,
                                fontSize = 11.5.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun XaiLogicTreeNode(
    step: String,
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    isHighlighted: Boolean = false
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isHighlighted) Color(0x331C1138) else Color(0x261B1828),
        borderBrush = Brush.linearGradient(
            listOf(Color(0x80FFFFFF), badgeColor.copy(alpha = 0.5f), Color(0x30FFFFFF))
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = step,
                    fontFamily = HelveticaFontFamily,
                    color = badgeColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                GlassBadge(
                    text = badge,
                    textColor = badgeColor,
                    borderColor = badgeColor.copy(alpha = 0.5f),
                    backgroundColor = badgeColor.copy(alpha = 0.15f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontFamily = HelveticaFontFamily,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontFamily = HelveticaFontFamily,
                color = TextSecondary,
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )
        }
    }
}
