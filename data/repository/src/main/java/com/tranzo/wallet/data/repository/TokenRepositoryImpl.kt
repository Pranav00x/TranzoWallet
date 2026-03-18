package com.tranzo.wallet.data.repository

import com.tranzo.wallet.data.localdb.dao.TokenDao
import com.tranzo.wallet.data.localdb.entity.TokenEntity
import com.tranzo.wallet.data.network.BlockchainService
import com.tranzo.wallet.data.network.api.CoinGeckoApi
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val blockchainService: BlockchainService,
    private val coinGeckoApi: CoinGeckoApi,
    private val tokenDao: TokenDao
) {

    suspend fun getTokens(address: String, network: Network): List<Token> =
        withContext(Dispatchers.IO) {
            val cachedTokens = tokenDao.getByChainId(network.chainId).first()

            val nativeBalance = try {
                val balanceWei = blockchainService.getBalance(address, network.rpcUrl)
                BigDecimal(balanceWei).divide(BigDecimal.TEN.pow(18), MathContext.DECIMAL128)
            } catch (e: Exception) {
                BigDecimal.ZERO
            }

            val nativePrice = try {
                val prices = coinGeckoApi.getTokenPrices(
                    ids = network.name.lowercase(),
                    vsCurrencies = "usd"
                )
                prices[network.name.lowercase()]?.get("usd") ?: 0.0
            } catch (e: Exception) {
                0.0
            }

            val nativeToken = Token(
                contractAddress = "",
                symbol = network.symbol,
                name = network.name,
                decimals = 18,
                balance = nativeBalance,
                priceUsd = nativePrice,
                isNative = true
            )

            val otherTokens = cachedTokens.map { entity ->
                Token(
                    contractAddress = entity.contractAddress,
                    symbol = entity.symbol,
                    name = entity.name,
                    decimals = entity.decimals,
                    iconUrl = entity.iconUrl,
                    isNative = entity.isNative
                )
            }

            listOf(nativeToken) + otherTokens
        }
}
