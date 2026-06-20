package com.arekalov.osexam.domain.usecase

import com.arekalov.osexam.domain.model.TicketBlock
import com.arekalov.osexam.domain.repository.TicketBlocksRepository

class GetTicketBlocksUseCase(
    private val repository: TicketBlocksRepository
) {
    suspend operator fun invoke(): List<TicketBlock> = repository.getBlocks()
}
