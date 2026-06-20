package com.arekalov.osexam.domain.repository

import com.arekalov.osexam.domain.model.TicketBlock

interface TicketBlocksRepository {
    suspend fun getBlocks(): List<TicketBlock>
    suspend fun getBlock(blockId: Int): TicketBlock?
}
