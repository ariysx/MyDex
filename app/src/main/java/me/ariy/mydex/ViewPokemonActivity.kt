package me.ariy.mydex

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.ui.theme.MyDexTheme
import me.ariy.mydex.ui.theme.Purple200
import java.util.*

class ViewPokemonActivity : ComponentActivity() {
    //    val pokemonViewModel by viewModels<PokemonViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val userDao = db.userDao()
//        val users: List<User> = userDao.getAll()
        setContent {
            MyDexTheme {
                val pokemon = intent.extras?.get("pokemon") as PokemonEntity
                Screen(pokemon)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Screen(pokemonEntity: PokemonEntity) {
    val baseStats = MapTypeConverter.stringToMap(pokemonEntity.baseStats)

    Scaffold(topBar = {
//            MyDexAppTopBar()
    }) {
//        Image(
//            painter = painterResource(id = R.drawable.bg_grass),
//            contentDescription = null,
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier.fillMaxSize(),
//        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://i.imgur.com/5gIRZ10.png").crossfade(false).build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
        Box(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .verticalScroll(rememberScrollState())
//                .offset(y = .dp)
            ) {
                PokemonImage(
                    thumbnail = pokemonEntity.thumbnail, modifier = Modifier
                        .align(CenterHorizontally)
                        .offset(y = 60.dp)
                        .zIndex(1f)
                )
                Column(
                    modifier = Modifier
                        .background(color = White.copy(alpha = 0.9f), shape = RoundedCornerShape(5))
                        .padding(15.dp, 50.dp, 15.dp, 10.dp)
                ) {
                    Text(
                        text = pokemonEntity.uuid.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        modifier = Modifier.align(CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h5
                    )
                    Row(
                        modifier = Modifier.align(CenterHorizontally),
                    ) {
                        for (i in 0 until ListTypeConverter.stringToList(pokemonEntity.type).size) {
                            TypeBadge(
                                text = ListTypeConverter.stringToList(pokemonEntity.type)[i],
                                color = Gray
                            )
                        }
                    }

                    Text(
                        text = pokemonEntity.description, modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = { /*TODO*/ }, modifier = Modifier.align(CenterHorizontally)) {
                        Text(text = "Add ${pokemonEntity.uuid} to MyTeam")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Base Statistics")
                    // {"speed":"45","defense":"49","special-attack":"65","special-defense":"65","attack":"49","hp":"45"}
                    baseStats["speed"]?.let { it1 ->
                        statsBar(
                            text = "Speed", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["defense"]?.let { it1 ->
                        statsBar(
                            text = "Defense", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["hp"]?.let { it1 ->
                        statsBar(
                            text = "HP",
                            value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["special-attack"]?.let { it1 ->
                        statsBar(
                            text = "Special Attack", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["special-defense"]?.let { it1 ->
                        statsBar(
                            text = "Special Defense", value = it1.toFloat() * 0.01f
                        )
                    }
                    baseStats["attack"]?.let { it1 ->
                        statsBar(
                            text = "Attack", value = it1.toFloat() * 0.01f
                        )
                    }

                }
            }
        }
    }
}

//@Preview
@Composable
fun statsBar(text: String, value: Float) {
    Column() {
        Text(text = text)
        Column(
            modifier = Modifier.border(
                width = 2.dp, color = MaterialTheme.colors.primary, shape = RoundedCornerShape(50)
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
                    .height(16.dp), progress = value
            )
        }
//        color = Color.RED, //progress color
    }
}


@Composable
fun TypeBadge(text: String, color: androidx.compose.ui.graphics.Color) {
    val c: androidx.compose.ui.graphics.Color
    when (text) {
        "fire" -> c = Red
        "grass" -> c = Green
        "normal" -> c = Gray
        "poison" -> c = Purple200
        "flying" -> c = Blue
        "water" -> c = Cyan
        "electric" -> c = Yellow
        "bug" -> c = Green
        "ground" -> c = Gray
        "fighting" -> c = Gray
        "ghost" -> c = Purple200
        "rock" -> c = Gray
        "psychic" -> c = Purple200
        "ice" -> c = Blue
        "fairy" -> c = Magenta
        else -> c = Gray
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color = c.copy(alpha = 0.3f)),
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
    TypeBadge(text = "Fire", color = Red)
}

@Preview
@Composable
fun PreviewStatsBar() {
    statsBar(text = "Attack", value = 0.07f)
}

@Composable
fun PokemonImage(thumbnail: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(thumbnail).crossfade(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED).build(),
        contentDescription = "Pokemon Image",
        contentScale = ContentScale.Crop,
        alignment = Center,
        modifier = modifier
            .size(256.dp)
            .padding(8.dp)
//            .clip(RoundedCornerShape(50)),
    )
}