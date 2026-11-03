package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GoldLight,
    onPrimary = DeepNavy,
    primaryContainer = NavyLight,
    onPrimaryContainer = GoldLight,
    secondary = SlateLight,
    onSecondary = IvorySurface,
    secondaryContainer = NavySurface,
    onSecondaryContainer = IvorySurface,
    tertiary = AccentBlue,
    onTertiary = Color.White,
    background = DeepNavy,
    onBackground = IvoryBackground,
    surface = NavySurface,
    onSurface = IvoryBackground,
    surfaceVariant = NavyLight,
    onSurfaceVariant = SlateMuted,
    outline = SlateGrey
)

private val LightColorScheme = lightColorScheme(
    primary = DeepNavy,
    onPrimary = Color.White,
    primaryContainer = GoldContainer,
    onPrimaryContainer = GoldDark,
    secondary = SlateGrey,
    onSecondary = Color.White,
    secondaryContainer = IvorySurfaceVariant,
    onSecondaryContainer = DeepNavy,
    tertiary = SubtleBlue,
    onTertiary = Color.White,
    tertiaryContainer = SubtleBlueContainer,
    onTertiaryContainer = SubtleBlue,
    background = IvoryBackground,
    onBackground = DeepNavy,
    surface = IvorySurface,
    onSurface = DeepNavy,
    surfaceVariant = IvorySurfaceVariant,
    onSurfaceVariant = SlateGrey,
    outline = SlateBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent Lexovant legal branding
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
