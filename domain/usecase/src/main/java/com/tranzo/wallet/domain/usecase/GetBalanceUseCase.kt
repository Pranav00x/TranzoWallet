package com.tranzo.wallet.domain.usecase

import com.tranzo.wallet.domain.model.Network
import com.tranzo.wallet.domain.model.Token

interface GetBalanceUseCase {

    suspend operator fun invoke(address: String, network: Network): Result<List<Token>>
}
