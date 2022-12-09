package me.ariy.mydex.screen

import android.app.Application
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
        pokemonViewModel.searchByName(query)
    }

    Surface() {
        Text(text = "Search Result for: $query", style = MaterialTheme.typography.h6)
        PokemonList(pokemon = items, navController = navController)   
    }
}