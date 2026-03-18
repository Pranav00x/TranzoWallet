package com.tranzo.wallet.ui.frost

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A glassmorphic card with a semi-transparent frosted background,
 * subtle white border, and rounded corners.
 */
@Composable
fun FrostCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(FrostDesignTokens.radiusLg)
    val backgroundColor = FrostDesignTokens.frostWhite.copy(alpha = 0.12f)
    val borderColor = Color.White.copy(alpha = 0.2f)

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier.blur(0.5.dp),
            shape = shape,
            color = backgroundColor,
            border = BorderStroke(1.dp, borderColor),
            shadowElevation = FrostDesignTokens.elevationLow,
        ) {
            Column(
                modifier = Modifier.padding(FrostDesignTokens.spacingMd),
                content = content,
            )
        }
    } else {
        Surface(
            modifier = modifier.blur(0.5.dp),
            shape = shape,
            color = backgroundColor,
            border = BorderStroke(1.dp, borderColor),
            shadowElevation = FrostDesignTokens.elevationLow,
        ) {
            Column(
                modifier = Modifier.padding(FrostDesignTokens.spacingMd),
                content = content,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FrostCardPreview() {
    FrostCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Frost Card",
            style = FrostDesignTokens.title,
        )
        Text(
            text = "Glassmorphic surface with frosted translucency",
            style = FrostDesignTokens.body,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FrostCardClickablePreview() {
    FrostCard(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        onClick = {},
    ) {
        Text(text = "Clickable Frost Card", style = FrostDesignTokens.title)
    }
}
