package com.example.ui.screens

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
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.TransactionItem
import com.example.ui.components.BusinessCategoryTile
import com.example.ui.components.BusinessQuickActionsGrid
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
    modifier: Modifier = Modifier
) {
    var showBudgetSlider by remember { mutableStateOf(false) }
    var showTopMenu by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var tempAllowanceSlider by remember(uiState.userData.allowance) {
        mutableFloatStateOf(uiState.userData.allowance.toFloat())
    }

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

                        // Right PhonePe Action Buttons (QR Scanner & Submenu Overflow)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                                                    text = if (showBudgetSlider) "Hide Allowance Slider" else "Adjust Allowance Limit",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            showTopMenu = false
                                            showBudgetSlider = !showBudgetSlider
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

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Interactive Allowance / Budget Slider Toggle Button
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (showBudgetSlider) PhonePeLightPurple
                                                else Color(0xFF201633)
                                            )
                                            .border(
                                                1.dp,
                                                if (showBudgetSlider) PhonePeLightPurple
                                                else Color(0x308B5CF6),
                                                CircleShape
                                            )
                                            .clickable { showBudgetSlider = !showBudgetSlider }
                                            .testTag("home_slider_option_toggle"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Tune,
                                            contentDescription = "Adjust Budget Slider",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
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

                            // Interactive Budget Allowance Slider Card (User Request: "where is our slider option")
                            AnimatedVisibility(visible = showBudgetSlider) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF1A132C))
                                        .border(1.dp, Color(0x408B5CF6), RoundedCornerShape(16.dp))
                                        .padding(14.dp)
                                        .testTag("home_allowance_slider_card")
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "🎯", fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Discipline Allowance Slider",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        GlassBadge(
                                            text = "₹${tempAllowanceSlider.toInt()}/mo",
                                            textColor = SoftLavender,
                                            borderColor = Color(0x308B5CF6),
                                            backgroundColor = Color(0x1A8B5CF6)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Slider(
                                        value = tempAllowanceSlider,
                                        onValueChange = {
                                            tempAllowanceSlider = it
                                            onUpdateAllowance(it.toInt())
                                        },
                                        valueRange = 1000f..30000f,
                                        steps = 29,
                                        colors = SliderDefaults.colors(
                                            thumbColor = PhonePeLightPurple,
                                            activeTrackColor = PhonePeLightPurple,
                                            inactiveTrackColor = Color(0x308B5CF6)
                                        ),
                                        modifier = Modifier.fillMaxWidth().testTag("home_budget_slider")
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Min ₹1,000",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                        Text(
                                            text = "Max ₹30,000",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2a. Sophisticated Google Pay / PhonePe Business Quick Actions Grid
        item {
            BusinessQuickActionsGrid(
                onScanQr = { onNavigateToTab("log") },
                onPayContact = { onNavigateToTab("friends") },
                onSplitExpenses = { onNavigateToTab("friends") },
                onBankTransfer = { onNavigateToTab("budget") },
                onCheckBalance = { showBudgetSlider = !showBudgetSlider },
                onSelfTransfer = { onNavigateToTab("budget") },
                onPayBills = { onOpenInsights() },
                onLogExpense = { onNavigateToTab("log") }
            )
        }

        // 2b. Runway: Autonomous Finance & XAI Engine Featured Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTab("runway") }
                        .testTag("home_runway_banner"),
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = Color(0xFF161124),
                    gradientBrush = Brush.verticalGradient(
                        listOf(Color(0xFF221538), Color(0xFF140E20))
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(Color(0x338B5CF6), Color(0x18FFFFFF))
                    ),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Glowing Icon
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF24163E))
                                .border(1.dp, Color(0x388B5CF6), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Runway Engine",
                                tint = SoftLavender,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "RUNWAY ENGINE",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                GlassBadge(
                                    text = "XAI ACTIVE",
                                    textColor = EmeraldGreen,
                                    borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                    backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "30-Day Liquidity Forecast • Decision Tree • Roast & Rescue",
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
                                .background(Color(0xFF261942))
                                .border(1.dp, Color(0x408B5CF6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Open Runway",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2c. Deep Behavioral KYC Pending Action Banner (Available later to complete survey)
        if (!uiState.userData.isBehavioralKycCompleted) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenKyc() }
                            .testTag("home_kyc_incomplete_banner"),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF161124),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF201435), Color(0xFF130E20))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x408B5CF6), Color(0x14FFFFFF))
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
                                    .background(Color(0x208B5CF6))
                                    .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Behavioral KYC",
                                    tint = PhonePeAccentPurple,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Deep Behavioral KYC Pending",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    GlassBadge(
                                        text = "+250 XP",
                                        textColor = SoftLavender,
                                        borderColor = Color(0x308B5CF6),
                                        backgroundColor = Color(0x1A8B5CF6)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Complete behavioral survey to calibrate roasts & AI guardrails",
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
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Complete KYC",
                                    tint = PhonePeAccentPurple,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
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
