package com.tranzo.wallet.domain.usecase

import com.tranzo.wallet.domain.model.SendRequest

interface SendTokenUseCase {

    suspend operator fun invoke(request: SendRequest, walletId: String): Result<String>
}
