package com.arekalov.osexam.data

import android.content.res.AssetManager
import com.arekalov.osexam.domain.model.TicketBlock
import com.arekalov.osexam.domain.repository.TicketBlocksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetTicketBlocksRepository(
    private val assets: AssetManager,
    private val parser: TicketBlocksParser
) : TicketBlocksRepository {
    override suspend fun getBlocks(): List<TicketBlock> = withContext(Dispatchers.IO) {
        loadBlocks()
    }

    override suspend fun getBlock(blockId: Int): TicketBlock? = withContext(Dispatchers.IO) {
        loadBlocks().firstOrNull { it.id == blockId }
    }

    private fun loadBlocks(): List<TicketBlock> {
        val raw = assets.open(BLOCKS_FILE).bufferedReader().use { it.readText() }
        return parser.parse(raw)
    }

    companion object {
        const val BLOCKS_FILE = "ticket-blocks.md"
    }
}
