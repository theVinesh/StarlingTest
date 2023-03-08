@file:OptIn(ExperimentalMaterialApi::class)

package com.example.starlingtest.ui.roundups

import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.starlingtest.ui.ErrorScreen
import com.example.starlingtest.ui.LoadingScreen
import com.example.starlingtest.ui.roundups.states.Amount
import com.example.starlingtest.ui.roundups.states.RoundupsScreenEffects
import com.example.starlingtest.ui.roundups.states.RoundupsUiState
import com.example.starlingtest.ui.roundups.states.Transaction
import com.example.starlingtest.ui.roundups.states.roundUp
import com.example.starlingtest.ui.roundups.viewmodels.RoundupsScreenVm
import com.example.starlingtest.ui.theme.StarlingTestTheme
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun RoundupsScreen(
    viewModel: RoundupsScreenVm,
    onBack: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    val showDatePicker = remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = "",
        block = {
            viewModel.effect.collect {
                when (it) {
                    is RoundupsScreenEffects.ShowDatePicker -> showDatePicker.value = it.show
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable(onClick = onBack)
                    )
                },
                title = { Text(text = "Round Up") }
            )
        }
    ) {
        it.calculateBottomPadding()
        when (val uiState = state.value) {
            is RoundupsUiState.Loading -> LoadingScreen()
            is RoundupsUiState.Error -> ErrorScreen(
                message = uiState.message,
                onClick = onBack,
                ctaText = "Go Back"
            )
            is RoundupsUiState.Content -> {
                DateSelector(
                    show = showDatePicker.value,
                    dateToShow = uiState.since,
                    onDateSelected = viewModel::onDateSelected
                )
                when (uiState) {
                    is RoundupsUiState.Content.Initial -> ErrorScreen(
                        message = "Select a date to get started",
                        onClick = { viewModel.showDatePicker(true) },
                        ctaText = "Select Date"
                    )
                    is RoundupsUiState.Content.NoTransactions -> ErrorScreen(
                        message = "No transactions found for the selected date",
                        onClick = { viewModel.showDatePicker(true) },
                        ctaText = "Change Date"
                    )
                    is RoundupsUiState.Content.Transactions -> Box {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Button(onClick = {
                                viewModel.showDatePicker(true)
                            }) {
                                Text(text = "Select Date")
                            }
                            TransactionsList(uiState.transactionsWithRoundUp.outboundTransactions)
                        }
                        RoundUpCard(
                            modifier = Modifier.align(
                                Alignment.BottomCenter
                            ),
                            roundUpTotal = uiState.transactionsWithRoundUp.roundUpTotal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoundUpCard(
    modifier: Modifier = Modifier,
    roundUpTotal: Amount
) {
    Card(
        elevation = 16.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(start = 8.dp),
                text = "Round Up Total: ${roundUpTotal.valueString} ${roundUpTotal.currency}",
            )
            Button(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .padding(end = 8.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Save")
            }
        }

    }
}

@Composable
fun DateSelector(
    show: Boolean = false,
    dateToShow: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit
) {
    if (!show) return
    Dialog(onDismissRequest = {}) {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            AndroidView(
                { DatePicker(it) },
                modifier = Modifier.wrapContentWidth(),
                update = { view ->
                    val today = LocalDate.now(ZoneId.systemDefault())
                    val date = dateToShow ?: today
                    view.updateDate(date.year, date.monthValue - 1, date.dayOfMonth)
                    view.setOnDateChangedListener { _, year, month, dayOfMonth ->
                        onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                    }
                }
            )
        }
    }
}

@Composable
fun TransactionsList(
    transactions: List<Transaction>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 128.dp)
    ) {
        items(
            transactions.size,
            key = { index -> transactions[index].uid }
        ) { index ->
            val transaction = transactions[index]
            ListItem(
                text = { Text(text = "To ${transaction.sentTo}") },
                trailing = {
                    val amount = transaction.amount
                    val roundUp = amount.roundUp()
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "${amount.valueString} ${amount.currency}")
                        Text(
                            text = "(${roundUp.valueString} ${roundUp.currency})",
                            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.secondary)
                        )
                    }
                }
            )
            Divider()
        }
    }
}

@Preview
@Composable
fun RoundUpCardPreview() {
    StarlingTestTheme {
        RoundUpCard(
            roundUpTotal = Amount(172, currency = "GBP")
        )
    }
}

@Preview
@Composable
fun DatePickerPreview() {
    StarlingTestTheme {
        DateSelector(
            show = true,
            dateToShow = LocalDate.now(),
            onDateSelected = {}
        )
    }
}

@Preview
@Composable
fun AccountsListPreview() {
    StarlingTestTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TransactionsList(
                listOf(
                    Transaction(
                        uid = "1",
                        amount = Amount(1720, currency = "GBP"),
                        sentTo = "John"
                    ),
                    Transaction(
                        uid = "2",
                        amount = Amount(182, currency = "GBP"),
                        sentTo = "Jane"
                    ),
                    Transaction(
                        uid = "3",
                        amount = Amount(82, currency = "GBP"),
                        sentTo = "Mary"
                    ),
                    Transaction(
                        uid = "4",
                        amount = Amount(2, currency = "GBP"),
                        sentTo = "Adam"
                    )
                )
            )
        }
    }
}
