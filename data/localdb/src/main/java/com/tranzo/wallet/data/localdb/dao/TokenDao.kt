package com.tranzo.wallet.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tranzo.wallet.data.localdb.entity.TokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tokens: List<TokenEntity>)

    @Query("SELECT * FROM tokens WHERE chainId = :chainId")
    fun getByChainId(chainId: Long): Flow<List<TokenEntity>>

    @Query("DELETE FROM tokens WHERE contractAddress = :contractAddress AND chainId = :chainId")
    suspend fun delete(contractAddress: String, chainId: Long)
}
