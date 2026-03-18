package com.tranzo.wallet.feature.onboarding

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import com.tranzo.wallet.ui.theme.FrostBackground
import com.tranzo.wallet.ui.theme.FrostGray
import com.tranzo.wallet.ui.theme.FrostWhite
import com.tranzo.wallet.ui.theme.IcyBlue

@Composable
fun IntroScreen(
    onCreateWallet: () -> Unit,
    onImportWallet: () -> Unit
) {
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
                text = "Welcome to Tranzo",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = IcyBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            Text(
                text = "Your secure, non-custodial wallet",
                style = FrostDesignTokens.body,
                color = FrostGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXxl))

            FeatureHighlight(
                icon = Icons.Filled.Shield,
                title = "Security",
                description = "Your keys never leave your device"
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            FeatureHighlight(
                icon = Icons.Filled.Speed,
                title = "Speed",
                description = "Fast and seamless transactions"
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            FeatureHighlight(
                icon = Icons.Filled.Language,
                title = "Multi-chain",
                description = "Support for multiple blockchains"
            )

            Spacer(modifier = Modifier.weight(1f))

            FrostButtonPrimary(
                text = "Create New Wallet",
                onClick = onCreateWallet,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            FrostButtonSecondary(
                text = "Import Existing Wallet",
                onClick = onImportWallet,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXxl))
        }
    }
}

@Composable
private fun FeatureHighlight(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = IcyBlue,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(FrostDesignTokens.spacingMd))

        Column {
            Text(
                text = title,
                style = FrostDesignTokens.title,
                color = FrostWhite
            )
            Text(
                text = description,
                style = FrostDesignTokens.caption,
                color = FrostGray
            )
        }
    }
}

@Composable
private fun FrostButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = IcyBlue,
            contentColor = FrostBackground
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(FrostDesignTokens.radiusMd)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun FrostButtonSecondary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, IcyBlue),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(FrostDesignTokens.radiusMd)
    ) {
        Text(
            text = text,
            color = IcyBlue,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}
