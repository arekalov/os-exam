package com.arekalov.osexam.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arekalov.osexam.domain.model.TicketSummary
import com.arekalov.osexam.presentation.blocktickets.BlockTicketsIntent
import com.arekalov.osexam.presentation.blocktickets.BlockTicketsState
import com.arekalov.osexam.presentation.blocktickets.BlockTicketsViewModel

@Composable
fun BlockTicketsScreen(
    viewModel: BlockTicketsViewModel
) {
    val state by viewModel.state.collectAsState()
    BlockTicketsContent(
        state = state,
        onTicketClick = { number ->
            viewModel.onIntent(BlockTicketsIntent.TicketClicked(number))
        }
    )
}

@Composable
private fun BlockTicketsContent(
    state: BlockTicketsState,
    onTicketClick: (Int) -> Unit
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
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
            ) {
                if (state.blockTitle.isNotEmpty()) {
                    item {
                        Text(
                            text = "Блок ${state.blockId}: ${state.blockTitle}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
                
                items(state.tickets) { ticket ->
                    Card(
                        onClick = { onTicketClick(ticket.number) },
                        modifier = Modifier.padding(bottom = 12.dp)
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
