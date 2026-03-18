package com.tranzo.wallet.feature.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens

@Composable
fun CardManagementScreen(
    onBackClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = "Card Management",
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FrostDesignTokens.spacingMd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            // Frost-styled debit card mockup
            FrostCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.586f), // Standard card aspect ratio
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Card header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "TRANZO",
                            style = FrostDesignTokens.title.copy(
                                color = FrostDesignTokens.icyBlue,
                            ),
                        )

                        Icon(
                            imageVector = Icons.Default.Nfc,
                            contentDescription = "Contactless",
                            tint = FrostDesignTokens.frostGray,
                        )
                    }

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

                    // Card chip placeholder
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(36.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        FrostDesignTokens.frostSlate,
                                        FrostDesignTokens.frostGray.copy(alpha = 0.5f),
                                    ),
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
                            ),
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                    // Card number
                    Text(
                        text = "**** **** **** ****",
                        style = FrostDesignTokens.headline.copy(
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp,
                        ),
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                    // Card footer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(
                                text = "CARD HOLDER",
                                style = FrostDesignTokens.caption.copy(
                                    fontSize = 10.sp,
                                ),
                            )
                            Text(
                                text = "YOUR NAME",
                                style = FrostDesignTokens.label,
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "EXPIRES",
                                style = FrostDesignTokens.caption.copy(
                                    fontSize = 10.sp,
                                ),
                            )
                            Text(
                                text = "MM/YY",
                                style = FrostDesignTokens.label,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))

            // Coming Soon badge
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "Card",
                        tint = FrostDesignTokens.icyBlue,
                    )

                    Spacer(modifier = Modifier.width(FrostDesignTokens.spacingSm))

                    Text(
                        text = "Coming Soon",
                        style = FrostDesignTokens.title.copy(
                            color = FrostDesignTokens.icyBlue,
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                Text(
                    text = "Spend your crypto anywhere with a Tranzo debit card. Card rails integration is planned for a future release.",
                    style = FrostDesignTokens.body.copy(
                        color = FrostDesignTokens.frostGray,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                Text(
                    text = "Features planned:",
                    style = FrostDesignTokens.label,
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                val features = listOf(
                    "Virtual and physical card options",
                    "Instant crypto-to-fiat conversion",
                    "Real-time transaction notifications",
                    "Spending limits and controls",
                    "Multi-currency support",
                )
                features.forEach { feature ->
                    Text(
                        text = "  \u2022  $feature",
                        style = FrostDesignTokens.caption.copy(
                            color = FrostDesignTokens.frostWhite,
                        ),
                        modifier = Modifier.padding(vertical = 2.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))
        }
    }
}
