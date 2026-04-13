package com.arekalov.osexam.presentation.list

import com.arekalov.osexam.domain.model.TicketSummary

enum class SortMode { BY_NUMBER, BY_TITLE }

sealed interface TicketListIntent {
    object Refresh : TicketListIntent
    data class TicketClicked(val number: Int) : TicketListIntent
    object SearchClicked : TicketListIntent
    object ToggleSort : TicketListIntent
}

sealed interface TicketListEffect {
    data class NavigateToTicket(val number: Int) : TicketListEffect
    object NavigateToSearch : TicketListEffect
}

data class TicketListState(
    val isLoading: Boolean = true,
    val tickets: List<TicketSummary> = emptyList(),
    val error: String? = null,
    val sortMode: SortMode = SortMode.BY_NUMBER
)
