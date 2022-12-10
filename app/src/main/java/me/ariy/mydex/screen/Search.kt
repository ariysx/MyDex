package me.ariy.mydex.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory

@Composable
fun SearchScreen(navController: NavHostController, query: String) {
    val context = LocalContext.current
    val pokemonViewModel: PokemonViewModel = viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))
    var type = ""
    when(query) {
        "normal" -> type = "normal"
        "fire" -> type = "fire"
        "water" -> type = "water"
        "electric" -> type = "electric"
        "ice" -> type = "ice"
        "fighting" -> type = "fighting"
        "poison" -> type = "poison"
        "ground" -> type = "ground"
        "flying" -> type = "flying"
        "psychic" -> type = "psychic"
        "bug" -> type = "bug"
        "rock" -> type = "rock"
        "ghost" -> type = "ghost"
        "dark" -> type = "dark"
        "dragon" -> type = "dragon"
        "steel" -> type = "steel"
        "fairy" -> type = "fairy"
        "grass" -> type = "grass"
    }

    var items: List<PokemonEntity> = if(type.isNotEmpty()){
        pokemonViewModel.searchByType(type)
    } else {
        if(query == "legendary"){
            pokemonViewModel.searchLegendary()
        } else if (query == "mythical"){
            pokemonViewModel.searchMythical()
        } else {
            pokemonViewModel.searchByName(query)
        }
    }

    Surface() {
        Column(modifier = Modifier.padding(8.dp, 8.dp)){
            Text(text = "Search", style = MaterialTheme.typography.h5)
            Text(text = "Result for: $query", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.padding(8.dp))
        }
        if(items.isNotEmpty()){
            PokemonList(pokemon = items, navController = navController)
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "No results for $query", textAlign = TextAlign.Center)
            }
        }
    }
}