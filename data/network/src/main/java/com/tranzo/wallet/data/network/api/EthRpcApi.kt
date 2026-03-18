package com.tranzo.wallet.data.network.api

import retrofit2.http.Body
import retrofit2.http.POST

interface EthRpcApi {

    @POST(".")
    suspend fun call(@Body request: JsonRpcRequest): JsonRpcResponse
}

data class JsonRpcRequest(
    val jsonrpc: String = "2.0",
    val method: String,
    val params: List<Any>,
    val id: Int = 1
)

data class JsonRpcResponse(
    val jsonrpc: String,
    val id: Int,
    val result: Any?,
    val error: JsonRpcError?
)

data class JsonRpcError(
    val code: Int,
    val message: String
)
