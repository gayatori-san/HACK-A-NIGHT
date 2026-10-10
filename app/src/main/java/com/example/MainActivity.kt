package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CategoryIcon
import com.example.ui.components.GlassPillNav
import com.example.ui.components.KycBehavioralModal
import com.example.ui.components.NotifToast
import com.example.ui.components.PatternAnalysisModal
import com.example.ui.components.StatementAnalysisReportPopup
import com.example.ui.screens.BudgetScreen
import com.example.ui.screens.FriendsScreen
import com.example.ui.screens.HealthScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LogScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.RunwayScreen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonLime
import com.example.ui.theme.ObsidianBlack
import com.example.viewmodel.WalletViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: WalletViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle system back button gracefully
    BackHandler(enabled = uiState.showInsights || uiState.showKycModal || uiState.tab != "home") {
        if (uiState.showInsights) {
            viewModel.toggleInsights(false)
        } else if (uiState.showKycModal) {
            viewModel.toggleKycModal(false)
        } else if (uiState.tab != "home") {
            viewModel.setTab("home")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        if (!uiState.hasCompletedSurvey) {
            OnboardingScreen(
                onComplete = { data ->
                    viewModel.completeSurvey(data)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            )
        } else {
            Scaffold(
                containerColor = ObsidianBlack,
                topBar = {
                    if (uiState.tab != "home") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(com.example.ui.theme.PhonePePurple),
                                contentAlignment = Alignment.Center
                            ) {
                                CategoryIcon(type = "income", tint = Color.White, size = 18.dp)
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0x14FFFFFF))
                                    .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                                    .clickable { viewModel.setTab("runway") }
                                    .padding(horizontal = 12.dp, vertical = 5.dp)
                                    .testTag("app_bar_runway_pill")
                            ) {
                                Text(
                                    text = "⚡ RUNWAY XAI",
                                    fontFamily = HelveticaFontFamily,
                                    color = com.example.ui.theme.SoftLavender,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x228B5CF6))
                                    .border(1.dp, Color(0x60A78BFA), RoundedCornerShape(12.dp))
                                    .clickable { viewModel.toggleKycModal(true) }
                                    .testTag("app_bar_kyc_icon"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Behavioral KYC",
                                    tint = if (uiState.userData.isBehavioralKycCompleted) com.example.ui.theme.EmeraldGreen else com.example.ui.theme.SoftLavender,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x14FFFFFF))
                                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                    .clickable { viewModel.toggleInsights(true) }
                                    .testTag("app_bar_insights_bell"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(com.example.ui.theme.PhonePeAccentPurple)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                },
                bottomBar = {
                    GlassPillNav(
                        currentTab = uiState.tab,
                        onTabSelected = { tab ->
                            viewModel.setTab(tab)
                        },
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (uiState.tab) {
                        "home" -> HomeScreen(
                            uiState = uiState,
                            onOpenInsights = { viewModel.toggleInsights(true) },
                            onResist = { viewModel.resistTemptation() },
                            onFilterSelect = { cat -> viewModel.setFilterCat(cat) },
                            onDismissInsight = { viewModel.dismissRecentInsight() },
                            onNavigateToTab = { tab -> viewModel.setTab(tab) },
                            onUpdateAllowance = { newAllowance -> viewModel.updateAllowance(newAllowance) },
                            onOpenKyc = { viewModel.toggleKycModal(true) },
                            onImportDemoStatement = { bank -> viewModel.importDemoStatement(bank) },
                            onImportCsv = { csv -> viewModel.importCsvContent(csv) },
                            modifier = Modifier.fillMaxSize()
                        )

                        "budget" -> BudgetScreen(
                            uiState = uiState,
                            investTips = viewModel.investTips,
                            onSyncSupabase = { viewModel.syncWithSupabase() },
                            onUpdateAllowance = { newAllowance -> viewModel.updateAllowance(newAllowance) },
                            onAddGoal = { goal -> viewModel.addGoal(goal) },
                            onDepositGoal = { id, amt -> viewModel.depositToGoal(id, amt) },
                            onDeleteGoal = { id -> viewModel.deleteGoal(id) },
                            modifier = Modifier.fillMaxSize()
                        )

                        "log" -> LogScreen(
                            uiState = uiState,
                            onBack = { viewModel.setTab("home") },
                            onScanImageBytes = { bytes -> viewModel.onScanImage(bytes) },
                            onManualNext = { payee, amt -> viewModel.onManualNext(payee, amt) },
                            onUpdateDetails = { cat, nec, food, ctx ->
                                viewModel.updateLogDetails(cat, nec, food, ctx)
                            },
                            onSubmitLog = { viewModel.submitLog() },
                            modifier = Modifier.fillMaxSize()
                        )

                        "health" -> HealthScreen(
                            uiState = uiState,
                            swapList = viewModel.getSwapList(uiState.userData.healthGoal),
                            healthTips = viewModel.healthTips,
                            onClaimSwap = { swap -> viewModel.handleHealthSwapReward(swap) },
                            modifier = Modifier.fillMaxSize()
                        )

                        "friends" -> FriendsScreen(
                            uiState = uiState,
                            onSendBulkReminders = { viewModel.showNotif("Reminders generated via AI.") },
                            onRemindFriend = { friend -> viewModel.showNotif("Reminder sent to $friend!") },
                            onSettleFriend = { txn -> viewModel.handleSettleFriend(txn) },
                            modifier = Modifier.fillMaxSize()
                        )

                        "history" -> HistoryScreen(
                            uiState = uiState,
                            modifier = Modifier.fillMaxSize()
                        )

                        "runway" -> RunwayScreen(
                            uiState = uiState,
                            onBack = { viewModel.setTab("home") },
                            onApplyRescuePlan = { viewModel.applyRunwayRescuePlan() },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Global Notification Toast
            NotifToast(
                message = uiState.notifMessage,
                visible = uiState.notifVisible,
                onDismiss = { viewModel.dismissNotif() },
                modifier = Modifier
                    .statusBarsPadding()
                    .align(Alignment.TopCenter)
            )

            // Pattern Analysis Overlay Dialog
            PatternAnalysisModal(
                visible = uiState.showInsights,
                isAnalyzing = uiState.isAnalyzingInsights,
                insightsData = uiState.insightsData,
                handledHabits = uiState.handledHabits,
                onClose = { viewModel.toggleInsights(false) },
                onAnalyze = { viewModel.runInsightsAnalysis() },
                onHabitAction = { habit, isDump ->
                    viewModel.handleHabitAction(habit, isDump)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            )

            // KYC Behavioral Identity & Survey Modal
            KycBehavioralModal(
                visible = uiState.showKycModal,
                uiState = uiState,
                onClose = { viewModel.toggleKycModal(false) },
                onSaveKyc = { updatedData ->
                    viewModel.updateBehavioralKyc(updatedData)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            )

            // Statement Upload AI Analysis Pop-up (Bankruptcy meter, roast, dividend graph)
            val activeReport = uiState.statementReport
            if (uiState.showStatementReportPopup && activeReport != null) {
                StatementAnalysisReportPopup(
                    report = activeReport,
                    onDismiss = { viewModel.dismissStatementReport() }
                )
            }
        }
    }
}
