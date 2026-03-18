package com.tranzo.wallet.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tranzo.wallet.data.localdb.dao.TokenDao
import com.tranzo.wallet.data.localdb.dao.TransactionDao
import com.tranzo.wallet.data.localdb.dao.WalletDao
import com.tranzo.wallet.data.localdb.entity.TokenEntity
import com.tranzo.wallet.data.localdb.entity.TransactionEntity
import com.tranzo.wallet.data.localdb.entity.WalletEntity

@Database(
    entities = [WalletEntity::class, TokenEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TranzoDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    abstract fun tokenDao(): TokenDao

    abstract fun transactionDao(): TransactionDao
}
