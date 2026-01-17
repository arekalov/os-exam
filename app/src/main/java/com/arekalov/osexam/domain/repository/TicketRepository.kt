package com.arekalov.osexam.domain.repository

import com.arekalov.osexam.domain.model.Ticket

interface TicketRepository {
    suspend fun getTickets(): List<Ticket>
    suspend fun getTicket(number: Int): Ticket?
}
