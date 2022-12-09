package me.ariy.mydex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* me.ariy.mydex.data.pokemon.entity.sample.Other default colors to override
    background = me.ariy.mydex.data.pokemon.entity.species.Color.White,
    surface = me.ariy.mydex.data.pokemon.entity.species.Color.White,
    onPrimary = me.ariy.mydex.data.pokemon.entity.species.Color.White,
    onSecondary = me.ariy.mydex.data.pokemon.entity.species.Color.me.ariy.mydex.data.pokemon.entity.sample.Black,
    onBackground = me.ariy.mydex.data.pokemon.entity.species.Color.me.ariy.mydex.data.pokemon.entity.sample.Black,
    onSurface = me.ariy.mydex.data.pokemon.entity.species.Color.me.ariy.mydex.data.pokemon.entity.sample.Black,
    */
)

@Composable
fun MyDexTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

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
}