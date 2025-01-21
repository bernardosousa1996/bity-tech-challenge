package com.example.bity.domain.usecase

import com.example.bity.data.model.TransactionListDataModel
import com.example.bity.data.repository.EtherscanRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: EtherscanRepository
) {
    suspend operator fun invoke(address: String, apiKey: String, page: Int): TransactionListDataModel? {
        val response = repository.getTransactions(address, apiKey, page)
        return if (response.isSuccessful) response.body() else null
    }
}