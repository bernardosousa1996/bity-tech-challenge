package com.example.bity.data.api

import com.example.bity.data.model.BalanceDataModel
import com.example.bity.data.model.GasPriceDataModel
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.data.model.TransactionListDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EtherscanApi {

    @GET("api")
    suspend fun getTransactionList(
        @Query("module") module: String = "account",
        @Query("action") action: String = "txlist",
        @Query("address") address: String,
        @Query("startblock") startBlock: Int = 0,
        @Query("endblock") endBlock: Int = 99999999,
        @Query("sort") sort: String = "desc",
        @Query("apikey") apiKey: String,
        @Query("offset") offset: Int = 10,
        @Query("page") page: Int = 1
    ): Response<TransactionListDataModel>

    @GET("api")
    suspend fun getBalance(
        @Query("module") module: String = "account",
        @Query("action") action: String = "balance",
        @Query("address") address: String,
        @Query("tag") tag: String = "latest",
        @Query("apikey") apiKey: String
    ): Response<BalanceDataModel> {
        return getBalance(module, action, address, tag, apiKey)
    }

    @GET("v2/api")
    suspend fun getGasPrice(
        @Query("chainid") chainId: String = "1",
        @Query("module") module: String = "gastracker",
        @Query("action") action: String = "gasoracle",
        @Query("apikey") apiKey: String
    ): Response<GasPriceDataModel>


    @GET("api")
    suspend fun getTransactionDetails(
        @Query("module") module: String = "proxy",
        @Query("action") action: String = "eth_getTransactionByHash",
        @Query("txhash") transactionHash: String,
        @Query("apikey") apiKey: String
    ): Response<TransactionDataModel>
}