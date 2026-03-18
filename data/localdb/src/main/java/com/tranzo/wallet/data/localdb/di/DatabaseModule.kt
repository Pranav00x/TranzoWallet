package com.tranzo.wallet.data.localdb.di

import android.content.Context
import androidx.room.Room
import com.tranzo.wallet.data.localdb.TranzoDatabase
import com.tranzo.wallet.data.localdb.dao.TokenDao
import com.tranzo.wallet.data.localdb.dao.TransactionDao
import com.tranzo.wallet.data.localdb.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TranzoDatabase {
        return Room.databaseBuilder(
            context,
            TranzoDatabase::class.java,
            "tranzo_wallet.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWalletDao(database: TranzoDatabase): WalletDao {
        return database.walletDao()
    }

    @Provides
    @Singleton
    fun provideTokenDao(database: TranzoDatabase): TokenDao {
        return database.tokenDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: TranzoDatabase): TransactionDao {
        return database.transactionDao()
    }
}
