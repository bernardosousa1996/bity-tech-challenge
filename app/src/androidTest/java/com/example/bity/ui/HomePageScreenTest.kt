package com.example.bity.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.bity.data.model.GasPriceResult
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.ui.screen.HomePageScreen
import com.example.bity.ui.screen.HomePageScreenTestTags
import com.example.bity.ui.viewmodel.HomePageUiState
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class HomePageScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockGasPrice = mockk<GasPriceResult>(relaxed = true)

    @Test
    fun shouldShowSearchButtonEnabledWhenAddressIsNotEmpty() {
        val uiState = HomePageUiState.Success(
            balance = "1000000000000000000",
            transactions = emptyList()
        )

        composeTestRule.setContent {
            HomePageScreen(
                uiState = uiState,
                gasPriceModel = mockGasPrice,
                onSearchClicked = {},
                onClearError = {},
                onLoadMoreTransactions = {},
                onTransactionClicked = {}
            )
        }

        composeTestRule.onNodeWithTag(HomePageScreenTestTags.ETHEREUM_ADDRESS_FIELD)
            .performTextInput("0x1234567890")
        composeTestRule.onNodeWithTag(HomePageScreenTestTags.SEARCH_BUTTON).assertIsEnabled()
    }

    @Test
    fun shouldShowSearchButtonDisabledWhenAddressIsEmpty() {
        val uiState = HomePageUiState.Success(
            balance = "1000000000000000000",
            transactions = emptyList()
        )

        composeTestRule.setContent {
            HomePageScreen(
                uiState = uiState,
                gasPriceModel = mockGasPrice,
                onSearchClicked = {},
                onClearError = {},
                onLoadMoreTransactions = {},
                onTransactionClicked = {}
            )
        }

        composeTestRule.onNodeWithTag(HomePageScreenTestTags.ETHEREUM_ADDRESS_FIELD)
            .performTextInput("")
        composeTestRule.onNodeWithTag(HomePageScreenTestTags.SEARCH_BUTTON).assertIsNotEnabled()
    }

    @Test
    fun shouldShowBalanceDisplayCorrectAmount() {
        val uiState = HomePageUiState.Success(
            balance = "1000000000000000000",
            transactions = emptyList()
        )

        composeTestRule.setContent {
            HomePageScreen(
                uiState = uiState,
                gasPriceModel = mockGasPrice,
                onSearchClicked = { },
                isSearchClicked = true,
                onClearError = { },
                onLoadMoreTransactions = { },
                onTransactionClicked = { }
            )
        }

        composeTestRule.onNodeWithTag(HomePageScreenTestTags.ETHEREUM_ADDRESS_FIELD)
            .performTextInput("0x1234567890")
        composeTestRule.onNodeWithTag(HomePageScreenTestTags.SEARCH_BUTTON)
            .performClick()
        composeTestRule.onNodeWithTag(HomePageScreenTestTags.BALANCE)
            .assertTextEquals("1.00000 ETH")
    }

    @Test
    fun shouldShowTransactionList() {
        val uiState = HomePageUiState.Success(
            balance = "1000000000000000000",
            transactions = listOf(
                TransactionDataModel(
                    timeStamp = "1737331201",
                    hash = "0x123456789",
                    to = "0xdsq123",
                    value = "1000000"
                ),
                TransactionDataModel(
                    timeStamp = "1737331200",
                    hash = "0x987654321",
                    to = "0xdef456",
                    value = "200000"
                )
            ),
        )

        composeTestRule.setContent {
            HomePageScreen(
                uiState = uiState,
                gasPriceModel = mockGasPrice,
                onSearchClicked = { },
                isSearchClicked = true,
                onClearError = { },
                onLoadMoreTransactions = { },
                onTransactionClicked = { }
            )
        }

        composeTestRule.onNodeWithTag(HomePageScreenTestTags.ETHEREUM_ADDRESS_FIELD)
            .performTextInput("0x1234567890")
        composeTestRule.onNodeWithTag(HomePageScreenTestTags.SEARCH_BUTTON)
            .performClick()
        composeTestRule.onNodeWithTag(HomePageScreenTestTags.BALANCE)
            .assertTextEquals("1.00000 ETH")
        composeTestRule.onNodeWithTag(HomePageScreenTestTags.TRANSACTION_LIST)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Transaction Hash: 0x123456789")
        composeTestRule.onNodeWithText("Transaction Hash: 0x987654321")
    }

    @Test
    fun shouldDisplayGasPrices() {
        val gasPriceResult = GasPriceResult(
            safeGasPrice = "30",
            proposeGasPrice = "31.5",
            fastGasPrice = "32.3"
        )

        composeTestRule.setContent {
            HomePageScreen(
                uiState = HomePageUiState.Success(
                    balance = "1000000000000000000",
                    transactions = emptyList()
                ),
                gasPriceModel = gasPriceResult,
                onSearchClicked = {},
                onClearError = {},
                onLoadMoreTransactions = {},
                onTransactionClicked = {}
            )
        }

        composeTestRule.onNodeWithTag(HomePageScreenTestTags.GAS_TRACKER).assertIsDisplayed()
        composeTestRule.onNodeWithText("Low: 30 GWEI")
        composeTestRule.onNodeWithText("Average: 31.5 GWEI")
        composeTestRule.onNodeWithText("High: 32.3 GWEI")
    }

    @Test
    fun shouldShowErrorMessage() {
        val uiState = HomePageUiState.Error(message = "Something went wrong")

        composeTestRule.setContent {
            HomePageScreen(
                uiState = uiState,
                gasPriceModel = mockGasPrice,
                onSearchClicked = {},
                onClearError = {},
                onLoadMoreTransactions = {},
                onTransactionClicked = {}
            )
        }

        composeTestRule.onNodeWithText("Error: Something went wrong").assertIsDisplayed()
    }

    @Test
    fun showShowLoadingIndicator() {
        val uiState = HomePageUiState.Loading

        composeTestRule.setContent {
            HomePageScreen(
                uiState = uiState,
                gasPriceModel = mockGasPrice,
                onSearchClicked = {},
                onClearError = {},
                onLoadMoreTransactions = {},
                onTransactionClicked = {}
            )
        }

        composeTestRule.onNodeWithTag(HomePageScreenTestTags.LOADING).assertIsDisplayed()
    }
}