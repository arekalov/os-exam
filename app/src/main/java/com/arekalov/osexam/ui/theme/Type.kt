package com.arekalov.osexam.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Конфиг размеров шрифтов
 * Меняй здесь размеры для всего приложения
 */
object FontSizes {
    // Title Large - используется для заголовков MD уровня 1 (# Заголовок)
    val titleLargeFontSize: TextUnit = 10.sp
    val titleLargeLineHeight: TextUnit = 18.sp
    val titleLargeLetterSpacing: TextUnit = 0.sp
    
    // Title Medium - используется для заголовков MD уровня 2 (## Заголовок)
    val titleMediumFontSize: TextUnit = 10.sp
    val titleMediumLineHeight: TextUnit = 16.sp
    val titleMediumLetterSpacing: TextUnit = 0.15.sp
    
    // Title Small - используется для заголовков MD уровня 3 (### Заголовок)
    val titleSmallFontSize: TextUnit = 10.sp
    val titleSmallLineHeight: TextUnit = 10.sp
    val titleSmallLetterSpacing: TextUnit = 0.1.sp
    
    // Body Medium - используется для заголовков карточек билетов в списке
    val bodyMediumFontSize: TextUnit = 10.sp
    val bodyMediumLineHeight: TextUnit = 10.sp
    val bodyMediumLetterSpacing: TextUnit = 0.5.sp
    
    // Body Small - используется для:
    // - обычного текста (параграфы)
    // - элементов списков (маркированные и нумерованные)
    // - блоков кода
    val bodySmallFontSize: TextUnit = 9.sp
    val bodySmallLineHeight: TextUnit = 12.sp
    val bodySmallLetterSpacing: TextUnit = 0.4.sp
}

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = FontSizes.titleLargeFontSize,
        lineHeight = FontSizes.titleLargeLineHeight,
        letterSpacing = FontSizes.titleLargeLetterSpacing
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = FontSizes.titleMediumFontSize,
        lineHeight = FontSizes.titleMediumLineHeight,
        letterSpacing = FontSizes.titleMediumLetterSpacing
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = FontSizes.titleSmallFontSize,
        lineHeight = FontSizes.titleSmallLineHeight,
        letterSpacing = FontSizes.titleSmallLetterSpacing
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = FontSizes.bodyMediumFontSize,
        lineHeight = FontSizes.bodyMediumLineHeight,
        letterSpacing = FontSizes.bodyMediumLetterSpacing
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = FontSizes.bodySmallFontSize,
        lineHeight = FontSizes.bodySmallLineHeight,
        letterSpacing = FontSizes.bodySmallLetterSpacing
    )
)