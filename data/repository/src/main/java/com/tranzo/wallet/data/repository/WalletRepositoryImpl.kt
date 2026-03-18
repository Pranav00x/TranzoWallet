package com.tranzo.wallet.data.repository

import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.data.localdb.dao.WalletDao
import com.tranzo.wallet.data.localdb.entity.toModel
import com.tranzo.wallet.data.network.BlockchainService
import com.tranzo.wallet.domain.model.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao,
    private val secureStorageManager: SecureStorageManager,
    private val blockchainService: BlockchainService
) {

    suspend fun getActiveWallet(): Wallet? = withContext(Dispatchers.IO) {
        walletDao.getActive()?.toModel()
    }

    fun getAllWallets(): Flow<List<Wallet>> {
        return walletDao.getAll().map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun setActiveWallet(walletId: String) = withContext(Dispatchers.IO) {
        walletDao.setActive(walletId)
    }
}
