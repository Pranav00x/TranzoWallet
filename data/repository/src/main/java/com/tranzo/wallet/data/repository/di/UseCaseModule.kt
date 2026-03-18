package com.tranzo.wallet.data.repository.di

import com.tranzo.wallet.data.repository.usecase.GetBalanceUseCaseImpl
import com.tranzo.wallet.data.repository.usecase.SendTokenUseCaseImpl
import com.tranzo.wallet.domain.usecase.GetBalanceUseCase
import com.tranzo.wallet.domain.usecase.SendTokenUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetBalanceUseCase(impl: GetBalanceUseCaseImpl): GetBalanceUseCase

    @Binds
    @Singleton
    abstract fun bindSendTokenUseCase(impl: SendTokenUseCaseImpl): SendTokenUseCase
}
