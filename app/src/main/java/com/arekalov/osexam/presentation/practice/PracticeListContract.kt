package com.arekalov.osexam.presentation.practice

import com.arekalov.osexam.domain.model.TicketSummary

sealed interface PracticeListIntent {
    object Refresh : PracticeListIntent
    data class TicketClicked(val number: Int) : PracticeListIntent
}

sealed interface PracticeListEffect {
    data class NavigateToTicket(val number: Int) : PracticeListEffect
}

data class PracticeListState(
    val isLoading: Boolean = true,
    val tickets: List<TicketSummary> = emptyList(),
    val error: String? = null
)
