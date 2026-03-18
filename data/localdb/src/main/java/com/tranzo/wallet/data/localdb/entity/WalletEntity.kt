package com.tranzo.wallet.data.localdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tranzo.wallet.domain.model.Wallet

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String,
    val createdAt: Long,
    val isActive: Boolean
)

fun WalletEntity.toModel(): Wallet {
    return Wallet(
        id = id,
        name = name,
        address = address,
        createdAt = createdAt,
        isActive = isActive
    )
}
