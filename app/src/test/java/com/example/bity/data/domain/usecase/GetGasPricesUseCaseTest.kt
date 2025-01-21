package com.example.bity.data.domain.usecase

import com.example.bity.data.model.GasPriceDataModel
import com.example.bity.data.model.GasPriceResult
import com.example.bity.data.repository.EtherscanRepository
import com.example.bity.domain.usecase.GetGasPricesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class GetGasPricesUseCaseTest {

    @Test
    fun `test GetGasPricesUseCase returns gas price data correctly`() = runTest {
        val apiKey = "apiKey"
        val mockRepository = mockk<EtherscanRepository>()
        val mockGasPriceData = GasPriceDataModel(
            status = "1",
            message = "Success",
            result = GasPriceResult(
                safeGasPrice = "10",
                proposeGasPrice = "20",
                fastGasPrice = "30"
            )
        )
        coEvery { mockRepository.getGasPrice(eq(apiKey)) } returns Response.success(mockGasPriceData)


        val getGasPricesUseCase = GetGasPricesUseCase(repository = mockRepository)

        val result = getGasPricesUseCase.invoke(apiKey)

        assertEquals("10", result?.result?.safeGasPrice)
        assertEquals("20", result?.result?.proposeGasPrice)
        assertEquals("30", result?.result?.fastGasPrice)
    }

    @Test
    fun `test GetGasPricesUseCase returns null when response is not successful`() = runTest {
        val apiKey = "apiKey"
        val mockRepository = mockk<EtherscanRepository>()
        coEvery { mockRepository.getGasPrice(eq(apiKey)) } returns Response.error(
            500,
            "Error".toResponseBody()
        )

        val getGasPricesUseCase = GetGasPricesUseCase(repository = mockRepository)

        val result = getGasPricesUseCase.invoke(apiKey)

        assertEquals(null, result)
    }
}