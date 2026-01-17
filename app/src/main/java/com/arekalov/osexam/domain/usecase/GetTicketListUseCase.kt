package com.arekalov.osexam.domain.usecase

import com.arekalov.osexam.domain.model.TicketSummary
import com.arekalov.osexam.domain.repository.TicketRepository

class GetTicketListUseCase(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(): List<TicketSummary> {
        return repository.getTickets()
            .sortedBy { it.number }
            .map { TicketSummary(it.number, it.title) }
    }
}
