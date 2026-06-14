package com.arekalov.osexam.domain.repository

import com.arekalov.osexam.domain.model.Ticket
import com.arekalov.osexam.domain.model.TicketSource

interface TicketRepository {
    suspend fun getTickets(source: TicketSource = TicketSource.THEORY): List<Ticket>
    suspend fun getTicket(number: Int, source: TicketSource = TicketSource.THEORY): Ticket?
}
