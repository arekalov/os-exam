package com.arekalov.osexam.presentation.blocks

import com.arekalov.osexam.domain.model.TicketBlock

sealed interface BlocksIntent {
    data class BlockClicked(val blockId: Int) : BlocksIntent
}

sealed interface BlocksEffect {
    data class NavigateToBlock(val blockId: Int) : BlocksEffect
}

data class BlocksState(
    val blocks: List<TicketBlock> = emptyList()
)
