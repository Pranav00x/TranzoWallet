package com.tranzo.wallet.data.repository.di

import com.tranzo.wallet.data.repository.TokenRepositoryImpl
import com.tranzo.wallet.data.repository.TransactionRepositoryImpl
import com.tranzo.wallet.data.repository.WalletRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideWalletRepository(impl: WalletRepositoryImpl): WalletRepositoryImpl {
        return impl
    }

    @Provides
    @Singleton
    fun provideTokenRepository(impl: TokenRepositoryImpl): TokenRepositoryImpl {
        return impl
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepositoryImpl {
        return impl
    }
}
