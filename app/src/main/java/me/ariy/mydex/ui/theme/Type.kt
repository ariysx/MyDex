package me.ariy.mydex.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import me.ariy.mydex.R

val Poppins = FontFamily(
    Font(R.font.poppins)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h6 = TextStyle(
        fontFamily = Poppins,
        fontSize = 20.sp
    ),
    h5 = TextStyle(
        fontFamily = Poppins,
        fontSize = 24.sp
    ),
    h4 = TextStyle(
        fontFamily = Poppins,
        fontSize = 34.sp
    ),
    h3 = TextStyle(
        fontFamily = Poppins,
        fontSize = 48.sp
    ),
    h2 = TextStyle(
        fontFamily = Poppins,
        fontSize = 60.sp
    ),
    h1 = TextStyle(
        fontFamily = Poppins,
        fontSize = 96.sp,
    )
    /* me.ariy.mydex.data.pokemon.entity.sample.Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)