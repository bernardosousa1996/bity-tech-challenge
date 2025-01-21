package com.example.bity.ui


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.ui.screen.TransactionDetailScreen
import com.example.bity.ui.screen.TransactionDetailTestTags
import org.junit.Rule
import org.junit.Test

class TransactionDetailScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shouldShowTransactionDetailsCorrectly() {
        val transaction = TransactionDataModel(
            timeStamp = "1737331200",
            hash = "0x123456789",
            to = "0xdef456",
            value = "1000000000000000000"
        )

        composeTestRule.setContent {
            TransactionDetailScreen(
                transaction = transaction,
                onBackPressed = { }
            )
        }

        composeTestRule.onNodeWithTag(TransactionDetailTestTags.TRANSACTION_HASH).assertIsDisplayed()
            //.assertTextEquals("Transaction Hash: 0x123456789")
        composeTestRule.onNodeWithTag(TransactionDetailTestTags.DATE)
            .assertTextEquals("Date: Jan-20-2025 12:00:00 AM")
        composeTestRule.onNodeWithTag(TransactionDetailTestTags.DESTINATION)
            .assertTextEquals("To: 0xdef456")
        composeTestRule.onNodeWithTag(TransactionDetailTestTags.AMOUNT)
            .assertTextEquals("1.00000 ETH")
    }
}
