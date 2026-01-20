package com.arekalov.osexam.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arekalov.osexam.domain.model.TicketBlock
import com.arekalov.osexam.presentation.blocks.BlocksIntent
import com.arekalov.osexam.presentation.blocks.BlocksState
import com.arekalov.osexam.presentation.blocks.BlocksViewModel

@Composable
fun BlocksScreen(
    viewModel: BlocksViewModel
) {
    val state by viewModel.state.collectAsState()
    BlocksContent(
        state = state,
        onBlockClick = { blockId ->
            viewModel.onIntent(BlocksIntent.BlockClicked(blockId))
        }
    )
}

@Composable
private fun BlocksContent(
    state: BlocksState,
    onBlockClick: (Int) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            )
        ) {
            items(state.blocks) { block ->
                BlockCard(
                    block = block,
                    onClick = { onBlockClick(block.id) }
                )
            }
        }
    }
}

@Composable
private fun BlockCard(
    block: TicketBlock,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Text(
            text = "${block.id}. ${block.title}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
