package com.tranzo.wallet.domain.model

data class SendRequest(
    val toAddress: String,
    val amount: java.math.BigDecimal,
    val token: Token,
    val network: Network,
    val gasPrice: java.math.BigInteger? = null,
    val gasLimit: java.math.BigInteger? = null
)
