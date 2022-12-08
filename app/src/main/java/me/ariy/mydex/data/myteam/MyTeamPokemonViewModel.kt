package me.ariy.mydex.data.myteam

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import me.ariy.mydex.data.pokemon.PokemonEntity

class MyTeamPokemonViewModel : ViewModel() {
    var pokemon = mutableStateListOf<PokemonEntity>()

    fun remove(pokemonEntity: PokemonEntity) {
        pokemon.remove(pokemonEntity)
    }

    fun add(pokemonEntity: PokemonEntity){
        pokemon.add(pokemonEntity)
    }
}