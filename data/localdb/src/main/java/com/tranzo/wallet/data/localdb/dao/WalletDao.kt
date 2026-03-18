package com.tranzo.wallet.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tranzo.wallet.data.localdb.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: WalletEntity)

    @Query("SELECT * FROM wallets ORDER BY createdAt DESC")
    fun getAll(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets WHERE isActive = 1 LIMIT 1")
    suspend fun getActive(): WalletEntity?

    @Query("UPDATE wallets SET isActive = CASE WHEN id = :walletId THEN 1 ELSE 0 END")
    suspend fun setActive(walletId: String)

    @Query("DELETE FROM wallets WHERE id = :walletId")
    suspend fun delete(walletId: String)
}
