package com.example.bity.data.domain.usecase

import com.example.bity.data.model.TransactionDataModel
import com.example.bity.data.model.TransactionListDataModel
import com.example.bity.data.repository.EtherscanRepository
import com.example.bity.domain.usecase.GetTransactionsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class GetTransactionsUseCaseTest {

    @Test
    fun `test GetTransactionsUseCase returns transactions list correctly`() = runTest {
        val address = "0x12345"
        val apiKey = "apiKey"
        val page = 1
        val mockRepository = mockk<EtherscanRepository>()
        val mockTransactionList = TransactionListDataModel(
            status = "1",
            message = "Success",
            result = listOf(
                TransactionDataModel(
                    timeStamp = "123123",
                    hash = "0x1234",
                    to = "0xtoAddress1",
                    value = "100"
                ),
                TransactionDataModel(
                    timeStamp = "123121233",
                    hash = "0x3214",
                    to = "0xtoAddress2",
                    value = "200"
                )
            )
        )

        coEvery {
            mockRepository.getTransactions(
                eq(address),
                eq(apiKey),
                eq(page)
            )
        } returns Response.success(mockTransactionList)

        val getTransactionsUseCase = GetTransactionsUseCase(repository = mockRepository)

        val result = getTransactionsUseCase.invoke(address, apiKey, page)

        assertEquals(mockTransactionList, result)
    }

    @Test
    fun `test GetTransactionsUseCase returns null when response is not successful`() = runTest {
        val address = "0x12345"
        val apiKey = "apiKey"
        val page = 1
        val mockRepository = mockk<EtherscanRepository>()

        coEvery {
            mockRepository.getTransactions(
                eq(address),
                eq(apiKey),
                eq(page)
            )
        } returns Response.error(500, "Error".toResponseBody())

        val getTransactionsUseCase = GetTransactionsUseCase(repository = mockRepository)

        val result = getTransactionsUseCase.invoke(address, apiKey, page)

        assertEquals(null, result)
    }
}