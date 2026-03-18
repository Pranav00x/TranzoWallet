package com.tranzo.wallet.data.localdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tranzo.wallet.domain.model.Transaction
import com.tranzo.wallet.domain.model.TransactionStatus
import java.math.BigDecimal
import java.math.BigInteger

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val hash: String,
    val fromAddress: String,
    val toAddress: String,
    val value: String,
    val gasPrice: String,
    val gasUsed: String,
    val timestamp: Long,
    val status: String,
    val tokenSymbol: String,
    val blockNumber: Long
)

fun TransactionEntity.toModel(): Transaction {
    return Transaction(
        hash = hash,
        from = fromAddress,
        to = toAddress,
        value = BigDecimal(value),
        gasPrice = BigInteger(gasPrice),
        gasUsed = BigInteger(gasUsed),
        timestamp = timestamp,
        status = try {
            TransactionStatus.valueOf(status)
        } catch (e: IllegalArgumentException) {
            TransactionStatus.PENDING
        },
        tokenSymbol = tokenSymbol,
        blockNumber = blockNumber
    )
}
