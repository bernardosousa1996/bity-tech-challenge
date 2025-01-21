package com.example.bity.data.repository

import com.example.bity.data.api.EtherscanApi
import com.example.bity.data.model.BalanceDataModel
import com.example.bity.data.model.GasPriceDataModel
import com.example.bity.data.model.GasPriceResult
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.data.model.TransactionListDataModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class EtherscanRepositoryTest {

    private lateinit var repository: EtherscanRepository
    private val mockApi = mockk<EtherscanApi>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = EtherscanRepository(mockApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getBalance returns correct data when API call is successful`() = runTest {
        val expectedResponse = Response.success(BalanceDataModel("1", "Success", "100"))
        coEvery {
            mockApi.getBalance(
                apiKey = any(),
                address = any()
            )
        } returns expectedResponse

        val result = repository.getBalance("12345", "apiKey")

        assertEquals(expectedResponse, result)
        assertEquals("100", result.body()?.result)
    }

    @Test
    fun `getBalance returns null when API call fails`() = runTest {
        coEvery {
            mockApi.getBalance(
                address = any(),
                apiKey = any()
            )
        } returns Response.error(500, "".toResponseBody())

        val result = repository.getBalance("12345", "apiKey")

        assertFalse(result.isSuccessful)
    }

    @Test
    fun `getGasPrice returns correct data when API call is successful`() = runTest {
        val expectedResponse = Response.success(
            GasPriceDataModel(
                status = "1",
                message = "Success",
                result = GasPriceResult(
                    safeGasPrice = "30",
                    proposeGasPrice = "31",
                    fastGasPrice = "32"
                )
            )
        )
        coEvery {
            mockApi.getGasPrice(
                apiKey = any()
            )
        } returns expectedResponse

        val result = repository.getGasPrice("apiKey")

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `getGasPrice returns null when API call fails`() = runTest {
        coEvery {
            mockApi.getGasPrice(
                apiKey = any()
            )
        } returns Response.error(500, "".toResponseBody())

        val result = repository.getGasPrice("apiKey")

        assertFalse(result.isSuccessful)
    }

    @Test
    fun `getTransactionList returns correct data when API call is successful`() = runTest {
        val expectedResponse = Response.success(
            TransactionListDataModel(
                status = "1",
                message = "Success",
                result = listOf(
                    TransactionDataModel("1234", "0x1234", "BCA40DEF", "1000")
                )
            )
        )
        coEvery {
            mockApi.getTransactionList(
                address = any(),
                apiKey = any()
            )
        } returns expectedResponse

        val result = repository.getTransactions("Address", "apiKey", 1)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `getTransactionList returns null when API call fails`() = runTest {
        coEvery {
            mockApi.getTransactionList(
                address = any(),
                apiKey = any()
            )
        } returns Response.error(500, "".toResponseBody())

        val result = repository.getTransactions("address", "apiKey", 1)

        assertFalse(result.isSuccessful)
    }
}