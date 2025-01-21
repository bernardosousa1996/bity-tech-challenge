package com.example.bity.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bity.R
import com.example.bity.data.model.TransactionDataModel
import com.example.bity.ui.components.StyledText
import com.example.bity.ui.screen.TransactionDetailTestTags.AMOUNT
import com.example.bity.ui.screen.TransactionDetailTestTags.DATE
import com.example.bity.ui.screen.TransactionDetailTestTags.DESTINATION
import com.example.bity.ui.screen.TransactionDetailTestTags.TRANSACTION_HASH
import com.example.bity.utils.formatTimestamp
import com.example.bity.utils.weiToEth

@Composable
fun TransactionDetailScreen(
    transaction: TransactionDataModel,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val amount = weiToEth(transaction.value)

    LaunchedEffect(transaction) {
        if (transaction.hash.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.transaction_details_missing), Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
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
                    modifier = Modifier.testTag(AMOUNT),
                    text = stringResource(R.string.formated_eth, amount),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Tx Hash Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StyledText(
                    label = stringResource(R.string.tx_hash),
                    value = transaction.hash,
                    testTag = TRANSACTION_HASH
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Account to Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StyledText(
                    label = stringResource(R.string.to),
                    value = transaction.to,
                    testTag = DESTINATION
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            val formattedDate = formatTimestamp(transaction.timeStamp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
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
                    testTag = DATE
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBackPressed,
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(stringResource(R.string.back), fontSize = 16.sp, textAlign = TextAlign.Center)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewTransactionDetailScreen() {
    val mockTransaction = TransactionDataModel(
        hash = "0x123421398123912938912839812932139123881298312839",
        value = "1000000000000000000",
        timeStamp = "1628910000",
        to = "0x391E7C679d29bD940d63be94AD22A25d25b5A604"
    )

    TransactionDetailScreen(
        transaction = mockTransaction,
        onBackPressed = {}
    )
}

object TransactionDetailTestTags {
    const val TRANSACTION_HASH = "transactionHash"
    const val AMOUNT = "amount"
    const val DESTINATION = "TO"
    const val DATE = "date"
}