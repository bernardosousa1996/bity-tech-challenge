package com.example.bity.data.repository

import com.example.bity.data.api.EtherscanApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class EtherscanRepository @Inject constructor(
    private val etherscanApi: EtherscanApi
) {
    open suspend fun getBalance(address: String, apiKey: String) =
        etherscanApi.getBalance(address = address, apiKey = apiKey)

    suspend fun getTransactions(address: String, apiKey: String, page: Int) =
        etherscanApi.getTransactionList(address = address, apiKey = apiKey, page = page)

    suspend fun getGasPrice(apiKey: String) =
        etherscanApi.getGasPrice(apiKey = apiKey)

    suspend fun getTransactionDetails(transactionHash: String, apiKey: String) =
        etherscanApi.getTransactionDetails(transactionHash = transactionHash, apiKey = apiKey)
}