package me.ariy.mydex

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object home : BottomBarScreen(
        route = "home",
        title = "Pokedex",
        icon = Icons.Default.Home
    )

    object team : BottomBarScreen(
        route = "team",
        title = "MyTeam",
        icon = Icons.Default.Person
    )

    object settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}