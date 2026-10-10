package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Sophisticated business and fintech icon set inspired by Google Pay & PhonePe.
 * Provides clean vector icons instead of cartoonish emojis, with pleasant, muted
 * palette backgrounds that don't strain the eye.
 */
data class CategoryMeta(
    val icon: ImageVector,
    val iconTint: Color,
    val containerBg: Color,
    val borderColor: Color,
    val label: String
)

object BusinessIconRegistry {
    fun getCategoryMeta(type: String): CategoryMeta {
        return when (type.lowercase().trim()) {
            "food" -> CategoryMeta(
                icon = Icons.Default.Fastfood,
                iconTint = Color(0xFFFDBA74), // Muted peach/amber
                containerBg = Color(0xFF281C1B),
                borderColor = Color(0x40F97316),
                label = "Food & Dining"
            )
            "friends" -> CategoryMeta(
                icon = Icons.Default.ReceiptLong,
                iconTint = Color(0xFFC4B5FD), // Soft lavender
                containerBg = Color(0xFF201635),
                borderColor = Color(0x408B5CF6),
                label = "Friends & Splits"
            )
            "essential" -> CategoryMeta(
                icon = Icons.Default.LocalGroceryStore,
                iconTint = Color(0xFF6EE7B7), // Soft mint green
                containerBg = Color(0xFF132822),
                borderColor = Color(0x4010B981),
                label = "Groceries & Essentials"
            )
            "shopping" -> CategoryMeta(
                icon = Icons.Default.ShoppingBag,
                iconTint = Color(0xFF93C5FD), // Muted sky blue
                containerBg = Color(0xFF16233B),
                borderColor = Color(0x403B82F6),
                label = "Shopping"
            )
            "health" -> CategoryMeta(
                icon = Icons.Default.MedicalServices,
                iconTint = Color(0xFF5EEAD4), // Muted teal
                containerBg = Color(0xFF132828),
                borderColor = Color(0x4014B8A6),
                label = "Health & Wellness"
            )
            "travel" -> CategoryMeta(
                icon = Icons.Default.DirectionsCar,
                iconTint = Color(0xFFFED7AA), // Warm apricot
                containerBg = Color(0xFF271F18),
                borderColor = Color(0x40F59E0B),
                label = "Travel & Commute"
            )
            "income" -> CategoryMeta(
                icon = Icons.Default.Paid,
                iconTint = Color(0xFFA7F3D0), // Soft emerald
                containerBg = Color(0xFF132A20),
                borderColor = Color(0x4010B981),
                label = "Salary & Income"
            )
            "invest" -> CategoryMeta(
                icon = Icons.Default.TrendingUp,
                iconTint = Color(0xFFA5B4FC), // Muted indigo
                containerBg = Color(0xFF181C38),
                borderColor = Color(0x406366F1),
                label = "Investments"
            )
            "bills" -> CategoryMeta(
                icon = Icons.Default.Receipt,
                iconTint = Color(0xFFFDE68A), // Soft gold
                containerBg = Color(0xFF282417),
                borderColor = Color(0x40EAB308),
                label = "Bills & Utilities"
            )
            "entertainment" -> CategoryMeta(
                icon = Icons.Default.Movie,
                iconTint = Color(0xFFF472B6), // Muted rose
                containerBg = Color(0xFF2D1525),
                borderColor = Color(0x40EC4899),
                label = "Entertainment"
            )
            "bs" -> CategoryMeta(
                icon = Icons.Default.AttachMoney,
                iconTint = Color(0xFFFDA4AF), // Soft coral pink
                containerBg = Color(0xFF2C151B),
                borderColor = Color(0x40F43F5E),
                label = "Impulse / Waste"
            )
            else -> CategoryMeta(
                icon = Icons.Default.LocalAtm,
                iconTint = Color(0xFFDDD6FE), // Lavender
                containerBg = Color(0xFF1D1730),
                borderColor = Color(0x408B5CF6),
                label = "General Transaction"
            )
        }
    }
}

/**
 * Category Icon rendering professional vector icons with subtle, non-punchy
 * tinted containers matching Google Pay / PhonePe transaction list designs.
 */
@Composable
fun CategoryIcon(
    type: String,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    size: Dp = 20.dp
) {
    val meta = BusinessIconRegistry.getCategoryMeta(type)
    val resolvedTint = tint ?: meta.iconTint

    Icon(
        imageVector = meta.icon,
        contentDescription = meta.label,
        tint = resolvedTint,
        modifier = modifier.size(size)
    )
}

/**
 * Full Squircle Tile for transaction items and lists, replacing bright or saturated boxes
 * with calm, elegant Google Pay / PhonePe surface treatments.
 */
@Composable
fun BusinessCategoryTile(
    type: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp
) {
    val meta = BusinessIconRegistry.getCategoryMeta(type)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(meta.containerBg)
            .border(1.dp, meta.borderColor, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = meta.icon,
            contentDescription = meta.label,
            tint = meta.iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}
