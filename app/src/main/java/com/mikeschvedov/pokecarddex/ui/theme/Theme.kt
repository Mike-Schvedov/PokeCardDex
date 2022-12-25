package com.mikeschvedov.pokecarddex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Color.Yellow,
    background = Color(0xFF101010),
    onBackground = Color.White,
    surface = Color(0xFF303030),
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Color.Blue,
    background = backgroundYellow,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)


@Composable
fun PokeCardDexTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )

    //Change status bar color
    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setSystemBarsColor(
            Color.Black
        )
    } else {
        systemUiController.setSystemBarsColor(
            color = statusBarBlue
        )
    }
}