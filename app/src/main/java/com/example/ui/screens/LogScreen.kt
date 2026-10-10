package com.example.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.WalletUiState
import java.io.ByteArrayOutputStream

import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard

@Composable
fun LogScreen(
    uiState: WalletUiState,
    onBack: () -> Unit,
    onScanImageBytes: (ByteArray?) -> Unit,
    onManualNext: (String, String) -> Unit,
    onUpdateDetails: (String?, String?, String?, String?) -> Unit,
    onSubmitLog: () -> Unit,
    onImportCsv: (String) -> Unit = {},
    onImportDemoStatement: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showManualDialog by remember { mutableStateOf(false) }
    var manualPayee by remember { mutableStateOf("") }
    var manualAmount by remember { mutableStateOf("") }
    var customSlipText by remember { mutableStateOf("") }
    var showPasteSection by remember { mutableStateOf(false) }

    // Versatile Document, PDF, DOCX, CSV & Scanned Slip Picker
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val mimeType = context.contentResolver.getType(it) ?: ""
                val inputStream = context.contentResolver.openInputStream(it)
                if (mimeType.startsWith("image/") || mimeType.contains("jpeg") || mimeType.contains("png")) {
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val stream = ByteArrayOutputStream()
                    bitmap?.compress(android.graphics.Bitmap.CompressFormat.JPEG, 45, stream)
                    onScanImageBytes(stream.toByteArray())
                } else {
                    val textContent = inputStream?.bufferedReader()?.use { reader -> reader.readText() }
                    if (!textContent.isNullOrBlank()) {
                        onImportCsv(textContent)
                    }
                }
            } catch (e: Exception) {
                // Graceful fallback
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .testTag("upload_docx_screen")
    ) {
        AnimatedContent(
            targetState = uiState.logState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "log_state_anim"
        ) { state ->
            when (state) {
                "idle" -> {
                    // Dedicated "UPLOAD DOCX AND PAYMENT SLIPS" Screen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Top Header (User Request: "name the tab uplaod docx AND PAYMENT SLIPS")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { onBack() }
                                        .testTag("scan_back_button")
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "UPLOAD DOCX AND PAYMENT SLIPS",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.3.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Statements • Salary Slips • Electricity Bills • EMI Slips",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            GlassBadge(
                                text = "DOCX • SLIPS",
                                textColor = EmeraldGreen,
                                borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                backgroundColor = EmeraldGreen.copy(alpha = 0.15f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Scrollable Document Upload Options & Ledger
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // 1. Primary Document & Payment Slip Dropzone Card
                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = Color(0xFF161026),
                                gradientBrush = Brush.verticalGradient(
                                    listOf(Color(0xFF261440), Color(0xFF140D24))
                                ),
                                borderBrush = Brush.linearGradient(
                                    listOf(Color(0x6000D09C), Color(0x388B5CF6))
                                )
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color(0x2600D09C))
                                                .border(1.dp, Color(0x6000D09C), RoundedCornerShape(14.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CloudUpload,
                                                contentDescription = "Upload Document",
                                                tint = EmeraldGreen,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Upload Document or Payment Slip",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Select any .docx, .pdf, .csv, .xlsx or receipt slip to audit transactions",
                                                fontFamily = HelveticaFontFamily,
                                                color = TextSecondary,
                                                fontSize = 11.sp,
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Main Horizontal Browse Button (User Request: "uplaod option document option only not the scan qr option")
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(PhonePeLightPurple, PhonePePurple)
                                                )
                                            )
                                            .clickable {
                                                documentPickerLauncher.launch("*/*")
                                            }
                                            .padding(vertical = 13.dp)
                                            .testTag("upload_file_picker_button")
                                            .testTag("upload_qr_button"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.UploadFile,
                                                contentDescription = "Browse Files",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Browse Files & Slips on Device",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // 2. Quick 1-Tap Payment Slips & Statement Imports
                            Text(
                                text = "QUICK 1-TAP IMPORT PAYMENT SLIPS & STATEMENTS",
                                fontFamily = HelveticaFontFamily,
                                color = SoftLavender,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )

                            // Grid of Quick Payment Slips
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Electricity Bill Slip (Adani / Tata Power)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF1E1736))
                                        .border(1.dp, Color(0x40F59E0B), RoundedCornerShape(14.dp))
                                        .clickable { onImportDemoStatement("Electricity Bill Slip") }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0x28F59E0B)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.ElectricBolt,
                                                    contentDescription = "Electricity",
                                                    tint = AmberWarning,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "⚡ Electricity Bill Payment Slip",
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Adani & Tata Power slips (₹2,450 / ₹1,380) • Absolute Need",
                                                    color = TextSecondary,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        GlassBadge(
                                            text = "IMPORT SLIP",
                                            textColor = AmberWarning,
                                            borderColor = AmberWarning.copy(alpha = 0.4f),
                                            backgroundColor = AmberWarning.copy(alpha = 0.12f)
                                        )
                                    }
                                }

                                // Home Loan EMI Slip (HDFC / ICICI)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF1E1736))
                                        .border(1.dp, Color(0x40E91E63), RoundedCornerShape(14.dp))
                                        .clickable { onImportDemoStatement("Home Loan EMI Slip") }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0x28E91E63)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Home,
                                                    contentDescription = "Home Loan",
                                                    tint = Color(0xFFFF6090),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "🏠 Home Loan EMI Slip",
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "HDFC Bank Home Loan Principal & Interest (₹38,500)",
                                                    color = TextSecondary,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        GlassBadge(
                                            text = "IMPORT SLIP",
                                            textColor = Color(0xFFFF6090),
                                            borderColor = Color(0x60FF6090),
                                            backgroundColor = Color(0x18FF6090)
                                        )
                                    }
                                }

                                // Monthly Salary Slip
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF142426))
                                        .border(1.dp, Color(0x4010B981), RoundedCornerShape(14.dp))
                                        .clickable { onImportDemoStatement("Salary Slip") }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0x2810B981)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Work,
                                                    contentDescription = "Salary Slip",
                                                    tint = EmeraldGreen,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "💼 Monthly Salary Slip",
                                                    color = Color.White,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Net Take-Home Salary Credit (₹68,000 + ₹25,000 Bonus)",
                                                    color = TextSecondary,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        GlassBadge(
                                            text = "+INCOME",
                                            textColor = EmeraldGreen,
                                            borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                            backgroundColor = EmeraldGreen.copy(alpha = 0.12f)
                                        )
                                    }
                                }

                                // 2-Column: PhonePe Statement & HDFC Bank CSV
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF281845))
                                            .border(1.dp, Color(0x608B5CF6), RoundedCornerShape(12.dp))
                                            .clickable { onImportDemoStatement("PhonePe UPI") }
                                            .padding(vertical = 10.dp, horizontal = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "📱 PhonePe UPI CSV", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "6 Statement Txns", color = SoftLavender, fontSize = 10.sp)
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF1A263D))
                                            .border(1.dp, Color(0x6038BDF8), RoundedCornerShape(12.dp))
                                            .clickable { onImportDemoStatement("HDFC Statement") }
                                            .padding(vertical = 10.dp, horizontal = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "🏛️ HDFC Bank CSV", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "5 Statement Txns", color = SoftLavender, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }

                            // 3. Paste Statement / Slip Text Section
                            GlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                backgroundColor = Color(0xFF18122B)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { showPasteSection = !showPasteSection },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Description,
                                                contentDescription = null,
                                                tint = SoftLavender,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (showPasteSection) "Hide Manual Text Paste" else "Paste Statement CSV or Slip Text",
                                                fontFamily = HelveticaFontFamily,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = if (showPasteSection) "▲" else "▼",
                                            color = SoftLavender,
                                            fontSize = 11.sp
                                        )
                                    }

                                    if (showPasteSection) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        OutlinedTextField(
                                            value = customSlipText,
                                            onValueChange = { customSlipText = it },
                                            placeholder = {
                                                Text(
                                                    text = "e.g. Swiggy Food, 420\nTata Power Electricity, 1890\nHDFC Home Loan EMI, 35000\nInfosys Salary, 65000",
                                                    color = TextMuted,
                                                    fontSize = 11.sp
                                                )
                                            },
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = PhonePeLightPurple,
                                                unfocusedBorderColor = Color(0x408B5CF6)
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(96.dp)
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Button(
                                            onClick = {
                                                if (customSlipText.isNotBlank()) {
                                                    onImportCsv(customSlipText)
                                                    customSlipText = ""
                                                    showPasteSection = false
                                                }
                                            },
                                            enabled = customSlipText.isNotBlank(),
                                            colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Parse & Add Transactions", color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            // 4. Audited Payment Slips & Extracted Statements Ledger
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "AUDITED PAYMENT SLIPS & LEDGER",
                                    fontFamily = HelveticaFontFamily,
                                    color = SoftLavender,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )

                                Text(
                                    text = "${uiState.txns.size} Total Items",
                                    fontFamily = HelveticaFontFamily,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }

                            // List of transactions extracted from documents/slips
                            if (uiState.txns.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0x18FFFFFF))
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No payment slips or statements uploaded yet.\nUse Browse Files above or tap a 1-tap demo slip.",
                                        fontFamily = HelveticaFontFamily,
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    uiState.txns.take(8).forEach { txn ->
                                        val isIncome = txn.amount > 0
                                        val isSlip = txn.name.contains("Slip", true) || txn.name.contains("Bill", true) || txn.name.contains("EMI", true)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF1A132C))
                                                .border(
                                                    1.dp,
                                                    if (isSlip) Color(0x6000D09C) else Color(0x20FFFFFF),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(32.dp)
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(if (isIncome) Color(0x2600D09C) else Color(0x228B5CF6)),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = if (isIncome) Icons.Default.Work else Icons.Default.ReceiptLong,
                                                            contentDescription = null,
                                                            tint = if (isIncome) EmeraldGreen else SoftLavender,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    Column {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text(
                                                                text = txn.name,
                                                                fontFamily = HelveticaFontFamily,
                                                                color = Color.White,
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                            if (isSlip) {
                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                GlassBadge(
                                                                    text = "VERIFIED SLIP",
                                                                    textColor = EmeraldGreen,
                                                                    borderColor = EmeraldGreen.copy(alpha = 0.4f),
                                                                    backgroundColor = EmeraldGreen.copy(alpha = 0.12f)
                                                                )
                                                            }
                                                        }
                                                        Text(
                                                            text = "${txn.date} • ${txn.necessity}",
                                                            fontFamily = HelveticaFontFamily,
                                                            color = TextSecondary,
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                }

                                                Text(
                                                    text = if (isIncome) "+₹${txn.amount.toInt()}" else "-₹${kotlin.math.abs(txn.amount).toInt()}",
                                                    fontFamily = HelveticaFontFamily,
                                                    color = if (isIncome) EmeraldGreen else CoralRed,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 5. Manual Slip Record Option
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x188B5CF6))
                                    .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(12.dp))
                                    .clickable { showManualDialog = true }
                                    .padding(vertical = 12.dp)
                                    .testTag("manual_open_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Keyboard,
                                        contentDescription = "Manual Entry",
                                        tint = SoftLavender,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Record Payment Slip or Spend Manually",
                                        fontFamily = HelveticaFontFamily,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(36.dp))
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
