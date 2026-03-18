package com.tranzo.wallet.feature.wallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tranzo.wallet.domain.model.Token
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WalletHomeScreen(
    onSendClick: () -> Unit,
    onReceiveClick: () -> Unit,
    viewModel: WalletHomeViewModel = hiltViewModel()
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(FrostDesignTokens.spacingMd),
                verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingMd),
            ) {
                item {
                    WalletBalanceCard(
                        address = state.walletAddress,
                        totalBalance = state.totalBalance,
                        networkName = state.selectedNetwork.name,
                    )
                }

                item {
                    ActionButtonRow(
                        onSendClick = onSendClick,
                        onReceiveClick = onReceiveClick,
                        onBuyClick = { /* Buy flow */ },
                    )
                }

                item {
                    Text(
                        text = "Assets",
                        style = FrostDesignTokens.title,
                        modifier = Modifier.padding(
                            top = FrostDesignTokens.spacingSm,
                            bottom = FrostDesignTokens.spacingXs,
                        ),
                    )
                }

                items(
                    items = state.tokens,
                    key = { it.contractAddress.ifEmpty { it.symbol } }
                ) { token ->
                    TokenListItem(token = token)
                }

                if (state.tokens.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = state.error ?: "No tokens found",
                                style = FrostDesignTokens.body.copy(
                                    color = FrostDesignTokens.frostGray,
                                ),
                            )
                        }
                    }
                }
            }
        }

        // Floating send button
        FloatingActionButton(
            onClick = onSendClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(FrostDesignTokens.spacingLg),
            shape = CircleShape,
            containerColor = FrostDesignTokens.icyBlue,
            contentColor = FrostDesignTokens.frostWhite,
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
            )
        }
    }
}

@Composable
private fun WalletBalanceCard(
    address: String,
    totalBalance: BigDecimal,
    networkName: String,
) {
    val clipboardManager = LocalClipboardManager.current
    val truncatedAddress = if (address.length > 12) {
        "${address.take(6)}...${address.takeLast(4)}"
    } else {
        address
    }

    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    val formattedBalance = formatter.format(totalBalance.setScale(2, RoundingMode.HALF_UP))

    FrostCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
                color = FrostDesignTokens.icyBlue.copy(alpha = 0.15f),
            ) {
                Text(
                    text = networkName,
                    style = FrostDesignTokens.caption.copy(
                        color = FrostDesignTokens.icyBlue,
                    ),
                    modifier = Modifier.padding(
                        horizontal = FrostDesignTokens.spacingSm,
                        vertical = FrostDesignTokens.spacingXs,
                    ),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    clipboardManager.setText(AnnotatedString(address))
                },
            ) {
                Text(
                    text = truncatedAddress,
                    style = FrostDesignTokens.caption,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy address",
                    modifier = Modifier.size(14.dp),
                    tint = FrostDesignTokens.frostGray,
                )
            }
        }

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

        Text(
            text = "Total Balance",
            style = FrostDesignTokens.caption,
        )

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

        Text(
            text = formattedBalance,
            style = FrostDesignTokens.display,
        )

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))
    }
}

@Composable
private fun ActionButtonRow(
    onSendClick: () -> Unit,
    onReceiveClick: () -> Unit,
    onBuyClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ActionButton(
            icon = Icons.AutoMirrored.Filled.CallMade,
            label = "Send",
            onClick = onSendClick,
        )
        ActionButton(
            icon = Icons.AutoMirrored.Filled.CallReceived,
            label = "Receive",
            onClick = onReceiveClick,
        )
        ActionButton(
            icon = Icons.Default.Add,
            label = "Buy",
            onClick = onBuyClick,
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Surface(
            shape = CircleShape,
            color = FrostDesignTokens.icyBlue.copy(alpha = 0.15f),
            modifier = Modifier.size(56.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = FrostDesignTokens.icyBlue,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

        Text(
            text = label,
            style = FrostDesignTokens.caption.copy(
                color = FrostDesignTokens.frostWhite,
            ),
        )
    }
}

@Composable
private fun TokenListItem(token: Token) {
    val balanceFormatted = token.balance.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
    val valueUsd = token.balance.multiply(BigDecimal.valueOf(token.priceUsd))
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    val valueFormatted = formatter.format(valueUsd.setScale(2, RoundingMode.HALF_UP))

    FrostCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (token.iconUrl.isNotEmpty()) {
                AsyncImage(
                    model = token.iconUrl,
                    contentDescription = token.name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
            } else {
                Surface(
                    shape = CircleShape,
                    color = FrostDesignTokens.frostSlate,
                    modifier = Modifier.size(40.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = token.symbol.take(2),
                            style = FrostDesignTokens.label.copy(
                                color = FrostDesignTokens.frostWhite,
                            ),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(FrostDesignTokens.spacingMd))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = token.name,
                    style = FrostDesignTokens.label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${balanceFormatted} ${token.symbol}",
                    style = FrostDesignTokens.caption,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = valueFormatted,
                    style = FrostDesignTokens.label,
                )
                if (token.priceUsd > 0) {
                    Text(
                        text = "$${String.format("%.2f", token.priceUsd)}",
                        style = FrostDesignTokens.caption,
                    )
                }
            }
        }
    }
}
