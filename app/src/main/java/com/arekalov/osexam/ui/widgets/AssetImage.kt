package com.arekalov.osexam.ui.widgets

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AssetImage(
    path: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    limitSize: Boolean = true
) {
    val context = LocalContext.current
    val bitmapState by produceState<ImageBitmap?>(initialValue = null, key1 = path) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                Log.d("AssetImage", "Loading image: $path")
                context.assets.open(path).use { input ->
                    val bitmap = BitmapFactory.decodeStream(input)
                    if (bitmap != null) {
                        Log.d("AssetImage", "Image loaded successfully: $path (${bitmap.width}x${bitmap.height})")
                        bitmap.asImageBitmap()
                    } else {
                        Log.e("AssetImage", "Failed to decode image: $path")
                        null
                    }
                }
            }.onFailure { e ->
                Log.e("AssetImage", "Error loading image: $path", e)
            }.getOrNull()
        }
    }

    val bitmap = bitmapState
    if (bitmap != null) {
        val sizeModifier = if (limitSize) {
            Modifier
                .widthIn(max = 200.dp)
                .heightIn(max = 200.dp)
        } else {
            Modifier
        }
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = modifier.then(sizeModifier),
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Image not found: $path",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}
