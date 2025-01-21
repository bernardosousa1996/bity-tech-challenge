package com.example.bity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bity.data.model.GasPriceResult
import com.example.bity.ui.screen.HomePageScreen
import com.example.bity.ui.screen.TransactionDetailScreen
import com.example.bity.ui.theme.BityTheme
import com.example.bity.ui.viewmodel.EtherscanViewModel
import com.example.bity.ui.viewmodel.HomePageUiState
import dagger.hilt.android.AndroidEntryPoint

object Screen {
    const val HOME = "home"
    const val TRANSACTION_DETAIL = "transactionDetail"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val etherscanViewModel: EtherscanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BityTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.HOME) {
                        composable(Screen.HOME) {
                            val uiState = etherscanViewModel.uiState.collectAsState().value
                            val gasPriceModel =
                                etherscanViewModel.gasPriceModel.collectAsState().value

                            HomePageScreen(
                                uiState = uiState,
                                onSearchClicked = { address ->
                                    etherscanViewModel.fetchBalanceAndTransactions(address)
                                },
                                isSearchClicked = etherscanViewModel.isSearchClicked.collectAsState().value,
                                onClearError = { etherscanViewModel.clearError() },
                                onTransactionClicked = { transaction ->
                                    etherscanViewModel.selectTransaction(transaction)
                                    navController.navigate(Screen.TRANSACTION_DETAIL) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onLoadMoreTransactions = { address ->
                                    etherscanViewModel.loadMoreTransactions(address)
                                },
                                gasPriceModel = gasPriceModel
                            )
                        }
                        composable(Screen.TRANSACTION_DETAIL) {
                            val transaction =
                                etherscanViewModel.selectedTransaction.collectAsState().value

                            if (transaction != null) {
                                TransactionDetailScreen(
                                    transaction = transaction,
                                    onBackPressed = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BityTheme {
        val uiState = HomePageUiState.Loading
        HomePageScreen(
            uiState = uiState,
            onSearchClicked = {},
            onTransactionClicked = {},
            onLoadMoreTransactions = {},
            onClearError = {},
            gasPriceModel = GasPriceResult("10", "20", "30")
        )
    }
}