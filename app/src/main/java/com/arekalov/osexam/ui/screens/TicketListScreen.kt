package com.arekalov.osexam.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.arekalov.osexam.BuildConfig
import com.arekalov.osexam.domain.model.TicketSummary
import com.arekalov.osexam.presentation.list.SortMode
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
        },
        onSearchClick = {
            viewModel.onIntent(TicketListIntent.SearchClicked)
        },
        onSortToggle = {
            viewModel.onIntent(TicketListIntent.ToggleSort)
        }
    )
}

@Composable
private fun TicketListContent(
    state: TicketListState,
    onTicketClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onSortToggle: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (BuildConfig.FEATURE_SEARCH) {
                    item {
                        Card(
                            onClick = onSearchClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = "🔍", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Поиск по билетам",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

                if (BuildConfig.FEATURE_SORT_MODE) {
                    item {
                        Card(
                            onClick = onSortToggle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = if (state.sortMode == SortMode.BY_NUMBER) "🔢" else "🔤",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (state.sortMode == SortMode.BY_NUMBER) "По номеру" else "По алфавиту",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                items(state.tickets) { ticket ->
                    Card(
                        onClick = { onTicketClick(ticket.number) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "${ticket.number}. ${ticket.title}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
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

@Preview(name = "Ticket List Mobile")
@Composable
private fun PreviewTicketList() {
    OsexamTheme {
        TicketListContent(
            state = TicketListState(
                isLoading = false,
                tickets = listOf(
                    TicketSummary(1, "Архитектура компьютерных систем. Архитектура Фон-Неймана"),
                    TicketSummary(2, "Общая организация процессора, памяти, организация прерываний")
                )
            ),
            onTicketClick = {},
            onSearchClick = {},
            onSortToggle = {}
        )
    }
}
