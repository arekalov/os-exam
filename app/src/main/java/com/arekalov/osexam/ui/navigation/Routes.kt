package com.arekalov.osexam.ui.navigation

import android.net.Uri
import com.arekalov.osexam.domain.model.TicketSource

object Routes {
    const val LIST = "list"
    const val PRACTICE_LIST = "practice"
    const val DETAIL = "detail/{source}/{number}"
    const val IMAGE = "image/{path}"

    fun detail(number: Int, source: TicketSource = TicketSource.THEORY): String {
        return "detail/${source.name.lowercase()}/$number"
    }

    fun image(path: String): String = "image/${Uri.encode(path)}"
}
