package com.tranzo.wallet.ui.frost

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Frost Design System tokens.
 *
 * Central source of truth for colors, typography, spacing, elevation,
 * and corner radii used across all Frost glassmorphic components.
 */
object FrostDesignTokens {

    // ── Colors ──────────────────────────────────────────────────────────
    val frostBackground = Color(0xFF0A0E1A)
    val frostSurface = Color(0xFF111827)
    val frostCard = Color(0xFF1F2937).copy(alpha = 0.8f)
    val icyBlue = Color(0xFF60A5FA)
    val frostCyan = Color(0xFF22D3EE)
    val frostWhite = Color(0xFFF9FAFB)
    val frostGray = Color(0xFF9CA3AF)
    val frostSlate = Color(0xFF334155)
    val frostSuccess = Color(0xFF34D399)
    val frostError = Color(0xFFF87171)
    val frostWarning = Color(0xFFFBBF24)

    // ── Typography ──────────────────────────────────────────────────────
    val display = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = frostWhite,
    )

    val headline = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = frostWhite,
    )

    val title = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = frostWhite,
    )

    val body = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = frostWhite,
    )

    val label = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = frostWhite,
    )

    val caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = frostGray,
    )

    // ── Spacing ─────────────────────────────────────────────────────────
    val spacingXs: Dp = 4.dp
    val spacingSm: Dp = 8.dp
    val spacingMd: Dp = 16.dp
    val spacingLg: Dp = 24.dp
    val spacingXl: Dp = 32.dp
    val spacingXxl: Dp = 48.dp

    // ── Elevation ───────────────────────────────────────────────────────
    val elevationNone: Dp = 0.dp
    val elevationLow: Dp = 2.dp
    val elevationMedium: Dp = 4.dp
    val elevationHigh: Dp = 8.dp
    val elevationFloating: Dp = 16.dp

    // ── Corner Radius ───────────────────────────────────────────────────
    val radiusSm: Dp = 8.dp
    val radiusMd: Dp = 16.dp
    val radiusLg: Dp = 20.dp
    val radiusXl: Dp = 24.dp
    val radiusFull: Dp = 100.dp
}
