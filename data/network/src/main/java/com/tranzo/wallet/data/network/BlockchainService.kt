package com.tranzo.wallet.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockchainService @Inject constructor(
    private val okHttpClient: OkHttpClient
) {

    private val web3jCache = mutableMapOf<String, Web3j>()

    fun getWeb3(rpcUrl: String): Web3j {
        return web3jCache.getOrPut(rpcUrl) {
            Web3j.build(HttpService(rpcUrl, okHttpClient))
        }
    }

    suspend fun getBalance(address: String, rpcUrl: String): BigInteger =
        withContext(Dispatchers.IO) {
            val web3 = getWeb3(rpcUrl)
            web3.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .send()
                .balance
        }

    suspend fun getGasPrice(rpcUrl: String): BigInteger =
        withContext(Dispatchers.IO) {
            val web3 = getWeb3(rpcUrl)
            web3.ethGasPrice()
                .send()
                .gasPrice
        }

    suspend fun estimateGas(
        from: String,
        to: String,
        value: BigInteger,
        rpcUrl: String
    ): BigInteger = withContext(Dispatchers.IO) {
        val web3 = getWeb3(rpcUrl)
        val transaction = org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction(
            from, null, null, null, to, value
        )
        web3.ethEstimateGas(transaction)
            .send()
            .amountUsed
    }

    suspend fun getNonce(address: String, rpcUrl: String): BigInteger =
        withContext(Dispatchers.IO) {
            val web3 = getWeb3(rpcUrl)
            web3.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING)
                .send()
                .transactionCount
        }

    suspend fun sendRawTransaction(signedTx: String, rpcUrl: String): String =
        withContext(Dispatchers.IO) {
            val web3 = getWeb3(rpcUrl)
            val response = web3.ethSendRawTransaction(signedTx).send()
            if (response.hasError()) {
                throw RuntimeException("Transaction failed: ${response.error.message}")
            }
            response.transactionHash
        }
}
