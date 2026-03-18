package com.tranzo.wallet.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import com.tranzo.wallet.ui.theme.FrostBackground
import com.tranzo.wallet.ui.theme.FrostBorder
import com.tranzo.wallet.ui.theme.FrostError
import com.tranzo.wallet.ui.theme.FrostGray
import com.tranzo.wallet.ui.theme.FrostSurface
import com.tranzo.wallet.ui.theme.FrostWhite
import com.tranzo.wallet.ui.theme.IcyBlue

@Composable
fun PinSetupScreen(
    onPinSet: () -> Unit,
    viewModel: PinSetupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) {
            onPinSet()
        }
    }

    val currentPin = when (state.phase) {
        PinPhase.Enter -> state.pin
        PinPhase.Confirm -> state.confirmPin
    }

    val title = when (state.phase) {
        PinPhase.Enter -> "Set Your PIN"
        PinPhase.Confirm -> "Confirm Your PIN"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = FrostDesignTokens.spacingLg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = IcyBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            Text(
                text = "Enter a 6-digit PIN to secure your wallet",
                style = FrostDesignTokens.body,
                color = FrostGray,
                textAlign = TextAlign.Center
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))
                Text(
                    text = state.error!!,
                    color = FrostError,
                    style = FrostDesignTokens.label,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXxl))

            // PIN dots
            PinDots(filledCount = currentPin.length, total = 6)

            Spacer(modifier = Modifier.weight(1f))

            // Number pad
            NumberPad(
                onDigit = { viewModel.onDigitEntered(it) },
                onBackspace = { viewModel.onBackspace() }
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXxl))
        }
    }
}

@Composable
private fun PinDots(filledCount: Int, total: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(
                        if (index < filledCount) IcyBlue else FrostSurface
                    )
                    .border(
                        width = 1.dp,
                        color = if (index < filledCount) IcyBlue else FrostBorder,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun NumberPad(
    onDigit: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "back")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        when (key) {
                            "" -> {
                                // Empty spacer
                                Spacer(modifier = Modifier.size(72.dp))
                            }

                            "back" -> {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(FrostDesignTokens.radiusMd))
                                        .clickable { onBackspace() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                                        contentDescription = "Backspace",
                                        tint = FrostWhite,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(FrostDesignTokens.radiusMd))
                                        .background(FrostSurface)
                                        .border(
                                            width = 1.dp,
                                            color = FrostBorder,
                                            shape = RoundedCornerShape(FrostDesignTokens.radiusMd)
                                        )
                                        .clickable { onDigit(key) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = FrostWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
