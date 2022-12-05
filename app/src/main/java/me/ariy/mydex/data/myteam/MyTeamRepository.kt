package me.ariy.mydex.data.myteam

import android.content.Context
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.pokemon.PokemonEntity

object MyTeamRepository {
    fun load(context: Context, viewModel: MyTeamViewModel) {
        val pokemondb = AppDatabase.getInstance(context).pokemonDao()
        val myteamdb = AppDatabase.getInstance(context).myteamDao()

        for (pokemon in myteamdb.getAll()) {
            viewModel.addPokemon(pokemondb.findByName(pokemon.uuid))
        }
    }

    fun remove(viewModel: MyTeamViewModel, context: Context, pokemonEntity: PokemonEntity){
        viewModel.removePokemon(pokemonEntity)
        AppDatabase.getInstance(context).myteamDao().deleteOne(MyTeamEntity(pokemonEntity.uuid))
    }
}