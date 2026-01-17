package com.arekalov.osexam.presentation.detail

import com.arekalov.osexam.domain.model.Ticket

sealed interface TicketDetailIntent {
    data class ImageClicked(val path: String) : TicketDetailIntent
}

sealed interface TicketDetailEffect {
    data class NavigateToImage(val path: String) : TicketDetailEffect
}

data class TicketDetailState(
    val isLoading: Boolean = true,
    val ticket: Ticket? = null,
    val error: String? = null
)
