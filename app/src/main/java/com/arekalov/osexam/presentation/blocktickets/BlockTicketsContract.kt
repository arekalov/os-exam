package com.arekalov.osexam.presentation.blocktickets

import com.arekalov.osexam.domain.model.TicketSummary

sealed interface BlockTicketsIntent {
    data class TicketClicked(val number: Int) : BlockTicketsIntent
}

sealed interface BlockTicketsEffect {
    data class NavigateToTicket(val number: Int) : BlockTicketsEffect
}

data class BlockTicketsState(
    val blockId: Int = 0,
    val blockTitle: String = "",
    val isLoading: Boolean = true,
    val tickets: List<TicketSummary> = emptyList(),
    val error: String? = null
)
