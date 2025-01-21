package com.example.bity.data.ui.viewmodel

import com.example.bity.data.model.BalanceDataModel
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.data.model.TransactionListDataModel
import com.example.bity.domain.usecase.GetBalanceUseCase
import com.example.bity.domain.usecase.GetTransactionsUseCase
import com.example.bity.ui.viewmodel.EtherscanViewModel
import com.example.bity.ui.viewmodel.HomePageUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.eq

class EtherscanViewModelTest {

    private lateinit var viewModel: EtherscanViewModel
    private val mockBalanceUseCase = mockk<GetBalanceUseCase>()
    private val mockTransactionsUseCase = mockk<GetTransactionsUseCase>()
    private val address = "12345"

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel = EtherscanViewModel(
            getBalanceUseCase = mockBalanceUseCase,
            getGasPricesUseCase = mockk(),
            getTransactionsUseCase = mockTransactionsUseCase,
            application = mockk(),
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchBalanceAndTransactions should fetch correct data`() = runBlockingTest {
        val expectedBalance = 100
        val expectedTransactions = listOf(
            TransactionDataModel(timeStamp = "1", hash = "0x123", to = "0x456", value = "10"),
            TransactionDataModel(timeStamp = "2", hash = "0x124", to = "0x126", value = "13"),
        )
        coEvery { mockBalanceUseCase.invoke(eq(address), any()) } returns BalanceDataModel(
            status = "1",
            message = "Success",
            result = "100"
        )
        coEvery {
            mockTransactionsUseCase.invoke(
                eq(address),
                any(),
                any()
            )
        } returns TransactionListDataModel(
            status = "1",
            message = "Success",
            result = listOf(
                TransactionDataModel(timeStamp = "1", hash = "0x123", to = "0x456", value = "10"),
                TransactionDataModel(timeStamp = "2", hash = "0x124", to = "0x126", value = "13"),
            )
        )

        viewModel.fetchBalanceAndTransactions(eq(address))

        val uiState = viewModel.uiState.value

        assert(uiState is HomePageUiState.Success)
        val successState = uiState as HomePageUiState.Success
        assertEquals(expectedBalance, 100)
        assertEquals(expectedTransactions, successState.transactions)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchBalanceAndTransactions should have error handling`() = runBlockingTest {
        coEvery { mockBalanceUseCase.invoke(eq(address), any()) } throws Exception("Network error")

        viewModel.fetchBalanceAndTransactions(eq(address))

        assert(viewModel.uiState.value is HomePageUiState.Error)
        val errorState = viewModel.uiState.value as HomePageUiState.Error
        assertEquals("Failed to load data", errorState.message)
    }

    @Test
    fun `selectTransaction should update selectedTransaction state`() {
        val transaction = TransactionDataModel("tx1", "0x123", "1234from", "2025-01-01")

        viewModel.selectTransaction(transaction)

        assertEquals(transaction, viewModel.selectedTransaction.value)
    }

    @Test
    fun `clearError should change state to Empty`() {
        viewModel.clearError()

        assertEquals(HomePageUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `loadMoreTransactions should add new transactions`() {
        val initialTransactions = listOf(
            TransactionDataModel("tx1", "0x123", "1234from", "2025-01-01")
        )
        val newTransactions = listOf(
            TransactionDataModel("tx2", "0x456", "5678from", "2025-01-02")
        )
        val totalTransactions = initialTransactions.plus(newTransactions)

        coEvery { mockBalanceUseCase.invoke(eq(address), any()) } returns BalanceDataModel(
            status = "1",
            message = "Success",
            result = "100"
        )

        coEvery {
            mockTransactionsUseCase.invoke(
                eq(address),
                any(),
                1
            )
        } returns TransactionListDataModel(
            status = "1",
            message = "Success",
            result = initialTransactions,
        )

        coEvery {
            mockTransactionsUseCase.invoke(
                eq(address),
                any(),
                2
            )
        } returns TransactionListDataModel(
            status = "1",
            message = "Success",
            result = newTransactions,
        )

        viewModel.fetchBalanceAndTransactions(eq(address))
        viewModel.loadMoreTransactions(eq(address))

        val successState = viewModel.uiState.value as HomePageUiState.Success

        assertEquals(totalTransactions, successState.transactions)
    }
}