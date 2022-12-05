package me.ariy.mydex.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.*
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.myteam.MyTeamEntity
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.ui.theme.Green
import me.ariy.mydex.ui.theme.Purple200
import me.ariy.mydex.ui.theme.Red
import me.ariy.mydex.ui.theme.Teal
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ViewPokemonScreen(name: String, navController: NavHostController) {

    val context = LocalContext.current
//    val pokemons = mutableListOf<PokemonEntity>()

//    CoroutineScope(Dispatchers.Default).launch { // Use Dispatchers.IO for database or file I/O
//        val pokemon = findPokemon(context, name).await()
//        measureTimeMillis {
//            Log.d("TAG", "sum=${pokemon}")
//        }.also {
//            Log.d("TAG", "Completed in $it ms")
//            pokemons.add(pokemon)
//        }
//    }

    val pokemon = AppDatabase.getInstance(context).pokemonDao().findByName(name)

    Screen(pokemon, context, navController)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Screen(pokemonEntity: PokemonEntity, context: Context, navController: NavHostController) {
    val baseStats = MapTypeConverter.stringToMap(pokemonEntity.baseStats)
    val type = ListTypeConverter.stringToList(pokemonEntity.type)
    var bg = Color.Gray
    when (type[0]) {
        "fire" -> bg = me.ariy.mydex.ui.theme.DeepOrange
        "grass" -> bg = me.ariy.mydex.ui.theme.Green
        "normal" -> bg = me.ariy.mydex.ui.theme.Gray
        "poison" -> bg = me.ariy.mydex.ui.theme.Purple
        "flying" -> bg = me.ariy.mydex.ui.theme.BlueGray
        "water" -> bg = me.ariy.mydex.ui.theme.Cyan
        "electric" -> bg = me.ariy.mydex.ui.theme.Yellow
        "bug" -> bg = me.ariy.mydex.ui.theme.Lime
        "ground" -> bg = me.ariy.mydex.ui.theme.Gray
        "fighting" -> bg = me.ariy.mydex.ui.theme.Red
        "ghost" -> bg = Purple200
        "rock" -> bg = Color.Gray
        "psychic" -> bg = Purple200
        "ice" -> bg = me.ariy.mydex.ui.theme.Indigo
        "fairy" -> bg = Color.Magenta
        "dragon" -> bg = Teal
        else -> bg = Color.Gray
    }


    Surface(
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.bg_grass),
//            contentDescription = null,
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier.fillMaxSize(),
//        )
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(R.drawable.bg_grass).crossfade(false).build(),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillWidth
//        )
        Box(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(bg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(15.dp)
                    .verticalScroll(rememberScrollState())
//                .offset(y = .dp)
            ) {
                PokemonImage(
                    thumbnail = pokemonEntity.thumbnail, modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .offset(y = 60.dp)
                        .zIndex(1f)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(5)
                        )
                        .padding(15.dp, 50.dp, 15.dp, 25.dp)
                ) {
                    Text(
                        text = pokemonEntity.uuid.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h5
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    ) {
                        for (i in 0 until ListTypeConverter.stringToList(pokemonEntity.type).size) {
                            TypeBadge(
                                text = ListTypeConverter.stringToList(pokemonEntity.type)[i],
                                color = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = pokemonEntity.description, modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp), horizontalArrangement = Arrangement.Center
                    )
                    {
                        Column() {
                            Text(text = "Height")
                            Text(text = pokemonEntity.height.toString() + " decimetres")
                        }
                        Column() {
                            Text(text = "Weight")
                            Text(text = pokemonEntity.weight.toString() + " hectograms")
                        }
                    }

                    if(AppDatabase.getInstance(context).myteamDao().findByName(pokemonEntity.uuid)!=null){
                        Button(
                            onClick = {
                                AppDatabase.getInstance(context = context).myteamDao().deleteOne(
                                    MyTeamEntity(pokemonEntity.uuid)
                                )
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Red.copy(alpha = 0.6f),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Navigation Icon",
                            )
                            Text(
                                text = "Remove ${
                                    pokemonEntity.uuid.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    }
                                } from MyTeam"
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                AppDatabase.getInstance(context = context).myteamDao().insertOne(
                                    MyTeamEntity(pokemonEntity.uuid)
                                )
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Green.copy(alpha = 0.6f),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Navigation Icon",
                            )
                            Text(
                                text = "Add ${
                                    pokemonEntity.uuid.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    }
                                } to MyTeam"
                            )
                        }
                    }

                    Text(text = "Evolutions", style = MaterialTheme.typography.h6)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp), horizontalArrangement = Arrangement.Center
                    ) {
                        if (ListTypeConverter.stringToList(pokemonEntity.nextEvolution).size != 1) {
                            for (nextEvo in ListTypeConverter.stringToList(pokemonEntity.nextEvolution)) {
                                Card(onClick = {
                                    navController.navigate(
                                        "pokemon/{name}"
                                            .replace(
                                                oldValue = "{name}",
                                                newValue = nextEvo
                                            )
                                    )
                                }, modifier = Modifier.background(Color.Transparent).padding(4.dp)){
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    )
                                    {
                                        if (AppDatabase.getInstance(LocalContext.current).pokemonDao()
                                                .findByName(nextEvo) != null
                                        ) {
                                            Thumbnail(
                                                thumbnail = AppDatabase.getInstance(LocalContext.current)
                                                    .pokemonDao().findByName(nextEvo).thumbnail,
                                            )
                                        }
                                        Text(text = nextEvo.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.ROOT
                                            ) else it.toString()
                                        }, textAlign = TextAlign.Center)
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "This pokemon has no evolution",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Text(text = "Abilities", style = MaterialTheme.typography.h6)
                    Row() {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            for(ability in ListTypeConverter.stringToList(pokemonEntity.abilities)){
                                Text(text = ability)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Base Statistics",
                        style = MaterialTheme.typography.h6
                    )
                    // {"speed":"45","defense":"49","special-attack":"65","special-defense":"65","attack":"49","hp":"45"}
                    baseStats["speed"]?.let { it1 ->
                        StatsBar(
                            text = "Speed: $it1", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["defense"]?.let { it1 ->
                        StatsBar(
                            text = "Defense: $it1", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["hp"]?.let { it1 ->
                        StatsBar(
                            text = "HP: $it1",
                            value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["special-attack"]?.let { it1 ->
                        StatsBar(
                            text = "Special Attack: $it1", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["special-defense"]?.let { it1 ->
                        StatsBar(
                            text = "Special Defense: $it1", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["attack"]?.let { it1 ->
                        StatsBar(
                            text = "Attack: $it1", value = it1.toFloat() * 0.01f
                        )
                    }

                }
            }
        }
    }
}

//@Preview
@Composable
fun StatsBar(text: String, value: Float) {
    Column() {
        Text(text = text)
        Column(
            modifier = Modifier.border(
                width = 0.dp, color = Color.White, shape = RoundedCornerShape(50)
            )
        ) {
//            LinearProgressIndicator(
//                progress = 0.7f,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(15.dp),
//            )
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .height(5.dp), progress = value
            )
        }
//        color = Color.RED, //progress color
    }
}


@Composable
fun TypeBadge(text: String, color: androidx.compose.ui.graphics.Color) {
    val c: androidx.compose.ui.graphics.Color
    when (text) {
        "fire" -> c = me.ariy.mydex.ui.theme.DeepOrange
        "grass" -> c = me.ariy.mydex.ui.theme.Green
        "normal" -> c = me.ariy.mydex.ui.theme.Gray
        "poison" -> c = me.ariy.mydex.ui.theme.Purple
        "flying" -> c = me.ariy.mydex.ui.theme.BlueGray
        "water" -> c = me.ariy.mydex.ui.theme.Cyan
        "electric" -> c = me.ariy.mydex.ui.theme.Yellow
        "bug" -> c = me.ariy.mydex.ui.theme.Lime
        "ground" -> c = me.ariy.mydex.ui.theme.Gray
        "fighting" -> c = me.ariy.mydex.ui.theme.Red
        "ghost" -> c = Purple200
        "rock" -> c = Color.Gray
        "psychic" -> c = Purple200
        "ice" -> c = me.ariy.mydex.ui.theme.Indigo
        "fairy" -> c = Color.Magenta
        else -> c = Color.Gray
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color = c.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            modifier = Modifier.padding(15.dp, 5.dp)
        )
    }
}


@Preview
@Composable
fun PreviewTypeBadge() {
    TypeBadge(text = "Fire", color = Color.Red)
}

@Preview
@Composable
fun PreviewStatsBar() {
    StatsBar(text = "Attack", value = 0.07f)
}

@Composable
fun PokemonImage(thumbnail: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(thumbnail).crossfade(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED).build(),
        contentDescription = "Pokemon Image",
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
        modifier = modifier
            .size(256.dp)
            .padding(8.dp)
//            .clip(RoundedCornerShape(50)),
    )
}

@Composable
fun Thumbnail(thumbnail: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(thumbnail).crossfade(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED).build(),
        contentDescription = "Pokemon Image",
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .padding(8.dp)
//            .clip(RoundedCornerShape(50)),
    )
}