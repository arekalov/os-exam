package com.arekalov.osexam.presentation.search

import com.arekalov.osexam.domain.model.TicketSummary

sealed interface SearchIntent {
    data class QueryChanged(val query: String) : SearchIntent
    data class TicketClicked(val number: Int) : SearchIntent
}

sealed interface SearchEffect {
    data class NavigateToTicket(val number: Int) : SearchEffect
}

data class SearchState(
    val query: String = "",
    val results: List<TicketSummary> = emptyList(),
    val isLoading: Boolean = true
)
