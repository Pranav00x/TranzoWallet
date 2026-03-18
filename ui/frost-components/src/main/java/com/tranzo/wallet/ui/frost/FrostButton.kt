package com.tranzo.wallet.ui.frost

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Primary Frost button with a gradient background (icyBlue -> frostCyan),
 * white text, rounded corners, and optional loading state.
 */
@Composable
fun FrostButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val shape = RoundedCornerShape(FrostDesignTokens.radiusMd)
    val gradient = Brush.horizontalGradient(
        colors = listOf(FrostDesignTokens.icyBlue, FrostDesignTokens.frostCyan),
    )

    Button(
        onClick = { if (!isLoading) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = FrostDesignTokens.frostSlate.copy(alpha = 0.5f),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = FrostDesignTokens.elevationLow,
            pressedElevation = FrostDesignTokens.elevationMedium,
        ),
        contentPadding = ButtonDefaults.ContentPadding,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = if (enabled) gradient
                    else Brush.horizontalGradient(
                        listOf(
                            FrostDesignTokens.frostSlate,
                            FrostDesignTokens.frostSlate,
                        )
                    ),
                    shape = shape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = FrostDesignTokens.frostWhite,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = text,
                    style = FrostDesignTokens.label,
                    color = FrostDesignTokens.frostWhite,
                )
            }
        }
    }
}

/**
 * Secondary Frost button with a transparent background and an icyBlue border.
 */
@Composable
fun FrostButtonSecondary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(FrostDesignTokens.radiusMd)

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = shape,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) FrostDesignTokens.icyBlue
            else FrostDesignTokens.frostSlate,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = FrostDesignTokens.icyBlue,
            disabledContentColor = FrostDesignTokens.frostSlate,
        ),
    ) {
        Text(
            text = text,
            style = FrostDesignTokens.label,
            color = if (enabled) FrostDesignTokens.icyBlue else FrostDesignTokens.frostSlate,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FrostButtonPrimaryPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        FrostButtonPrimary(text = "Send Tokens", onClick = {})
        Spacer(modifier = Modifier.height(12.dp))
        FrostButtonPrimary(text = "Loading...", onClick = {}, isLoading = true)
        Spacer(modifier = Modifier.height(12.dp))
        FrostButtonPrimary(text = "Disabled", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FrostButtonSecondaryPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        FrostButtonSecondary(text = "Cancel", onClick = {})
        Spacer(modifier = Modifier.height(12.dp))
        FrostButtonSecondary(text = "Disabled", onClick = {}, enabled = false)
    }
}
