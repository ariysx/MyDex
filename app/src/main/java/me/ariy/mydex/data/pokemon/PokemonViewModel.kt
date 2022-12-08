package me.ariy.mydex.data.pokemon

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>(). applicationContext

    var pokemons = mutableStateListOf<PokemonEntity>()

    init {
        PokemonRepository.loadLocal(context,pokemons)
    }

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