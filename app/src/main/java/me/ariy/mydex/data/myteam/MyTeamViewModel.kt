package me.ariy.mydex.data.myteam

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import me.ariy.mydex.data.pokemon.PokemonEntity

class MyTeamViewModel : ViewModel() {
    var team = mutableStateListOf<MyTeamEntity>()

    fun addTeam(myTeamEntity: MyTeamEntity) {
        team.add(myTeamEntity)
    }

    fun clear(){
        team.clear()
    }

    fun removeTeam(myTeamEntity: MyTeamEntity) {
        team.remove(myTeamEntity)
    }
}