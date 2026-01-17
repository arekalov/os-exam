package com.arekalov.osexam.ui.screens

import androidx.activity.compose.BackHandler
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.MaterialTheme
import com.arekalov.osexam.ui.widgets.AssetImage

@Composable
fun ImageViewerScreen(
    path: String,
    onClose: () -> Unit
) {
    val decodedPath = Uri.decode(path)
    BackHandler { onClose() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        AssetImage(
            path = decodedPath,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
            limitSize = false
        )
    }
}
