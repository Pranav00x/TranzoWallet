package com.tranzo.wallet.feature.wallet

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens

@Composable
fun CardScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "card_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse_alpha",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
            .padding(FrostDesignTokens.spacingMd),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))

        // ── Card preview ─────────────────────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .graphicsLayer { alpha = pulseAlpha },
            shape = RoundedCornerShape(FrostDesignTokens.radiusLg),
            color = Color.Transparent,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1A1A2E),
                                Color(0xFF16213E),
                                Color(0xFF0F3460),
                                FrostDesignTokens.icyBlue.copy(alpha = 0.4f),
                            ),
                        ),
                        shape = RoundedCornerShape(FrostDesignTokens.radiusLg),
                    )
                    .padding(FrostDesignTokens.spacingLg),
            ) {
                // Card header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = "TRANZO",
                        style = FrostDesignTokens.title.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp,
                            color = FrostDesignTokens.frostWhite.copy(alpha = 0.9f),
                        ),
                    )
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = FrostDesignTokens.icyBlue.copy(alpha = 0.7f),
                        modifier = Modifier.size(32.dp),
                    )
                }

                // Card number placeholder
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                ) {
                    Text(
                        text = "•••• •••• •••• ••••",
                        style = FrostDesignTokens.title.copy(
                            color = FrostDesignTokens.frostWhite.copy(alpha = 0.5f),
                            letterSpacing = 3.sp,
                        ),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "COMING SOON",
                            style = FrostDesignTokens.caption.copy(
                                color = FrostDesignTokens.icyBlue,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 2.sp,
                            ),
                        )
                        Text(
                            text = "VISA",
                            style = FrostDesignTokens.label.copy(
                                color = FrostDesignTokens.frostWhite.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))

        // ── Title ────────────────────────────────────────────────────
        Text(
            text = "Tranzo Card",
            style = FrostDesignTokens.headline.copy(
                color = FrostDesignTokens.frostWhite,
            ),
        )

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

        Text(
            text = "Spend your crypto anywhere.\nThe Tranzo Card is launching soon.",
            style = FrostDesignTokens.body.copy(
                color = FrostDesignTokens.frostGray,
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))

        // ── Feature list ─────────────────────────────────────────────
        CardFeatureRow(
            icon = Icons.Default.Payments,
            title = "Instant Crypto Spending",
            description = "Use crypto at 60M+ merchants worldwide",
        )
        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))
        CardFeatureRow(
            icon = Icons.Default.Shield,
            title = "Zero Fees",
            description = "No annual fees, no hidden charges",
        )
        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))
        CardFeatureRow(
            icon = Icons.Default.Lock,
            title = "Secure & Private",
            description = "Freeze & unfreeze instantly from the app",
        )
        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))
        CardFeatureRow(
            icon = Icons.Default.QrCode,
            title = "Virtual & Physical",
            description = "Get a virtual card instantly, physical later",
        )
    }
}

@Composable
private fun CardFeatureRow(
    icon: ImageVector,
    title: String,
    description: String,
) {
    FrostCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = FrostDesignTokens.icyBlue.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = FrostDesignTokens.icyBlue,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(FrostDesignTokens.spacingMd))
            Column {
                Text(
                    text = title,
                    style = FrostDesignTokens.label.copy(
                        color = FrostDesignTokens.frostWhite,
                    ),
                )
                Text(
                    text = description,
                    style = FrostDesignTokens.caption,
                )
            }
        }
    }
}
