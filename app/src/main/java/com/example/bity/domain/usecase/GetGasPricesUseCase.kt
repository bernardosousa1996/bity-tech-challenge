package com.example.bity.domain.usecase

import com.example.bity.data.model.GasPriceDataModel
import com.example.bity.data.repository.EtherscanRepository
import javax.inject.Inject

class GetGasPricesUseCase @Inject constructor(
    private val repository: EtherscanRepository
) {
    suspend operator fun invoke(apiKey: String): GasPriceDataModel? {
        val response = repository.getGasPrice(apiKey)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}