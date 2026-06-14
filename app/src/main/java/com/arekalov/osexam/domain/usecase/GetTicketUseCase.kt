package com.arekalov.osexam.domain.usecase

import com.arekalov.osexam.domain.model.Ticket
import com.arekalov.osexam.domain.model.TicketSource
import com.arekalov.osexam.domain.repository.TicketRepository

class GetTicketUseCase(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(number: Int, source: TicketSource = TicketSource.THEORY): Ticket? {
        return repository.getTicket(number, source)
    }
}
