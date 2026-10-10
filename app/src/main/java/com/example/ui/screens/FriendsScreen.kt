package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState
import kotlin.math.abs

@Composable
fun FriendsScreen(
    uiState: WalletUiState,
    onSendBulkReminders: () -> Unit,
    onRemindFriend: (String) -> Unit,
    onSettleFriend: (TransactionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val friendTxns = uiState.friendTxns
    val totalOwed = uiState.totalOwed.toInt()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("friends_screen_lazy_column")
    ) {
        // 1. Header with PhonePe branding
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Friend Payments",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.6).sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "🫂", fontSize = 22.sp)
                    }

                    GlassBadge(
                        text = "UPI SPLIT",
                        textColor = SoftLavender,
                        borderColor = Color(0x338B5CF6),
                        backgroundColor = Color(0x1A8B5CF6)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Track shared expenses and manage UPI settlements seamlessly.",
                    fontFamily = HelveticaFontFamily,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // 1b. Quick Send Horizontal Avatar Carousel
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Send & Split Contacts",
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "+ Add New",
                        fontFamily = HelveticaFontFamily,
                        color = PhonePeLightPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Add Friend Tile
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2E1B5B))
                                .border(1.2.dp, PhonePeLightPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Contact",
                                tint = PhonePeLightPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Add",
                            fontFamily = HelveticaFontFamily,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Friends Avatar List
                    val friendsList = listOf(
                        Triple("Rahul", "₹1,200", Color(0xFF3B82F6)),
                        Triple("Sneha", "₹800", Color(0xFFEC4899)),
                        Triple("Amit", "₹450", Color(0xFF10B981)),
                        Triple("Priya", "₹300", Color(0xFFF59E0B)),
                        Triple("Vikram", "₹620", Color(0xFF8B5CF6))
                    )

                    friendsList.forEach { (name, owed, avatarColor) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onRemindFriend(name) }
                                .padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(avatarColor.copy(alpha = 0.35f))
                                    .border(
                                        1.5.dp,
                                        avatarColor,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = name.take(2).uppercase(),
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = name,
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = owed,
                                fontFamily = HelveticaFontFamily,
                                color = NeonLime,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. Hero Total Owed Vibrant Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = Color(0xFF1B1433),
                    gradientBrush = Brush.verticalGradient(
                        listOf(Color(0xFF2E1B5B), Color(0xFF16112C))
                    ),
                    borderBrush = Brush.linearGradient(
                        listOf(Color(0x668B5CF6), Color(0x33A855F7), Color(0x22FFFFFF))
                    ),
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL OWED TO YOU",
                                fontFamily = HelveticaFontFamily,
                                color = Color(0xCCFFFFFF),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp
                            )

                            GlassBadge(
                                text = "${friendTxns.size} Active",
                                textColor = Color(0xFF93C5FD),
                                borderColor = Color(0x3393C5FD),
                                backgroundColor = Color(0x1493C5FD)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "₹$totalOwed",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-1.2).sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Glassmorphic Send Reminders Action Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color.White, Color(0xFFF1F5F9))
                                    )
                                )
                                .clickable { onSendBulkReminders() }
                                .padding(vertical = 13.dp)
                                .testTag("send_bulk_reminders_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = Color(0xFF1E3A8A),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Send Reminders to All",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color(0xFF1E3A8A),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.1).sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Friend Split List Header
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PENDING SPLITS (${friendTxns.size})",
                    fontFamily = HelveticaFontFamily,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp
                )

                Text(
                    text = "Instant Settle",
                    fontFamily = HelveticaFontFamily,
                    color = PhonePeLightPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (friendTxns.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = "No friend splits pending! Everything is settled.",
                            fontFamily = HelveticaFontFamily,
                            color = TextMuted,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
            }
        } else {
            items(friendTxns, key = { it.id }) { txn ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                ) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = Color(0x1F1A172B),
                        gradientBrush = Brush.verticalGradient(
                            listOf(Color(0x22FFFFFF), Color(0x0AFFFFFF))
                        ),
                        borderBrush = Brush.linearGradient(
                            listOf(Color(0x50FFFFFF), Color(0x203B82F6), Color(0x30FFFFFF))
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Friend Avatar Initial with PhonePe Ring
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x263B82F6))
                                    .border(1.2.dp, Color(0x663B82F6), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = txn.name.take(1).uppercase(),
                                    fontFamily = HelveticaFontFamily,
                                    color = Color(0xFF93C5FD),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = txn.name,
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = (-0.2).sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(1.dp))
                                Text(
                                    text = if (txn.context.isNotBlank()) "${txn.context} • ${txn.date}" else txn.date,
                                    fontFamily = HelveticaFontFamily,
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "UPI: ${txn.name.lowercase().replace(" ", "")}@upi",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 9.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${abs(txn.amount).toInt()}.00",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.3).sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Remind Glass Pill
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0x20FFFFFF))
                                            .border(0.8.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                                            .clickable { onRemindFriend(txn.name) }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .testTag("remind_friend_${txn.name}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "REMIND",
                                            fontFamily = HelveticaFontFamily,
                                            color = TextSecondary,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }

                                    // Settle PhonePe Purple Pill
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(PhonePePurple)
                                            .clickable { onSettleFriend(txn) }
                                            .padding(horizontal = 9.dp, vertical = 4.dp)
                                            .testTag("settle_friend_${txn.name}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "SETTLE ⚡",
                                            fontFamily = HelveticaFontFamily,
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
