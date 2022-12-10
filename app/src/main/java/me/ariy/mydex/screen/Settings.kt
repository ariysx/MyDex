package me.ariy.mydex.screen

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.pokemon.MyTeamViewModelFactory
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: PokemonViewModel =
        viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))

    val teamModel: MyTeamViewModel =
        viewModel(factory = MyTeamViewModelFactory(context.applicationContext as Application))

    val uriHandler = LocalUriHandler.current
    Surface() {
        Column() {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(8.dp, 0.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { viewModel.dropAll() }) {
                    Text(text = "Reset Pokemon Database")
                }
                Button(onClick = { teamModel.removeAll() }) {
                    Text(text = "Reset MyTeam Database")
                }
                Text(text = "Made with ❤ by Rin")
                Button(onClick = {
                    uriHandler.openUri("https://github.com/ariysx")
                }) {
                    Text(text = "GitHub")
                }
            }
        }
    }
}