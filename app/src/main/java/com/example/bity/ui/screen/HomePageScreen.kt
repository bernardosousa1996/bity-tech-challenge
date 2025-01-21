package com.example.bity.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bity.R
import com.example.bity.data.model.GasPriceResult
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.ui.components.StyledText
import com.example.bity.ui.screen.HomePageScreenTestTags.BALANCE
import com.example.bity.ui.screen.HomePageScreenTestTags.ETHEREUM_ADDRESS_FIELD
import com.example.bity.ui.screen.HomePageScreenTestTags.GAS_TRACKER
import com.example.bity.ui.screen.HomePageScreenTestTags.LOADING
import com.example.bity.ui.screen.HomePageScreenTestTags.SEARCH_BUTTON
import com.example.bity.ui.screen.HomePageScreenTestTags.TRANSACTION
import com.example.bity.ui.screen.HomePageScreenTestTags.TRANSACTION_AMOUNT
import com.example.bity.ui.screen.HomePageScreenTestTags.TRANSACTION_DATE
import com.example.bity.ui.screen.HomePageScreenTestTags.TRANSACTION_HASH
import com.example.bity.ui.screen.HomePageScreenTestTags.TRANSACTION_LIST
import com.example.bity.ui.theme.BityTheme
import com.example.bity.ui.viewmodel.HomePageUiState
import com.example.bity.utils.formatTimestamp
import com.example.bity.utils.weiToEth
import kotlinx.coroutines.delay

@Composable
fun HomePageScreen(
    uiState: HomePageUiState,
    gasPriceModel: GasPriceResult?,
    onSearchClicked: (String) -> Unit,
    isSearchClicked: Boolean = false,
    onClearError: () -> Unit,
    onLoadMoreTransactions: (String) -> Unit,
    onTransactionClicked: (TransactionDataModel) -> Unit,
) {
    var address by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberLazyListState()
    val transactions = when (uiState) {
        is HomePageUiState.Success -> uiState.transactions
        else -> emptyList()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            EthereumAddressInput(
                value = address,
                onValueChange = { address = it },
                onSearchClicked = {
                    onSearchClicked(address)
                },
                onClearField = {
                    onClearError()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchButton(
                onClick = {
                    onSearchClicked(address)
                },
                enabled = address.isNotBlank(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            GasPriceDisplay(gasPriceModel)

            when (uiState) {
                is HomePageUiState.Success -> {
                    if (isSearchClicked) {
                        Spacer(modifier = Modifier.height(16.dp))
                        BalanceDisplay(uiState.balance)
                        Spacer(modifier = Modifier.height(4.dp))
                        TransactionsList(
                            uiState.transactions ?: emptyList(),
                            onTransactionClicked,
                            scrollState
                        )
                    }
                }

                is HomePageUiState.Error -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.error, uiState.message),
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                is HomePageUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .testTag(LOADING)
                    )
                }

                is HomePageUiState.Empty -> {
                }
            }
        }
    }

    LaunchedEffect(scrollState, transactions) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .collect { lastVisibleItemIndex ->
                val totalItems = transactions?.size
                if (totalItems != null) {
                    if (totalItems > 0 && lastVisibleItemIndex >= totalItems - 5) {
                        onLoadMoreTransactions(address)
                    }
                }
            }
    }
}

@Composable
fun EthereumAddressInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearField: () -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.enter_ethereum_address)) },
        modifier = Modifier
            .fillMaxWidth()
            .testTag(ETHEREUM_ADDRESS_FIELD),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSearchClicked()
            }
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear text")
                }
            } else {
                onClearField()
            }
        }
    )
}

@Composable
fun SearchButton(onClick: () -> Unit, enabled: Boolean) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Button(
        onClick = {
            onClick()
            keyboardController?.hide()
        },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(SEARCH_BUTTON)
    ) {
        Text(stringResource(R.string.search))
    }
}

@Composable
fun BalanceDisplay(balance: String?) {
    balance?.let {
        val amount = weiToEth(balance)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_eth),
                contentDescription = "Ethereum Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.formated_eth, amount),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                modifier = Modifier.testTag(BALANCE)
            )
        }
    }
}


@Composable
fun GasPriceDisplay(gasPrice: GasPriceResult?) {
    var shouldAnimate by remember { mutableStateOf(false) }

    val flashColor by animateColorAsState(
        targetValue = if (shouldAnimate) Color.White else Color.Black,
        animationSpec = if (shouldAnimate) {
            infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse
            )
        } else {
            tween(1000)
        }
    )

    LaunchedEffect(gasPrice) {
        shouldAnimate = true
        delay(2500)
        shouldAnimate = false
    }
    gasPrice?.let {
        Column(modifier = Modifier.testTag(GAS_TRACKER)) {
            Text(
                text = stringResource(R.string.gas_price_low_gwei, it.safeGasPrice.toString()),
                color = flashColor,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(
                    R.string.gas_price_average_gwei,
                    it.proposeGasPrice.toString()
                ),
                color = flashColor,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(R.string.gas_price_high_gwei, it.fastGasPrice.toString()),
                color = flashColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TransactionsList(
    transactions: List<TransactionDataModel>,
    onTransactionClicked: (TransactionDataModel) -> Unit,
    scrollState: LazyListState
) {
    LazyColumn(
        state = scrollState,

        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .testTag(TRANSACTION_LIST),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction, onTransactionClicked)
        }
    }
}

@Composable
fun TransactionItem(
    transaction: TransactionDataModel,
    onTransactionClicked: (TransactionDataModel) -> Unit
) {
    val formattedDate = formatTimestamp(transaction.timeStamp)
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(durationMillis = 1000)),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag(TRANSACTION)
                .clickable { onTransactionClicked(transaction) },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val amount = weiToEth(transaction.value)
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Hash Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StyledText(
                        label = stringResource(R.string.tx_hash),
                        value = transaction.hash,
                        fontSize = 12,
                        testTag = TRANSACTION_HASH
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eth),
                        contentDescription = "Ethereum Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StyledText(
                        label = stringResource(R.string.amount),
                        value = "$amount ETH",
                        fontSize = 12,
                        testTag = TRANSACTION_AMOUNT
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Calendar Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StyledText(
                        label = stringResource(R.string.date),
                        value = formattedDate,
                        fontSize = 12,
                        testTag = TRANSACTION_DATE
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePageScreen() {

    BityTheme {
        val mockState = HomePageUiState.Success(
            balance = "1247350000000000000",
            transactions = listOf(
                TransactionDataModel(
                    hash = "0x123421398123912938912839812932139123881298312839",
                    value = "1000000000000000000",
                    timeStamp = "1628910000",
                    to = "BCA4...x02134"
                ),
                TransactionDataModel(
                    hash = "0x5678",
                    value = "0x12342139812391293891283981981298329381298312839",
                    timeStamp = "1628910200",
                    to = "BCA4...x02134"
                )
            )
        )

        HomePageScreen(
            uiState = mockState,
            gasPriceModel = GasPriceResult(
                safeGasPrice = "30",
                proposeGasPrice = "31.5",
                fastGasPrice = "32.3"
            ),
            onSearchClicked = { println("onSearchClicked") },
            onLoadMoreTransactions = {},
            isSearchClicked = true,
            onTransactionClicked = {},
            onClearError = {}
        )
    }
}

object HomePageScreenTestTags {
    const val ETHEREUM_ADDRESS_FIELD = "ethereumAddressField"
    const val SEARCH_BUTTON = "searchButton"
    const val BALANCE = "balance"
    const val TRANSACTION_LIST = "transactionList"
    const val TRANSACTION = "transaction"
    const val TRANSACTION_HASH = "transactionHash"
    const val TRANSACTION_AMOUNT = "transactionAmount"
    const val TRANSACTION_DATE = "transactionDate"
    const val GAS_TRACKER = "gasTracker"
    const val LOADING = "loading"
}