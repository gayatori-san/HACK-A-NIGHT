package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SwapHoriz
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Data representation for Google Pay / PhonePe style business actions.
 */
data class BusinessActionItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val badge: String? = null,
    val iconTint: Color = SoftLavender,
    val containerBg: Color = Color(0xFF1E1730),
    val containerBorder: Color = Color(0x338B5CF6)
)

/**
 * Sophisticated Google Pay / PhonePe style 4-column Transfer & Business Actions Grid.
 * Muted, elegant surfaces that are easy on the eyes without flashiness.
 */
@Composable
fun BusinessQuickActionsGrid(
    onScanQr: () -> Unit,
    onPayContact: () -> Unit,
    onSplitExpenses: () -> Unit,
    onBankTransfer: () -> Unit,
    onCheckBalance: () -> Unit,
    onSelfTransfer: () -> Unit,
    onPayBills: () -> Unit,
    onLogExpense: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transferActions = listOf(
        BusinessActionItem(
            id = "scan_qr",
            title = "Scan QR",
            icon = Icons.Default.QrCodeScanner,
            iconTint = Color(0xFFE2D9F8),
            containerBg = Color(0xFF241A3D),
            containerBorder = Color(0x4D8B5CF6)
        ),
        BusinessActionItem(
            id = "pay_contacts",
            title = "To Contact",
            icon = Icons.Default.ContactPhone,
            iconTint = Color(0xFFC7D2FE),
            containerBg = Color(0xFF1C1E3A),
            containerBorder = Color(0x336366F1)
        ),
        BusinessActionItem(
            id = "split_friends",
            title = "Split Bill",
            icon = Icons.Default.Group,
            badge = "UPI",
            iconTint = Color(0xFFA7F3D0),
            containerBg = Color(0xFF142926),
            containerBorder = Color(0x3310B981)
        ),
        BusinessActionItem(
            id = "to_bank",
            title = "To Bank / UPI",
            icon = Icons.Default.AccountBalance,
            iconTint = Color(0xFFBAE6FD),
            containerBg = Color(0xFF132538),
            containerBorder = Color(0x330284C7)
        )
    )

    val utilityActions = listOf(
        BusinessActionItem(
            id = "self_transfer",
            title = "Self Transfer",
            icon = Icons.Default.SwapHoriz,
            iconTint = Color(0xFFDDD6FE),
            containerBg = Color(0xFF22173B),
            containerBorder = Color(0x338B5CF6)
        ),
        BusinessActionItem(
            id = "check_balance",
            title = "Check Balance",
            icon = Icons.Default.CreditCard,
            iconTint = Color(0xFFFED7AA),
            containerBg = Color(0xFF2C1D18),
            containerBorder = Color(0x33F97316)
        ),
        BusinessActionItem(
            id = "pay_bills",
            title = "Pay Bills",
            icon = Icons.Default.Receipt,
            iconTint = Color(0xFFFDE68A),
            containerBg = Color(0xFF2B2516),
            containerBorder = Color(0x33EAB308)
        ),
        BusinessActionItem(
            id = "log_expense",
            title = "Log Spend",
            icon = Icons.Default.Add,
            badge = "+XP",
            iconTint = Color(0xFFFBCFE8),
            containerBg = Color(0xFF2F182A),
            containerBorder = Color(0x33EC4899)
        )
    )

    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF140F22))
            .border(1.dp, Color(0x288B5CF6), RoundedCornerShape(20.dp))
            .padding(vertical = 14.dp, horizontal = 10.dp)
            .testTag("business_quick_actions_card")
    ) {
        // Section Header: Money Transfers + Submenu toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "MONEY TRANSFERS",
                    fontFamily = HelveticaFontFamily,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x208B5CF6))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "UPI LITE",
                        fontFamily = HelveticaFontFamily,
                        color = SoftLavender,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Minimal Submenu Expander Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x1F8B5CF6))
                    .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(12.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Less Services" else "More Services",
                    fontFamily = HelveticaFontFamily,
                    color = SoftLavender,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Show Less" else "Show More",
                    tint = SoftLavender,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Row 1: Primary Transfers (Minimalist 4 Actions)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            transferActions.forEach { action ->
                BusinessActionTile(
                    item = action,
                    onClick = {
                        when (action.id) {
                            "scan_qr" -> onScanQr()
                            "pay_contacts" -> onPayContact()
                            "split_friends" -> onSplitExpenses()
                            "to_bank" -> onBankTransfer()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 2: Secondary Utilities Submenu (Collapsible to keep home screen uncluttered)
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    utilityActions.forEach { action ->
                        BusinessActionTile(
                            item = action,
                            onClick = {
                                when (action.id) {
                                    "self_transfer" -> onSelfTransfer()
                                    "check_balance" -> onCheckBalance()
                                    "pay_bills" -> onPayBills()
                                    "log_expense" -> onLogExpense()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual subtle, non-flashy action button matching Google Pay / PhonePe.
 */
@Composable
private fun BusinessActionTile(
    item: BusinessActionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp)
            .testTag("action_tile_${item.id}"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(item.containerBg)
                .border(1.dp, item.containerBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.iconTint,
                modifier = Modifier.size(22.dp)
            )

            // Optional tiny badge (like UPI or +XP)
            if (item.badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 1.dp, end = 1.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PhonePePurple)
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = item.badge,
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = item.title,
            fontFamily = HelveticaFontFamily,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            lineHeight = 13.sp
        )
    }
}
