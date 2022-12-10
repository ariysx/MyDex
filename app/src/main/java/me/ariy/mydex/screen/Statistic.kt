/**
 * Class that holds multiple JetPack composable component for the Statistics
 * screen
 */

package me.ariy.mydex.screen

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.myteam.MyTeamViewModelFactory
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory

@Composable
fun Statistic(
    navController: NavHostController
) {
    val context = LocalContext.current

    val pDB: PokemonViewModel = viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))
    val mDB: MyTeamViewModel = viewModel(factory = MyTeamViewModelFactory(context.applicationContext as Application))

    val pVM = pDB.pokemon.observeAsState(listOf()).value
    val countLocalPokemon = pVM.size
    var countLegendary = 0
    pVM.forEach {
        if(it.isLegendary){
            countLegendary++
        }
    }
    var countMythical = 0
    pVM.forEach {
        if(it.isMythical){
            countMythical++
        }
    }
//    val countMyTeam by remember { mutableStateOf( mDB.team.value?.size)}

    val types = listOf(
        "Normal",
        "Fire",
        "Water",
        "Grass",
        "Electric",
        "Ice",
        "Fighting",
        "Poison",
        "Ground",
        "Flying",
        "Psychic",
        "Bug",
        "Rock",
        "Ghost",
        "Dark",
        "Dragon",
        "Steel",
        "Fairy",
    )

    LazyColumn(
        modifier = Modifier.padding(8.dp, 0.dp),
        contentPadding = PaddingValues(0.dp, 4.dp)
    ) {
        item {
            Text(text = "Statistic", style = MaterialTheme.typography.h5,modifier = Modifier.padding(8.dp, 0.dp))
            StatsBar(text = "Total Pokemon: $countLocalPokemon", value = countLocalPokemon * 0.001f, color = MaterialTheme.colors.primary)
            StatsBar(text = "Legendary Pokemon: $countLegendary", value = countLegendary * 0.001f, color = MaterialTheme.colors.primary)
            StatsBar(text = "Mythical Pokemon: $countMythical", value = countMythical * 0.001f, color = MaterialTheme.colors.primary)
            Text(text = "Types", style = MaterialTheme.typography.h6)
            types.forEach {
                getTypeCount(pDB = pDB, s = it, pVM)
            }
        }
    }
}

@Composable
fun getTypeCount(pDB: PokemonViewModel, s: String, pVM: List<PokemonEntity>) {

    var count = 0
    pVM.forEach {
        if(it.type.contains(s.lowercase())){
            count++
        }
    }
    StatsBar(text = "$s Pokemon: $count", value = count * 0.001f, color = getTypeColor(text = s.lowercase()))
}