package com.tranzo.wallet.feature.tokens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tranzo.wallet.domain.model.Token
import com.tranzo.wallet.domain.model.Transaction
import com.tranzo.wallet.domain.model.TransactionStatus
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TokenDetailScreen(
    token: Token,
    transactions: List<Transaction> = emptyList(),
    onBackClick: () -> Unit = {}
) {
    val balanceFormatted = token.balance
        .setScale(6, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString()
    val valueUsd = token.balance.multiply(BigDecimal.valueOf(token.priceUsd))
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    val valueFormatted = formatter.format(valueUsd.setScale(2, RoundingMode.HALF_UP))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = token.name,
            onBackClick = onBackClick,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingMd),
        ) {
            // Token header with icon, name, symbol
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FrostDesignTokens.spacingMd),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                    // Large token icon
                    if (token.iconUrl.isNotEmpty()) {
                        AsyncImage(
                            model = token.iconUrl,
                            contentDescription = "${token.name} icon",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = FrostDesignTokens.frostSlate,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Token,
                                    contentDescription = "${token.name} icon",
                                    tint = FrostDesignTokens.icyBlue,
                                    modifier = Modifier.size(40.dp),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                    Text(
                        text = token.name,
                        style = FrostDesignTokens.headline,
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                    Text(
                        text = token.symbol,
                        style = FrostDesignTokens.body.copy(
                            color = FrostDesignTokens.frostGray,
                        ),
                    )
                }
            }

            // Balance card
            item {
                FrostCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FrostDesignTokens.spacingMd),
                ) {
                    Text(
                        text = "Balance",
                        style = FrostDesignTokens.caption,
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                    Text(
                        text = "$balanceFormatted ${token.symbol}",
                        style = FrostDesignTokens.headline,
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                    Text(
                        text = valueFormatted,
                        style = FrostDesignTokens.body.copy(
                            color = FrostDesignTokens.frostGray,
                        ),
                    )

                    if (token.priceUsd > 0) {
                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                        Text(
                            text = "Price: $${String.format("%.2f", token.priceUsd)}",
                            style = FrostDesignTokens.caption,
                        )
                    }
                }
            }

            // Price chart placeholder
            item {
                FrostCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FrostDesignTokens.spacingMd),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Chart",
                            tint = FrostDesignTokens.icyBlue,
                            modifier = Modifier.size(20.dp),
                        )

                        Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))

                        Text(
                            text = "Price Chart",
                            style = FrostDesignTokens.label,
                        )
                    }

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                    // Chart placeholder
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
                        color = FrostDesignTokens.frostSlate.copy(alpha = 0.3f),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Chart coming soon",
                                style = FrostDesignTokens.caption,
                            )
                        }
                    }
                }
            }

            // Transactions section header
            item {
                Text(
                    text = "Transactions",
                    style = FrostDesignTokens.title,
                    modifier = Modifier.padding(
                        horizontal = FrostDesignTokens.spacingMd,
                        vertical = FrostDesignTokens.spacingXs,
                    ),
                )
            }

            if (transactions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No transactions for this token",
                            style = FrostDesignTokens.body.copy(
                                color = FrostDesignTokens.frostGray,
                            ),
                        )
                    }
                }
            } else {
                items(
                    items = transactions,
                    key = { it.hash }
                ) { tx ->
                    TransactionRow(
                        transaction = tx,
                        modifier = Modifier.padding(
                            horizontal = FrostDesignTokens.spacingMd,
                        ),
                    )
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))
            }
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US)
    val dateStr = dateFormat.format(Date(transaction.timestamp * 1000))
    val truncatedHash = "${transaction.hash.take(10)}...${transaction.hash.takeLast(6)}"

    val statusColor = when (transaction.status) {
        TransactionStatus.CONFIRMED -> FrostDesignTokens.frostSuccess
        TransactionStatus.PENDING -> FrostDesignTokens.frostWarning
        TransactionStatus.FAILED -> FrostDesignTokens.frostError
    }

    FrostCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = truncatedHash,
                    style = FrostDesignTokens.label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                Text(
                    text = dateStr,
                    style = FrostDesignTokens.caption,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${transaction.value.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()} ${transaction.tokenSymbol}",
                    style = FrostDesignTokens.label,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                Text(
                    text = transaction.status.name,
                    style = FrostDesignTokens.caption.copy(color = statusColor),
                )
            }
        }
    }
}
