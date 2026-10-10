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
