package com.tranzo.wallet.domain.model

data class NFT(
    val tokenId: String,
    val contractAddress: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val collectionName: String = ""
)
