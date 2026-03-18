package com.tranzo.wallet.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApi {

    @GET("api/v3/simple/price")
    suspend fun getTokenPrices(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String = "usd"
    ): Map<String, Map<String, Double>>
}
