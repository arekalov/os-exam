package com.arekalov.osexam.data

import android.content.res.AssetManager
import com.arekalov.osexam.domain.model.Ticket
import com.arekalov.osexam.domain.repository.TicketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetTicketRepository(
    private val assets: AssetManager,
    private val parser: TicketParser
) : TicketRepository {
    override suspend fun getTickets(): List<Ticket> = withContext(Dispatchers.IO) {
        val files = assets.list(TICKETS_DIR).orEmpty()
        files.filter { it.endsWith(".md") }
            .mapNotNull { file ->
                val raw = assets.open("$TICKETS_DIR/$file").bufferedReader().use { it.readText() }
                try {
                    parser.parse(raw)
                } catch (e: Exception) {
                    null
                }
            }
            .sortedBy { it.number }
    }

    override suspend fun getTicket(number: Int): Ticket? = withContext(Dispatchers.IO) {
        getTickets().firstOrNull { it.number == number }
    }

    companion object {
        const val TICKETS_DIR = "tickets"
    }
}
