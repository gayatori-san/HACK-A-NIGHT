package com.example.ui.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberWarning
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.GlassSurfaceHigh
import com.example.ui.theme.HelveticaFontFamily
import com.example.ui.theme.NeonLime
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PhonePeAccentPurple
import com.example.ui.theme.PhonePeLightPurple
import com.example.ui.theme.PhonePePurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState
import java.io.ByteArrayOutputStream

@Composable
fun LogScreen(
    uiState: WalletUiState,
    onBack: () -> Unit,
    onScanImageBytes: (ByteArray?) -> Unit,
    onManualNext: (String, String) -> Unit,
    onUpdateDetails: (String?, String?, String?, String?) -> Unit,
    onSubmitLog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isTorchOn by remember { mutableStateOf(false) }
    var showManualDialog by remember { mutableStateOf(false) }
    var manualPayee by remember { mutableStateOf("") }
    var manualAmount by remember { mutableStateOf("") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                bitmap?.compress(android.graphics.Bitmap.CompressFormat.JPEG, 40, stream)
                onScanImageBytes(stream.toByteArray())
            } catch (e: Exception) {
                onScanImageBytes(null)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        AnimatedContent(
            targetState = uiState.logState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "log_state_anim"
        ) { state ->
            when (state) {
                "idle" -> {
                    // Full Screen PhonePe QR Scanner (Screenshot 9)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Header (Screenshot 9)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { onBack() }
                                        .testTag("scan_back_button")
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Scan any QR",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = (-0.3).sp
                                    )
                                    Text(
                                        text = "PhonePe • Google Pay • BHIM • Paytm",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "Help",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Centered Frosted Glass Viewfinder with Purple Corner Brackets
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .shadow(32.dp, RoundedCornerShape(28.dp), spotColor = Color(0xFFA855F7))
                                .clip(RoundedCornerShape(28.dp))
                                .background(Color(0x331E1B2E))
                                .border(1.dp, GlassBorderGradient, RoundedCornerShape(28.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Custom Viewfinder Canvas with Neon Purple Corner Brackets
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val strokeWidth = 5.dp.toPx()
                                val cornerLength = 36.dp.toPx()
                                val cornerColor = Color(0xFFA855F7)

                                // Top-Left
                                drawLine(cornerColor, Offset(0f, 0f), Offset(cornerLength, 0f), strokeWidth)
                                drawLine(cornerColor, Offset(0f, 0f), Offset(0f, cornerLength), strokeWidth)

                                // Top-Right
                                drawLine(cornerColor, Offset(size.width, 0f), Offset(size.width - cornerLength, 0f), strokeWidth)
                                drawLine(cornerColor, Offset(size.width, 0f), Offset(size.width, cornerLength), strokeWidth)

                                // Bottom-Left
                                drawLine(cornerColor, Offset(0f, size.height), Offset(cornerLength, size.height), strokeWidth)
                                drawLine(cornerColor, Offset(0f, size.height), Offset(0f, size.height - cornerLength), strokeWidth)

                                // Bottom-Right
                                drawLine(cornerColor, Offset(size.width, size.height), Offset(size.width - cornerLength, size.height), strokeWidth)
                                drawLine(cornerColor, Offset(size.width, size.height), Offset(size.width, size.height - cornerLength), strokeWidth)
                            }

                            // Center Subtle Scanning Pulse
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(Color(0x33A855F7), Color.Transparent)
                                        )
                                    )
                            )
                        }

                        // Actions beneath viewfinder: Upload QR, Torch, Manual Entry (Screenshot 9)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(36.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Upload QR Button
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x26FFFFFF))
                                            .border(1.dp, Color(0x33FFFFFF), CircleShape)
                                            .clickable {
                                                photoPickerLauncher.launch(
                                                    PickVisualMediaRequest(
                                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                                    )
                                                )
                                            }
                                            .testTag("upload_qr_button"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Image,
                                            contentDescription = "Upload QR",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Upload QR",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Torch Button
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(if (isTorchOn) PhonePeLightPurple else Color(0x26FFFFFF))
                                            .border(1.dp, Color(0x33FFFFFF), CircleShape)
                                            .clickable { isTorchOn = !isTorchOn }
                                            .testTag("torch_button"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FlashlightOn,
                                            contentDescription = "Torch",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Torch",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Manual Input Button
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x26FFFFFF))
                                            .border(1.dp, Color(0x33FFFFFF), CircleShape)
                                            .clickable { showManualDialog = true }
                                            .testTag("manual_open_button"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Keyboard,
                                            contentDescription = "Manual Entry",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Manual",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(28.dp))

                            // Bottom BHIM | UPI Watermark (Screenshot 9)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "BHIM",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = " | ",
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "UPI",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }

                "reason" -> {
                    val currentTxn = uiState.tempData.getOrNull(uiState.currentTxnIndex)
                    if (currentTxn != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = "Categorize Spend",
                                fontFamily = HelveticaFontFamily,
                                color = NeonLime,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.6).sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Paid Summary Glassmorphic Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(20.dp, RoundedCornerShape(22.dp))
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(GlassSurfaceHigh)
                                    .border(1.dp, GlassBorderGradient, RoundedCornerShape(22.dp))
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "YOU PAID",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.8.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "₹${currentTxn.amount.toInt()}.00",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextPrimary,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = (-1).sp
                                    )
                                    Text(
                                        text = "to ${currentTxn.payee}",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "1. What was this for?",
                                fontFamily = HelveticaFontFamily,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            val categories = listOf(
                                "food" to "🍔 food",
                                "essential" to "🛒 essential",
                                "shopping" to "🛍️ shopping",
                                "travel" to "🚕 travel",
                                "friends" to "🫂 friends",
                                "health" to "🏥 health",
                                "bs" to "🗑️ bs",
                                "income" to "💰 income"
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                val rows = categories.chunked(4)
                                rows.forEach { rowItems ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        rowItems.forEach { (catKey, label) ->
                                            val isSelected = uiState.logCategory == catKey
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(if (isSelected) PhonePeLightPurple else Color(0x1AFFFFFF))
                                                    .border(
                                                        1.dp,
                                                        if (isSelected) PhonePeLightPurple else BorderSubtle,
                                                        RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable {
                                                        onUpdateDetails(catKey, null, null, null)
                                                    }
                                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                                    .testTag("log_category_$catKey"),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = label,
                                                    fontFamily = HelveticaFontFamily,
                                                    color = if (isSelected) Color.White else TextSecondary,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Food Junk vs Healthy check
                            if (uiState.logCategory == "food") {
                                Spacer(modifier = Modifier.height(18.dp))
                                Text(
                                    text = "Health Check: Junk or Healthy?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    val isHealthy = uiState.logFoodType == "healthy"
                                    val isJunk = uiState.logFoodType == "junk"

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isHealthy) Color(0x2610B981) else Color(0x14FFFFFF))
                                            .border(
                                                1.dp,
                                                if (isHealthy) EmeraldGreen else BorderSubtle,
                                                RoundedCornerShape(14.dp)
                                            )
                                            .clickable { onUpdateDetails(null, null, "healthy", null) }
                                            .padding(vertical = 12.dp)
                                            .testTag("food_healthy_toggle"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "🥗 Healthy",
                                            fontFamily = HelveticaFontFamily,
                                            color = if (isHealthy) EmeraldGreen else TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isJunk) Color(0x26F43F5E) else Color(0x14FFFFFF))
                                            .border(
                                                1.dp,
                                                if (isJunk) CoralRed else BorderSubtle,
                                                RoundedCornerShape(14.dp)
                                            )
                                            .clickable { onUpdateDetails(null, null, "junk", null) }
                                            .padding(vertical = 12.dp)
                                            .testTag("food_junk_toggle"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "🍟 Junk Food",
                                            fontFamily = HelveticaFontFamily,
                                            color = if (isJunk) CoralRed else TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            // Necessity check
                            if (uiState.logCategory != "income") {
                                Spacer(modifier = Modifier.height(18.dp))
                                Text(
                                    text = "2. Was it necessary?",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val necessities = listOf(
                                        "Absolute Need" to ("🟢 Need" to EmeraldGreen),
                                        "Planned Want" to ("🟡 Want" to AmberWarning),
                                        "Impulse Buy" to ("🔴 Impulse" to CoralRed)
                                    )

                                    necessities.forEach { (key, pair) ->
                                        val (label, accentColor) = pair
                                        val isSelected = uiState.logNecessity == key
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) accentColor.copy(alpha = 0.2f) else Color(0x14FFFFFF))
                                                .border(
                                                    1.dp,
                                                    if (isSelected) accentColor else BorderSubtle,
                                                    RoundedCornerShape(14.dp)
                                                )
                                                .clickable { onUpdateDetails(null, key, null, null) }
                                                .padding(vertical = 12.dp)
                                                .testTag("necessity_$key"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontFamily = HelveticaFontFamily,
                                                color = if (isSelected) accentColor else TextSecondary,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "3. Context note (Optional)",
                                fontFamily = HelveticaFontFamily,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = uiState.logContext,
                                onValueChange = { onUpdateDetails(null, null, null, it) },
                                placeholder = {
                                    Text(
                                        text = "e.g., Late night snack, Chai split with Rahul...",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextMuted,
                                        fontSize = 13.sp
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                textStyle = TextStyle(fontFamily = HelveticaFontFamily, color = TextPrimary, fontSize = 13.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonLime,
                                    unfocusedBorderColor = BorderMedium,
                                    focusedContainerColor = Color(0x14FFFFFF),
                                    unfocusedContainerColor = Color(0x0AFFFFFF)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(84.dp)
                                    .testTag("log_context_input")
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            val isIncome = uiState.logCategory == "income"
                            val isFood = uiState.logCategory == "food"
                            val isComplete = uiState.logCategory.isNotBlank() &&
                                    (isIncome || uiState.logNecessity.isNotBlank()) &&
                                    (!isFood || uiState.logFoodType.isNotBlank())

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(if (isComplete) PhonePePurple else Color(0x288B5CF6))
                                    .clickable(enabled = isComplete) { onSubmitLog() }
                                    .padding(vertical = 16.dp)
                                    .testTag("submit_log_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (uiState.currentTxnIndex < uiState.tempData.size - 1) "Analyze & Next Payment ➡️" else "Analyze & Save",
                                    fontFamily = HelveticaFontFamily,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.2).sp
                                )
                            }

                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }

        // Manual Input Dialog Sheet
        if (showManualDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xD9000000))
                    .clickable { showManualDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(GlassSurfaceHigh)
                        .border(1.2.dp, GlassBorderGradient, RoundedCornerShape(24.dp))
                        .clickable(enabled = false) {}
                        .padding(22.dp)
                ) {
                    Column {
                        Text(
                            text = "Manual Payment Entry",
                            fontFamily = HelveticaFontFamily,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = manualPayee,
                            onValueChange = { manualPayee = it },
                            placeholder = { Text(text = "Payee name (e.g. Swiggy, Rahul)", fontFamily = HelveticaFontFamily, color = TextMuted) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            textStyle = TextStyle(fontFamily = HelveticaFontFamily, color = TextPrimary, fontSize = 14.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PhonePeLightPurple,
                                unfocusedBorderColor = BorderMedium,
                                focusedContainerColor = Color(0x1FFFFFFF),
                                unfocusedContainerColor = Color(0x0FFFFFFF)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = manualAmount,
                            onValueChange = { manualAmount = it.filter { c -> c.isDigit() || c == '.' } },
                            placeholder = { Text(text = "Amount (₹)", fontFamily = HelveticaFontFamily, color = TextMuted) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(14.dp),
                            textStyle = TextStyle(fontFamily = HelveticaFontFamily, color = TextPrimary, fontSize = 14.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PhonePeLightPurple,
                                unfocusedBorderColor = BorderMedium,
                                focusedContainerColor = Color(0x1FFFFFFF),
                                unfocusedContainerColor = Color(0x0FFFFFFF)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val canSubmit = manualPayee.isNotBlank() && (manualAmount.toDoubleOrNull() ?: 0.0) > 0.0

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (canSubmit) PhonePePurple else Color(0x288B5CF6))
                                .clickable(enabled = canSubmit) {
                                    showManualDialog = false
                                    onManualNext(manualPayee, manualAmount)
                                }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Continue to Categorize",
                                fontFamily = HelveticaFontFamily,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Overlay AI Thinking State with Deep Glassmorphism
        if (uiState.logState == "analyzing") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xE608080E)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = PhonePeLightPurple,
                            modifier = Modifier.size(64.dp),
                            strokeWidth = 3.dp
                        )
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(PhonePeAccentPurple)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "AI Thinking...",
                        fontFamily = HelveticaFontFamily,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Analyzing patterns and context.",
                        fontFamily = HelveticaFontFamily,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
