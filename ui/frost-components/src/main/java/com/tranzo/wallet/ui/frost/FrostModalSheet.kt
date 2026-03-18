package com.tranzo.wallet.ui.frost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Bottom modal sheet with glassmorphic dark-slate background,
 * rounded top corners, a drag handle, optional title, and content slot.
 */
@Composable
fun FrostModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String = "",
    content: @Composable ColumnScope.() -> Unit
) {
    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.BottomCenter,
        ) {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300),
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300),
                ),
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { /* Consume click to prevent dismiss */ },
                        ),
                    shape = RoundedCornerShape(
                        topStart = FrostDesignTokens.radiusXl,
                        topEnd = FrostDesignTokens.radiusXl,
                    ),
                    color = FrostDesignTokens.frostSurface.copy(alpha = 0.95f),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(FrostDesignTokens.spacingLg),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Drag handle
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(FrostDesignTokens.radiusFull))
                                .background(FrostDesignTokens.frostGray.copy(alpha = 0.4f)),
                        )

                        if (title.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))
                            Text(
                                text = title,
                                style = FrostDesignTokens.headline,
                                color = FrostDesignTokens.frostWhite,
                            )
                        }

                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                        content()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0E1A)
@Composable
private fun FrostModalSheetPreview() {
    FrostModalSheet(
        visible = true,
        onDismiss = {},
        title = "Select Network",
    ) {
        Text(
            text = "Sheet content goes here",
            style = FrostDesignTokens.body,
            color = FrostDesignTokens.frostWhite,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
