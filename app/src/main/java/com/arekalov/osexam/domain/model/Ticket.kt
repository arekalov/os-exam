package com.arekalov.osexam.domain.model

data class Ticket(
    val number: Int,
    val title: String,
    val content: String
)

data class TicketSummary(
    val number: Int,
    val title: String
)

data class TicketBlock(
    val id: Int,
    val title: String,
    val ticketNumbers: List<Int>
)
