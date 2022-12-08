package me.ariy.mydex.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.PokemonTypeConverter
import me.ariy.mydex.data.myteam.MyTeamEntity
import me.ariy.mydex.data.myteam.MyTeamPokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.ui.theme.Green
import me.ariy.mydex.ui.theme.Red
import java.util.*

@Composable
fun ViewTeamScreen(
    name: String,
    navController: NavHostController,
    viewModel: MyTeamPokemonViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current
    val team = AppDatabase.getInstance(context).myteamDao().findById(name)
    ViewTeam(team, context, navController, viewModel)

}

@Composable
fun ViewTeam(
    team: MyTeamEntity,
    context: Context,
    navController: NavHostController,
    viewModel: MyTeamPokemonViewModel
) {
    viewModel.pokemon.clear()
    val pokemon = ArrayList<PokemonEntity>()

    var pokemons: List<String>

    if (team.pokemon.isNotEmpty()) {
        pokemons = ListTypeConverter.stringToList(team.pokemon)
        for (i in pokemons.indices) {
            pokemon.add(PokemonTypeConverter.stringToPokemonEntity(pokemons[i]))
            viewModel.add(PokemonTypeConverter.stringToPokemonEntity(pokemons[i]))
        }
    }
    var teamScore = 0f

    fun updateTeamScore() {
        for (i in pokemon.indices) {
            var pokemonScore: Float = 0f
            val pokemonStats = MapTypeConverter.stringToMap(pokemon[i].baseStats)
            pokemonStats.forEach { entry ->
                pokemonScore += entry.value.toFloat()
            }
            teamScore += pokemonScore
        }
    }
    updateTeamScore()

    Surface() {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = team.uuid)
            Text(text = "Team Combat Power: $teamScore")
            MyTeamPokemonContent(viewModel = viewModel, onCardClicked = {
                navController.navigate(
                    "team/{team}/{name}"
                        .replace(
                            oldValue = "{team}",
                            newValue = team.uuid
                        )
                        .replace(
                            oldValue = "{name}",
                            newValue = it
                        )
                )
            }, team = team, pokemons = pokemon, updateTeamScore = {
                updateTeamScore()
            })
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyTeamPokemonContent(
    viewModel: MyTeamPokemonViewModel,
    context: Context = LocalContext.current,
    dismissed: (listItem: PokemonEntity) -> Unit = {},
    onCardClicked: (uuid: String) -> Unit,
    team: MyTeamEntity,
    pokemons: ArrayList<PokemonEntity>,
    updateTeamScore: () -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = viewModel.pokemon, key = { item -> item.uid }) { pokemon ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                dismissed(pokemon)
                viewModel.remove(pokemon)
                pokemons.remove(pokemon)
                updateTeamScore()

                val pokemonString = ArrayList<String>()
                for (i in pokemons.indices) {
                    pokemonString.add(PokemonTypeConverter.pokemonEntityToString(pokemons[i]))
                }
                AppDatabase.getInstance(context).myteamDao()
                    .updatePokemon(team.uuid, ListTypeConverter.listToString(pokemonString))
                println("Removing: " + pokemon.uuid)
            }
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 1.dp),
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray.copy(0.7f)
                            DismissValue.DismissedToEnd -> Green.copy(alpha = 0.7f)
                            DismissValue.DismissedToStart -> Red.copy(alpha = 0.7f)
                        }
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Localized description",
                            modifier = Modifier.scale(scale)
                        )
                    }
                },
                dismissContent = {
                    Card(
                        elevation = 2.dp, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onClick = {
                            onCardClicked(pokemon.uid)
                        }
                    ) {
                        Column() {
                            PokemonThumbnail(thumbnail = pokemon.thumbnail)
                            var name = pokemon.nickname
                            if(name.isEmpty()){
                                name = pokemon.uuid
                            }
                            Text(text = "$name")
                        }
                    }
                }
            )
        }
    }
}