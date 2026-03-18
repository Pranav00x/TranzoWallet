package com.tranzo.wallet.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportWalletScreen(
    onWalletImported: () -> Unit,
    onBack: () -> Unit,
    viewModel: ImportWalletViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.importedWallet) {
        if (state.importedWallet != null) {
            onWalletImported()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Import Wallet",
                        color = FrostWhite,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = FrostWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FrostBackground
                )
            )
        },
        containerColor = FrostBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = FrostDesignTokens.spacingLg)
                .background(FrostBackground)
        ) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            Text(
                text = "Enter your 12-word seed phrase to restore your wallet.",
                style = FrostDesignTokens.body,
                color = FrostGray
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            OutlinedTextField(
                value = state.mnemonic,
                onValueChange = { viewModel.onMnemonicChanged(it) },
                label = {
                    Text(
                        text = "Enter your 12-word seed phrase",
                        color = FrostGray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                minLines = 4,
                maxLines = 6,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = FrostWhite,
                    unfocusedTextColor = FrostWhite,
                    focusedBorderColor = IcyBlue,
                    unfocusedBorderColor = FrostBorder,
                    focusedContainerColor = FrostSurface,
                    unfocusedContainerColor = FrostSurface,
                    cursorColor = IcyBlue,
                    focusedLabelColor = IcyBlue,
                    unfocusedLabelColor = FrostGray
                ),
                shape = RoundedCornerShape(FrostDesignTokens.radiusMd)
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))
                Text(
                    text = state.error!!,
                    color = FrostError,
                    style = FrostDesignTokens.caption
                )
            }

            if (state.mnemonic.isNotEmpty() && !state.isValid) {
                val wordCount = state.mnemonic.trim().split("\\s+".toRegex())
                    .filter { it.isNotEmpty() }.size
                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))
                Text(
                    text = "$wordCount/12 words entered",
                    color = FrostGray,
                    style = FrostDesignTokens.caption
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.importWallet() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.isValid && !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IcyBlue,
                    contentColor = FrostBackground,
                    disabledContainerColor = IcyBlue.copy(alpha = 0.3f),
                    disabledContentColor = FrostBackground.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(FrostDesignTokens.radiusMd)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = FrostBackground,
                        strokeWidth = 2.dp,
                        modifier = Modifier.height(24.dp)
                    )
                } else {
                    Text(
                        text = "Import",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXxl))
        }
    }
}
