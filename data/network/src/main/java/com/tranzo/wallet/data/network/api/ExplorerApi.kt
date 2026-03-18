package com.tranzo.wallet.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ExplorerApi {

    @GET("api")
    suspend fun getTransactions(
        @Query("module") module: String = "account",
        @Query("action") action: String = "txlist",
        @Query("address") address: String,
        @Query("startblock") startBlock: Long = 0,
        @Query("sort") sort: String = "desc",
        @Query("apikey") apiKey: String = ""
    ): ExplorerResponse
}

data class ExplorerResponse(
    val status: String,
    val message: String,
    val result: List<ExplorerTransaction>
)

data class ExplorerTransaction(
    val hash: String,
    val from: String,
    val to: String,
    val value: String,
    val gasPrice: String,
    val gasUsed: String,
    val timeStamp: String,
    val isError: String,
    val blockNumber: String
)
