package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.SoftLavender

@Composable
fun GlassPillNav(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Vibrant Minimalist Glass Dock Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(16.dp, RoundedCornerShape(26.dp), spotColor = Color(0x557C3AED))
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xF5130D24))
                .border(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(Color(0x40A78BFA), Color(0x187C3AED), Color(0x12FFFFFF))
                    ),
                    RoundedCornerShape(26.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Home
                NavBarItem(
                    label = "Home",
                    icon = Icons.Default.Home,
                    isSelected = currentTab == "home",
                    onClick = { onTabSelected("home") },
                    testTag = "nav_home",
                    modifier = Modifier.weight(1f)
                )

                // 2. Budget
                NavBarItem(
                    label = "Budget",
                    icon = Icons.Default.AccountBalanceWallet,
                    isSelected = currentTab == "budget",
                    onClick = { onTabSelected("budget") },
                    testTag = "nav_budget",
                    modifier = Modifier.weight(1f)
                )

                // Center spacer for elevated QR Scanner button
                Spacer(modifier = Modifier.weight(1.05f))

                // 4. Health
                NavBarItem(
                    label = "Health",
                    icon = Icons.Default.Favorite,
                    isSelected = currentTab == "health",
                    onClick = { onTabSelected("health") },
                    testTag = "nav_health",
                    modifier = Modifier.weight(1f)
                )

                // 5. Splits & Friends
                NavBarItem(
                    label = "Splits",
                    icon = Icons.Default.People,
                    isSelected = currentTab == "friends",
                    onClick = { onTabSelected("friends") },
                    testTag = "nav_splits",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Elevated Center "Upload Docx & Payment Slips" Button with sleek floating styling
        val isUploadSelected = currentTab == "log"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-16).dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(16.dp, CircleShape, spotColor = if (isUploadSelected) EmeraldGreen else Color(0xAA7C3AED))
                    .clip(CircleShape)
                    .background(
                        if (isUploadSelected) Brush.verticalGradient(
                            listOf(Color(0xFF00D09C), Color(0xFF059669))
                        ) else Brush.verticalGradient(
                            listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9))
                        )
                    )
                    .border(
                        1.5.dp,
                        if (isUploadSelected) Color(0xFF6EE7B7) else Color(0x80DDD6FE),
                        CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onTabSelected("log")
                    }
                    .testTag("nav_upload_docx")
                    .testTag("nav_qr_scan"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = "Upload Docx and Payment Slips",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Tab label: "DOCX & SLIPS"
            Text(
                text = "DOCX & SLIPS",
                fontFamily = HelveticaFontFamily,
                color = if (isUploadSelected) EmeraldGreen else SoftLavender,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    testTag: String = ""
) {
    val tintColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF8E88A2),
        label = "nav_item_tint"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color(0x288B5CF6) else Color.Transparent,
        label = "nav_item_bg"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .background(bgColor)
            .padding(vertical = 6.dp, horizontal = 2.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(20.dp)
            )

            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 6.dp, y = (-4).dp)
                        .size(13.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$badgeCount",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontFamily = HelveticaFontFamily,
            color = tintColor,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 0.2.sp
        )

        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(width = 12.dp, height = 2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(PhonePeLightPurple)
            )
        }
    }
}
