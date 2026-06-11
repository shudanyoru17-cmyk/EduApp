package com.edu.app.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import com.edu.app.R

// Bundled Cairo font (Google Fonts, OFL 1.1) — static weights in res/font/.
private val Cairo = FontFamily(
    Font(R.font.cairo_regular, FontWeight.Normal),
    Font(R.font.cairo_medium,  FontWeight.Medium),
    Font(R.font.cairo_bold,    FontWeight.Bold)
)

// Apply Cairo to EVERY Material 3 text style (buttons, chips, labels, etc.)
// by deriving from the defaults and overriding fontFamily, then tweak the few
// styles the app uses most.
private val base = Typography()
val AppTypography = Typography(
    displayLarge   = base.displayLarge.copy(fontFamily = Cairo),
    displayMedium  = base.displayMedium.copy(fontFamily = Cairo),
    displaySmall   = base.displaySmall.copy(fontFamily = Cairo),
    headlineLarge  = base.headlineLarge.copy(fontFamily = Cairo),
    headlineMedium = base.headlineMedium.copy(fontFamily = Cairo),
    headlineSmall  = base.headlineSmall.copy(fontFamily = Cairo),
    titleLarge     = base.titleLarge.copy(fontFamily = Cairo, fontWeight = FontWeight.Bold),
    titleMedium    = base.titleMedium.copy(fontFamily = Cairo, fontWeight = FontWeight.Medium),
    titleSmall     = base.titleSmall.copy(fontFamily = Cairo),
    bodyLarge      = base.bodyLarge.copy(fontFamily = Cairo),
    bodyMedium     = base.bodyMedium.copy(fontFamily = Cairo),
    bodySmall      = base.bodySmall.copy(fontFamily = Cairo),
    labelLarge     = base.labelLarge.copy(fontFamily = Cairo, fontWeight = FontWeight.Medium),
    labelMedium    = base.labelMedium.copy(fontFamily = Cairo),
    labelSmall     = base.labelSmall.copy(fontFamily = Cairo, fontWeight = FontWeight.Medium)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF1E6F5C),
    secondary = Color(0xFF289672),
    tertiary = Color(0xFF2D6A9F),
    background = Color(0xFFF7F9FB),
    surface = Color.White,
    error = Color(0xFFD64545)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF289672),
    secondary = Color(0xFF289672),
    tertiary = Color(0xFF2D6A9F),
    background = Color(0xFF0F1417),
    surface = Color(0xFF161C20),
    error = Color(0xFFD64545)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    // Force RTL for the whole UI tree (Arabic).
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(colorScheme = colors, typography = AppTypography, content = content)
    }
}
