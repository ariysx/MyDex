package me.ariy.mydex.data.myteam

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import me.ariy.mydex.data.pokemon.PokemonEntity

class MyTeamViewModel : ViewModel() {
    var pokemons = mutableStateListOf<PokemonEntity>()

    fun addPokemon(pokemonEntity: PokemonEntity) {
        pokemons.add(pokemonEntity)
    }

    fun clear(){
        pokemons.clear()
    }

    fun removePokemon(pokemonEntity: PokemonEntity) {
        pokemons.remove(pokemonEntity)
    }
}