package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.TransactionItem
import com.example.ui.components.BusinessCategoryTile
import com.example.ui.components.CategoryIcon
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.AmberCashback
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
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
import kotlin.math.abs

@Composable
fun HomeScreen(
    uiState: WalletUiState,
    onOpenInsights: () -> Unit,
    onResist: () -> Unit,
    onFilterSelect: (String) -> Unit,
    onDismissInsight: () -> Unit,
    onNavigateToTab: (String) -> Unit = {},
    onUpdateAllowance: (Int) -> Unit = {},
    onOpenKyc: () -> Unit = {},
    onImportDemoStatement: (String) -> Unit = {},
    onImportCsv: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showTopMenu by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showUploadStatementModal by remember { mutableStateOf(false) }

    val filteredTxns = if (uiState.filterCat == "All") {
        uiState.txns
    } else {
        uiState.txns.filter { it.cat.equals(uiState.filterCat, ignoreCase = true) }
    }

    val balanceRatio = uiState.balance / uiState.userData.allowance.coerceAtLeast(1)
    val isLowBalance = balanceRatio <= 0.25

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("home_screen_lazy_column")
    ) {
        // 1. PhonePe Signature Header & Glassmorphic Hero
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PhonePeHeroGradient)
                    .padding(top = 12.dp, bottom = 18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Top PhonePe Navigation Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Profile & Location Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onNavigateToTab("budget") }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(PhonePeAccentPurple, PhonePePurple)
                                        )
                                    )
                                    .border(1.5.dp, Color(0x80FFFFFF), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "SG",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Bengaluru, KA",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "▼", color = Color(0xCCFFFFFF), fontSize = 9.sp)
                                }
                                Text(
                                    text = "samyak@habitpay • UPI Lite",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Right PhonePe Action Buttons (Behavioral KYC, QR Scanner & Submenu Overflow)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Deep Behavioral Analysis Small Icon at Top Taskbar (Requested in #4)
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x338B5CF6))
                                    .border(1.dp, Color(0x60A78BFA), CircleShape)
                                    .clickable { onOpenKyc() }
                                    .testTag("home_top_behavioral_kyc_icon"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Deep Behavioral Analysis",
                                    tint = if (uiState.userData.isBehavioralKycCompleted) EmeraldGreen else SoftLavender,
                                    modifier = Modifier.size(20.dp)
                                )
                                if (!uiState.userData.isBehavioralKycCompleted) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(AmberCashback)
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            }

                            // PhonePe QR Code Scanner
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x28FFFFFF))
                                    .border(1.dp, Color(0x38FFFFFF), CircleShape)
                                    .clickable { onNavigateToTab("log") }
                                    .testTag("home_qr_scan_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Scan QR",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Minimalist Submenu Overflow Button
                            Box {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x28FFFFFF))
                                        .border(1.dp, Color(0x38FFFFFF), CircleShape)
                                        .clickable { showTopMenu = !showTopMenu }
                                        .testTag("home_menu_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More Options",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    // Notification dot if KYC not completed or new insight available
                                    if (!uiState.userData.isBehavioralKycCompleted || uiState.recentInsight != null) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(PhonePeAccentPurple)
                                                .align(Alignment.TopEnd)
                                        )
                                    }
                                }

                                // Elegant Submenu Dropdown
                                DropdownMenu(
                                    expanded = showTopMenu,
                                    onDismissRequest = { showTopMenu = false },
                                    modifier = Modifier
                                        .background(Color(0xFF1E1632))
                                        .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(14.dp))
                                ) {
                                    // Submenu Item: Behavioral KYC
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = if (uiState.userData.isBehavioralKycCompleted) Icons.Default.VerifiedUser else Icons.Default.Badge,
                                                        contentDescription = null,
                                                        tint = if (uiState.userData.isBehavioralKycCompleted) EmeraldGreen else SoftLavender,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = "Behavioral KYC",
                                                        fontFamily = HelveticaFontFamily,
                                                        color = Color.White,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Text(
                                                    text = if (uiState.userData.isBehavioralKycCompleted) "100%" else "+250 XP",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = if (uiState.userData.isBehavioralKycCompleted) EmeraldGreen else AmberCashback,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        },
                                        onClick = {
                                            showTopMenu = false
                                            onOpenKyc()
                                        },
                                        modifier = Modifier.testTag("home_kyc_badge_button")
                                    )

                                    // Submenu Item: AI Insights & Roasts
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Notifications,
                                                    contentDescription = null,
                                                    tint = SoftLavender,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = "Discipline AI Insights",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            showTopMenu = false
                                            onOpenInsights()
                                        },
                                        modifier = Modifier.testTag("home_notifications_bell")
                                    )

                                    // Submenu Item: Runway Autonomous Finance
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.TrendingUp,
                                                    contentDescription = null,
                                                    tint = SoftLavender,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = "Runway XAI Forecast",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            showTopMenu = false
                                            onNavigateToTab("runway")
                                        }
                                    )

                                    // Submenu Item: Allowance & Budget Limits
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Tune,
                                                    contentDescription = null,
                                                    tint = SoftLavender,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = "Goals & Allowance Budget",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            showTopMenu = false
                                            onNavigateToTab("budget")
                                        }
                                    )

                                    // Submenu Item: Help & Support
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.HelpOutline,
                                                    contentDescription = null,
                                                    tint = SoftLavender,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = "Help & Support",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            showTopMenu = false
                                            onOpenInsights()
                                        },
                                        modifier = Modifier.testTag("home_help_button")
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 2. High-End Habit Wallet & Discipline Pass Banner (Minimal Dark Glassmorphism)
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_glassmorphic_wallet_card"),
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = Color(0xFF191228),
                        gradientBrush = Brush.verticalGradient(
                            listOf(
                                Color(0xFF261642),
                                Color(0xFF18102B),
                                Color(0xFF120C20)
                            )
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(
                                Color(0x388B5CF6),
                                Color(0x18A855F7),
                                Color(0x14FFFFFF)
                            )
                        ),
                        elevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            // Top Tag: PhonePe Habit-Wallet + VIP Discipline Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(PhonePePurple),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "₹",
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "HABIT-WALLET • UPI LITE",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.8.sp
                                    )
                                }

                                GlassBadge(
                                    text = "${uiState.xp} XP • LVL 4",
                                    textColor = SoftLavender,
                                    borderColor = Color(0x308B5CF6),
                                    backgroundColor = Color(0x1A8B5CF6),
                                    dotColor = PhonePeAccentPurple
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Big Balance Display
                            Text(
                                text = "Available Discipline Balance",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = "₹${uiState.balance.toInt()}",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-1.0).sp
                                )
                                Text(
                                    text = ".00",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "/ ₹${uiState.userData.allowance} limit",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 5-Bar Discipline Histogram + "I Resisted" Dopamine Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 5-Bar Discipline Streak Histogram
                                Column {
                                    Text(
                                        text = "7-Day Impulse Streak",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier
                                            .width(130.dp)
                                            .height(30.dp),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        val heights = listOf(0.4f, 0.65f, 0.85f, 1.0f, 0.55f)
                                        heights.forEachIndexed { i, ratio ->
                                            val isCurrent = i == 3
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight(ratio)
                                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(
                                                    if (isCurrent) PhonePeLightPurple
                                                    else Color(0x288B5CF6)
                                                )
                                            )
                                        }
                                    }
                                }

                                // Quick Dopamine "I Resisted" Glass Button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF201635))
                                        .border(
                                            1.dp,
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0x408B5CF6),
                                                    Color(0x25A855F7),
                                                    Color(0x20F59E0B)
                                                )
                                            ),
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable { onResist() }
                                        .padding(horizontal = 14.dp, vertical = 9.dp)
                                        .testTag("home_resist_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "⚡ I Resisted",
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "+15 XP",
                                            fontFamily = HelveticaFontFamily,
                                            color = AmberCashback,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2a. Big Runway XAI Engine Hero Card (Requested: Big button below audit card)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTab("runway") }
                        .testTag("home_runway_banner"),
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = Color(0xFF1B122F),
                    gradientBrush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF2E1754),
                            Color(0xFF1E1138),
                            Color(0xFF140B26)
                        )
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(
                            Color(0x808B5CF6),
                            Color(0x40C4B5FD),
                            Color(0x2000D09C)
                        )
                    ),
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))
                                            )
                                        )
                                        .border(1.2.dp, Color(0x60DDD6FE), RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = "Runway Engine",
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "RUNWAY XAI ENGINE",
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.6.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Autonomous Liquidity & Decision Trees",
                                        fontFamily = HelveticaFontFamily,
                                        color = SoftLavender,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            GlassBadge(
                                text = "LIVE XAI",
                                textColor = EmeraldGreen,
                                borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                backgroundColor = EmeraldGreen.copy(alpha = 0.15f),
                                dotColor = EmeraldGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Live Telemetry Highlights (Runway days and burn velocity)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x20000000))
                                .border(1.dp, Color(0x1A8B5CF6), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "PROJECTED RUNWAY",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp
                                )
                                Text(
                                    text = "19.2 Days",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(28.dp)
                                    .background(Color(0x20FFFFFF))
                            )

                            Column {
                                Text(
                                    text = "BURN VELOCITY",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp
                                )
                                Text(
                                    text = "₹${uiState.currentOutflowDay} / day",
                                    fontFamily = HelveticaFontFamily,
                                    color = if (uiState.isCriticalBurn) CoralRed else EmeraldGreen,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Big Launch Action Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(PhonePeLightPurple, PhonePePurple)
                                    )
                                )
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Launch Runway AI Simulation",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2b. Upload CSV / Excel Statement Button Card (User Request: "make the uplaod csv and excel file button")
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showUploadStatementModal = true }
                        .testTag("home_upload_csv_card"),
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = Color(0xFF161126),
                    gradientBrush = Brush.verticalGradient(
                        listOf(Color(0xFF22143B), Color(0xFF140D22))
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(Color(0x388B5CF6), Color(0x1FFFFFFF))
                    ),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x2600D09C))
                                .border(1.dp, Color(0x4000D09C), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = "Upload CSV",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Upload Statement (CSV / Excel)",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                GlassBadge(
                                    text = "AUTO-AUDIT",
                                    textColor = EmeraldGreen,
                                    borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                    backgroundColor = EmeraldGreen.copy(alpha = 0.12f)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Import PhonePe, GPay, HDFC, or SBI records to auto-categorize expenses",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0x208B5CF6))
                                .border(1.dp, Color(0x338B5CF6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.UploadFile,
                                contentDescription = "Upload",
                                tint = SoftLavender,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Low Balance Warning Banner (if triggered)
        if (isLowBalance) {
            item {
                val roastMsg = when {
                    balanceRatio <= 0.05 -> "Bro, you're practically paying in thoughts & prayers now. Stop spending."
                    balanceRatio <= 0.15 -> "Your bank account just filed a restraining order against you."
                    else -> "Danger zone! Time to look up recipes for plain rice and ice cubes."
                }
                val emoji = if (balanceRatio <= 0.10) "💀" else "🚨"

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = Color(0xFF2E0D17),
                    gradientBrush = Brush.verticalGradient(
                        listOf(Color(0xFF3F1321), Color(0xFF240A12))
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(Color(0xFFF43F5E), Color(0x60F43F5E))
                    ),
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = emoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = roastMsg,
                            fontFamily = HelveticaFontFamily,
                            color = CoralRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // 5. Recent AI Insight Glassmorphic Card
        item {
            AnimatedVisibility(
                visible = uiState.recentInsight != null,
                exit = shrinkVertically() + fadeOut()
            ) {
                uiState.recentInsight?.let { insight ->
                    val isBad = insight.quality == "BAD"
                    val isGood = insight.quality == "GOOD"
                    val accentColor = if (isBad) CoralRed else if (isGood) EmeraldGreen else PhonePeAccentPurple
                    val badgeEmoji = if (isBad) "📉" else if (isGood) "📈" else "⚖️"
                    val title = if (isBad) "Leakage Detected" else if (isGood) "Solid Choice" else "Neutral Spend"

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("recent_insight_card"),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF1F1836),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF2B204C), Color(0xFF18122B))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(accentColor, Color(0x608B5CF6), Color(0x403B82F6))
                        ),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(accentColor.copy(alpha = 0.2f))
                                    .border(1.dp, accentColor.copy(alpha = 0.5f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = badgeEmoji, fontSize = 18.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = title,
                                    fontFamily = HelveticaFontFamily,
                                    color = accentColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = insight.msg,
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = TextMuted,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onDismissInsight() }
                                    .testTag("dismiss_insight_button")
                            )
                        }
                    }
                }
            }
        }

        // 6. Transaction Log Header & Filter Chips (in Vibrant PhonePe Style)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Color(0xFF140F24))
                    .border(
                        1.dp,
                        Brush.verticalGradient(listOf(Color(0xFF8B5CF6), Color(0x338B5CF6))),
                        RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    // Center drag pill indicator
                    Box(
                        modifier = Modifier
                            .size(width = 38.dp, height = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0x608B5CF6))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val filterCategories = listOf(
                        "All" to "All Spends",
                        "food" to "Food",
                        "friends" to "Friends",
                        "essential" to "Grocery",
                        "shopping" to "Shopping",
                        "income" to "Income",
                        "bs" to "Impulse",
                        "health" to "Health",
                        "travel" to "Travel"
                    )
                    val activeLabel = filterCategories.find { it.first.equals(uiState.filterCat, ignoreCase = true) }?.second ?: "All Spends"

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Recent Activity",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.2).sp
                            )
                            Text(
                                text = "${filteredTxns.size} transactions",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        // Category Filter Submenu Dropdown Button
                        Box {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1E1734))
                                    .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(12.dp))
                                    .clickable { showCategoryMenu = !showCategoryMenu }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("txn_category_dropdown_button"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (uiState.filterCat != "All") {
                                    CategoryIcon(type = uiState.filterCat, size = 13.dp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = activeLabel,
                                    fontFamily = HelveticaFontFamily,
                                    color = SoftLavender,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = if (showCategoryMenu) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Select Category",
                                    tint = SoftLavender,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            // Submenu Filter Dropdown Menu
                            DropdownMenu(
                                expanded = showCategoryMenu,
                                onDismissRequest = { showCategoryMenu = false },
                                modifier = Modifier
                                    .background(Color(0xFF1E1632))
                                    .border(1.dp, Color(0x388B5CF6), RoundedCornerShape(14.dp))
                            ) {
                                filterCategories.forEach { (catKey, label) ->
                                    val isSelected = uiState.filterCat.equals(catKey, ignoreCase = true)
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                if (catKey != "All") {
                                                    CategoryIcon(type = catKey, size = 15.dp)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                }
                                                Text(
                                                    text = label,
                                                    fontFamily = HelveticaFontFamily,
                                                    color = if (isSelected) Color.White else TextSecondary,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        },
                                        trailingIcon = {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = PhonePeLightPurple,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        },
                                        onClick = {
                                            onFilterSelect(catKey)
                                            showCategoryMenu = false
                                        },
                                        modifier = Modifier.testTag("filter_chip_$catKey")
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.filterCat != "All") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x1F8B5CF6))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Filter: $activeLabel",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "✕ Reset",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onFilterSelect("All") }
                            )
                        }
                    }
                }
            }
        }

        // 7. Transaction Items (Rendered with Vibrant High-Contrast Cards)
        items(filteredTxns, key = { it.id }) { txn ->
            PhonePeTransactionCard(txn = txn)
        }

        item {
            Spacer(modifier = Modifier.height(96.dp))
        }
    }

    if (showUploadStatementModal) {
        StatementUploadModal(
            visible = showUploadStatementModal,
            onDismiss = { showUploadStatementModal = false },
            onImportDemoStatement = { bank ->
                onImportDemoStatement(bank)
                showUploadStatementModal = false
            },
            onImportCsv = { csv ->
                onImportCsv(csv)
                showUploadStatementModal = false
            }
        )
    }
}

@Composable
fun StatementUploadModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    onImportDemoStatement: (String) -> Unit,
    onImportCsv: (String) -> Unit
) {
    if (!visible) return
    val context = LocalContext.current
    var customCsvText by remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val content = context.contentResolver.openInputStream(it)?.bufferedReader()?.use { reader ->
                    reader.readText()
                }
                if (!content.isNullOrBlank()) {
                    onImportCsv(content)
                }
            } catch (e: Exception) {
                // Ignore file read error gracefully
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1535),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Upload Statement (CSV / Excel)",
                    fontFamily = HelveticaFontFamily,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Import transactions from PhonePe, GPay, HDFC, SBI, or ICICI CSV/Excel files to automatically audit and categorize spending.",
                    fontFamily = HelveticaFontFamily,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                // Native Device File Picker Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(PhonePeLightPurple, PhonePePurple)
                            )
                        )
                        .clickable { filePickerLauncher.launch("*/*") }
                        .padding(vertical = 12.dp, horizontal = 14.dp)
                        .testTag("upload_file_picker_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = "Pick File",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Browse CSV / Excel on Device",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Quick 1-Tap Statement Import Options
                Text(
                    text = "QUICK 1-TAP BANK DEMO IMPORTS",
                    fontFamily = HelveticaFontFamily,
                    color = SoftLavender,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF281845))
                            .border(1.dp, Color(0x608B5CF6), RoundedCornerShape(12.dp))
                            .clickable { onImportDemoStatement("PhonePe UPI") }
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "📱 PhonePe UPI", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "6 Sample Txns", color = SoftLavender, fontSize = 10.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1A263D))
                            .border(1.dp, Color(0x6038BDF8), RoundedCornerShape(12.dp))
                            .clickable { onImportDemoStatement("HDFC Statement") }
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🏛️ HDFC Bank", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "6 Sample Txns", color = SoftLavender, fontSize = 10.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Custom CSV Paste
                Text(
                    text = "OR PASTE STATEMENT CSV",
                    fontFamily = HelveticaFontFamily,
                    color = SoftLavender,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                OutlinedTextField(
                    value = customCsvText,
                    onValueChange = { customCsvText = it },
                    placeholder = { Text("e.g. Swiggy, 420\nDecathlon, 1200\nSalary Credit, 25000", color = TextMuted, fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = PhonePeLightPurple,
                        unfocusedBorderColor = Color(0x408B5CF6)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )

                if (customCsvText.isNotBlank()) {
                    Button(
                        onClick = { onImportCsv(customCsvText) },
                        colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Parse & Add Transactions", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x28FFFFFF))
            ) {
                Text("Close", color = Color.White)
            }
        }
    )
}


/**
 * PhonePe Transaction Card with vibrant styling, merchant avatar,
 * status subtitle ("Debited from Habit Wallet"), and health impact badge.
 */
@Composable
fun PhonePeTransactionCard(
    txn: TransactionItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(0xFF151022),
            gradientBrush = null,
            borderBrush = Brush.linearGradient(
                listOf(Color(0x228B5CF6), Color(0x14FFFFFF))
            ),
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Merchant/Category Icon in Google Pay / PhonePe Squircle container
                BusinessCategoryTile(
                    type = txn.cat,
                    size = 44.dp,
                    iconSize = 22.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Transaction Title, Context, and Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = txn.name,
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.2).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (txn.amount > 0) "Credited to Habit-Wallet • ${txn.date}"
                        else "Debited from Habit-Wallet • ${txn.date}",
                        fontFamily = HelveticaFontFamily,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 1.dp)
                    )

                    if (txn.context.isNotBlank()) {
                        Text(
                            text = txn.context,
                            fontFamily = HelveticaFontFamily,
                            color = SoftLavender,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // PhonePe Amount & Health/Discipline Impact
                val isPositive = txn.amount > 0
                val amountColor = if (isPositive) EmeraldGreen else Color.White
                val prefix = if (isPositive) "+₹" else "-₹"

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$prefix${abs(txn.amount).toInt()}.00",
                        fontFamily = HelveticaFontFamily,
                        color = amountColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.2).sp
                    )

                    if (txn.isJunk) {
                        Text(
                            text = "💔 -45m life",
                            fontFamily = HelveticaFontFamily,
                            color = CoralRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (txn.cat.equals("food", ignoreCase = true) || txn.cat.equals("essential", ignoreCase = true)) {
                        Text(
                            text = "🥗 +20 XP",
                            fontFamily = HelveticaFontFamily,
                            color = EmeraldGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
