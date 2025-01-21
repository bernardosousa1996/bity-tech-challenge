package com.example.bity.data.repository

import com.example.bity.data.api.EtherscanApi
import com.example.bity.data.model.BalanceDataModel
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
}