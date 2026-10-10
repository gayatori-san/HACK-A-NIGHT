package com.example.data.models

data class UserData(
    val allowance: Int = 5000,
    val weakness: String = "Dining Out / Delivery",
    val goal: String = "Tech / Gadget Upgrade",
    val healthGoal: String = "Fat Loss",
    val mode: String = "strict", // "strict" or "gentle"
    // Deep Behavioral KYC Profile
    val isBehavioralKycCompleted: Boolean = false,
    val vulnerabilityTrigger: String = "Late Night Cravings (11 PM - 2 AM)",
    val riskArchetype: String = "Balanced Builder",
    val deliveryFrequency: String = "3–4 Times / Week",
    val interventionStyle: String = "5-Second Cooling Timer",
    val behavioralArchetype: String = "The Strategic Optimizer",
    val resilienceScore: Int = 78
)

data class TransactionItem(
    val id: Long,
    val name: String,
    val amount: Double, // negative for spend, positive for income
    val cat: String, // "food", "friends", "essential", "shopping", "health", "bs", "travel", "income"
    val date: String,
    val colorHex: Long = 0xFF5E2BFF,
    val healthImpact: String = "NEUTRAL", // "POSITIVE", "NEGATIVE", "NEUTRAL"
    val context: String = "",
    val necessity: String = "", // "Absolute Need", "Planned Want", "Impulse Buy"
    val foodType: String = "" // "healthy", "junk"
) {
    val isJunk: Boolean
        get() = foodType.equals("junk", ignoreCase = true) || healthImpact.equals("NEGATIVE", ignoreCase = true)
}

data class SwapTip(
    val bad: String,
    val badIcon: String,
    val good: String,
    val goodIcon: String,
    val save: String,
    val xp: Int,
    val benefit: String
)

data class HealthTip(
    val title: String,
    val icon: String,
    val foods: List<String>,
    val budget: String,
    val cal: String
)

data class InvestTip(
    val asset: String,
    val icon: String,
    val returnRate: String,
    val risk: String,
    val tag: String
)

data class RecentInsight(
    val msg: String,
    val quality: String // "GOOD", "BAD", "NEUTRAL"
)

data class PatternAnalysisData(
    val goodHabits: List<String>,
    val badHabits: List<String>
)

data class SavingGoal(
    val id: String,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0,
    val category: String, // "Asset" or "Liability"
    val walletType: String, // "Habit Lock Vault", "Amazon Pay Wallet", "Paytm / UPI Lite", "Digital Gold"
    val externalUrl: String? = null
) {
    val progress: Float
        get() = if (targetAmount > 0) (savedAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f

    val remainingAmount: Double
        get() = (targetAmount - savedAmount).coerceAtLeast(0.0)

    val isCompleted: Boolean
        get() = savedAmount >= targetAmount
}

data class StatementAnalysisReport(
    val title: String,
    val bankruptcyRiskPercent: Int, // 0 - 100
    val daysRemaining: Double,
    val funnyRoast: String,
    val psychologicalNudge: String,
    val totalWastedOnImpulse: Double,
    val projectedDividendsYearly: Double,
    val projectedWealth5Yrs: Double,
    val topLeakageCategory: String,
    val monthlyBurn: Double
)

