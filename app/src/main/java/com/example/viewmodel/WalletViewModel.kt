package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiAiService
import com.example.data.ai.ScannedTransaction
import com.example.data.models.HealthTip
import com.example.data.models.InvestTip
import com.example.data.models.PatternAnalysisData
import com.example.data.models.RecentInsight
import com.example.data.models.SwapTip
import com.example.data.models.TransactionItem
import com.example.data.models.UserData
import com.example.data.supabase.SupabaseService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

data class WalletUiState(
    val hasCompletedSurvey: Boolean = false,
    val userData: UserData = UserData(),
    val txns: List<TransactionItem> = emptyList(),
    val tab: String = "home", // "home", "budget", "log", "health", "friends"
    val xp: Int = 250,
    val notifMessage: String = "",
    val notifVisible: Boolean = false,
    val filterCat: String = "All",
    val recentInsight: RecentInsight? = null,

    // Supabase State
    val isSupabaseConfigured: Boolean = false,
    val supabaseSyncState: String = "idle", // "idle", "syncing", "synced", "offline"

    // Log state
    val logState: String = "idle", // "idle", "analyzing", "reason"
    val tempData: List<ScannedTransaction> = emptyList(),
    val currentTxnIndex: Int = 0,
    val manualPayee: String = "",
    val manualAmount: String = "",
    val logCategory: String = "",
    val logNecessity: String = "",
    val logFoodType: String = "",
    val logContext: String = "",

    // Insights overlay
    val showInsights: Boolean = false,
    val isAnalyzingInsights: Boolean = false,
    val insightsData: PatternAnalysisData? = null,
    val handledHabits: Set<String> = emptySet(),

    // Health state
    val proteinScore: Int = 4,
    val veggieLevel: Int = 0,
    val junkSaved: Int = 0,
    val swapIndex: Int = 0,
    val isSwapping: Boolean = false,

    // KYC Modal state
    val showKycModal: Boolean = false
) {
    val totalSpent: Double
        get() = abs(txns.filter { it.amount < 0 }.sumOf { it.amount })

    val balance: Double
        get() = txns.sumOf { it.amount }

    val currentOutflowDay: Int
        get() = (totalSpent / 5.0).roundToInt()

    val safeLimitDay: Int
        get() = ((userData.allowance * 0.8) / 30.0).roundToInt()

    val isCriticalBurn: Boolean
        get() = currentOutflowDay > safeLimitDay

    val silverGrowth: Long
        get() = if (balance > 0) (balance * 1.09.pow(5)).roundToInt().toLong() else 0L

    val goldGrowth: Long
        get() = if (balance > 0) (balance * 1.11.pow(5)).roundToInt().toLong() else 0L

    val indexGrowth: Long
        get() = if (balance > 0) (balance * 1.15.pow(5)).roundToInt().toLong() else 0L

    val foodTxns: List<TransactionItem>
        get() = txns.filter { it.cat == "food" }

    val totalFoodSpend: Double
        get() = abs(foodTxns.sumOf { it.amount })

    val rawJunkSpend: Double
        get() = abs(foodTxns.filter { it.healthImpact == "NEGATIVE" }.sumOf { it.amount })

    val junkFoodSpend: Double
        get() = (rawJunkSpend - junkSaved).coerceAtLeast(0.0)

    val junkPercentage: Int
        get() = if (totalFoodSpend > 0) ((junkFoodSpend / totalFoodSpend) * 100).roundToInt() else 0

    val friendTxns: List<TransactionItem>
        get() = txns.filter { it.cat == "friends" }

    val totalOwed: Double
        get() = abs(friendTxns.sumOf { it.amount })
}

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("habit_wallet_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    val healthTips = listOf(
        HealthTip("Iron Deficiency?", "🩸", listOf("Spinach", "Rajma", "Jaggery", "Eggs"), "₹80/day", "1800 kcal"),
        HealthTip("Protein for Gym", "💪", listOf("Eggs", "Paneer", "Dal", "Peanut butter"), "₹100/day", "2200 kcal"),
        HealthTip("Cut Fat (Deficit)", "🔥", listOf("Oats", "Fruits", "Chicken", "Curd"), "₹90/day", "1500 kcal")
    )

    val investTips = listOf(
        InvestTip("Digital Gold / Metals", "🥇", "+11.2% YTD", "Low Volatility", "Start ₹100"),
        InvestTip("Nifty 50 Index", "🇮🇳", "+14.8% YTD", "Market Risk", "SIP ₹500/mo"),
        InvestTip("US S&P 500", "🌍", "+22.1% YTD", "Currency + Market", "Via INDmoney")
    )

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val completed = prefs.getBoolean("completed_survey", false)
        val allowance = prefs.getInt("allowance", 5000)
        val weakness = prefs.getString("weakness", "Dining Out / Delivery") ?: "Dining Out / Delivery"
        val goal = prefs.getString("goal", "Tech / Gadget Upgrade") ?: "Tech / Gadget Upgrade"
        val healthGoal = prefs.getString("health_goal", "Fat Loss") ?: "Fat Loss"
        val mode = prefs.getString("mode", "strict") ?: "strict"

        // Deep Behavioral KYC
        val isBehavioralKycCompleted = prefs.getBoolean("is_behavioral_kyc_completed", false)
        val vulnerabilityTrigger = prefs.getString("vulnerability_trigger", "Late Night Cravings (11 PM - 2 AM)") ?: "Late Night Cravings (11 PM - 2 AM)"
        val riskArchetype = prefs.getString("risk_archetype", "Balanced Builder") ?: "Balanced Builder"
        val deliveryFrequency = prefs.getString("delivery_frequency", "3–4 Times / Week") ?: "3–4 Times / Week"
        val interventionStyle = prefs.getString("intervention_style", "5-Second Cooling Timer") ?: "5-Second Cooling Timer"
        val behavioralArchetype = prefs.getString("behavioral_archetype", "The Strategic Optimizer") ?: "The Strategic Optimizer"
        val resilienceScore = prefs.getInt("resilience_score", 78)

        val loadedUserData = UserData(
            allowance = allowance,
            weakness = weakness,
            goal = goal,
            healthGoal = healthGoal,
            mode = mode,
            isBehavioralKycCompleted = isBehavioralKycCompleted,
            vulnerabilityTrigger = vulnerabilityTrigger,
            riskArchetype = riskArchetype,
            deliveryFrequency = deliveryFrequency,
            interventionStyle = interventionStyle,
            behavioralArchetype = behavioralArchetype,
            resilienceScore = resilienceScore
        )

        val initialTxns = listOf(
            TransactionItem(1, "Pizza Hut Delivery", -800.0, "food", "04 May, 08:30 PM", 0xFFFF9800, "NEGATIVE", "Late night craving", "Impulse Buy", "junk"),
            TransactionItem(2, "Fresh Fruits Market", -150.0, "food", "05 May, 09:00 AM", 0xFF10B981, "POSITIVE", "Weekly groceries", "Absolute Need", "healthy"),
            TransactionItem(3, "Rahul", -340.0, "friends", "2 days ago", 0xFF2563EB, "NEUTRAL", "Chai & Maggi", "Planned Want"),
            TransactionItem(5, "Sneha", -500.0, "friends", "Yesterday", 0xFF2563EB, "NEUTRAL", "Movie Ticket", "Planned Want"),
            TransactionItem(6, "Amit", -400.0, "friends", "Today", 0xFF2563EB, "NEUTRAL", "Auto Fare", "Absolute Need"),
            TransactionItem(4, "Incoming Allowance", allowance.toDouble(), "income", "01 May, 10:00 AM", 0xFF10B981, "NEUTRAL", "Monthly capital")
        )

        val isSupabaseReady = SupabaseService.isConfigured()

        _uiState.update {
            it.copy(
                hasCompletedSurvey = completed,
                userData = loadedUserData,
                txns = initialTxns,
                xp = prefs.getInt("xp", 250),
                proteinScore = prefs.getInt("protein_score", 4),
                veggieLevel = prefs.getInt("veggie_level", 0),
                junkSaved = prefs.getInt("junk_saved", 0),
                isSupabaseConfigured = isSupabaseReady,
                supabaseSyncState = if (isSupabaseReady) "idle" else "offline"
            )
        }

        if (isSupabaseReady) {
            syncWithSupabase()
        }
    }

    fun syncWithSupabase() {
        if (!SupabaseService.isConfigured()) return
        viewModelScope.launch {
            _uiState.update { it.copy(supabaseSyncState = "syncing") }
            try {
                // Fetch transactions
                val remoteTxns = SupabaseService.fetchTransactions()
                var syncSucceeded = false

                if (remoteTxns != null) {
                    syncSucceeded = true
                    if (remoteTxns.isNotEmpty()) {
                        _uiState.update { it.copy(txns = remoteTxns) }
                    } else {
                        // Empty remote table - populate with initial transactions
                        _uiState.value.txns.forEach { txn ->
                            SupabaseService.insertTransaction(txn)
                        }
                    }
                }

                // Fetch user profile
                val profileObj = SupabaseService.fetchProfile()
                if (profileObj != null) {
                    syncSucceeded = true
                    val currentData = _uiState.value.userData
                    val allowance = profileObj.optInt("allowance", currentData.allowance)
                    val weakness = profileObj.optString("weakness", currentData.weakness)
                    val goal = profileObj.optString("goal", currentData.goal)
                    val healthGoal = profileObj.optString("health_goal", currentData.healthGoal)
                    val mode = profileObj.optString("mode", currentData.mode)
                    val xp = profileObj.optInt("xp", _uiState.value.xp)
                    val protein = profileObj.optInt("protein_score", _uiState.value.proteinScore)
                    val veggie = profileObj.optInt("veggie_level", _uiState.value.veggieLevel)
                    val junk = profileObj.optInt("junk_saved", _uiState.value.junkSaved)

                    val updatedUserData = currentData.copy(
                        allowance = allowance,
                        weakness = weakness,
                        goal = goal,
                        healthGoal = healthGoal,
                        mode = mode,
                        isBehavioralKycCompleted = profileObj.optBoolean("is_behavioral_kyc_completed", currentData.isBehavioralKycCompleted),
                        vulnerabilityTrigger = profileObj.optString("vulnerability_trigger", currentData.vulnerabilityTrigger),
                        riskArchetype = profileObj.optString("risk_archetype", currentData.riskArchetype),
                        deliveryFrequency = profileObj.optString("delivery_frequency", currentData.deliveryFrequency),
                        interventionStyle = profileObj.optString("intervention_style", currentData.interventionStyle),
                        behavioralArchetype = profileObj.optString("behavioral_archetype", currentData.behavioralArchetype),
                        resilienceScore = profileObj.optInt("resilience_score", currentData.resilienceScore)
                    )

                    _uiState.update {
                        it.copy(
                            userData = updatedUserData,
                            xp = xp,
                            proteinScore = protein,
                            veggieLevel = veggie,
                            junkSaved = junk,
                            hasCompletedSurvey = true
                        )
                    }
                } else if (remoteTxns != null) {
                    // Push local profile to Supabase if connection worked but profile table was empty
                    SupabaseService.upsertProfile(
                        _uiState.value.userData,
                        _uiState.value.xp,
                        _uiState.value.proteinScore,
                        _uiState.value.veggieLevel,
                        _uiState.value.junkSaved
                    )
                }

                if (syncSucceeded) {
                    _uiState.update { it.copy(supabaseSyncState = "synced") }
                    showNotif("⚡ Supabase database synced successfully!")
                } else {
                    _uiState.update { it.copy(supabaseSyncState = "idle") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(supabaseSyncState = "error") }
            }
        }
    }

    fun toggleKycModal(show: Boolean) {
        _uiState.update { it.copy(showKycModal = show) }
    }

    fun updateBehavioralKyc(data: UserData) {
        val wasAlreadyDone = _uiState.value.userData.isBehavioralKycCompleted
        val xpGain = if (!wasAlreadyDone) 250 else 50
        val newXp = _uiState.value.xp + xpGain

        val calibratedData = data.copy(isBehavioralKycCompleted = true)

        prefs.edit()
            .putBoolean("is_behavioral_kyc_completed", true)
            .putString("vulnerability_trigger", calibratedData.vulnerabilityTrigger)
            .putString("risk_archetype", calibratedData.riskArchetype)
            .putString("delivery_frequency", calibratedData.deliveryFrequency)
            .putString("intervention_style", calibratedData.interventionStyle)
            .putString("behavioral_archetype", calibratedData.behavioralArchetype)
            .putInt("resilience_score", calibratedData.resilienceScore)
            .putInt("xp", newXp)
            .apply()

        _uiState.update {
            it.copy(
                userData = calibratedData,
                xp = newXp,
                showKycModal = false
            )
        }

        showNotif("🧬 KYC 100% Calibrated: ${calibratedData.behavioralArchetype} (+$xpGain XP)")

        if (SupabaseService.isConfigured()) {
            viewModelScope.launch {
                SupabaseService.upsertProfile(
                    calibratedData,
                    newXp,
                    _uiState.value.proteinScore,
                    _uiState.value.veggieLevel,
                    _uiState.value.junkSaved
                )
            }
        }
    }

    fun completeSurvey(data: UserData) {
        val xpBonus = if (data.isBehavioralKycCompleted) 500 else 250
        val newXp = _uiState.value.xp + xpBonus

        prefs.edit()
            .putBoolean("completed_survey", true)
            .putInt("allowance", data.allowance)
            .putString("weakness", data.weakness)
            .putString("goal", data.goal)
            .putString("health_goal", data.healthGoal)
            .putString("mode", data.mode)
            .putBoolean("is_behavioral_kyc_completed", data.isBehavioralKycCompleted)
            .putString("vulnerability_trigger", data.vulnerabilityTrigger)
            .putString("risk_archetype", data.riskArchetype)
            .putString("delivery_frequency", data.deliveryFrequency)
            .putString("intervention_style", data.interventionStyle)
            .putString("behavioral_archetype", data.behavioralArchetype)
            .putInt("resilience_score", data.resilienceScore)
            .putInt("xp", newXp)
            .apply()

        _uiState.update { state ->
            val updatedTxns = state.txns.map {
                if (it.id == 4L) it.copy(amount = data.allowance.toDouble()) else it
            }
            state.copy(
                hasCompletedSurvey = true,
                userData = data,
                xp = newXp,
                txns = updatedTxns
            )
        }

        val kycMsg = if (data.isBehavioralKycCompleted) {
            "🎉 KYC & Behavioral Profiling 100% Complete! (+500 XP)"
        } else {
            "✅ Basic KYC Complete! (+250 XP). Complete Behavioral Survey anytime."
        }
        showNotif(kycMsg)

        // Asynchronously push to Supabase
        if (SupabaseService.isConfigured()) {
            viewModelScope.launch {
                SupabaseService.upsertProfile(
                    data,
                    _uiState.value.xp,
                    _uiState.value.proteinScore,
                    _uiState.value.veggieLevel,
                    _uiState.value.junkSaved
                )
            }
        }

        val msg = if (data.mode == "strict") {
            "System active. Let's limit expenses on ${data.weakness} to reach your ${data.goal}."
        } else {
            "System active. Let's work towards your ${data.goal} together."
        }
        showNotif(msg)
    }

    fun showNotif(msg: String) {
        _uiState.update { it.copy(notifMessage = msg, notifVisible = true) }
        viewModelScope.launch {
            delay(5000)
            _uiState.update { it.copy(notifVisible = false) }
        }
    }

    fun dismissNotif() {
        _uiState.update { it.copy(notifVisible = false) }
    }

    fun applyRunwayRescuePlan() {
        _uiState.update { state ->
            state.copy(
                xp = state.xp + 50,
                junkSaved = state.junkSaved + 450
            )
        }
        showNotif("🛡️ Runway Rescue Applied: ₹450 diverted to Liquid Buffer! (+50 XP)")
    }

    fun setTab(newTab: String) {
        _uiState.update {
            it.copy(
                tab = newTab,
                logState = "idle",
                manualPayee = "",
                manualAmount = "",
                logCategory = "",
                logNecessity = "",
                logFoodType = "",
                logContext = ""
            )
        }
    }

    fun setFilterCat(cat: String) {
        _uiState.update { it.copy(filterCat = cat) }
    }

    fun resistTemptation() {
        val newXp = (_uiState.value.xp + 20).coerceAtMost(500)
        _uiState.update { it.copy(xp = newXp) }
        prefs.edit().putInt("xp", newXp).apply()

        if (SupabaseService.isConfigured()) {
            viewModelScope.launch {
                SupabaseService.upsertProfile(
                    _uiState.value.userData,
                    newXp,
                    _uiState.value.proteinScore,
                    _uiState.value.veggieLevel,
                    _uiState.value.junkSaved
                )
            }
        }

        showNotif("🛡️ +20 XP! You stood your ground. Keep saving for that ${_uiState.value.userData.goal}!")
    }

    fun dismissRecentInsight() {
        _uiState.update { it.copy(recentInsight = null) }
    }

    fun toggleInsights(show: Boolean) {
        _uiState.update { it.copy(showInsights = show) }
        if (!show) {
            _uiState.update { it.copy(insightsData = null, handledHabits = emptySet()) }
        }
    }

    fun runInsightsAnalysis() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzingInsights = true) }
            val result = GeminiAiService.analyzeHabitPatterns(
                _uiState.value.txns,
                _uiState.value.userData
            )
            _uiState.update {
                it.copy(
                    isAnalyzingInsights = false,
                    insightsData = result
                )
            }
        }
    }

    fun handleHabitAction(habit: String, isDump: Boolean) {
        val xpGain = if (isDump) 15 else 25
        val newXp = (_uiState.value.xp + xpGain).coerceAtMost(500)
        _uiState.update {
            it.copy(
                handledHabits = it.handledHabits + habit,
                xp = newXp
            )
        }
        prefs.edit().putInt("xp", newXp).apply()

        if (SupabaseService.isConfigured()) {
            viewModelScope.launch {
                SupabaseService.upsertProfile(
                    _uiState.value.userData,
                    newXp,
                    _uiState.value.proteinScore,
                    _uiState.value.veggieLevel,
                    _uiState.value.junkSaved
                )
            }
        }

        showNotif(if (isDump) "🗑️ Dumped bad habit! +$xpGain XP" else "⚡ Adopted good habit! +$xpGain XP")
    }

    fun onScanImage(bytes: ByteArray?) {
        viewModelScope.launch {
            _uiState.update { it.copy(logState = "analyzing") }
            val extracted = GeminiAiService.parseScannedReceipt(bytes)
            if (extracted.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        tempData = extracted,
                        currentTxnIndex = 0,
                        logState = "reason",
                        logCategory = "",
                        logNecessity = "",
                        logFoodType = "",
                        logContext = ""
                    )
                }
            } else {
                _uiState.update { it.copy(logState = "idle") }
                showNotif("Could not read receipt clearly. Use manual entry.")
            }
        }
    }

    fun onManualNext(payee: String, amountStr: String) {
        val amount = amountStr.toDoubleOrNull() ?: return
        if (payee.isBlank()) return

        _uiState.update {
            it.copy(
                tempData = listOf(ScannedTransaction(amount, payee)),
                currentTxnIndex = 0,
                logState = "reason",
                logCategory = "",
                logNecessity = "",
                logFoodType = "",
                logContext = ""
            )
        }
    }

    fun updateLogDetails(
        category: String? = null,
        necessity: String? = null,
        foodType: String? = null,
        context: String? = null
    ) {
        _uiState.update {
            it.copy(
                logCategory = category ?: it.logCategory,
                logNecessity = necessity ?: it.logNecessity,
                logFoodType = foodType ?: it.logFoodType,
                logContext = context ?: it.logContext
            )
        }
    }

    fun submitLog() {
        val state = _uiState.value
        val isIncome = state.logCategory == "income"
        val isFood = state.logCategory == "food"

        if (state.logCategory.isBlank()) return
        if (!isIncome && state.logNecessity.isBlank()) return
        if (isFood && state.logFoodType.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(logState = "analyzing") }
            val currentTxn = state.tempData.getOrNull(state.currentTxnIndex) ?: return@launch
            val amount = currentTxn.amount
            val payee = currentTxn.payee

            val result = GeminiAiService.analyzeSpend(
                amount = amount,
                payee = payee,
                category = state.logCategory,
                necessity = state.logNecessity,
                foodType = state.logFoodType,
                context = state.logContext,
                userData = state.userData,
                remainingBalance = state.balance - amount
            )

            var newXp = state.xp
            if (!isIncome) {
                if (result.habitQuality == "GOOD") newXp = (newXp + 15).coerceAtMost(500)
                if (result.habitQuality == "BAD") newXp = (newXp - 15).coerceAtLeast(0)
                prefs.edit().putInt("xp", newXp).apply()
            }

            val newTxn = TransactionItem(
                id = System.currentTimeMillis(),
                name = payee,
                amount = if (isIncome) abs(amount) else -abs(amount),
                cat = if (isIncome) "income" else result.category,
                date = "Just now",
                colorHex = if (isIncome) 0xFF10B981 else if (result.category == "friends") 0xFF2563EB else 0xFF5E2BFF,
                healthImpact = result.healthImpact,
                context = state.logContext,
                necessity = state.logNecessity,
                foodType = state.logFoodType
            )

            // Asynchronously sync new transaction to Supabase
            if (SupabaseService.isConfigured()) {
                SupabaseService.insertTransaction(newTxn)
                SupabaseService.upsertProfile(
                    state.userData,
                    newXp,
                    state.proteinScore,
                    state.veggieLevel,
                    state.junkSaved
                )
            }

            val nextIndex = state.currentTxnIndex + 1
            if (nextIndex < state.tempData.size) {
                _uiState.update {
                    it.copy(
                        txns = listOf(newTxn) + it.txns,
                        xp = newXp,
                        currentTxnIndex = nextIndex,
                        logCategory = "",
                        logNecessity = "",
                        logFoodType = "",
                        logContext = "",
                        logState = "reason"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        txns = listOf(newTxn) + it.txns,
                        xp = newXp,
                        recentInsight = if (!isIncome) RecentInsight(result.message, result.habitQuality) else null,
                        logState = "idle",
                        tempData = emptyList(),
                        currentTxnIndex = 0,
                        manualPayee = "",
                        manualAmount = "",
                        tab = "home"
                    )
                }
                if (isIncome) {
                    showNotif(result.message)
                }
            }
        }
    }

    fun handleSettleFriend(txn: TransactionItem) {
        viewModelScope.launch {
            showNotif("AI processing settlement...")
            val msg = GeminiAiService.getFriendSettlementReaction(
                txn.name,
                abs(txn.amount),
                txn.context
            )
            val settledTxn = TransactionItem(
                id = System.currentTimeMillis(),
                name = "${txn.name} (Repayment)",
                amount = abs(txn.amount),
                cat = "income",
                date = "Just now",
                colorHex = 0xFF10B981,
                healthImpact = "NEUTRAL",
                context = "Settled via AI"
            )

            _uiState.update { state ->
                val remaining = state.txns.filter { it.id != txn.id }
                state.copy(txns = listOf(settledTxn) + remaining)
            }

            // Sync with Supabase
            if (SupabaseService.isConfigured()) {
                SupabaseService.deleteTransaction(txn.id)
                SupabaseService.insertTransaction(settledTxn)
            }

            showNotif(msg)
        }
    }

    fun getSwapList(goal: String): List<SwapTip> {
        return when (goal) {
            "Muscle Gain" -> listOf(
                SwapTip("₹50 Breadwada", "🥪", "₹40 Boiled Eggs (3)", "🥚", "₹10", 35, "+18g Pure Protein"),
                SwapTip("₹40 Vadapav", "🍔", "₹50 Chicken/Soya", "🍗", "-₹10", 40, "Muscle Recovery"),
                SwapTip("₹30 Pohe", "🍲", "₹40 Sprouts Salad", "🥗", "-₹10", 25, "+12g Protein"),
                SwapTip("₹20 Sweet Tea", "☕", "₹15 Black Coffee", "🍵", "₹5", 15, "Pre-workout Energy")
            )
            "Fat Loss" -> listOf(
                SwapTip("₹40 Vadapav", "🍔", "₹30 Apple & Oats", "🍎", "₹10", 30, "Cut 300 Empty Calories"),
                SwapTip("₹50 Breadwada", "🥪", "₹30 Mixed Veggies", "🥗", "₹20", 35, "High Fiber, Low Cal"),
                SwapTip("₹30 Pohe", "🍲", "₹20 Watermelon", "🍉", "₹10", 25, "Hydration & Satiety"),
                SwapTip("₹20 Sweet Tea", "☕", "₹15 Green Tea", "🍵", "₹5", 15, "Boosts Metabolism")
            )
            else -> listOf(
                SwapTip("₹40 Vadapav", "🍔", "₹30 Apple & Oats", "🍎", "₹10", 30, "Better Digestion"),
                SwapTip("₹50 Breadwada", "🥪", "₹30 Veg Salad", "🥗", "₹20", 35, "Vitamins & Minerals"),
                SwapTip("₹30 Pohe", "🍲", "₹20 Fruits", "🍉", "₹10", 25, "Natural Energy"),
                SwapTip("₹20 Sweet Tea", "☕", "₹15 Green Tea", "🍵", "₹5", 15, "Antioxidants")
            )
        }
    }

    fun handleHealthSwapReward(swap: SwapTip) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSwapping = true) }
            val newXp = (_uiState.value.xp + swap.xp).coerceAtMost(500)

            var newProtein = _uiState.value.proteinScore
            var newVeggie = _uiState.value.veggieLevel

            val text = (swap.good + " " + swap.benefit).lowercase()
            if (text.contains("protein") || text.contains("egg") || text.contains("chicken") || text.contains("soya")) {
                newProtein = (newProtein + 2).coerceAtMost(10)
            }
            if (text.contains("veg") || text.contains("salad") || text.contains("fruit") || text.contains("apple") || text.contains("watermelon") || text.contains("fiber")) {
                newVeggie = (newVeggie + 1).coerceAtMost(3)
            }

            val badCost = swap.bad.filter { it.isDigit() }.toIntOrNull() ?: 30
            val newJunkSaved = _uiState.value.junkSaved + badCost

            prefs.edit()
                .putInt("xp", newXp)
                .putInt("protein_score", newProtein)
                .putInt("veggie_level", newVeggie)
                .putInt("junk_saved", newJunkSaved)
                .apply()

            if (SupabaseService.isConfigured()) {
                SupabaseService.upsertProfile(
                    _uiState.value.userData,
                    newXp,
                    newProtein,
                    newVeggie,
                    newJunkSaved
                )
            }

            showNotif("🍎 +${swap.xp} XP! You chose the healthy route! Keep going!")

            delay(400)
            val currentList = getSwapList(_uiState.value.userData.healthGoal)
            val nextIndex = (_uiState.value.swapIndex + 1) % currentList.size

            _uiState.update {
                it.copy(
                    xp = newXp,
                    proteinScore = newProtein,
                    veggieLevel = newVeggie,
                    junkSaved = newJunkSaved,
                    swapIndex = nextIndex,
                    isSwapping = false
                )
            }
        }
    }

    fun updateAllowance(newAllowance: Int) {
        val clamped = newAllowance.coerceIn(1000, 50000)
        val updatedUser = _uiState.value.userData.copy(allowance = clamped)
        prefs.edit().putInt("allowance", clamped).apply()
        _uiState.update { state ->
            val updatedTxns = state.txns.map {
                if (it.id == 4L) it.copy(amount = clamped.toDouble()) else it
            }
            state.copy(userData = updatedUser, txns = updatedTxns)
        }
    }
}
