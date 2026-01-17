package com.arekalov.osexam.data

import com.arekalov.osexam.domain.model.Ticket

class TicketParser {
    fun parse(raw: String): Ticket {
        val lines = raw.lines()
        if (lines.isEmpty() || lines.first().trim() != "---") {
            throw IllegalArgumentException("Missing front matter delimiter")
        }

        val headerLines = mutableListOf<String>()
        var i = 1
        while (i < lines.size && lines[i].trim() != "---") {
            headerLines.add(lines[i])
            i++
        }
        if (i >= lines.size) {
            throw IllegalArgumentException("Front matter not закрыт")
        }
        val header = parseHeader(headerLines)
        val content = lines.drop(i + 1).joinToString("\n").trim()

        val number = header["number"]?.toIntOrNull()
            ?: throw IllegalArgumentException("number не указан или неверный")
        val title = header["title"]?.trim().orEmpty()
        if (title.isBlank()) {
            throw IllegalArgumentException("title не указан")
        }

        return Ticket(
            number = number,
            title = title,
            content = content
        )
    }

    private fun parseHeader(lines: List<String>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        lines.forEach { line ->
            val idx = line.indexOf(':')
            if (idx > 0) {
                val key = line.substring(0, idx).trim()
                val value = line.substring(idx + 1).trim()
                if (key.isNotBlank()) {
                    map[key] = value
                }
            }
        }
        return map
    }
}
