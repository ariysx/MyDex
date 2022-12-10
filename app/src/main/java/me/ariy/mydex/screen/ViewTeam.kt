/**
 * Class that holds multiple JetPack composable component for the View the selected team
 * screen
 */

package me.ariy.mydex.screen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.PokemonTypeConverter
import me.ariy.mydex.data.myteam.MyTeamEntity
import me.ariy.mydex.data.myteam.MyTeamPokemonViewModel
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.myteam.MyTeamViewModelFactory
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.ui.theme.Green
import me.ariy.mydex.ui.theme.Red
import java.util.*

@Composable
fun ViewTeamScreen(
    name: String,
    navController: NavHostController,
) {

    val context = LocalContext.current
    val teamViewModel: MyTeamViewModel =
        viewModel(factory = MyTeamViewModelFactory(context.applicationContext as Application))
    val teams = teamViewModel.team.observeAsState(listOf()).value
    println(name)
    val team = teams.find { it.uuid == name }

    val myTeamPokemonViewModel: MyTeamPokemonViewModel = viewModel()
    println(team)
    if (team != null) {
        ViewTeam(teamViewModel = teamViewModel, team, navController, myTeamPokemonViewModel)
    }

}

@Composable
fun ViewTeam(
    teamViewModel: MyTeamViewModel,
    team: MyTeamEntity,
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
    var teamScore = 0

    fun updateTeamScore() {
        for (i in pokemon.indices) {
            var pokemonScore = 0
            val pokemonStats = MapTypeConverter.stringToMap(pokemon[i].baseStats)
            pokemonStats.forEach { entry ->
                pokemonScore += entry.value.toInt()
            }
            teamScore += pokemonScore
        }
    }
    updateTeamScore()

    Surface() {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(8.dp, 0.dp)
            )
            Text(text = "Team Combat Power: ${teamScore}", modifier = Modifier.padding(8.dp, 0.dp))
            if (viewModel.pokemon.isEmpty() || viewModel.pokemon.size == 0) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "This team does not have a pokemon.\nGo to Pokedex and get started!", textAlign = TextAlign.Center)
                    Button(onClick = {
                        navController.navigate("home")
                    }, shape = RoundedCornerShape(50)) {
                        Text(text = "Go to Pokedex")
                    }
                }
            }
            MyTeamPokemonContent(
                teamViewModel = teamViewModel,
                viewModel = viewModel,
                onCardClicked = {
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
                },
                team = team,
                pokemons = pokemon,
                updateTeamScore = {
                    updateTeamScore()
                },
                navController = navController
            )
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyTeamPokemonContent(
    teamViewModel: MyTeamViewModel,
    viewModel: MyTeamPokemonViewModel,
    context: Context = LocalContext.current,
    dismissed: (listItem: PokemonEntity) -> Unit = {},
    onCardClicked: (uuid: String) -> Unit,
    team: MyTeamEntity,
    pokemons: ArrayList<PokemonEntity>,
    updateTeamScore: () -> Unit,
    navController: NavHostController,
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
                team.pokemon = ListTypeConverter.listToString(pokemonString)
                teamViewModel.updateTeam(team)

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
                        Row(
                            modifier = Modifier.background(
                                color = getTypeColor(
                                    text = ListTypeConverter.stringToList(
                                        pokemon.type
                                    )[0]
                                ).copy(0.3f)
                            )
                        ) {
                            PokemonThumbnail(thumbnail = pokemon.thumbnail)
                            var name = pokemon.nickname
                            if (name.isEmpty()) {
                                name = pokemon.uuid
                            }
                            Column() {
                                Text(text = name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                }, style = MaterialTheme.typography.h6)
                                val cp = getCombatPower(pokemon)
                                Text(text = "$cp Combat Power")
                            }
                        }
                    }
                }
            )
        }
    }
}