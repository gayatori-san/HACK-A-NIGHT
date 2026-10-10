package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// 1. Helvetica (Arimo - metric-compatible Helvetica typeface for Android)
val HelveticaFontFamily = FontFamily(
    Font(R.font.arimo, FontWeight.Normal)
)

// 2. Aptos / Aptof (Plus Jakarta Sans - contemporary humanist geometric font matching Aptos aesthetics)
val AptosFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal)
)
val AptofFontFamily = AptosFontFamily
val JakartaFontFamily = AptosFontFamily

// 3. Inter (Original clean UI font)
val InterFontFamily = FontFamily(
    Font(R.font.inter, FontWeight.Normal)
)
val OriginalFontFamily = InterFontFamily

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        letterSpacing = (-1.0).sp,
        lineHeight = 48.sp
    ),
    displayMedium = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.6).sp,
        lineHeight = 38.sp
    ),
    titleLarge = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = (-0.3).sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = (-0.2).sp,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = (-0.1).sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.1).sp
    ),
    bodyMedium = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.1).sp
    ),
    bodySmall = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        letterSpacing = 0.3.sp
    ),
    labelMedium = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = HelveticaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.8.sp
    )
)
