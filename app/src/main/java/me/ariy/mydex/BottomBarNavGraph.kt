package me.ariy.mydex

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.ariy.mydex.screen.*


@Composable
fun BottomBarNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.home.route
    ) {
        composable(route = BottomBarScreen.home.route){
            MyDexApp(navController = navController)
        }
        composable(route = BottomBarScreen.team.route){
            MyTeamScreen(navController = navController)
        }
        composable(route = BottomBarScreen.settings.route){
            SettingsScreen(navController = navController)
        }
        composable(route = "pokemon/{name}"){
            val name = it.arguments?.getString("name")
            name?.let {
                ViewPokemonScreen(navController = navController, name = name)
            }
        }
        composable(route = BottomBarScreen.statistic.route){
            Statistic(navController = navController)
        }
        composable(route = BottomBarScreen.compare.route){
            CompareScreen(navController = navController)
        }
        composable(route = "team/{name}"){
            val name = it.arguments?.getString("name")
            name?.let {
                ViewTeamScreen(navController = navController, name = name)
            }
        }
        composable(route = "team/{team}/{uid}"){
            val team = it.arguments?.getString("team")
            val uid = it.arguments?.getString("uid")
            uid?.let {
                if (team != null) {
                    ViewTeamEditPokemonScreen(navController = navController, uid = uid, team = team)
                }
            }
        }
        composable(route = "search/{query}"){
            val query = it.arguments?.getString("query")
            if (query != null) {
                SearchScreen(navController = navController, query = query)
            }
        }

    }
}
