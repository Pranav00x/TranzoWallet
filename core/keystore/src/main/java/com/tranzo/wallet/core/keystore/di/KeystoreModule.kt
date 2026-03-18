package com.tranzo.wallet.core.keystore.di

import com.tranzo.wallet.core.keystore.SecureStorageManager
import com.tranzo.wallet.core.keystore.SecureStorageManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KeystoreModule {

    @Binds
    @Singleton
    abstract fun bindSecureStorageManager(
        impl: SecureStorageManagerImpl
    ): SecureStorageManager
}
