package com.example.bity.domain.usecase

import android.util.Log
import com.example.bity.data.model.BalanceDataModel
import com.example.bity.data.repository.EtherscanRepository
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: EtherscanRepository
) {
    suspend operator fun invoke(address: String, apiKey: String): BalanceDataModel? {
        val response = repository.getBalance(address, apiKey)
        return if (response.isSuccessful) response.body() else null
    }
}