package me.ariy.mydex.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.PokemonTypeConverter
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.ui.theme.Green
import java.util.ArrayList

@Composable
fun ViewTeamEditPokemonScreen(navController: NavController, team: String, uid: String) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val pokemonDB = db.pokemonDao()
    val teamDB = db.myteamDao()

    val team = teamDB.findById(team)
    var pokemonEntity: PokemonEntity? = null

    val stringPokemon: List<String> = ListTypeConverter.stringToList(team.pokemon)
    for (i in stringPokemon.indices) {
        val pokemon = PokemonTypeConverter.stringToPokemonEntity(stringPokemon[i])
        if (pokemon.uid == uid) {
            pokemonEntity = pokemon
        }
    }

    if (pokemonEntity != null) {
        val stats = remember { mutableStateMapOf<String, Float>() }
        var nickname by remember { mutableStateOf(pokemonEntity.nickname) }
        MapTypeConverter.stringToMap(pokemonEntity.baseStats).forEach { item ->
            stats[item.key] = item.value.toFloat()
        }

        Surface() {
//            Text(text = pokemonEntity.uid)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    PokemonImage(thumbnail = pokemonEntity.thumbnail)
                    Text(text = pokemonEntity.uuid)
                    TextField(value = nickname, onValueChange = {
                        nickname = it
                    }, modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Give this pokemon a nickname")
                        },
                        singleLine = true)
                    stats.forEach { item ->
                        Text(text = "${item.key}")
                        TextField(
                            value = "${stats[item.key]}",
                            onValueChange = {
                                stats[item.key] = it.toFloat()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )
                    }
                    Button(
                        onClick = {
                            pokemonEntity.nickname = nickname
                            val newStats = HashMap<String, String>()
                            stats.forEach {
                                item ->
                                newStats[item.key] = item.value.toString()
                            }
                            pokemonEntity.baseStats = MapTypeConverter.mapToString(newStats)

                            val pokemonString = ArrayList<String>()
                            val pokemonList = ArrayList<PokemonEntity>()
                            for(i in stringPokemon.indices){
                                if(pokemonEntity.uid != PokemonTypeConverter.stringToPokemonEntity(
                                        stringPokemon[i]
                                    ).uid) {
                                    pokemonList.add(PokemonTypeConverter.stringToPokemonEntity(stringPokemon[i]))
                                }
                                if(pokemonEntity.uid == PokemonTypeConverter.stringToPokemonEntity(
                                        stringPokemon[i]
                                    ).uid) {
                                    pokemonList.add(pokemonEntity)
                                }
                            }

                            for(i in pokemonList.indices){
                                pokemonString.add(PokemonTypeConverter.pokemonEntityToString(
                                    pokemonList[i]
                                ))
                            }

                            AppDatabase.getInstance(context).myteamDao()
                                .updatePokemon(team.uuid, ListTypeConverter.listToString(pokemonString))



                        },
                        modifier = Modifier,
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