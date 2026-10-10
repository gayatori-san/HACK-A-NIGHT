package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.InvestTip
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralRed
import com.example.ui.theme.DeepPurple1
import com.example.ui.theme.DeepPurple2
import com.example.ui.theme.DeepPurple3
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState
import kotlin.math.max

@Composable
fun BudgetScreen(
    uiState: WalletUiState,
    investTips: List<InvestTip>,
    onSyncSupabase: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allowance = uiState.userData.allowance
    val essentials = (allowance * 0.5).toInt()
    val discretionary = (allowance * 0.3).toInt()
    val savings = (allowance * 0.2).toInt()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("budget_screen_lazy_column")
    ) {
        // 1. Header Title
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "CFO Mode.",
                    fontFamily = HelveticaFontFamily,
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Target: ${uiState.userData.goal}. Restriction: ${uiState.userData.weakness}.",
                    fontFamily = HelveticaFontFamily,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp
                )
            }
        }

        // 2. The 50/30/20 Capital Allocation Card
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
                                text = "HEALTHY",
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

                        Spacer(modifier = Modifier.height(18.dp))

                        // Interactive Savings Target Slider Option
                        var savingsRateSlider by remember { mutableFloatStateOf(20f) }
                        val simulatedSavings = (allowance * (savingsRateSlider / 100f)).toInt()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF181028))
                                .border(1.dp, Color(0x408B5CF6), RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🎛️", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Simulate Custom Savings Rate",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                GlassBadge(
                                    text = "${savingsRateSlider.toInt()}% • ₹$simulatedSavings/mo",
                                    textColor = PhonePeLightPurple,
                                    borderColor = Color(0x338B5CF6),
                                    backgroundColor = Color(0x1A8B5CF6)
                                )
                            }

                            Slider(
                                value = savingsRateSlider,
                                onValueChange = { savingsRateSlider = it },
                                valueRange = 10f..50f,
                                steps = 7,
                                colors = SliderDefaults.colors(
                                    thumbColor = PhonePeLightPurple,
                                    activeTrackColor = PhonePeLightPurple,
                                    inactiveTrackColor = Color(0x408B5CF6)
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("budget_savings_slider")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "10% (Relaxed)",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = "50% (Hardcore FIRE)",
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

        // 3. Supabase Cloud Database Status Card
        item {
            var showSqlSchema by remember { mutableStateOf(false) }
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
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isConfigured) {
                            "Real-time PostgREST sync active with your Supabase backend."
                        } else {
                            "Set SUPABASE_URL & SUPABASE_KEY in AI Studio Secrets to sync your financial records to PostgreSQL."
                        },
                        fontFamily = HelveticaFontFamily,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (isConfigured) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PhonePePurple)
                                    .clickable { onSyncSupabase() }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .testTag("supabase_sync_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Sync Now 🔄",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x1AFFFFFF))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { showSqlSchema = !showSqlSchema }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("supabase_schema_toggle"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (showSqlSchema) "Hide SQL DDL" else "View SQL Schema",
                                fontFamily = HelveticaFontFamily,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (showSqlSchema) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xE608080A))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "-- Run in Supabase SQL Editor:\ncreate table if not exists transactions (\n  id bigint primary key,\n  name text,\n  amount numeric,\n  cat text,\n  date text,\n  color_hex bigint,\n  health_impact text,\n  context text,\n  necessity text,\n  food_type text\n);\ncreate table if not exists user_profile (\n  id int primary key default 1,\n  allowance int,\n  weakness text,\n  goal text,\n  health_goal text,\n  mode text,\n  xp int,\n  protein_score int,\n  veggie_level int,\n  junk_saved int\n);",
                                fontFamily = HelveticaFontFamily,
                                color = TextSecondary,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // 4. Burn Rate Analysis Card
        item {
            val isCritical = uiState.isCriticalBurn
            val accentColor = if (isCritical) CoralRed else EmeraldGreen
            val statusLabel = if (isCritical) "CRITICAL" else "HEALTHY"

            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = Color(0x22141419),
                    gradientBrush = Brush.verticalGradient(
                        listOf(Color(0x24FFFFFF), Color(0x0AFFFFFF))
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(Color(0x66FFFFFF), accentColor.copy(alpha = 0.5f), Color(0x33FFFFFF))
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BURN RATE ANALYSIS",
                                fontFamily = HelveticaFontFamily,
                                color = accentColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp
                            )
                            GlassBadge(
                                text = statusLabel,
                                textColor = accentColor,
                                borderColor = accentColor.copy(alpha = 0.4f),
                                backgroundColor = accentColor.copy(alpha = 0.15f),
                                dotColor = accentColor
                            )
                        }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CURRENT VELOCITY",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "₹${uiState.currentOutflowDay}/day",
                                fontFamily = HelveticaFontFamily,
                                color = accentColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "SAFE LIMIT",
                                fontFamily = HelveticaFontFamily,
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "₹${uiState.safeLimitDay}/day",
                                fontFamily = HelveticaFontFamily,
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val explanation = if (isCritical) {
                        "You are exceeding your safe daily limit by ₹${uiState.currentOutflowDay - uiState.safeLimitDay}. Discretionary budget will deplete early at this rate."
                    } else {
                        "Your daily burn rate is sustainable. Your cashflow is currently optimal."
                    }

                    Text(
                        text = explanation,
                        fontFamily = HelveticaFontFamily,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

        // 5. Financial Directives
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Financial Directives",
                    fontFamily = HelveticaFontFamily,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.3).sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                val directives = listOf(
                    "Transfer 20% to savings immediately upon receiving income. Treat it as a non-negotiable expense.",
                    "Severely restrict exposure to ${uiState.userData.weakness}. Implement friction (e.g., delete saved cards).",
                    "Enforce bill splitting. Personal relationships should not compromise financial targets.",
                    "Conduct a weekly 10-minute audit of all outflows. Awareness dictates behavior."
                )

                directives.forEachIndexed { i, rule ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x0FFFFFFF))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(PhonePePurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${i + 1}",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = rule,
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 6. Capital Growth Projection Models
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Capital Growth",
                    fontFamily = HelveticaFontFamily,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.3).sp
                )

                Text(
                    text = "5-Year Growth Projection on current balance: ₹${uiState.balance.toInt()}",
                    fontFamily = HelveticaFontFamily,
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                if (uiState.balance <= 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x0AFFFFFF))
                            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Capital required to initiate growth models.",
                            fontFamily = HelveticaFontFamily,
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    val maxGrowth = max(uiState.indexGrowth, 1L).toFloat()
                    val silverRatio = (uiState.silverGrowth / maxGrowth).coerceIn(0.1f, 1f)
                    val goldRatio = (uiState.goldGrowth / maxGrowth).coerceIn(0.1f, 1f)
                    val indexRatio = 1.0f

                    val silverWidth by animateFloatAsState(targetValue = silverRatio, animationSpec = tween(800), label = "silver_anim")
                    val goldWidth by animateFloatAsState(targetValue = goldRatio, animationSpec = tween(800), label = "gold_anim")
                    val indexWidth by animateFloatAsState(targetValue = indexRatio, animationSpec = tween(800), label = "index_anim")

                    val models = listOf(
                        Triple("Silver (9% p.a)", "₹${uiState.silverGrowth}", silverWidth),
                        Triple("Gold (11% p.a)", "₹${uiState.goldGrowth}", goldWidth),
                        Triple("Index Funds (15% p.a)", "₹${uiState.indexGrowth}", indexWidth)
                    )

                    models.forEach { (label, value, width) ->
                        Column(modifier = Modifier.padding(vertical = 5.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = label, fontFamily = HelveticaFontFamily, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Normal)
                                Text(text = value, fontFamily = HelveticaFontFamily, color = SoftLavender, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x1AFFFFFF))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(width)
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

        // 7. Asset Allocation
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Asset Allocation",
                    fontFamily = HelveticaFontFamily,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.3).sp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        itemsIndexed(investTips) { _, tip ->
            val assetVector = when (tip.asset.lowercase()) {
                "nifty 50 index" -> Icons.Default.TrendingUp
                "gold mutual funds" -> Icons.Default.Savings
                "sovereign gold bonds" -> Icons.Default.Savings
                "liquid funds" -> Icons.Default.AccountBalance
                "us tech etf" -> Icons.Default.Language
                "reits" -> Icons.Default.AccountBalance
                else -> Icons.Default.Assessment
            }
            val assetBg = when (tip.risk.lowercase()) {
                "low" -> Color(0xFF142822)
                "moderate" -> Color(0xFF1E1E38)
                else -> Color(0xFF281C1B)
            }
            val assetBorder = when (tip.risk.lowercase()) {
                "low" -> Color(0x3310B981)
                "moderate" -> Color(0x336366F1)
                else -> Color(0x33F97316)
            }
            val assetTint = when (tip.risk.lowercase()) {
                "low" -> Color(0xFFA7F3D0)
                "moderate" -> Color(0xFFC7D2FE)
                else -> Color(0xFFFED7AA)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF151022))
                    .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(assetBg)
                            .border(1.dp, assetBorder, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = assetVector,
                            contentDescription = tip.asset,
                            tint = assetTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = tip.asset, fontFamily = HelveticaFontFamily, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Risk: ${tip.risk}", fontFamily = HelveticaFontFamily, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Normal)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = tip.returnRate, fontFamily = HelveticaFontFamily, color = EmeraldGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = tip.tag, fontFamily = HelveticaFontFamily, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Normal)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
