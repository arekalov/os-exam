package com.arekalov.osexam.domain.usecase

import com.arekalov.osexam.domain.model.Ticket
import com.arekalov.osexam.domain.repository.TicketRepository

class GetTicketUseCase(
    private val repository: TicketRepository
) {
    suspend operator fun invoke(number: Int): Ticket? {
        return repository.getTicket(number)
    }
}
