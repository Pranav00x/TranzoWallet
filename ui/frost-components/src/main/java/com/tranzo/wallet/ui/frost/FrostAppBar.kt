package com.tranzo.wallet.ui.frost

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Frost-themed top app bar with transparent background,
 * optional back navigation, and trailing action slots.
 */
@Composable
fun FrostAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(
                horizontal = FrostDesignTokens.spacingMd,
                vertical = FrostDesignTokens.spacingSm,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBackClick != null) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = FrostDesignTokens.frostWhite,
                )
            }
            Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))
        }

        Text(
            text = title,
            style = FrostDesignTokens.title,
            color = FrostDesignTokens.frostWhite,
            modifier = Modifier.weight(1f),
        )

        actions()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FrostAppBarPreview() {
    FrostAppBar(title = "Wallet")
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FrostAppBarWithBackPreview() {
    FrostAppBar(title = "Send", onBackClick = {})
}
