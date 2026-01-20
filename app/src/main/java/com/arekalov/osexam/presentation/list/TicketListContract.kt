package com.arekalov.osexam.presentation.list

import com.arekalov.osexam.domain.model.TicketSummary

sealed interface TicketListIntent {
    object Refresh : TicketListIntent
    data class TicketClicked(val number: Int) : TicketListIntent
    object BlocksClicked : TicketListIntent
}

sealed interface TicketListEffect {
    data class NavigateToTicket(val number: Int) : TicketListEffect
    object NavigateToBlocks : TicketListEffect
}

data class TicketListState(
    val isLoading: Boolean = true,
    val tickets: List<TicketSummary> = emptyList(),
    val error: String? = null
)
