package com.tranzo.wallet.domain.model

data class Token(
    val contractAddress: String,
    val symbol: String,
    val name: String,
    val decimals: Int,
    val balance: java.math.BigDecimal = java.math.BigDecimal.ZERO,
    val priceUsd: Double = 0.0,
    val iconUrl: String = "",
    val isNative: Boolean = false
)
