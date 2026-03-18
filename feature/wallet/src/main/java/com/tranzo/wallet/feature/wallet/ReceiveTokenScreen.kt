package com.tranzo.wallet.feature.wallet

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tranzo.wallet.ui.frost.FrostButtonPrimary
import com.tranzo.wallet.ui.frost.FrostButtonSecondary
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiveTokenScreen(
    onNavigateBack: () -> Unit,
    viewModel: WalletHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(state.walletAddress) {
        if (state.walletAddress.isNotEmpty()) {
            qrBitmap = QrCodeGenerator.generate(
                content = state.walletAddress,
                size = 512,
                foregroundColor = android.graphics.Color.parseColor("#0A0E1A"),
                backgroundColor = android.graphics.Color.WHITE,
            )
        }
    }

    Scaffold(
        containerColor = FrostDesignTokens.frostBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Receive",
                        style = FrostDesignTokens.title,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = FrostDesignTokens.frostWhite,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FrostDesignTokens.frostSurface,
                    titleContentColor = FrostDesignTokens.frostWhite,
                ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(FrostDesignTokens.spacingMd)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingMd),
        ) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

            // Network indicator
            Surface(
                shape = RoundedCornerShape(FrostDesignTokens.radiusFull),
                color = FrostDesignTokens.icyBlue.copy(alpha = 0.15f),
            ) {
                Text(
                    text = state.selectedNetwork.name,
                    style = FrostDesignTokens.label.copy(
                        color = FrostDesignTokens.icyBlue,
                    ),
                    modifier = Modifier.padding(
                        horizontal = FrostDesignTokens.spacingMd,
                        vertical = FrostDesignTokens.spacingSm,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            // QR Code
            FrostCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = FrostDesignTokens.spacingXl),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    qrBitmap?.let { bitmap ->
                        Surface(
                            shape = RoundedCornerShape(FrostDesignTokens.radiusMd),
                            color = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.padding(FrostDesignTokens.spacingSm),
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Wallet QR Code",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .padding(FrostDesignTokens.spacingMd),
                                contentScale = ContentScale.Fit,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                    Text(
                        text = "Scan to send ${state.selectedNetwork.symbol}",
                        style = FrostDesignTokens.caption,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            // Wallet address
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Your Address",
                    style = FrostDesignTokens.label,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                Text(
                    text = state.walletAddress,
                    style = FrostDesignTokens.body.copy(
                        color = FrostDesignTokens.frostCyan,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Copy button
            FrostButtonPrimary(
                text = "Copy Address",
                onClick = {
                    clipboardManager.setText(AnnotatedString(state.walletAddress))
                },
                modifier = Modifier.fillMaxWidth(),
            )

            // Share button
            FrostButtonSecondary(
                text = "Share",
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, state.walletAddress)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Address"))
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))
        }
    }
}
