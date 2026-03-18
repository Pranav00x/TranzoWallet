package com.tranzo.wallet.feature.nft

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tranzo.wallet.domain.model.NFT
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens
import androidx.compose.foundation.layout.Box

@Composable
fun NFTDetailScreen(
    nft: NFT,
    onBackClick: () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = nft.name,
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FrostDesignTokens.spacingMd),
        ) {
            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

            // Large NFT image
            if (nft.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = nft.imageUrl,
                    contentDescription = nft.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(FrostDesignTokens.radiusLg)),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(FrostDesignTokens.radiusLg),
                    color = FrostDesignTokens.frostSlate.copy(alpha = 0.3f),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = "No image",
                            modifier = Modifier.size(64.dp),
                            tint = FrostDesignTokens.frostGray,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            // Name
            Text(
                text = nft.name,
                style = FrostDesignTokens.headline,
            )

            // Collection
            if (nft.collectionName.isNotEmpty()) {
                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

                Text(
                    text = nft.collectionName,
                    style = FrostDesignTokens.body.copy(
                        color = FrostDesignTokens.icyBlue,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingLg))

            // Description
            if (nft.description.isNotEmpty()) {
                FrostCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Description",
                        style = FrostDesignTokens.label.copy(
                            color = FrostDesignTokens.frostGray,
                        ),
                    )

                    Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                    Text(
                        text = nft.description,
                        style = FrostDesignTokens.body,
                    )
                }

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))
            }

            // Details card
            FrostCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Details",
                    style = FrostDesignTokens.label.copy(
                        color = FrostDesignTokens.frostGray,
                    ),
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                // Contract Address
                DetailRow(
                    label = "Contract Address",
                    value = nft.contractAddress,
                    truncate = true,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(nft.contractAddress))
                    },
                )

                Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                // Token ID
                DetailRow(
                    label = "Token ID",
                    value = nft.tokenId,
                    truncate = false,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(nft.tokenId))
                    },
                )
            }

            Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXl))
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    truncate: Boolean,
    onCopy: () -> Unit
) {
    Column {
        Text(
            text = label,
            style = FrostDesignTokens.caption,
        )

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingXs))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (truncate && value.length > 16) {
                    "${value.take(8)}...${value.takeLast(6)}"
                } else {
                    value
                },
                style = FrostDesignTokens.label,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            IconButton(
                onClick = onCopy,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    modifier = Modifier.size(16.dp),
                    tint = FrostDesignTokens.frostGray,
                )
            }
        }
    }
}
