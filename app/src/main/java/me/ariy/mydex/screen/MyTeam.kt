package me.ariy.mydex.screen

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.DismissDirection.EndToStart
import androidx.compose.material.DismissDirection.StartToEnd
import androidx.compose.material.DismissValue.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.PokemonTypeConverter
import me.ariy.mydex.data.myteam.MyTeamEntity
import me.ariy.mydex.data.myteam.MyTeamRepository
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.pokemon.MyTeamViewModelFactory
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.ui.theme.Green
import me.ariy.mydex.ui.theme.Red
import java.util.*
import kotlin.concurrent.thread

@Composable
fun MyTeamScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
//    thread {
//        println("${Thread.currentThread()} has run.")
//    }

    var viewModel: MyTeamViewModel =
        viewModel(factory = MyTeamViewModelFactory(context.applicationContext as Application))

    MyTeamList(viewModel, navController, context)
}

@Composable
fun MyTeamList(viewModel: MyTeamViewModel, navController: NavHostController, context: Context) {
    Surface() {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            Text(text = "MyTeam", style = MaterialTheme.typography.h5)

            MyTeamAddBar(onCloseClicked = {

            }, onAddClicked = {
                if (it.isEmpty()) {
                    return@MyTeamAddBar
                }
                var pokemon = ArrayList<String>()

                viewModel.addTeam(
                    MyTeamEntity(
                        name = it,
                        pokemon = ListTypeConverter.listToString(pokemon)
                    )
                )
            })

            MyContent(viewModel = viewModel, onCardClicked = {
                navController.navigate(
                    "team/{name}"
                        .replace(
                            oldValue = "{name}",
                            newValue = it
                        )
                )
            })
        }
    }
}

@Composable
fun MyTeamAddBar(
    onSearch: (String) -> Unit = {},
    onCloseClicked: () -> Unit,
    onAddClicked: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(50),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.background,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(50),
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = "Insert team name",
                    color = Color.Black
                )
            },
//            textStyle =
            singleLine = true,
            trailingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    onClick = {
                        if (text.isNotEmpty()) {
                            text = ""
                            onCloseClicked()
                        }
                    })
                {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddClicked(text)
                }
            )
        )
    }
}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyContent(
    viewModel: MyTeamViewModel,
    dismissed: (listItem: MyTeamEntity) -> Unit = {},
    onCardClicked: (uuid: String) -> Unit,
) {

    val items = viewModel.team.observeAsState(listOf()).value

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = items, key = { item -> item.uuid }) { team ->
            var teamScore = 0
            if(team.pokemon.isNotEmpty()){

                var pokemons = ListTypeConverter.stringToList(team.pokemon)
                for (i in pokemons.indices) {
                    val pokemon =
                        PokemonTypeConverter.stringToPokemonEntity(pokemons[i])
                    teamScore += getCombatPower(pokemon)
                }
            }

            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(EndToStart)) {
                dismissed(team)
                viewModel.removeTeam(team)
                println("Removing: " + team.uuid)
            }
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 1.dp),
                directions = setOf(EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == StartToEnd) 0.25f else 0.5f)
                },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            Default -> Color.LightGray.copy(0.7f)
                            DismissedToEnd -> Green.copy(alpha = 0.7f)
                            DismissedToStart -> Red.copy(alpha = 0.7f)
                        }
                    )
                    val alignment = when (direction) {
                        StartToEnd -> Alignment.CenterStart
                        EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        StartToEnd -> Icons.Default.Done
                        EndToStart -> Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == Default) 0.75f else 1f
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
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            if (viewModel.findById(team.uuid) != null) {
                                onCardClicked(team.uuid)
                            }
                        }
                    ) {
                        var bg: Color = Color.White
//                        if(team.pokemon != null || team.pokemon != ""){
//                            val firstPokemon = ListTypeConverter.stringToList(team.pokemon)[0]
//                            val firstPokemonType = PokemonTypeConverter.stringToPokemonEntity(firstPokemon).type
//                            val firstType = ListTypeConverter.stringToList(firstPokemonType)[0]
//                            bg = getTypeColor(text = firstType)
//                        }
                        if(team.pokemon.isNotEmpty()){

                            var pokemons = ListTypeConverter.stringToList(team.pokemon)
                            for (i in pokemons.indices) {

                                val pokemon =
                                    PokemonTypeConverter.stringToPokemonEntity(pokemons[i])
                                val type = ListTypeConverter.stringToList(pokemon.type)
                                bg = getTypeColor(text = type[0])
                                break
                            }
                        }

                        Column(modifier = Modifier
                            .background(
                                color = bg.copy(0.3f))) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(text = team.name, style = MaterialTheme.typography.h6)
                                Text(text = "$teamScore Team Combat Power", style = MaterialTheme.typography.body1)
                            }

                            FlowRow() {
                                if (team.pokemon.isNotEmpty()) {
                                    var pokemons = ListTypeConverter.stringToList(team.pokemon)
                                    for (i in pokemons.indices) {
                                        val pokemon =
                                            PokemonTypeConverter.stringToPokemonEntity(pokemons[i])
                                        PokemonThumbnail(
                                            thumbnail = pokemon.thumbnail, modifier = Modifier
                                                .size(52.dp)
                                                .padding(0.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}