package me.ariy.mydex.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import kotlinx.coroutines.launch
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.data.pokemon.PokemonRepository
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory
import me.ariy.mydex.ui.theme.Green

@Composable
fun CompareScreen(navController: NavHostController) {

    val context = LocalContext.current
    val viewModel: PokemonViewModel = viewModel(
        factory = PokemonViewModelFactory(context.applicationContext as Application)
    )
    val items = viewModel.pokemon.observeAsState(listOf()).value

    val pokemon = ArrayList<String>()
    items.forEach {
        pokemon.add(it.uuid)
    }

    var pokemon1 = remember { mutableStateOf( PokemonEntity()) }
    var pokemon2 = remember { mutableStateOf( PokemonEntity()) }

    var result by remember { mutableStateOf("") }

    Surface() {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "Compare Pokemon", style = MaterialTheme.typography.h5)
            SearchableExpandedDropDownMenu(
                listOfItems = pokemon, // provide the list of items of any type you want to populated in the dropdown,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 2.dp, ),
                onDropDownItemSelected = { item -> // Returns the item selected in the dropdown
                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                    pokemon1.value = viewModel.getPokemon(item)
                },
                enable = false,
                placeholder = "Select a pokemon",
                openedIcon = Icons.Default.Close,
                closedIcon = Icons.Default.ArrowDropDown,
                parentTextFieldCornerRadius = 12.dp,
                dropdownItem = { name -> // Provide a Composable that will be used to populate the dropdown and that takes a type i.e String,Int or even a custom type
                    Text(name)
                },
            )

            SearchableExpandedDropDownMenu(
                listOfItems = pokemon, // provide the list of items of any type you want to populated in the dropdown,
                modifier = Modifier.fillMaxWidth().padding(0.dp, 2.dp),
                onDropDownItemSelected = { item -> // Returns the item selected in the dropdown
                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                    pokemon2.value = viewModel.getPokemon(item)
                },
                enable = false,
                placeholder = "Select a pokemon",
                openedIcon = Icons.Default.Close,
                closedIcon = Icons.Default.ArrowDropDown,
                parentTextFieldCornerRadius = 12.dp,
                dropdownItem = { name -> // Provide a Composable that will be used to populate the dropdown and that takes a type i.e String,Int or even a custom type
                    Text(name)
                },
            )
            val statsName = listOf("hp", "attack", "defense", "speed", "special-attack", "special-defense")

            Button(onClick = {
                if(pokemon1.value.uuid.isNotEmpty() && pokemon2.value.uuid.isNotEmpty()){
                    var stats1 = MapTypeConverter.stringToMap(pokemon1.value.baseStats)
                    var stats2 = MapTypeConverter.stringToMap(pokemon2.value.baseStats)

                    var p1Score = 0
                    var p2Score = 0

                    statsName.forEachIndexed {
                            i, e ->
                        p1Score += stats1[statsName[i]]?.toInt()!!
                        p2Score += stats2[statsName[i]]?.toInt()!!
                    }
                    if(p1Score > p2Score){
//                        Toast.makeText(context, "${pokemon1.value.uuid} is overall better than ${pokemon2.value.uuid} ", Toast.LENGTH_SHORT).show()
                        result = "${pokemon1.value.uuid} is overall better than ${pokemon2.value.uuid}"
                    } else {
//                        Toast.makeText(context, "${pokemon2.value.uuid} is overall better than ${pokemon1.value.uuid} ", Toast.LENGTH_SHORT).show()
                        result = "${pokemon2.value.uuid} is overall better than ${pokemon1.value.uuid}"
                    }
                }
            }, modifier = Modifier.align(CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Green.copy(0.7f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(text = "Compare Pokemon")
            }
            Column(modifier= Modifier.fillMaxSize(),
//                contentAlignment = Alignment.TopCenter
            )
            {
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = result, textAlign = TextAlign.Center, modifier = Modifier.align(CenterHorizontally))
                Spacer(modifier = Modifier.padding(10.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        if(pokemon1.value.uuid.isNotEmpty()) {
                            Column(modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally) {

                                PokemonThumbnail(thumbnail = pokemon1.value.thumbnail)
                                Text(text = pokemon1.value.uuid)
                            }
                        }
                    }
                    item {
                        if(pokemon2.value.uuid.isNotEmpty()) {
                            Column(modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                PokemonThumbnail(thumbnail = pokemon2.value.thumbnail)
                                Text(text = pokemon2.value.uuid)
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    statsName.forEachIndexed { index, it ->
                        if(pokemon1.value.uuid.isNotEmpty()){
                            var stats = MapTypeConverter.stringToMap(pokemon1.value.baseStats)
                            stats[it]?.let { StatsBar(text = "${pokemon1.value.uuid} ${statsName[index]}: $it", value = it.toFloat() * 0.0075f, color = getTypeColor(
                                text = ListTypeConverter.stringToList(pokemon1.value.type)[0]
                            )) }
                        }
                        if(pokemon2.value.uuid.isNotEmpty()){
                            var stats = MapTypeConverter.stringToMap(pokemon2.value.baseStats)
                            stats[it]?.let { StatsBar(text = "${pokemon2.value.uuid} ${statsName[index]}: $it", value = it.toFloat() * 0.0075f, color = getTypeColor(
                                text = ListTypeConverter.stringToList(pokemon2.value.type)[0]
                            )) }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                    }
                }
            }
        }
    }
}

fun getCombatPower(pokemonEntity: PokemonEntity): Int {
    val statsName = listOf("hp", "attack", "defense", "speed", "special-attack", "special-defense")

    var score = 0

    val stats = MapTypeConverter.stringToMap(pokemonEntity.baseStats)

    statsName.forEachIndexed {
            i, e ->
        score += stats[e]?.toInt()!!
    }

    return score
}