/**
 * Stores objects of navigation pages including route, title, and icon
 * for the bottom navigation bar
 */

package me.ariy.mydex

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