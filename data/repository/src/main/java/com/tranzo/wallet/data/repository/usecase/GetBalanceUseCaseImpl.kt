package com.tranzo.wallet.data.repository.usecase

import com.tranzo.wallet.data.repository.TokenRepositoryImpl
import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.Token
import com.tranzo.wallet.domain.usecase.GetBalanceUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBalanceUseCaseImpl @Inject constructor(
    private val tokenRepository: TokenRepositoryImpl
) : GetBalanceUseCase {

    override suspend fun invoke(address: String, network: Network): Result<List<Token>> {
        return try {
            val tokens = tokenRepository.getTokens(address, network)
            Result.success(tokens)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
