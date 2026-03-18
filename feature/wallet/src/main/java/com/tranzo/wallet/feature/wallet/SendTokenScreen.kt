package com.tranzo.wallet.feature.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tranzo.wallet.ui.frost.FrostButtonPrimary
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendTokenScreen(
    onNavigateBack: () -> Unit,
    viewModel: SendTokenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = FrostDesignTokens.frostBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Send",
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
            verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingMd),
        ) {
            // Token selector
            TokenSelectorField(
                selectedToken = state.selectedToken,
                tokens = state.availableTokens,
                onTokenSelected = viewModel::selectToken,
            )

            // Recipient address
            SecureInputField(
                value = state.toAddress,
                onValueChange = viewModel::updateToAddress,
                label = "Recipient Address",
                placeholder = "0x...",
                keyboardType = KeyboardType.Ascii,
            )

            // Amount with Max button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SecureInputField(
                        value = state.amount,
                        onValueChange = viewModel::updateAmount,
                        label = "Amount",
                        placeholder = "0.0",
                        keyboardType = KeyboardType.Decimal,
                    )
                }

                Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))

                Surface(
                    shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
                    color = FrostDesignTokens.icyBlue.copy(alpha = 0.15f),
                    modifier = Modifier
                        .height(56.dp)
                        .clickable { viewModel.setMaxAmount() },
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = FrostDesignTokens.spacingMd),
                    ) {
                        Text(
                            text = "Max",
                            style = FrostDesignTokens.label.copy(
                                color = FrostDesignTokens.icyBlue,
                            ),
                        )
                    }
                }
            }

            // Balance display
            state.selectedToken?.let { token ->
                Text(
                    text = "Available: ${token.balance.stripTrailingZeros().toPlainString()} ${token.symbol}",
                    style = FrostDesignTokens.caption,
                    modifier = Modifier.padding(start = FrostDesignTokens.spacingXs),
                )
            }

            // Gas estimation
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Transaction Fee",
                    style = FrostDesignTokens.label,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Gas Limit",
                        style = FrostDesignTokens.caption,
                    )
                    Text(
                        text = if (state.gasEstimate > BigInteger.ZERO) {
                            state.gasEstimate.toString()
                        } else {
                            "21,000 (default)"
                        },
                        style = FrostDesignTokens.caption.copy(
                            color = FrostDesignTokens.frostWhite,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Gas Price",
                        style = FrostDesignTokens.caption,
                    )
                    Text(
                        text = "${state.gasPriceGwei} Gwei",
                        style = FrostDesignTokens.caption.copy(
                            color = FrostDesignTokens.frostWhite,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                val gasLimit = if (state.gasEstimate > BigInteger.ZERO) {
                    state.gasEstimate
                } else {
                    BigInteger.valueOf(21_000)
                }
                val gasPriceWei = try {
                    BigInteger(state.gasPriceGwei).multiply(BigInteger.TEN.pow(9))
                } catch (e: Exception) {
                    BigInteger.valueOf(20_000_000_000L)
                }
                val feeWei = gasLimit.multiply(gasPriceWei)
                val feeEth = BigDecimal(feeWei).divide(
                    BigDecimal.TEN.pow(18),
                    8,
                    RoundingMode.HALF_UP,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Estimated Fee",
                        style = FrostDesignTokens.label.copy(
                            color = FrostDesignTokens.frostCyan,
                        ),
                    )
                    Text(
                        text = "${feeEth.stripTrailingZeros().toPlainString()} ${state.selectedNetwork.symbol}",
                        style = FrostDesignTokens.label.copy(
                            color = FrostDesignTokens.frostCyan,
                        ),
                    )
                }
            }

            // Error
            AnimatedVisibility(visible = state.error != null) {
                Text(
                    text = state.error ?: "",
                    style = FrostDesignTokens.caption.copy(
                        color = FrostDesignTokens.frostError,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            // Send button
            FrostButtonPrimary(
                text = "Send",
                onClick = { viewModel.send() },
                isLoading = state.isSending,
                enabled = state.toAddress.isNotBlank() &&
                        state.amount.isNotBlank() &&
                        state.selectedToken != null &&
                        !state.isSending,
            )

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))
        }
    }

    // Success dialog
    if (state.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialogs() },
            containerColor = FrostDesignTokens.frostSurface,
            icon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = FrostDesignTokens.frostSuccess,
                    modifier = Modifier.size(48.dp),
                )
            },
            title = {
                Text(
                    text = "Transaction Sent",
                    style = FrostDesignTokens.title,
                )
            },
            text = {
                Column {
                    Text(
                        text = "Your transaction has been submitted.",
                        style = FrostDesignTokens.body,
                    )
                    state.txHash?.let { hash ->
                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))
                        Text(
                            text = "Tx: ${hash.take(10)}...${hash.takeLast(6)}",
                            style = FrostDesignTokens.caption,
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissDialogs()
                        viewModel.resetState()
                        onNavigateBack()
                    }
                ) {
                    Text(
                        text = "Done",
                        color = FrostDesignTokens.icyBlue,
                    )
                }
            },
        )
    }

    // Failure dialog
    if (state.showFailureDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialogs() },
            containerColor = FrostDesignTokens.frostSurface,
            icon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = FrostDesignTokens.frostError,
                    modifier = Modifier.size(48.dp),
                )
            },
            title = {
                Text(
                    text = "Transaction Failed",
                    style = FrostDesignTokens.title,
                )
            },
            text = {
                Text(
                    text = state.error ?: "An unknown error occurred.",
                    style = FrostDesignTokens.body,
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissDialogs() }) {
                    Text(
                        text = "OK",
                        color = FrostDesignTokens.icyBlue,
                    )
                }
            },
        )
    }
}

@Composable
private fun TokenSelectorField(
    selectedToken: com.tranzo.wallet.domain.model.Token?,
    tokens: List<com.tranzo.wallet.domain.model.Token>,
    onTokenSelected: (com.tranzo.wallet.domain.model.Token) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Token",
            style = FrostDesignTokens.label,
            modifier = Modifier.padding(bottom = FrostDesignTokens.spacingXs),
        )

        Box {
            Surface(
                shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
                color = FrostDesignTokens.frostSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { expanded = true },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FrostDesignTokens.spacingMd),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = selectedToken?.let { "${it.symbol} - ${it.name}" } ?: "Select token",
                        style = FrostDesignTokens.body,
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select",
                        tint = FrostDesignTokens.frostGray,
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(FrostDesignTokens.frostSurface)
                    .fillMaxWidth(0.9f),
            ) {
                tokens.forEach { token ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = "${token.symbol} - ${token.name}",
                                    style = FrostDesignTokens.body,
                                )
                                Text(
                                    text = token.balance.stripTrailingZeros().toPlainString(),
                                    style = FrostDesignTokens.caption,
                                )
                            }
                        },
                        onClick = {
                            onTokenSelected(token)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SecureInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column {
        Text(
            text = label,
            style = FrostDesignTokens.label,
            modifier = Modifier.padding(bottom = FrostDesignTokens.spacingXs),
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = FrostDesignTokens.body.copy(
                        color = FrostDesignTokens.frostGray.copy(alpha = 0.5f),
                    ),
                )
            },
            textStyle = FrostDesignTokens.body,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = FrostDesignTokens.frostWhite,
                unfocusedTextColor = FrostDesignTokens.frostWhite,
                cursorColor = FrostDesignTokens.icyBlue,
                focusedBorderColor = FrostDesignTokens.icyBlue,
                unfocusedBorderColor = FrostDesignTokens.frostSlate,
                focusedContainerColor = FrostDesignTokens.frostSurface,
                unfocusedContainerColor = FrostDesignTokens.frostSurface,
            ),
            shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
        )
    }
}
