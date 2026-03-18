package com.tranzo.wallet.ui.frost

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Large glassmorphic card that displays total wallet balance,
 * a truncated address with a copy button, and a network badge.
 */
@Composable
fun WalletBalanceCard(
    totalBalance: String,
    walletAddress: String,
    networkName: String,
    onCopyAddress: () -> Unit = {},
    onNetworkClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val truncatedAddress = if (walletAddress.length > 10) {
        "${walletAddress.take(6)}...${walletAddress.takeLast(4)}"
    } else {
        walletAddress
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .blur(0.5.dp),
        shape = RoundedCornerShape(FrostDesignTokens.radiusXl),
        color = FrostDesignTokens.frostWhite.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        shadowElevation = FrostDesignTokens.elevationMedium,
    ) {
        Column(
            modifier = Modifier.padding(FrostDesignTokens.spacingLg),
        ) {
            // Network badge
            Surface(
                onClick = onNetworkClick,
                shape = RoundedCornerShape(FrostDesignTokens.radiusFull),
                color = FrostDesignTokens.frostSlate.copy(alpha = 0.6f),
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = FrostDesignTokens.spacingSm + FrostDesignTokens.spacingXs,
                        vertical = FrostDesignTokens.spacingXs,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = "Network",
                        tint = FrostDesignTokens.frostCyan,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(FrostDesignTokens.spacingXs))
                    Text(
                        text = networkName,
                        style = FrostDesignTokens.caption,
                        color = FrostDesignTokens.frostCyan,
                    )
                }
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            // Total balance
            Text(
                text = totalBalance,
                style = FrostDesignTokens.display,
                color = FrostDesignTokens.frostWhite,
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            // Wallet address with copy button
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = truncatedAddress,
                    style = FrostDesignTokens.caption,
                    color = FrostDesignTokens.frostGray,
                )
                Spacer(modifier = Modifier.width(FrostDesignTokens.spacingXs))
                IconButton(
                    onClick = onCopyAddress,
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copy address",
                        tint = FrostDesignTokens.frostGray,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun WalletBalanceCardPreview() {
    WalletBalanceCard(
        totalBalance = "$12,847.32",
        walletAddress = "0x742d35Cc6634C0532925a3b844Bc9e7595f2bD18",
        networkName = "Ethereum",
        modifier = Modifier.padding(16.dp),
    )
}
