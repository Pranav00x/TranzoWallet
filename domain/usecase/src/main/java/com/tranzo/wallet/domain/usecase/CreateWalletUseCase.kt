package com.tranzo.wallet.domain.usecase

import com.tranzo.wallet.core.crypto.KeyDerivation
import com.tranzo.wallet.core.crypto.MnemonicGenerator
import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.domain.model.Wallet
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateWalletUseCase @Inject constructor(
    private val secureStorageManager: SecureStorageManager
) {

    suspend operator fun invoke(walletName: String): Result<Wallet> {
        return try {
            val mnemonic = MnemonicGenerator.generateMnemonic()
            val credentials = KeyDerivation.deriveEthereumCredentials(mnemonic)
            val address = KeyDerivation.getEthereumAddress(credentials)

            val walletId = UUID.randomUUID().toString()

            secureStorageManager.storeMnemonic(walletId, mnemonic)

            val wallet = Wallet(
                id = walletId,
                name = walletName,
                address = address,
                createdAt = System.currentTimeMillis(),
                isActive = true
            )

            Result.success(wallet)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
