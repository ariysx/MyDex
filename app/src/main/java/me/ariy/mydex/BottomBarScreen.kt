package me.ariy.mydex

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object home : BottomBarScreen(
        route = "home",
        title = "Pokedex",
        icon = R.drawable.ic_baseline_home_24
    )

    object team : BottomBarScreen(
        route = "team",
        title = "MyTeam",
        icon = R.drawable.ic_baseline_groups_24
    )

    object settings : BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = R.drawable.ic_baseline_settings_24
    )

    object statistic : BottomBarScreen(
        route = "statistic",
        title = "Statistic",
        icon = R.drawable.ic_baseline_auto_graph_24
    )
    
    object compare : BottomBarScreen(
        route = "compare",
        title = "Compare",
        icon = R.drawable.ic_baseline_compare_arrows_24
    )

}