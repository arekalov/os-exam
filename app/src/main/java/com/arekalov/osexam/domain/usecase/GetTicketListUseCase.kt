package com.arekalov.osexam.domain.usecase

import com.arekalov.osexam.domain.model.TicketSource
import com.arekalov.osexam.domain.model.TicketSummary
import com.arekalov.osexam.domain.repository.TicketRepository

class GetTicketListUseCase(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(source: TicketSource = TicketSource.THEORY): List<TicketSummary> {
        return repository.getTickets(source)
            .sortedBy { it.number }
            .map { TicketSummary(it.number, it.title) }
    }
}
