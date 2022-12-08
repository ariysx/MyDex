package me.ariy.mydex.data.myteam

import android.content.Context
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.pokemon.PokemonEntity

object MyTeamRepository {
    fun load(context: Context, viewModel: MyTeamViewModel) {
        val pokemondb = AppDatabase.getInstance(context).pokemonDao()
        val myteamdb = AppDatabase.getInstance(context).myteamDao()

        for (team in myteamdb.getAll()) {
            viewModel.addTeam(team)
        }
    }

    fun remove(viewModel: MyTeamViewModel, context: Context, myTeamEntity: MyTeamEntity){
        viewModel.removeTeam(myTeamEntity)
        AppDatabase.getInstance(context).myteamDao().deleteOne(myTeamEntity)
    }

    fun add(viewModel: MyTeamViewModel, context: Context, myTeamEntity: MyTeamEntity) {
        viewModel.addTeam(myTeamEntity)
        AppDatabase.getInstance(context).myteamDao().insertOne(myTeamEntity)
    }
}