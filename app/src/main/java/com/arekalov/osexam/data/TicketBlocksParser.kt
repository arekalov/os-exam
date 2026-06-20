package com.arekalov.osexam.data

import com.arekalov.osexam.domain.model.TicketBlock

class TicketBlocksParser {
    fun parse(raw: String): List<TicketBlock> {
        val grouped = linkedMapOf<Int, MutableList<Int>>()

        raw.lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { line ->
                val match = LINE_REGEX.matchEntire(line)
                    ?: throw IllegalArgumentException("Invalid ticket-blocks line: $line")
                val ticketNumber = match.groupValues[1].toInt()
                val blockNumber = match.groupValues[3].toInt()
                grouped.getOrPut(blockNumber) { mutableListOf() }.add(ticketNumber)
            }

        return grouped.entries
            .sortedBy { it.key }
            .map { (blockId, ticketNumbers) ->
                TicketBlock(
                    id = blockId,
                    title = "Билет $blockId",
                    ticketNumbers = ticketNumbers.sorted()
                )
            }
    }

    companion object {
        private val LINE_REGEX = Regex("""^(\d+)\.\s+(.+?)\s+(\d+)$""")
    }
}
