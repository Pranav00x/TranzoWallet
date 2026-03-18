package com.tranzo.wallet.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import com.tranzo.wallet.ui.theme.FrostBackground
import com.tranzo.wallet.ui.theme.FrostError
import com.tranzo.wallet.ui.theme.FrostGray
import com.tranzo.wallet.ui.theme.FrostWhite
import com.tranzo.wallet.ui.theme.IcyBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWalletScreen(
    onWalletCreated: (walletId: String, mnemonic: String) -> Unit,
    onBack: () -> Unit,
    viewModel: CreateWalletViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.createWallet()
    }

    LaunchedEffect(state) {
        if (state is CreateWalletState.Success) {
            val success = state as CreateWalletState.Success
            onWalletCreated(success.wallet.id, success.mnemonic)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Creating Your Wallet",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(FrostBackground),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is CreateWalletState.Idle,
                is CreateWalletState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = IcyBlue,
                            modifier = Modifier.size(64.dp),
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))
                        Text(
                            text = "Generating your secure wallet...",
                            style = FrostDesignTokens.body,
                            color = FrostGray
                        )
                    }
                }

                is CreateWalletState.Success -> {
                    // Navigation handled via LaunchedEffect
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Wallet Created!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = IcyBlue
                        )
                    }
                }

                is CreateWalletState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = FrostError
                        )
                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))
                        Text(
                            text = (state as CreateWalletState.Error).message,
                            style = FrostDesignTokens.body,
                            color = FrostGray
                        )
                    }
                }
            }
        }
    }
}
