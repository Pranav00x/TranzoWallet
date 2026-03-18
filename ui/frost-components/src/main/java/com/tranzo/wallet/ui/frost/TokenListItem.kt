package com.tranzo.wallet.ui.frost

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * A glassmorphic list item displaying a token with icon, name, symbol,
 * balance, and USD value.
 */
@Composable
fun TokenListItem(
    symbol: String,
    name: String,
    balance: String,
    valueUsd: String,
    iconUrl: String = "",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(FrostDesignTokens.radiusMd),
        color = FrostDesignTokens.frostWhite.copy(alpha = 0.08f),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = FrostDesignTokens.spacingMd,
                    vertical = FrostDesignTokens.spacingSm + FrostDesignTokens.spacingXs,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Token icon
            if (iconUrl.isNotEmpty()) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = "$name icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = FrostDesignTokens.frostSlate.copy(alpha = 0.5f),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Token,
                        contentDescription = "$name icon",
                        tint = FrostDesignTokens.icyBlue,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm + FrostDesignTokens.spacingXs))

            // Name and symbol
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = FrostDesignTokens.label,
                    color = FrostDesignTokens.frostWhite,
                )
                Text(
                    text = symbol,
                    style = FrostDesignTokens.caption,
                    color = FrostDesignTokens.frostGray,
                )
            }

            // Balance and USD value
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = balance,
                    style = FrostDesignTokens.label,
                    color = FrostDesignTokens.frostWhite,
                )
                Text(
                    text = valueUsd,
                    style = FrostDesignTokens.caption,
                    color = FrostDesignTokens.frostGray,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun TokenListItemPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        TokenListItem(
            symbol = "ETH",
            name = "Ethereum",
            balance = "2.4531",
            valueUsd = "$4,812.30",
        )
    }
}
