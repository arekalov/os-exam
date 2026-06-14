package com.arekalov.osexam.domain.model

enum class TicketSource(val assetsDir: String) {
    THEORY("tickets"),
    PRACTICE("practice");

    companion object {
        fun fromRouteValue(value: String?): TicketSource {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: THEORY
        }
    }
}
