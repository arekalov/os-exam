package com.arekalov.osexam.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
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
import com.arekalov.osexam.domain.model.Ticket
import com.arekalov.osexam.presentation.detail.TicketDetailIntent
import com.arekalov.osexam.presentation.detail.TicketDetailState
import com.arekalov.osexam.presentation.detail.TicketDetailViewModel
import com.arekalov.osexam.ui.theme.OsexamTheme
import com.arekalov.osexam.ui.widgets.MarkdownRenderer

@Composable
fun TicketDetailScreen(
    viewModel: TicketDetailViewModel
) {
    val state by viewModel.state.collectAsState()
    TicketDetailContent(
        state = state,
        onImageClick = { path ->
            viewModel.onIntent(TicketDetailIntent.ImageClicked(path))
        }
    )
}

@Composable
private fun TicketDetailContent(
    state: TicketDetailState,
    onImageClick: (String) -> Unit
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
            val ticket = state.ticket
            if (ticket != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
                ) {
                    item {
                        Text(
                            text = "${ticket.number}. ${ticket.title}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    }
                    item {
                        MarkdownRenderer(
                            markdown = ticket.content,
                            onImageClick = onImageClick
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

@Preview(name = "Ticket Detail Mobile")
@Composable
private fun PreviewTicketDetail() {
    OsexamTheme {
        TicketDetailContent(
            state = TicketDetailState(
                isLoading = false,
                ticket = Ticket(
                    number = 1,
                    title = "Планирование процессов",
                    content = "# Заголовок\nТекст абзаца.\n- Пункт 1\n- Пункт 2"
                )
            ),
            onImageClick = {}
        )
    }
}
