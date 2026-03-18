package com.tranzo.wallet.data.repository

import com.tranzo.wallet.data.localdb.dao.TransactionDao
import com.tranzo.wallet.data.localdb.entity.TransactionEntity
import com.tranzo.wallet.data.localdb.entity.toModel
import com.tranzo.wallet.data.network.BlockchainService
import com.tranzo.wallet.data.network.api.ExplorerApi
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.Transaction
import com.tranzo.wallet.domain.model.TransactionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val explorerApi: ExplorerApi,
    private val transactionDao: TransactionDao,
    private val blockchainService: BlockchainService
) {

    suspend fun getTransactions(address: String, network: Network): List<Transaction> =
        withContext(Dispatchers.IO) {
            try {
                val response = explorerApi.getTransactions(address = address)
                val entities = response.result.map { tx ->
                    TransactionEntity(
                        hash = tx.hash,
                        fromAddress = tx.from,
                        toAddress = tx.to,
                        value = tx.value,
                        gasPrice = tx.gasPrice,
                        gasUsed = tx.gasUsed,
                        timestamp = tx.timeStamp.toLongOrNull() ?: 0L,
                        status = if (tx.isError == "0") TransactionStatus.CONFIRMED.name else TransactionStatus.FAILED.name,
                        tokenSymbol = network.symbol,
                        blockNumber = tx.blockNumber.toLongOrNull() ?: 0L
                    )
                }
                transactionDao.insertAll(entities)
                entities.map { it.toModel() }
            } catch (e: Exception) {
                transactionDao.getByAddress(address).first().map { it.toModel() }
            }
        }

    suspend fun sendTransaction(signedTx: ByteArray, rpcUrl: String): String =
        withContext(Dispatchers.IO) {
            val hexTx = "0x" + signedTx.joinToString("") { "%02x".format(it) }
            blockchainService.sendRawTransaction(hexTx, rpcUrl)
        }
}
