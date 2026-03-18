package com.tranzo.wallet.domain.model

data class Transaction(
    val hash: String,
    val from: String,
    val to: String,
    val value: java.math.BigDecimal,
    val gasPrice: java.math.BigInteger,
    val gasUsed: java.math.BigInteger,
    val timestamp: Long,
    val status: TransactionStatus,
    val tokenSymbol: String = "ETH",
    val blockNumber: Long = 0
)

enum class TransactionStatus {
    PENDING, CONFIRMED, FAILED
}
