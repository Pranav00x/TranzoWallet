package com.tranzo.wallet.data.localdb.entity

import androidx.room.Entity

@Entity(
    tableName = "tokens",
    primaryKeys = ["contractAddress", "chainId"]
)
data class TokenEntity(
    val contractAddress: String,
    val chainId: Long,
    val symbol: String,
    val name: String,
    val decimals: Int,
    val iconUrl: String,
    val isNative: Boolean
)
