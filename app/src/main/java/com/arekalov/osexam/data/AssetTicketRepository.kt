package com.arekalov.osexam.data

import android.content.res.AssetManager
import com.arekalov.osexam.domain.model.Ticket
import com.arekalov.osexam.domain.model.TicketSource
import com.arekalov.osexam.domain.repository.TicketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetTicketRepository(
    private val assets: AssetManager,
    private val parser: TicketParser
) : TicketRepository {
    override suspend fun getTickets(source: TicketSource): List<Ticket> = withContext(Dispatchers.IO) {
        loadTicketsFromDir(source.assetsDir)
    }

    override suspend fun getTicket(number: Int, source: TicketSource): Ticket? = withContext(Dispatchers.IO) {
        getTickets(source).firstOrNull { it.number == number }
    }

    private fun loadTicketsFromDir(dir: String): List<Ticket> {
        val files = assets.list(dir).orEmpty()
        return files.filter { it.endsWith(".md") }
            .mapNotNull { file ->
                val raw = assets.open("$dir/$file").bufferedReader().use { it.readText() }
                try {
                    parser.parse(raw)
                } catch (e: Exception) {
                    null
                }
            }
            .sortedBy { it.number }
    }
}
