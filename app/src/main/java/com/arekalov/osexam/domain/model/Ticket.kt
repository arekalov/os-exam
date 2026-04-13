package com.arekalov.osexam.domain.model

data class Ticket(
    val number: Int,
    val title: String,
    val keywords: List<String> = emptyList(),
    val content: String
)

data class TicketSummary(
    val number: Int,
    val title: String,
    val keywords: List<String> = emptyList()
)

data class TicketBlock(
    val id: Int,
    val title: String,
    val ticketNumbers: List<Int>
)
