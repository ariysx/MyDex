/**
 * Class that holds multiple JetPack composable component for the View selected pokemon of a team
 * screen
 */

package me.ariy.mydex.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.PokemonTypeConverter
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.myteam.MyTeamViewModelFactory
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory
import me.ariy.mydex.ui.theme.Green
import java.util.*
import kotlin.collections.HashMap

@Composable
fun ViewTeamEditPokemonScreen(navController: NavController, team: String, uid: String) {
    val context = LocalContext.current
    val pokemonDB: PokemonViewModel =
        viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))
    val teamDB: MyTeamViewModel =
        viewModel(factory = MyTeamViewModelFactory(context.applicationContext as Application))

    val teams = teamDB.team.observeAsState(listOf()).value
    val myteam = teams.find { it.uuid == team }
    var pokemonEntity: PokemonEntity? = null

    if (myteam == null) {
        return
    }

    val stringPokemon: List<String> = ListTypeConverter.stringToList(myteam.pokemon)
    for (i in stringPokemon.indices) {
        val pokemon = PokemonTypeConverter.stringToPokemonEntity(stringPokemon[i])
        if (pokemon.uid == uid) {
            pokemonEntity = pokemon
        }
    }

    if (pokemonEntity != null) {
        val stats = remember { mutableStateMapOf<String, Int>() }
        var nickname by remember { mutableStateOf(pokemonEntity.nickname) }
        MapTypeConverter.stringToMap(pokemonEntity.baseStats).forEach { item ->
            stats[item.key] = item.value.toInt()
        }

        Surface(
        ) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                getTypeColor(ListTypeConverter.stringToList(pokemonEntity.type)[0]),
                                Color.White
                            )
                        )
                    )
            )
//            Text(text = pokemonEntity.uid)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PokemonImage(
                    thumbnail = pokemonEntity.thumbnail, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .offset(y = 60.dp)
                        .zIndex(1f)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(5)
                        )
                        .padding(15.dp, 50.dp, 15.dp, 25.dp)
                ) {
                    Text(text = pokemonEntity.uuid.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }, style = MaterialTheme.typography.h6, textAlign = TextAlign.Center, modifier = Modifier.align(CenterHorizontally))
                    TextField(
                        value = nickname, onValueChange = {
                            nickname = it
                        }, modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Give this pokemon a nickname")
                        },
                        singleLine = true
                    )
                    stats.forEach { item ->
                        Text(text = "${item.key}")
                        TextField(
                            value = "${stats[item.key]}",
                            onValueChange = {
                                stats[item.key] = it.toInt()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )
                    }
                    Button(
                        onClick = {
                            pokemonEntity.nickname = nickname
                            val newStats = HashMap<String, Int>()
                            stats.forEach { item ->
                                newStats[item.key] = item.value
                            }
                            pokemonEntity.baseStats = MapTypeConverter.mapToString(newStats)

                            val pokemonString = ArrayList<String>()
                            val pokemonList = ArrayList<PokemonEntity>()
                            for (i in stringPokemon.indices) {
                                if (pokemonEntity.uid != PokemonTypeConverter.stringToPokemonEntity(
                                        stringPokemon[i]
                                    ).uid
                                ) {
                                    pokemonList.add(
                                        PokemonTypeConverter.stringToPokemonEntity(
                                            stringPokemon[i]
                                        )
                                    )
                                }
                                if (pokemonEntity.uid == PokemonTypeConverter.stringToPokemonEntity(
                                        stringPokemon[i]
                                    ).uid
                                ) {
                                    pokemonList.add(pokemonEntity)
                                }
                            }

                            for (i in pokemonList.indices) {
                                pokemonString.add(
                                    PokemonTypeConverter.pokemonEntityToString(
                                        pokemonList[i]
                                    )
                                )
                            }

                            if (myteam != null) {
                                myteam.pokemon = ListTypeConverter.listToString(pokemonString)
                            }
                            if (myteam != null) {
                                teamDB.updateTeam(myteam)
                            }


                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green.copy(0.7f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = "Done Icon")
                        Text("Save Changes")
                    }
                }
            }
        }
    }
}

@Composable
fun ViewTeamEditPokemon() {

}