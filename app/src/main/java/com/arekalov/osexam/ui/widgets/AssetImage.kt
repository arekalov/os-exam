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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class ImageLoadState {
    object Loading : ImageLoadState()
    data class Success(val bitmap: ImageBitmap) : ImageLoadState()
    data class Error(val message: String) : ImageLoadState()
}

@Composable
fun AssetImage(
    path: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    limitSize: Boolean = true
) {
    val context = LocalContext.current
    var loadState by remember(path) { mutableStateOf<ImageLoadState>(ImageLoadState.Loading) }
    
    LaunchedEffect(path) {
        loadState = ImageLoadState.Loading
        loadState = withContext(Dispatchers.IO) {
            runCatching {
                Log.d("AssetImage", "Loading image: $path")
                context.assets.open(path).use { input ->
                    val bitmap = BitmapFactory.decodeStream(input)
                    if (bitmap != null) {
                        Log.d("AssetImage", "Image loaded successfully: $path (${bitmap.width}x${bitmap.height})")
                        ImageLoadState.Success(bitmap.asImageBitmap())
                    } else {
                        Log.e("AssetImage", "Failed to decode image: $path")
                        ImageLoadState.Error("Failed to decode image")
                    }
                }
            }.onFailure { e ->
                Log.e("AssetImage", "Error loading image: $path", e)
            }.getOrElse { 
                ImageLoadState.Error("Image not found: $path")
            }
        }
    }

    when (val state = loadState) {
        is ImageLoadState.Loading -> {
            // Пустой бокс для состояния загрузки - ничего не показываем
            Box(modifier = modifier)
        }
        is ImageLoadState.Success -> {
            val sizeModifier = if (limitSize) {
                Modifier
                    .widthIn(max = 200.dp)
                    .heightIn(max = 200.dp)
            } else {
                Modifier
            }
            Image(
                bitmap = state.bitmap,
                contentDescription = null,
                modifier = modifier.then(sizeModifier),
                contentScale = contentScale
            )
        }
        is ImageLoadState.Error -> {
            Box(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
