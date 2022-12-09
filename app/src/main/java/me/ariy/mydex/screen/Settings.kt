package me.ariy.mydex.screen

import android.app.Application
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory

@Composable
fun SettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: PokemonViewModel = viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))
    
    Surface() {
        Button(onClick = { viewModel.dropAll() }) {
            Text(text = "Delete All Pokemon From Database")
        }
    }
}