package com.example.bity.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bity.BuildConfig
import com.example.bity.data.model.GasPriceResult
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.domain.usecase.GetBalanceUseCase
import com.example.bity.domain.usecase.GetGasPricesUseCase
import com.example.bity.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val GAS_PRICE_TIMER: Long = 30_000

sealed class HomePageUiState {
    data object Empty : HomePageUiState()
    data object Loading : HomePageUiState()
    data class Success(
        val balance: String?,
        val transactions: List<TransactionDataModel>?
    ) : HomePageUiState()

    data class Error(val message: String) : HomePageUiState()
}

@HiltViewModel
open class EtherscanViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getGasPricesUseCase: GetGasPricesUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val apiKey = BuildConfig.ETHERSCAN_API_KEY
    private var currentPage = 1

    private val _uiState = MutableStateFlow<HomePageUiState>(HomePageUiState.Empty)
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()

    private val _selectedTransaction = MutableStateFlow<TransactionDataModel?>(null)
    val selectedTransaction: StateFlow<TransactionDataModel?> = _selectedTransaction

    private val _gasPriceModel =
        MutableStateFlow<GasPriceResult?>(null)
    val gasPriceModel: StateFlow<GasPriceResult?> = _gasPriceModel

    private val _isSearchClicked =
        MutableStateFlow(false)
    val isSearchClicked: StateFlow<Boolean> = _isSearchClicked

    init {
        fetchGasPriceEvery30Seconds()
    }

    private fun fetchGasPriceEvery30Seconds() {
        viewModelScope.launch {
            while (true) {
                fetchGasPrice()
                delay(GAS_PRICE_TIMER)
            }
        }
    }

    fun selectTransaction(transaction: TransactionDataModel) {
        _selectedTransaction.value = transaction
    }

    fun fetchBalanceAndTransactions(address: String) {
        viewModelScope.launch {
            updateUiState(HomePageUiState.Loading)
            try {
                val balance = getBalanceUseCase(address, apiKey)?.result
                val transactions = getTransactionsUseCase(address, apiKey, 1)?.result
                updateUiState(
                    HomePageUiState.Success(
                        balance = balance,
                        transactions = transactions
                    )
                )
                _isSearchClicked.value = true
            } catch (e: Exception) {
                updateUiState(HomePageUiState.Error("Failed to load data"))
            }
        }
    }

    private fun updateUiState(state: HomePageUiState) {
        _uiState.value = state
    }

    fun clearError() {
        updateUiState(HomePageUiState.Empty)
        _isSearchClicked.value = false
    }

    private fun fetchGasPrice() {
        viewModelScope.launch {
            try {
                val gasPrice = getGasPricesUseCase(apiKey)
                _gasPriceModel.value = gasPrice?.result
            } catch (e: Exception) {
                _gasPriceModel.value = null
            }
        }
    }

    fun loadMoreTransactions(address: String) {
        currentPage += 1
        viewModelScope.launch {
            val newTransactions = getTransactionsUseCase(address, apiKey, currentPage)?.result
            val currentState = _uiState.value
            updateUiState(
                if (currentState is HomePageUiState.Success) {
                    val updatedList = (currentState.transactions ?: emptyList()) + (newTransactions
                        ?: emptyList())
                    currentState.copy(transactions = updatedList)
                } else {
                    currentState
                }
            )
        }
    }
}

