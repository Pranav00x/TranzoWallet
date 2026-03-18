package com.tranzo.wallet.domain.model

data class Wallet(
    val id: String,
    val name: String,
    val address: String,
    val createdAt: Long,
    val isActive: Boolean = false
)
