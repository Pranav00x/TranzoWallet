package com.tranzo.wallet.data.repository.usecase

import com.tranzo.wallet.core.crypto.KeyDerivation
import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.data.network.BlockchainService
import com.tranzo.wallet.data.repository.TransactionRepositoryImpl
import com.tranzo.wallet.domain.model.SendRequest
import com.tranzo.wallet.domain.usecase.SendTokenUseCase
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.utils.Numeric
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendTokenUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepositoryImpl,
    private val secureStorageManager: SecureStorageManager,
    private val blockchainService: BlockchainService
) : SendTokenUseCase {

    override suspend fun invoke(request: SendRequest, walletId: String): Result<String> {
        return try {
            val mnemonic = secureStorageManager.getMnemonic(walletId) 
                ?: return Result.failure(Exception("Wallet not found or locked"))
            
            val credentials = KeyDerivation.deriveEthereumCredentials(mnemonic)
            val rpcUrl = request.network.rpcUrl
            
            val nonce = blockchainService.getNonce(credentials.address, rpcUrl)
            val gasPrice = request.gasPrice ?: blockchainService.getGasPrice(rpcUrl)
            val gasLimit = request.gasLimit ?: BigInteger.valueOf(21_000)
            
            val amountInWei = request.amount.multiply(java.math.BigDecimal.TEN.pow(18)).toBigInteger()
            
            val rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gasPrice, gasLimit, request.toAddress, amountInWei
            )
            
            val signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials)
            val signedTxHex = Numeric.toHexString(signedMessage)
            
            val txHash = blockchainService.sendRawTransaction(signedTxHex, rpcUrl)
            Result.success(txHash)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
