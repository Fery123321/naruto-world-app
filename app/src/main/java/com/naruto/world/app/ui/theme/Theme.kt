package com.naruto.world.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NarutoOrange,
    onPrimary = InkBlack,
    primaryContainer = NarutoOrangeDark,
    onPrimaryContainer = PaperWhite,

    secondary = NinjaBlue,
    onSecondary = PaperWhite,
    secondaryContainer = NinjaBlueDark,
    onSecondaryContainer = PaperWhite,

    tertiary = ChakraRed,
    onTertiary = PaperWhite,
    tertiaryContainer = ChakraRedDark,
    onTertiaryContainer = PaperWhite,

    error = ErrorRed,
    onError = PaperWhite,
    errorContainer = ChakraRedDark,
    onErrorContainer = PaperWhite,

    background = SurfaceDark,
    onBackground = PaperWhite,
    surface = CardBackgroundDark,
    onSurface = PaperWhite,
    surfaceVariant = ScrollGrayDark,
    onSurfaceVariant = ScrollGrayLight,

    outline = ScrollGray,
    outlineVariant = ScrollGrayDark
)

private val LightColorScheme = lightColorScheme(
    primary = NarutoOrange,
    onPrimary = PaperWhite,
    primaryContainer = NarutoOrangeLight,
    onPrimaryContainer = InkBlack,

    secondary = NinjaBlue,
    onSecondary = PaperWhite,
    secondaryContainer = NinjaBlueLight,
    onSecondaryContainer = InkBlack,

    tertiary = ChakraRed,
    onTertiary = PaperWhite,
    tertiaryContainer = ChakraRedLight,
    onTertiaryContainer = InkBlack,

    error = ErrorRed,
    onError = PaperWhite,
    errorContainer = ChakraRedLight,
    onErrorContainer = InkBlack,

    background = SurfaceLight,
    onBackground = InkBlack,
    surface = CardBackground,
    onSurface = InkBlack,
    surfaceVariant = ScrollGrayLight,
    onSurfaceVariant = ScrollGrayDark,

    outline = ScrollGray,
    outlineVariant = ScrollGrayLight
)

@Composable
fun NarutoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
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