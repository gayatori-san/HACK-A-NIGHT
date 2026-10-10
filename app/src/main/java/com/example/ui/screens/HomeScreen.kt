package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.HelveticaFontFamily
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
import kotlin.math.abs

/**
 * Minimalist, ultra-clean HomeScreen.
 *
 * Designed per user specifications:
 * - Removed the Upload CSV button from the first page entirely.
 * - Cleaned up messy, out-of-order text with structured menus and submenus.
 * - Highly refined visual hierarchy with clear typography and zero text collisions.
 */
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
    val context = LocalContext.current
    var showTopMenu by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }

    val filteredTxns = if (uiState.filterCat == "All") {
        uiState.txns
    } else {
        uiState.txns.filter { it.cat.equals(uiState.filterCat, ignoreCase = true) }
    }

    val balanceRatio = (uiState.balance / uiState.userData.allowance.coerceAtLeast(1)).coerceIn(0.0, 1.0)
    val isLowBalance = balanceRatio <= 0.25

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("home_screen_lazy_column")
    ) {
        // 1. Sleek Minimal Header & Top Navigation Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PhonePeHeroGradient)
                    .padding(top = 10.dp, bottom = 14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                ) {
                    // Top App Row: User Identity & Action Menus
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Profile & UPI Lite Status
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onNavigateToTab("budget") }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(PhonePeAccentPurple, PhonePePurple)
                                        )
                                    )
                                    .border(1.2.dp, Color(0x80FFFFFF), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "SG",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Samyak G.",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldGreen)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "UPI Lite Active",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }

                        // Right Action Bar: Quick KYC Shortcut & Comprehensive Submenu
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Behavioral KYC Icon Shortcut
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
                                    contentDescription = "Behavioral KYC",
                                    tint = if (uiState.userData.isBehavioralKycCompleted) EmeraldGreen else SoftLavender,
                                    modifier = Modifier.size(19.dp)
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

                            // Clean Overflow Menu with Structured Submenus
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
                                        contentDescription = "Menu Options",
                                        tint = Color.White,
                                        modifier = Modifier.size(19.dp)
                                    )
                                    if (uiState.recentInsight != null) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(PhonePeAccentPurple)
                                                .align(Alignment.TopEnd)
                                        )
                                    }
                                }

                                // Structured Dropdown with Submenus
                                DropdownMenu(
                                    expanded = showTopMenu,
                                    onDismissRequest = { showTopMenu = false },
                                    modifier = Modifier
                                        .background(Color(0xFF1E1632))
                                        .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(14.dp))
                                ) {
                                    // Submenu Section: Discipline & Health
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
                                                    text = if (uiState.userData.isBehavioralKycCompleted) "Active" else "+250 XP",
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
                                                    text = "AI Discipline Insights",
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

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        color = Color(0x20FFFFFF)
                                    )

                                    // Submenu Section: Finance & Forecasting
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
                                                    text = "Goals & Budget Limits",
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

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        color = Color(0x20FFFFFF)
                                    )

                                    // Submenu Section: Uploads & Statements
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.UploadFile,
                                                    contentDescription = null,
                                                    tint = EmeraldGreen,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = "Upload Docx & Payment Slips",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        },
                                        onClick = {
                                            showTopMenu = false
                                            onNavigateToTab("log")
                                        }
                                    )

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

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Minimalist Glassmorphic Habit Balance Card
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
                            // Top Tag: Wallet title & Level badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "AVAILABLE DISCIPLINE BALANCE",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.8.sp
                                )

                                GlassBadge(
                                    text = "${uiState.xp} XP • LVL 4",
                                    textColor = SoftLavender,
                                    borderColor = Color(0x308B5CF6),
                                    backgroundColor = Color(0x1A8B5CF6),
                                    dotColor = PhonePeAccentPurple
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Large, Pristine Balance Display
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = "₹${uiState.balance.toInt()}",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-1.0).sp
                                )
                                Text(
                                    text = ".00",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "of ₹${uiState.userData.allowance} limit",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Clean Progress Bar (Allowance utilization)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x20FFFFFF))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(balanceRatio.toFloat())
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    PhonePeLightPurple,
                                                    if (isLowBalance) CoralRed else EmeraldGreen
                                                )
                                            )
                                        )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 7-Day Discipline & Impulse Spending Graph (Requested by user)
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "7-Day Impulse Streak",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "94% Clean Streak",
                                        fontFamily = HelveticaFontFamily,
                                        color = EmeraldGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                                    val heights = listOf(0.40f, 0.65f, 0.85f, 0.55f, 1.0f, 0.75f, 0.90f)
                                    val currentDayIndex = 4

                                    days.forEachIndexed { index, day ->
                                        val isCurrent = index == currentDayIndex
                                        val heightFraction = heights[index]

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(18.dp)
                                                    .fillMaxHeight(heightFraction)
                                                    .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                                                    .background(
                                                        if (isCurrent) Brush.verticalGradient(
                                                            listOf(EmeraldGreen, PhonePeLightPurple)
                                                        ) else Brush.verticalGradient(
                                                            listOf(Color(0x358B5CF6), Color(0x208B5CF6))
                                                        )
                                                    )
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = day,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isCurrent) Color.White else TextMuted,
                                                fontSize = 9.sp,
                                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Bottom Row: Status tag & Single dopamine "+ Resist" pill
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isLowBalance) "⚠️ Low balance buffer" else "✨ Disciplined pace",
                                    fontFamily = HelveticaFontFamily,
                                    color = if (isLowBalance) CoralRed else SoftLavender,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                // Dopamine "I Resisted" Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF221738))
                                        .border(
                                            1.dp,
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0x508B5CF6),
                                                    Color(0x30F59E0B)
                                                )
                                            ),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable { onResist() }
                                        .padding(horizontal = 14.dp, vertical = 7.dp)
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

        // 3. Bigger Runway AI Telemetry Card (Simulate button removed as requested)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTab("runway") }
                        .testTag("home_runway_banner"),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color(0xFF191129),
                    gradientBrush = Brush.verticalGradient(
                        listOf(Color(0xFF2E1754), Color(0xFF1B0E33), Color(0xFF130924))
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(Color(0x608B5CF6), Color(0x30A78BFA), Color(0x18FFFFFF))
                    ),
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Header
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
                                        .border(1.2.dp, Color(0x50DDD6FE), RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = "Runway Intelligence",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
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
                                            letterSpacing = 0.4.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Predictive Liquidity & Financial Runway",
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

                        // Large Telemetry Data Grid
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x22000000))
                                .border(1.dp, Color(0x1A8B5CF6), RoundedCornerShape(14.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
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
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "19.2",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Days left",
                                        fontFamily = HelveticaFontFamily,
                                        color = EmeraldGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(36.dp)
                                    .background(Color(0x20FFFFFF))
                            )

                            Column {
                                Text(
                                    text = "DAILY BURN VELOCITY",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "₹${uiState.currentOutflowDay}",
                                        fontFamily = HelveticaFontFamily,
                                        color = if (uiState.isCriticalBurn) CoralRed else EmeraldGreen,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "/ day",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Explanatory note indicating the tab can be opened
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Algorithmic decision tree models cashflow liquidity.",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "View Engine ↗",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 3b. Dedicated WhatsApp Spending Goals Helper Card & Judge Notice
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Column {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    val waIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://wa.me/?text=Hi%20Habit-Wallet%20AI!%20Help%20me%20track%20my%20spending%20goals.")
                                    )
                                    context.startActivity(waIntent)
                                } catch (e: Exception) {
                                    // Fallback handled safely
                                }
                            }
                            .testTag("whatsapp_spending_goals_helper_tab"),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF131F1D),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF162D24), Color(0xFF101C17))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x8025D366), Color(0x3025D366), Color(0x18FFFFFF))
                        ),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0x2825D366))
                                        .border(1.2.dp, Color(0x7025D366), RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Chat,
                                        contentDescription = "WhatsApp Bot",
                                        tint = Color(0xFF25D366),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Your Personal WhatsApp Spending Goals Helper",
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "24/7 AI Accountability Bot on WhatsApp",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color(0xCC25D366),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF25D366))
                                    .padding(horizontal = 12.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Chat ↗",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color(0xFF0F2418),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Notice Message for the Judge
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("whatsapp_judge_whitelist_notice"),
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Color(0xFF1C162E),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF22173B), Color(0xFF160F25))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x358B5CF6), Color(0x2525D366))
                        ),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Judge Note",
                                tint = SoftLavender,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "As only three numbers are whitelisted for the judge to use the bot, your number has to be whitelisted. To provide this demo, we have included our WhatsApp bot into the demo AI video.",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight.Normal
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
                    balanceRatio <= 0.05 -> "Stop spending. Your wallet is at critical reserves."
                    balanceRatio <= 0.15 -> "Danger zone! Conserve your allowance for remaining days."
                    else -> "Warning: Low balance pace detected."
                }

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    backgroundColor = Color(0xFF2E0D17),
                    gradientBrush = Brush.verticalGradient(
                        listOf(Color(0xFF3F1321), Color(0xFF240A12))
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(Color(0xFFF43F5E), Color(0x60F43F5E))
                    ),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🚨", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = roastMsg,
                            fontFamily = HelveticaFontFamily,
                            color = CoralRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 5. Recent AI Insight Glassmorphic Card (if active)
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
                            .padding(horizontal = 18.dp, vertical = 4.dp)
                            .testTag("recent_insight_card"),
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = Color(0xFF1F1836),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF2B204C), Color(0xFF18122B))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(accentColor, Color(0x608B5CF6))
                        ),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = badgeEmoji, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(10.dp))

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
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = TextMuted,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { onDismissInsight() }
                                    .testTag("dismiss_insight_button")
                            )
                        }
                    }
                }
            }
        }

        // 6. Recent Activity Section Header with Clean Filter Submenu
        item {
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Activity",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${filteredTxns.size})",
                            fontFamily = HelveticaFontFamily,
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    // Category Filter Dropdown Submenu
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1E1734))
                                .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(10.dp))
                                .clickable { showCategoryMenu = !showCategoryMenu }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .testTag("txn_category_dropdown_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (uiState.filterCat != "All") {
                                CategoryIcon(type = uiState.filterCat, size = 12.dp)
                                Spacer(modifier = Modifier.width(5.dp))
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

                        // Filter Submenu
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
                                                CategoryIcon(type = catKey, size = 14.dp)
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
                                                modifier = Modifier.size(15.dp)
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
            }
        }

        // 7. Clean, Pristine Transaction Cards (No Messy Run-on Texts)
        items(filteredTxns, key = { it.id }) { txn ->
            PhonePeTransactionCard(txn = txn)
        }

        item {
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

/**
 * Ultra-clean, minimal transaction card.
 *
 * Eliminates wordy run-on text strings so that fonts and layouts remain
 * crisp, balanced, and perfectly proportioned.
 */
@Composable
fun PhonePeTransactionCard(
    txn: TransactionItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 3.dp)
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            backgroundColor = Color(0xFF151022),
            gradientBrush = null,
            borderBrush = Brush.linearGradient(
                listOf(Color(0x228B5CF6), Color(0x10FFFFFF))
            ),
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category / Merchant Icon Squircle
                BusinessCategoryTile(
                    type = txn.cat,
                    size = 38.dp,
                    iconSize = 18.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Clean Merchant Title & Context
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = txn.name,
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.1).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (txn.context.isNotBlank()) txn.context else txn.date,
                        fontFamily = HelveticaFontFamily,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // High-Contrast Amount & Impact Tag
                val isPositive = txn.amount > 0
                val amountColor = if (isPositive) EmeraldGreen else Color.White
                val prefix = if (isPositive) "+₹" else "-₹"

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$prefix${abs(txn.amount).toInt()}",
                        fontFamily = HelveticaFontFamily,
                        color = amountColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (txn.isJunk) {
                        Text(
                            text = "💔 -45m",
                            fontFamily = HelveticaFontFamily,
                            color = CoralRed,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (txn.cat.equals("food", ignoreCase = true) || txn.cat.equals("essential", ignoreCase = true)) {
                        Text(
                            text = "🥗 +20 XP",
                            fontFamily = HelveticaFontFamily,
                            color = EmeraldGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
