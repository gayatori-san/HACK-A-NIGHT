package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// PhonePe Signature Colors (Professional, deep, elegant)
val PhonePePurple = Color(0xFF5F259F)
val PhonePeDeepPurple = Color(0xFF4A1580)
val PhonePeLightPurple = Color(0xFF7C3AED)
val PhonePeAccentPurple = Color(0xFF9333EA)
val PhonePeDarkHeader = Color(0xFF140B26)

// Deep Purple gradients used in hero & cards
val DeepPurple1 = Color(0xFF501EAE)
val DeepPurple2 = Color(0xFF381084)
val DeepPurple3 = Color(0xFF240A5C)

// Dimmed Minimal Fintech Palette (Removed neon glare, replaced with refined PhonePe lilac & muted gold)
val SoftLavender = Color(0xFFC4B5FD)
val MutedViolet = Color(0xFFA78BFA)
val NeonLime = Color(0xFFA78BFA) // Dimmed soft lilac/violet alias for backwards-compatibility
val ElectricPurple = Color(0xFF7C3AED)
val AmberCashback = Color(0xFFF59E0B)
val VibrantCyan = Color(0xFF38BDF8)
val VibrantIndigo = Color(0xFF6366F1)

// Rich Dark Obsidian Background with deep sleek PhonePe tone
val ObsidianBlack = Color(0xFF0C0A14)
val DarkSurface = Color(0xFF130F20)
val DarkCard = Color(0xFF181326)
val DarkCardElevated = Color(0xFF1E1730)

// Vibrant Solid-Tinted Surfaces (Minimal, dark glassmorphism)
val GlassSurfaceUltra = Color(0xFF201834)
val GlassSurfaceHigh = Color(0xFF1B142D)
val GlassSurfaceMid = Color(0xFF161124)
val GlassSurfaceLow = Color(0xFF120E1D)
val GlassSurfaceTinted = Color(0xFF22163E)
val GlassHighlightTop = Color(0x1FFFFFFF)

// Clean Crisp Borders (Subtle hairline borders, minimal and sleek)
val GlassBorderGradient = Brush.verticalGradient(
    listOf(Color(0x308B5CF6), Color(0x18A855F7), Color(0x10FFFFFF))
)

val GlassBorderHorizontal = Brush.horizontalGradient(
    listOf(Color(0x28A855F7), Color(0x188B5CF6), Color(0x15FFFFFF))
)

val GlassCardGradient = Brush.verticalGradient(
    listOf(Color(0xFF1A1429), Color(0xFF130E20))
)

val PhonePeHeroGradient = Brush.verticalGradient(
    listOf(Color(0xFF38146E), Color(0xFF1E0B3C), Color(0xFF0C0A14))
)

val PhonePeIconGradient = Brush.linearGradient(
    listOf(Color(0xFF7C3AED), Color(0xFF5F259F))
)

val PhonePeScannerBorder = Brush.sweepGradient(
    listOf(Color(0xFF9333EA), Color(0xFF6739B7), Color(0xFFA855F7), Color(0xFF9333EA))
)

// Semantic colors
val EmeraldGreen = Color(0xFF10B981)
val CoralRed = Color(0xFFF43F5E)
val AmberWarning = Color(0xFFF59E0B)
val FriendBlue = Color(0xFF3B82F6)

// Typography colors
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA6A1BD)
val TextMuted = Color(0xFF706B86)

val BorderSubtle = Color(0x1FFFFFFF)
val BorderMedium = Color(0x2EFFFFFF)
val BorderHighlight = Color(0x4DFFFFFF)
