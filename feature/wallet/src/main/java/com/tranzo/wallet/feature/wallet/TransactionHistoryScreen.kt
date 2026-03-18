package com.tranzo.wallet.feature.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tranzo.wallet.domain.model.Transaction
import com.tranzo.wallet.domain.model.TransactionStatus
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        AnimatedVisibility(
            visible = state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = FrostDesignTokens.icyBlue,
                    strokeWidth = 3.dp,
                )
            }
        }

        AnimatedVisibility(
            visible = !state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (state.transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No Transactions",
                            style = FrostDesignTokens.title,
                        )
                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))
                        Text(
                            text = state.error ?: "Your transaction history will appear here",
                            style = FrostDesignTokens.body.copy(
                                color = FrostDesignTokens.frostGray,
                            ),
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(FrostDesignTokens.spacingMd),
                    verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
                ) {
                    item {
                        Text(
                            text = "Transaction History",
                            style = FrostDesignTokens.headline,
                            modifier = Modifier.padding(bottom = FrostDesignTokens.spacingSm),
                        )
                    }

                    items(
                        items = state.transactions,
                        key = { it.hash }
                    ) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            walletAddress = state.walletAddress,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: Transaction,
    walletAddress: String,
) {
    val isSent = transaction.from.equals(walletAddress, ignoreCase = true)
    val counterparty = if (isSent) transaction.to else transaction.from
    val truncatedAddress = if (counterparty.length > 12) {
        "${counterparty.take(6)}...${counterparty.takeLast(4)}"
    } else {
        counterparty
    }

    val amountEth = transaction.value.divide(
        BigDecimal.TEN.pow(18),
        8,
        RoundingMode.HALF_UP,
    ).stripTrailingZeros()

    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(transaction.timestamp * 1000))

    val statusColor = when (transaction.status) {
        TransactionStatus.CONFIRMED -> FrostDesignTokens.frostSuccess
        TransactionStatus.PENDING -> FrostDesignTokens.frostWarning
        TransactionStatus.FAILED -> FrostDesignTokens.frostError
    }

    val statusText = when (transaction.status) {
        TransactionStatus.CONFIRMED -> "Confirmed"
        TransactionStatus.PENDING -> "Pending"
        TransactionStatus.FAILED -> "Failed"
    }

    FrostCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Direction icon
            Surface(
                shape = CircleShape,
                color = if (isSent) {
                    FrostDesignTokens.frostError.copy(alpha = 0.15f)
                } else {
                    FrostDesignTokens.frostSuccess.copy(alpha = 0.15f)
                },
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isSent) {
                            Icons.AutoMirrored.Filled.CallMade
                        } else {
                            Icons.AutoMirrored.Filled.CallReceived
                        },
                        contentDescription = if (isSent) "Sent" else "Received",
                        tint = if (isSent) {
                            FrostDesignTokens.frostError
                        } else {
                            FrostDesignTokens.frostSuccess
                        },
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(FrostDesignTokens.spacingMd))

            // Address and timestamp
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isSent) "Sent" else "Received",
                    style = FrostDesignTokens.label,
                )
                Text(
                    text = truncatedAddress,
                    style = FrostDesignTokens.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = formattedDate,
                    style = FrostDesignTokens.caption.copy(
                        color = FrostDesignTokens.frostGray.copy(alpha = 0.7f),
                    ),
                )
            }

            Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))

            // Amount and status
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isSent) "-" else "+"}${amountEth.toPlainString()} ${transaction.tokenSymbol}",
                    style = FrostDesignTokens.label.copy(
                        color = if (isSent) FrostDesignTokens.frostError else FrostDesignTokens.frostSuccess,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                Surface(
                    shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
                    color = statusColor.copy(alpha = 0.15f),
                ) {
                    Text(
                        text = statusText,
                        style = FrostDesignTokens.caption.copy(color = statusColor),
                        modifier = Modifier.padding(
                            horizontal = FrostDesignTokens.spacingSm,
                            vertical = 2.dp,
                        ),
                    )
                }
            }
        }
    }
}
