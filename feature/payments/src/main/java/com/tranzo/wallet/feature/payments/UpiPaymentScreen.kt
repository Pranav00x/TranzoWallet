package com.tranzo.wallet.feature.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens

@Composable
fun UpiPaymentScreen(
    onBackClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = "UPI Payments",
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FrostDesignTokens.spacingMd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            // Coming Soon header
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = "UPI",
                        tint = FrostDesignTokens.icyBlue,
                    )

                    Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))

                    Text(
                        text = "Coming Soon",
                        style = FrostDesignTokens.title.copy(
                            color = FrostDesignTokens.icyBlue,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                Text(
                    text = "Pay anyone in India using UPI, powered by your crypto wallet. Seamless crypto-to-INR conversion at the point of payment.",
                    style = FrostDesignTokens.body.copy(
                        color = FrostDesignTokens.frostGray,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            // UPI Adapter Architecture Diagram
            Text(
                text = "UPI Adapter Architecture",
                style = FrostDesignTokens.title,
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            // Architecture diagram using boxes
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                // Row 1: Wallet
                ArchitectureBox(
                    label = "Tranzo Wallet",
                    icon = Icons.Default.AccountBalance,
                    color = FrostDesignTokens.icyBlue,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                // Arrow down
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Flow",
                        tint = FrostDesignTokens.frostGray,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp),
                    )
                }

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                // Row 2: UPI Adapter
                ArchitectureBox(
                    label = "UPI Adapter Layer",
                    icon = Icons.Default.SwapHoriz,
                    color = FrostDesignTokens.frostCyan,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                // Arrow down
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Flow",
                        tint = FrostDesignTokens.frostGray,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp),
                    )
                }

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                // Row 3: Payment Gateway
                ArchitectureBox(
                    label = "UPI Payment Gateway",
                    icon = Icons.Default.Payment,
                    color = FrostDesignTokens.frostSuccess,
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            // Flow description
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "How it works",
                    style = FrostDesignTokens.label,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                val steps = listOf(
                    "1. User initiates UPI payment from Tranzo",
                    "2. Adapter converts crypto to INR in real-time",
                    "3. INR is routed through UPI payment rails",
                    "4. Merchant receives INR via standard UPI",
                    "5. Transaction is recorded on-chain for transparency",
                )
                steps.forEach { step ->
                    Text(
                        text = step,
                        style = FrostDesignTokens.caption.copy(
                            color = FrostDesignTokens.frostWhite,
                        ),
                        modifier = Modifier.padding(vertical = 3.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))
        }
    }
}

@Composable
private fun ArchitectureBox(
    label: String,
    icon: ImageVector,
    color: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.4f),
                shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
            ),
        shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
        color = color.copy(alpha = 0.1f),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FrostDesignTokens.spacingMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))

            Text(
                text = label,
                style = FrostDesignTokens.label.copy(color = color),
            )
        }
    }
}
