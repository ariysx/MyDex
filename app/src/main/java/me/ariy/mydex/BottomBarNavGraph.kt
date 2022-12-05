package me.ariy.mydex

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.ariy.mydex.screen.MyDexApp
import me.ariy.mydex.screen.MyTeamScreen
import me.ariy.mydex.screen.ViewPokemonScreen


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
            MyTeamScreen(navController = navController)
        }
        composable(route = "pokemon/{name}"){
            val name = it.arguments?.getString("name")
            name?.let {
                ViewPokemonScreen(navController = navController, name = name)
            }
        }
    }
}
