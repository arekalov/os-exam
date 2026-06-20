package com.arekalov.osexam.presentation.blocks

import com.arekalov.osexam.domain.model.TicketBlock
import com.arekalov.osexam.domain.model.TicketSummary

object BlockTitleFormatter {
    private const val FIRST_TITLE_MAX_LENGTH = 36

    fun enrichBlocks(
        blocks: List<TicketBlock>,
        tickets: List<TicketSummary>
    ): List<TicketBlock> {
        val titlesByNumber = tickets.associate { it.number to it.title }
        return blocks.map { block ->
            block.copy(summary = buildSummary(block, titlesByNumber))
        }
    }

    private fun buildSummary(
        block: TicketBlock,
        titlesByNumber: Map<Int, String>
    ): String {
        val ticketTitles = block.ticketNumbers.mapNotNull { number ->
            titlesByNumber[number]
        }
        if (ticketTitles.isEmpty()) {
            return block.title
        }
        if (ticketTitles.size == 1) {
            return "${block.title}. ${truncate(ticketTitles[0], FIRST_TITLE_MAX_LENGTH)}"
        }
        val first = truncate(ticketTitles[0], FIRST_TITLE_MAX_LENGTH)
        val second = ticketTitles[1]
        return "${block.title}. $first | $second"
    }

    private fun truncate(text: String, maxLength: Int): String {
        if (text.length <= maxLength) {
            return text
        }
        return text.take(maxLength).trimEnd() + "..."
    }
}
