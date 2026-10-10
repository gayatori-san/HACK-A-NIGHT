package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.InvestTip
import com.example.data.models.SavingGoal
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.CoralRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PhonePeAccentPurple
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState
import kotlin.math.max

data class ImportantPaymentOption(
    val id: String,
    val name: String,
    val subtitle: String,
    val emoji: String,
    val defaultTarget: Double,
    val category: String, // "Liability" or "Asset"
    val defaultWallet: String,
    val priorityTag: String,
    val tagColor: Color,
    val psychologicalNote: String
)

val IMPORTANT_PAYMENT_OPTIONS = listOf(
    ImportantPaymentOption(
        id = "home_loan",
        name = "Home Loan / Housing EMI",
        subtitle = "Multi-Year Long-Term Mortgage",
        emoji = "🏠",
        defaultTarget = 35000.0,
        category = "Liability",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "CRITICAL MULTI-YEAR",
        tagColor = CoralRed,
        psychologicalNote = "Non-negotiable multi-year commitment. Missing home loan EMIs triggers high penalty interest and severely degrades your credit score."
    ),
    ImportantPaymentOption(
        id = "big_emis",
        name = "Big EMIs & Vehicle Loan",
        subtitle = "Car / Automobile Multi-Year EMI",
        emoji = "🚗",
        defaultTarget = 18000.0,
        category = "Liability",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "HIGH PRIORITY EMI",
        tagColor = AmberWarning,
        psychologicalNote = "Fixed recurring liability. Pre-allocating to your vault prevents dangerous month-end deficit crunches."
    ),
    ImportantPaymentOption(
        id = "electricity_bills",
        name = "Electricity & Power Utility Bills",
        subtitle = "Monthly Non-Negotiable Power Bills",
        emoji = "⚡",
        defaultTarget = 3500.0,
        category = "Liability",
        defaultWallet = "UPI Lite Vault",
        priorityTag = "ESSENTIAL BILL",
        tagColor = PhonePeLightPurple,
        psychologicalNote = "Fundamental life utility. Always reserve upfront so your family's essential living services are never interrupted."
    ),
    ImportantPaymentOption(
        id = "water_gas_bills",
        name = "Water, Piped Gas & Utility Dues",
        subtitle = "Essential Civic Utilities",
        emoji = "💧",
        defaultTarget = 1500.0,
        category = "Liability",
        defaultWallet = "UPI Lite Vault",
        priorityTag = "ESSENTIAL BILL",
        tagColor = SoftLavender,
        psychologicalNote = "Basic household recurring dues required for everyday life, sanitation, and health."
    ),
    ImportantPaymentOption(
        id = "house_rent",
        name = "House Rent & Maintenance",
        subtitle = "Monthly Shelter Obligation",
        emoji = "🏢",
        defaultTarget = 24000.0,
        category = "Liability",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "ESSENTIAL SHELTER",
        tagColor = CoralRed,
        psychologicalNote = "Due on the 1st of every month. Locking this capital away prevents accidental weekend overspending."
    ),
    ImportantPaymentOption(
        id = "insurance",
        name = "Health & Term Insurance Premium",
        subtitle = "Annual Policy Defense Shield",
        emoji = "🏥",
        defaultTarget = 24000.0,
        category = "Asset",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "VITAL DEFENSE",
        tagColor = EmeraldGreen,
        psychologicalNote = "Prevents sudden medical emergency bankruptcy. Your single most vital defensive financial shield."
    ),
    ImportantPaymentOption(
        id = "education_loan",
        name = "Education / Tuition Loan EMI",
        subtitle = "Child Tuition or Degree Loan",
        emoji = "🎓",
        defaultTarget = 15000.0,
        category = "Liability",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "MULTI-YEAR LOAN",
        tagColor = AmberWarning,
        psychologicalNote = "Multi-year educational investment. Punctual payment safeguards your family's academic future."
    ),
    ImportantPaymentOption(
        id = "credit_card",
        name = "Credit Card Total Dues & Debt",
        subtitle = "Clear High-Interest 42% APR Debt",
        emoji = "💳",
        defaultTarget = 30000.0,
        category = "Liability",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "DANGEROUS DEBT",
        tagColor = CoralRed,
        psychologicalNote = "Credit cards compound at 42% APR. Eliminating this liability yields an instant guaranteed 42% return."
    ),
    ImportantPaymentOption(
        id = "emergency_runway",
        name = "Emergency Runway (6-12 Months)",
        subtitle = "Liquid Buffer for Job Loss & Emergencies",
        emoji = "🛡️",
        defaultTarget = 150000.0,
        category = "Asset",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "PEACE OF MIND",
        tagColor = EmeraldGreen,
        psychologicalNote = "6 months of living expenses safely stashed gives you total leverage and autonomy in life."
    ),
    ImportantPaymentOption(
        id = "custom_goal",
        name = "Custom Milestone / Goal",
        subtitle = "Enter your own goal name and budget",
        emoji = "✨",
        defaultTarget = 10000.0,
        category = "Asset",
        defaultWallet = "Habit Lock Vault",
        priorityTag = "CUSTOM GOAL",
        tagColor = SoftLavender,
        psychologicalNote = "Categorize carefully whether this will grow your net worth (Asset) or drain cashflow (Liability)."
    )
)

@Composable
fun BudgetScreen(
    uiState: WalletUiState,
    investTips: List<InvestTip>,
    onSyncSupabase: () -> Unit,
    onUpdateAllowance: (Int) -> Unit = {},
    onAddGoal: (SavingGoal) -> Unit = {},
    onDepositGoal: (String, Double) -> Unit = { _, _ -> },
    onDeleteGoal: (String) -> Unit = {},
    onToggleCfoMode: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCfoModeActive by remember(uiState.isCfoModeActive) { mutableStateOf(uiState.isCfoModeActive) }
    var showCreateGoalDialog by remember { mutableStateOf(false) }
    var showButtonDropdown by remember { mutableStateOf(false) }
    var preselectedPaymentOption by remember { mutableStateOf<ImportantPaymentOption?>(null) }

    BackHandler(enabled = isCfoModeActive) {
        isCfoModeActive = false
        onToggleCfoMode(false)
    }

    val allowance = uiState.userData.allowance
    val essentials = (allowance * 0.5).toInt()
    val discretionary = (allowance * 0.3).toInt()
    val savings = (allowance * 0.2).toInt()

    var tempAllowanceSlider by remember(uiState.userData.allowance) {
        mutableFloatStateOf(uiState.userData.allowance.toFloat())
    }

    val totalGoalsTarget = uiState.goals.sumOf { it.targetAmount }
    val totalGoalsSaved = uiState.goals.sumOf { it.savedAmount }
    val overallGoalProgress = if (totalGoalsTarget > 0) (totalGoalsSaved / totalGoalsTarget).toFloat().coerceIn(0f, 1f) else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("budget_screen_lazy_column")
    ) {
        if (!isCfoModeActive) {
            // ==========================================
            // PRIMARY VIEW: PRIORITY AUDIT FIRST -> GOALS & WALLETS -> DISCIPLINE SLIDER -> CFO
            // ==========================================

            // 1. FIRSTLY: "What are your most important payments over the years?" Priority Assessment
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = Color(0xFF161026),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF281442), Color(0xFF130C22))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x60F59E0B), Color(0x308B5CF6))
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
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(AmberWarning.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ElectricBolt,
                                            contentDescription = null,
                                            tint = AmberWarning,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "IMPORTANT PAYMENTS OVER THE YEARS",
                                        fontFamily = HelveticaFontFamily,
                                        color = AmberWarning,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp
                                    )
                                }
                                GlassBadge(
                                    text = "MUST SECURE FIRST",
                                    textColor = CoralRed,
                                    borderColor = CoralRed.copy(alpha = 0.4f),
                                    backgroundColor = CoralRed.copy(alpha = 0.15f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "What are your most important payments over the years?",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Home loans, big EMIs, and bills like electricity must be prioritized before allocating savings to discretionary goals.",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "SELECT PRIORITY OBLIGATION TO LOCK WITH 1-TAP:",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(IMPORTANT_PAYMENT_OPTIONS.take(7)) { option ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x28FFFFFF))
                                            .border(1.dp, option.tagColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                            .clickable {
                                                preselectedPaymentOption = option
                                                showCreateGoalDialog = true
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = option.emoji, fontSize = 15.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(
                                                    text = option.name,
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "₹${option.defaultTarget.toInt()} • ${option.subtitle}",
                                                    color = SoftLavender,
                                                    fontSize = 9.sp
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

            // 2. Goals & Wallets Header & Overview
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Goals & Wallets",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Categorize Assets vs Liabilities & Link Dedicated Wallets",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        // Create Goal Action Button with INSTANT DROPDOWN MENU
                        Box {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PhonePePurple)
                                    .clickable {
                                        showButtonDropdown = true
                                    }
                                    .padding(horizontal = 14.dp, vertical = 9.dp)
                                    .testTag("set_new_goal_button")
                                    .testTag("create_goal_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Set a New Goal",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Set a New Goal",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown Menu",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            // The Instant Dropdown Menu right when "Set a New Goal" button is clicked
                            DropdownMenu(
                                expanded = showButtonDropdown,
                                onDismissRequest = { showButtonDropdown = false },
                                modifier = Modifier
                                    .background(Color(0xFF1E1335))
                                    .border(1.dp, Color(0x608B5CF6), RoundedCornerShape(12.dp))
                                    .testTag("set_new_goal_instant_dropdown")
                            ) {
                                Text(
                                    text = "  MOST IMPORTANT PAYMENTS OVER THE YEARS",
                                    color = AmberWarning,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                                IMPORTANT_PAYMENT_OPTIONS.forEach { opt ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = opt.emoji, fontSize = 16.sp)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Column {
                                                        Text(
                                                            text = opt.name,
                                                            color = Color.White,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Text(
                                                            text = "₹${opt.defaultTarget.toInt()} • ${opt.subtitle}",
                                                            color = TextMuted,
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                GlassBadge(
                                                    text = opt.priorityTag,
                                                    textColor = opt.tagColor,
                                                    borderColor = opt.tagColor.copy(alpha = 0.3f),
                                                    backgroundColor = opt.tagColor.copy(alpha = 0.12f)
                                                )
                                            }
                                        },
                                        onClick = {
                                            preselectedPaymentOption = opt
                                            showButtonDropdown = false
                                            showCreateGoalDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Overall Goals Metric Card
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF1B132B),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF281545), Color(0xFF160E26))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x508B5CF6), Color(0x20FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TOTAL GOAL CAPITAL",
                                    fontFamily = HelveticaFontFamily,
                                    color = SoftLavender,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                                GlassBadge(
                                    text = "${(overallGoalProgress * 100).toInt()}% FUNDED",
                                    textColor = EmeraldGreen,
                                    borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                    backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "₹${totalGoalsSaved.toInt()}",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "/ ₹${totalGoalsTarget.toInt()} target",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(bottom = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Overall progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x30FFFFFF))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(overallGoalProgress)
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(PhonePeLightPurple, EmeraldGreen)
                                            )
                                        )
                                    )
                            }
                        }
                    }
                }
            }

            // 2. Goals List
            itemsIndexed(uiState.goals) { _, goal ->
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                    val isAsset = goal.category.equals("Asset", ignoreCase = true)
                    val categoryColor = if (isAsset) EmeraldGreen else AmberWarning

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("goal_item_${goal.id}"),
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = Color(0xFF171124),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF221538), Color(0xFF140D20))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x388B5CF6), Color(0x18FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Top Row: Goal Name, Category Badge (Asset vs Liability), Delete
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isAsset) Color(0x2200D09C) else Color(0x22F59E0B)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when {
                                                goal.name.contains("Home", true) || goal.name.contains("House", true) || goal.name.contains("Rent", true) -> Icons.Default.Home
                                                goal.name.contains("Electric", true) || goal.name.contains("Bill", true) || goal.name.contains("Power", true) -> Icons.Default.ElectricBolt
                                                goal.name.contains("Loan", true) || goal.name.contains("EMI", true) || goal.name.contains("Car", true) -> Icons.Default.AccountBalance
                                                goal.name.contains("Insurance", true) || goal.name.contains("Health", true) -> Icons.Default.Security
                                                goal.name.contains("Bike", true) -> Icons.Default.DirectionsBike
                                                goal.name.contains("MacBook", true) || goal.name.contains("Laptop", true) -> Icons.Default.Laptop
                                                isAsset -> Icons.Default.Savings
                                                else -> Icons.Default.ShoppingCart
                                            },
                                            contentDescription = null,
                                            tint = categoryColor,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = goal.name,
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            GlassBadge(
                                                text = if (isAsset) "ASSET" else "LIABILITY",
                                                textColor = categoryColor,
                                                borderColor = categoryColor.copy(alpha = 0.4f),
                                                backgroundColor = categoryColor.copy(alpha = 0.12f)
                                            )
                                            if (goal.name.contains("Home", true) || goal.name.contains("Loan", true) || goal.name.contains("EMI", true)) {
                                                GlassBadge(
                                                    text = "PRIORITY EMI",
                                                    textColor = CoralRed,
                                                    borderColor = CoralRed.copy(alpha = 0.4f),
                                                    backgroundColor = CoralRed.copy(alpha = 0.15f)
                                                )
                                            } else if (goal.name.contains("Electric", true) || goal.name.contains("Bill", true)) {
                                                GlassBadge(
                                                    text = "MANDATORY BILL",
                                                    textColor = PhonePeLightPurple,
                                                    borderColor = PhonePeLightPurple.copy(alpha = 0.4f),
                                                    backgroundColor = PhonePeLightPurple.copy(alpha = 0.15f)
                                                )
                                            }
                                            Text(
                                                text = "• ${goal.walletType}",
                                                fontFamily = HelveticaFontFamily,
                                                color = SoftLavender,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { onDeleteGoal(goal.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Saved Amount vs Target
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "₹${goal.savedAmount.toInt()} saved",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Target: ₹${goal.targetAmount.toInt()} (${(goal.progress * 100).toInt()}%)",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Progress Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x28FFFFFF))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(goal.progress)
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(if (isAsset) EmeraldGreen else PhonePeLightPurple)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Wallet Redirection & Deposit Actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // If external wallet (e.g. Amazon Pay Wallet), button redirects to Amazon!
                                if (!goal.externalUrl.isNullOrBlank() || goal.walletType.contains("Amazon", true)) {
                                    val amazonUrl = goal.externalUrl ?: "https://www.amazon.in/gp/payment/wallet/"
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF261A3E))
                                            .border(1.dp, Color(0x608B5CF6), RoundedCornerShape(10.dp))
                                            .clickable {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(amazonUrl))
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    // Fallback browser
                                                }
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                            .testTag("goal_redirect_amazon_${goal.id}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Open Amazon Wallet",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.OpenInNew,
                                                contentDescription = "Redirect",
                                                tint = SoftLavender,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "Remaining: ₹${goal.remainingAmount.toInt()}",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }

                                // Quick Deposit Action (+₹1,000 / +₹5,000)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0x228B5CF6))
                                            .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(8.dp))
                                            .clickable { onDepositGoal(goal.id, 1000.0) }
                                            .padding(horizontal = 8.dp, vertical = 5.dp)
                                            .testTag("deposit_1k_${goal.id}")
                                    ) {
                                        Text(
                                            text = "+₹1,000",
                                            fontFamily = HelveticaFontFamily,
                                            color = SoftLavender,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0x228B5CF6))
                                            .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(8.dp))
                                            .clickable { onDepositGoal(goal.id, 5000.0) }
                                            .padding(horizontal = 8.dp, vertical = 5.dp)
                                            .testTag("deposit_5k_${goal.id}")
                                    ) {
                                        Text(
                                            text = "+₹5,000",
                                            fontFamily = HelveticaFontFamily,
                                            color = SoftLavender,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. The Discipline Allowance Slider (Right after Goals, as requested!)
            item {
                Spacer(modifier = Modifier.height(14.dp))

                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("budget_discipline_slider_card"),
                        shape = RoundedCornerShape(22.dp),
                        backgroundColor = Color(0xFF1E1433),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF2A174A), Color(0xFF170E28))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x608B5CF6), Color(0x20FFFFFF))
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
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x288B5CF6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Tune,
                                            contentDescription = "Slider",
                                            tint = SoftLavender,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Discipline Allowance Slider",
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Hard Monthly Living Spending Cap",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                GlassBadge(
                                    text = "₹${tempAllowanceSlider.toInt()}/mo",
                                    textColor = Color.White,
                                    borderColor = Color(0x408B5CF6),
                                    backgroundColor = Color(0x308B5CF6)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Explanatory Guidance (What does the slider actually do?)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x20000000))
                                    .border(1.dp, Color(0x188B5CF6), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                val goalContribution = (50000 - tempAllowanceSlider.toInt()).coerceAtLeast(0) / 4
                                Column {
                                    Text(
                                        text = "🎯 HOW THIS ACCELERATES YOUR GOALS",
                                        fontFamily = HelveticaFontFamily,
                                        color = SoftLavender,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.6.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "By tightening your allowance to ₹${tempAllowanceSlider.toInt()}/mo, you save an extra ₹$goalContribution/mo directly into your Goals Wallet.",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Slider(
                                value = tempAllowanceSlider,
                                onValueChange = {
                                    tempAllowanceSlider = it
                                    onUpdateAllowance(it.toInt())
                                },
                                valueRange = 1000f..40000f,
                                steps = 39,
                                colors = SliderDefaults.colors(
                                    thumbColor = PhonePeLightPurple,
                                    activeTrackColor = PhonePeLightPurple,
                                    inactiveTrackColor = Color(0x308B5CF6)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("budget_allowance_slider")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Strict (₹1,000)",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "Safe Cap: ₹${(tempAllowanceSlider / 30).toInt()}/day",
                                    fontFamily = HelveticaFontFamily,
                                    color = EmeraldGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Relaxed (₹40,000)",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }

            // 4. CFO Mode Action Button (Assigned button to open CFO mode after goals)
            item {
                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isCfoModeActive = true }
                            .testTag("open_cfo_mode_button"),
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = Color(0xFF1B112D),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF2C164F), Color(0xFF150D24))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x808B5CF6), Color(0x3000D09C))
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF7C3AED), Color(0xFF4C1D95))
                                        )
                                    )
                                    .border(1.dp, Color(0x60DDD6FE), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Assessment,
                                    contentDescription = "CFO Mode",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Open AI CFO Mode",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    GlassBadge(
                                        text = "ADVANCED",
                                        textColor = EmeraldGreen,
                                        borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                        backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "50/30/20 Capital Allocation, Rebalancing Engine & Supabase Cloud",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x288B5CF6))
                                    .border(1.dp, Color(0x408B5CF6), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Open",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

        } else {
            // ==========================================
            // SECONDARY VIEW: DEDICATED CFO MODE SUITE
            // ==========================================

            // CFO Top Header with Back to Goals Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            isCfoModeActive = false
                            onToggleCfoMode(false)
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Goals",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CFO Suite",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            GlassBadge(
                                text = "AI STRATEGY",
                                textColor = EmeraldGreen,
                                borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                            )
                        }
                        Text(
                            text = "Target: ${uiState.userData.goal} • Restriction: ${uiState.userData.weakness}",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // 1. The 50/30/20 Capital Allocation Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        backgroundColor = Color(0xFF22163E),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF3B1D6B), Color(0xFF241347), Color(0xFF160B2B))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9), Color(0x20FFFFFF))
                        ),
                        elevation = 0.dp
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "THE 50/30/20 CAPITAL ALLOCATION",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color(0xCCFFFFFF),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.8.sp
                                )

                                GlassBadge(
                                    text = "OPTIMIZED",
                                    textColor = EmeraldGreen,
                                    borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                    backgroundColor = EmeraldGreen.copy(alpha = 0.15f),
                                    dotColor = EmeraldGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Essentials 50%
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Essentials (50%)", fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text(text = "₹$essentials", fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x2BFFFFFF))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(EmeraldGreen)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Discretionary 30%
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Discretionary (30%)", fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text(text = "₹$discretionary", fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x2BFFFFFF))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.3f)
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(AmberWarning)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Savings 20%
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Savings Target (20%)", fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text(text = "₹$savings", fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x2BFFFFFF))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.2f)
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(PhonePeLightPurple)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Supabase Cloud Database Status Card
            item {
                val isConfigured = uiState.isSupabaseConfigured
                val statusColor = if (isConfigured) EmeraldGreen else AmberWarning
                val statusText = if (isConfigured) {
                    if (uiState.supabaseSyncState == "syncing") "SYNCING..." else "CONNECTED"
                } else "LOCAL MODE (KEY PENDING)"

                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("supabase_status_card"),
                        shape = RoundedCornerShape(22.dp),
                        backgroundColor = Color(0x22131318),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0x24FFFFFF), Color(0x0AFFFFFF))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x66FFFFFF), Color(0x203ECF8E), Color(0x33FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "⚡", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Supabase Cloud Database",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                GlassBadge(
                                    text = statusText,
                                    textColor = statusColor,
                                    borderColor = statusColor.copy(alpha = 0.4f),
                                    backgroundColor = statusColor.copy(alpha = 0.15f),
                                    dotColor = statusColor
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (isConfigured) {
                                    "Real-time PostgREST sync active with your Supabase backend."
                                } else {
                                    "Configure SUPABASE_URL & SUPABASE_KEY in Secrets panel to sync records."
                                },
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )

                            if (isConfigured) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(PhonePePurple)
                                        .clickable { onSyncSupabase() }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Sync Cloud Now 🔄",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Investment & Growth Forecast
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = Color(0xFF1A132C),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0xFF261545), Color(0xFF140D24))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x408B5CF6), Color(0x18FFFFFF))
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "📈 5-YEAR WEALTH TRAJECTORY",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "Silver (+9%)", color = TextMuted, fontSize = 11.sp)
                                    Text(text = "₹${uiState.silverGrowth}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(text = "Gold (+11%)", color = TextMuted, fontSize = 11.sp)
                                    Text(text = "₹${uiState.goldGrowth}", color = AmberWarning, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(text = "Index Fund (+15%)", color = TextMuted, fontSize = 11.sp)
                                    Text(text = "₹${uiState.indexGrowth}", color = EmeraldGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // ==========================================
    // CREATE GOAL DIALOG / MODAL
    // ==========================================
    if (showCreateGoalDialog) {
        val initialOption = preselectedPaymentOption ?: IMPORTANT_PAYMENT_OPTIONS.first()
        var selectedOption by remember { mutableStateOf(initialOption) }
        var isDropdownExpanded by remember { mutableStateOf(true) } // Drops down instantly when button is clicked
        var goalName by remember { mutableStateOf(selectedOption.name) }
        var targetAmtStr by remember { mutableStateOf(selectedOption.defaultTarget.toInt().toString()) }
        var savedAmtStr by remember { mutableStateOf("0") }
        var category by remember { mutableStateOf(selectedOption.category) } // "Asset" or "Liability"
        var walletType by remember { mutableStateOf(selectedOption.defaultWallet) }

        val selectPaymentOption: (ImportantPaymentOption) -> Unit = { opt ->
            selectedOption = opt
            goalName = opt.name
            targetAmtStr = opt.defaultTarget.toInt().toString()
            category = opt.category
            walletType = opt.defaultWallet
            isDropdownExpanded = false
        }

        AlertDialog(
            onDismissRequest = { showCreateGoalDialog = false },
            containerColor = Color(0xFF1B1230),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Set a New Goal & Wallet",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Priority Financial Obligations",
                            fontFamily = HelveticaFontFamily,
                            color = SoftLavender,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(
                        onClick = { showCreateGoalDialog = false },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Firstly: Question Prompt Card
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Color(0xFF261545),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x60F59E0B), Color(0x308B5CF6))
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⚡", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "FIRST: CHOOSE PRIORITY OBLIGATION",
                                    fontFamily = HelveticaFontFamily,
                                    color = AmberWarning,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "What is your most important payment over the years?",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Home loans, big EMIs, and bills like electricity must be prioritized before discretionary spending.",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // THE INSTANT DROPDOWN MENU SELECTOR
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Priority Obligation Dropdown",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isDropdownExpanded) "Tap to close ✕" else "Tap to change ▾",
                                fontFamily = HelveticaFontFamily,
                                color = PhonePeLightPurple,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { isDropdownExpanded = !isDropdownExpanded }
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        // Clickable trigger box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF241540))
                                .border(
                                    1.5.dp,
                                    if (isDropdownExpanded) PhonePeLightPurple else Color(0x408B5CF6),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { isDropdownExpanded = !isDropdownExpanded }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                                .testTag("important_payment_dropdown_trigger")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = selectedOption.emoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = selectedOption.name,
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = selectedOption.subtitle,
                                            fontFamily = HelveticaFontFamily,
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    GlassBadge(
                                        text = selectedOption.priorityTag,
                                        textColor = selectedOption.tagColor,
                                        borderColor = selectedOption.tagColor.copy(alpha = 0.4f),
                                        backgroundColor = selectedOption.tagColor.copy(alpha = 0.15f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = if (isDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Toggle Dropdown",
                                        tint = SoftLavender,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Inline Animated Dropdown Menu for Guaranteed Smooth Visibility
                        this.AnimatedVisibility(
                            visible = isDropdownExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF1E1335))
                                    .border(1.dp, Color(0x608B5CF6), RoundedCornerShape(14.dp))
                                    .padding(vertical = 4.dp)
                                    .testTag("important_payment_inline_dropdown_menu")
                            ) {
                                IMPORTANT_PAYMENT_OPTIONS.forEach { opt ->
                                    val isCurrent = opt.id == selectedOption.id
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectPaymentOption(opt) }
                                            .background(if (isCurrent) Color(0x358B5CF6) else Color.Transparent)
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(text = opt.emoji, fontSize = 18.sp)
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = opt.name,
                                                            color = if (isCurrent) PhonePeLightPurple else Color.White,
                                                            fontSize = 13.sp,
                                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                                                        )
                                                        if (isCurrent) {
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Icon(
                                                                imageVector = Icons.Default.Check,
                                                                contentDescription = "Selected",
                                                                tint = EmeraldGreen,
                                                                modifier = Modifier.size(14.dp)
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        text = "${opt.subtitle} • ₹${opt.defaultTarget.toInt()}",
                                                        color = TextMuted,
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }
                                            GlassBadge(
                                                text = opt.priorityTag,
                                                textColor = opt.tagColor,
                                                borderColor = opt.tagColor.copy(alpha = 0.3f),
                                                backgroundColor = opt.tagColor.copy(alpha = 0.12f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Psychological & Priority Advice Box
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0x22FFFFFF),
                        borderBrush = Brush.linearGradient(
                            listOf(selectedOption.tagColor.copy(alpha = 0.3f), Color(0x15FFFFFF))
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "💡", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedOption.psychologicalNote,
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Editable Goal Name TextField
                    OutlinedTextField(
                        value = goalName,
                        onValueChange = { goalName = it },
                        label = { Text("Goal Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = PhonePeLightPurple,
                            unfocusedBorderColor = Color(0x408B5CF6)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("goal_name_input")
                    )

                    // Editable Target Amount TextField
                    Column {
                        OutlinedTextField(
                            value = targetAmtStr,
                            onValueChange = { targetAmtStr = it },
                            label = { Text("Target Amount (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = PhonePeLightPurple,
                                unfocusedBorderColor = Color(0x408B5CF6)
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("target_amount_input")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Quick Amount Adjuster Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val chips = listOf(
                                "₹3.5k" to 3500.0,
                                "₹15k" to 15000.0,
                                "₹25k" to 25000.0,
                                "₹35k" to 35000.0,
                                "₹50k" to 50000.0,
                                "₹1 Lakh" to 100000.0,
                                "₹25 Lakhs" to 2500000.0
                            )
                            items(chips) { (label, amount) ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (targetAmtStr == amount.toInt().toString()) PhonePePurple else Color(0x18FFFFFF))
                                        .clickable { targetAmtStr = amount.toInt().toString() }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = label,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Category Toggle: Asset vs Liability
                    Column {
                        Text(
                            text = "Categorization",
                            fontFamily = HelveticaFontFamily,
                            color = SoftLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (category == "Asset") EmeraldGreen.copy(alpha = 0.25f) else Color(0x18FFFFFF))
                                    .border(1.dp, if (category == "Asset") EmeraldGreen else Color(0x30FFFFFF), RoundedCornerShape(10.dp))
                                    .clickable { category = "Asset" }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Asset (Wealth/Protection)",
                                    color = if (category == "Asset") EmeraldGreen else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (category == "Liability") CoralRed.copy(alpha = 0.25f) else Color(0x18FFFFFF))
                                    .border(1.dp, if (category == "Liability") CoralRed else Color(0x30FFFFFF), RoundedCornerShape(10.dp))
                                    .clickable { category = "Liability" }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Liability (Mandatory/EMI)",
                                    color = if (category == "Liability") CoralRed else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Dedicated Wallet Selector
                    Column {
                        Text(
                            text = "Dedicated Wallet Destination",
                            fontFamily = HelveticaFontFamily,
                            color = SoftLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        val wallets = listOf(
                            "Habit Lock Vault",
                            "UPI Lite Vault",
                            "Amazon Pay Wallet",
                            "Digital Gold"
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            wallets.forEach { w ->
                                val isSelected = walletType == w
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) PhonePePurple else Color(0x18FFFFFF))
                                        .clickable { walletType = w }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = w.replace(" Vault", "").replace(" Wallet", ""),
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = targetAmtStr.toDoubleOrNull() ?: 10000.0
                        val saved = savedAmtStr.toDoubleOrNull() ?: 0.0
                        val external = if (walletType.contains("Amazon", true)) "https://www.amazon.in/gp/payment/wallet/" else null
                        onAddGoal(
                            SavingGoal(
                                id = "goal_${System.currentTimeMillis()}",
                                name = goalName.ifBlank { "Custom Goal" },
                                targetAmount = target,
                                savedAmount = saved,
                                category = category,
                                walletType = walletType,
                                externalUrl = external
                            )
                        )
                        showCreateGoalDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    modifier = Modifier.testTag("confirm_create_goal_button")
                ) {
                    Text("Set Goal & Link Wallet", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                IconButton(onClick = { showCreateGoalDialog = false }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel", tint = TextMuted)
                }
            }
        )
    }
}
