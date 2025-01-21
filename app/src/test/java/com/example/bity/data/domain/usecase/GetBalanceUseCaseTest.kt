package com.example.bity.data.domain.usecase

import com.example.bity.data.model.BalanceDataModel
import com.example.bity.domain.usecase.GetBalanceUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.eq

class GetBalanceUseCaseTest {

    @Test
    fun `test GetBalanceUseCase returns data correctly`() = runTest {
        val address = "12345"
        val apiKey = "apiKey"
        val mockBalanceUseCase = mockk<GetBalanceUseCase>()
        val balanceData = BalanceDataModel(status = "1", message = "Success", result = "100")

        coEvery { mockBalanceUseCase.invoke(eq(address), any()) } returns balanceData

        val result = mockBalanceUseCase.invoke(address, eq(apiKey))

        assertEquals("100", result?.result)
        assertEquals("Success", result?.message)
    }
}