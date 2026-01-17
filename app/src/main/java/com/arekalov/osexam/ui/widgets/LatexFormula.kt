package com.arekalov.osexam.ui.widgets

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun LatexFormula(
    latex: String,
    isInline: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap = remember(latex) {
        try {
            val drawable = ru.noties.jlatexmath.JLatexMathDrawable.builder(latex)
                .textSize(if (isInline) 40f else 60f)
                .color(Color.WHITE)
                .padding(8)
                .background(Color.TRANSPARENT)
                .build()
            
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            
            if (width > 0 && height > 0) {
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = android.graphics.Canvas(bmp)
                drawable.setBounds(0, 0, width, height)
                drawable.draw(canvas)
                bmp
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    if (bitmap != null) {
        Box(modifier = modifier) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "LaTeX: $latex",
                modifier = if (isInline) Modifier else Modifier.fillMaxWidth()
            )
        }
    } else {
        // Fallback: show as monospace text
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(8.dp)
        ) {
            Text(
                text = latex,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
