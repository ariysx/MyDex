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
import androidx.compose.material.DismissDirection.EndToStart
import androidx.compose.material.DismissDirection.StartToEnd
import androidx.compose.material.DismissValue.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.ariy.mydex.data.myteam.MyTeamRepository
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.ui.theme.Green
import me.ariy.mydex.ui.theme.Red
import java.util.*
import kotlin.concurrent.thread

@Composable
fun MyTeamScreen(
    viewModel: MyTeamViewModel = MyTeamViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
//    thread {
//        println("${Thread.currentThread()} has run.")
//    }
    MyTeamRepository.load(context, viewModel)
    MyTeamList(viewModel, navController)
}

@Composable
fun MyTeamList(viewModel: MyTeamViewModel, navController: NavHostController) {
    Surface() {
        Column() {
            Text(text = "MyTeam", style = MaterialTheme.typography.h5)
            MyContent(viewModel = viewModel, onCardClicked = {
                navController.navigate(
                    "pokemon/{name}"
                        .replace(
                            oldValue = "{name}",
                            newValue = it
                        )
                )
            })
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyContent(
    viewModel: MyTeamViewModel,
    context: Context = LocalContext.current,
    dismissed: (listItem: PokemonEntity) -> Unit = {},
    onCardClicked: (name: String) -> Unit,
) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = viewModel.pokemons, key = {item -> item.uuid}) { pokemon ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(EndToStart)) {
                dismissed(pokemon)
                MyTeamRepository.remove(viewModel = viewModel, context = context, pokemonEntity = pokemon)
                println("Removing: " + pokemon.uuid)
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
                        onClick = {onCardClicked(pokemon.uuid)}
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            PokemonThumbnail(thumbnail = pokemon.thumbnail)
                            Text(text = pokemon.uuid.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            }, style = MaterialTheme.typography.h6)
                        }
                    }
                }
            )
        }
    }
}