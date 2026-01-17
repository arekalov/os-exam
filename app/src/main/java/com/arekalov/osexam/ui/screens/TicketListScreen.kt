package com.arekalov.osexam.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import com.arekalov.osexam.domain.model.TicketSummary
import com.arekalov.osexam.presentation.list.TicketListIntent
import com.arekalov.osexam.presentation.list.TicketListState
import com.arekalov.osexam.presentation.list.TicketListViewModel
import com.arekalov.osexam.ui.theme.OsexamTheme

@Composable
fun TicketListScreen(
    viewModel: TicketListViewModel
) {
    val state by viewModel.state.collectAsState()
    TicketListContent(
        state = state,
        onTicketClick = { number ->
            viewModel.onIntent(TicketListIntent.TicketClicked(number))
        }
    )
}

@Composable
private fun TicketListContent(
    state: TicketListState,
    onTicketClick: (Int) -> Unit
) {
    val listState = rememberScalingLazyListState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ScalingLazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,
                    bottom = 20.dp
                )
            ) {
                items(state.tickets) { ticket ->
                    Card(
                        onClick = { onTicketClick(ticket.number) },
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Text(
                            text = "${ticket.number}. ${ticket.title}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            if (state.isLoading) {
                Text(
                    text = "Загрузка...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(name = "Ticket List", device = "id:wearos_small_round")
@Composable
private fun PreviewTicketList() {
    OsexamTheme {
        TicketListContent(
            state = TicketListState(
                isLoading = false,
                tickets = listOf(
                    TicketSummary(1, "Архитектура компьютерных систем. Архитектура Фон-Ндская архитектура"),
                    TicketSummary(2, "Общая организация процессора, памяти, организация прерываний")
                )
            ),
            onTicketClick = {}
        )
    }
}
