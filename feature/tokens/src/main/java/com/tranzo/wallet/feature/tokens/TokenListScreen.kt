package com.tranzo.wallet.feature.tokens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tranzo.wallet.domain.model.Token
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import com.tranzo.wallet.ui.frost.TokenListItem
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TokenListScreen(
    onTokenClick: (Token) -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    viewModel: TokenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = "Tokens",
            onBackClick = onBackClick,
        )

        // Search bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FrostDesignTokens.spacingMd),
            placeholder = {
                Text(
                    text = "Search tokens...",
                    style = FrostDesignTokens.body.copy(color = FrostDesignTokens.frostGray),
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = FrostDesignTokens.frostGray,
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(FrostDesignTokens.radiusMd),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = FrostDesignTokens.frostWhite,
                unfocusedTextColor = FrostDesignTokens.frostWhite,
                cursorColor = FrostDesignTokens.icyBlue,
                focusedBorderColor = FrostDesignTokens.icyBlue,
                unfocusedBorderColor = FrostDesignTokens.frostSlate,
                focusedContainerColor = FrostDesignTokens.frostSurface,
                unfocusedContainerColor = FrostDesignTokens.frostSurface,
            ),
            textStyle = FrostDesignTokens.body,
        )

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

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
            if (state.filteredTokens.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.error ?: "No tokens found",
                        style = FrostDesignTokens.body.copy(
                            color = FrostDesignTokens.frostGray,
                        ),
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = FrostDesignTokens.spacingMd,
                        vertical = FrostDesignTokens.spacingSm,
                    ),
                    verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
                ) {
                    items(
                        items = state.filteredTokens,
                        key = { it.contractAddress.ifEmpty { it.symbol } }
                    ) { token ->
                        val balanceFormatted = token.balance
                            .setScale(6, RoundingMode.HALF_UP)
                            .stripTrailingZeros()
                            .toPlainString()
                        val valueUsd = token.balance.multiply(
                            BigDecimal.valueOf(token.priceUsd)
                        )
                        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
                        val valueFormatted = formatter.format(
                            valueUsd.setScale(2, RoundingMode.HALF_UP)
                        )

                        TokenListItem(
                            symbol = token.symbol,
                            name = token.name,
                            balance = balanceFormatted,
                            valueUsd = valueFormatted,
                            iconUrl = token.iconUrl,
                            onClick = { onTokenClick(token) },
                        )
                    }
                }
            }
        }
    }
}
