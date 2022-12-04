package me.ariy.mydex.data.pokemon

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PokemonViewModel : ViewModel() {
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