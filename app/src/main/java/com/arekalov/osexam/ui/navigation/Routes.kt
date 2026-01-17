package com.arekalov.osexam.ui.navigation

import android.net.Uri

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{number}"
    const val IMAGE = "image/{path}"

    fun detail(number: Int): String = "detail/$number"
    fun image(path: String): String = "image/${Uri.encode(path)}"
}
