package com.tranzo.wallet.feature.nft

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tranzo.wallet.domain.model.NFT
import com.tranzo.wallet.ui.frost.FrostAppBar
import com.tranzo.wallet.ui.frost.FrostCard
import com.tranzo.wallet.ui.frost.FrostDesignTokens

@Composable
fun NFTGalleryScreen(
    onNFTClick: (NFT) -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    viewModel: NFTViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FrostDesignTokens.frostBackground)
    ) {
        FrostAppBar(
            title = "NFT Gallery",
            onBackClick = onBackClick,
        )

        when {
            state.isLoading -> {
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

            state.nfts.isEmpty() -> {
                // Empty state placeholder
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "No NFTs",
                            modifier = Modifier.size(64.dp),
                            tint = FrostDesignTokens.frostSlate,
                        )

                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingMd))

                        Text(
                            text = "No NFTs",
                            style = FrostDesignTokens.title.copy(
                                color = FrostDesignTokens.frostGray,
                            ),
                        )

                        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

                        Text(
                            text = "Your NFT collectibles will appear here",
                            style = FrostDesignTokens.body.copy(
                                color = FrostDesignTokens.frostSlate,
                            ),
                        )
                    }
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(FrostDesignTokens.spacingMd),
                    horizontalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
                    verticalArrangement = Arrangement.spacedBy(FrostDesignTokens.spacingSm),
                ) {
                    items(
                        items = state.nfts,
                        key = { "${it.contractAddress}:${it.tokenId}" }
                    ) { nft ->
                        NFTGridCard(
                            nft = nft,
                            onClick = { onNFTClick(nft) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NFTGridCard(
    nft: NFT,
    onClick: () -> Unit
) {
    FrostCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        // NFT image
        if (nft.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = nft.imageUrl,
                contentDescription = nft.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(FrostDesignTokens.radiusSm)),
                contentScale = ContentScale.Crop,
            )
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                shape = RoundedCornerShape(FrostDesignTokens.radiusSm),
                color = FrostDesignTokens.frostSlate.copy(alpha = 0.3f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.BrokenImage,
                        contentDescription = "No image",
                        modifier = Modifier.size(32.dp),
                        tint = FrostDesignTokens.frostGray,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(FrostDesignTokens.spacingSm))

        // NFT name
        Text(
            text = nft.name,
            style = FrostDesignTokens.label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // Collection name
        if (nft.collectionName.isNotEmpty()) {
            Text(
                text = nft.collectionName,
                style = FrostDesignTokens.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
