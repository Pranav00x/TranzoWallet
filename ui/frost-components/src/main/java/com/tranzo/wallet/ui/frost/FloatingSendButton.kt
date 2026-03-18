package com.tranzo.wallet.ui.frost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Circular floating action button with a gradient background
 * and an upward-arrow / send icon.
 */
@Composable
fun FloatingSendButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(FrostDesignTokens.icyBlue, FrostDesignTokens.frostCyan),
    )

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(64.dp),
        shape = CircleShape,
        containerColor = Color.Transparent,
        contentColor = FrostDesignTokens.frostWhite,
        elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(
            defaultElevation = FrostDesignTokens.elevationHigh,
            pressedElevation = FrostDesignTokens.elevationFloating,
        ),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(brush = gradient, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = FrostDesignTokens.frostWhite,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FloatingSendButtonPreview() {
    Box(modifier = Modifier.padding(24.dp)) {
        FloatingSendButton(onClick = {})
    }
}
