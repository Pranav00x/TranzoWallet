package com.tranzo.wallet.core.keystore

interface SecureStorageManager {

    suspend fun storeMnemonic(walletId: String, mnemonic: String)

    suspend fun getMnemonic(walletId: String): String?

    suspend fun storePrivateKey(walletId: String, key: String)

    suspend fun getPrivateKey(walletId: String): String?

    suspend fun storePin(pin: String)

    suspend fun verifyPin(pin: String): Boolean

    suspend fun clearAll()

    suspend fun hasWallet(): Boolean
}
