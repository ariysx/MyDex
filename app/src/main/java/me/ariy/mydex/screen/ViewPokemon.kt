/**
 * Class that holds multiple JetPack composable component for the ViewPokemon
 * screen
 */

package me.ariy.mydex.screen

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetError
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetSuccess
import kotlinx.coroutines.launch
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.PokemonTypeConverter
import me.ariy.mydex.data.myteam.MyTeamEntity
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.myteam.MyTeamViewModelFactory
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory
import me.ariy.mydex.ui.theme.*
import java.util.*

@Composable
fun ViewPokemonScreen(name: String, navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: PokemonViewModel =
        viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))
    val observePokemon = viewModel.pokemon.observeAsState().value
    val pokemon = observePokemon?.find { it.uuid == name }
    if (pokemon != null) {
        if(pokemon.uuid.isNotEmpty()){
            Screen(viewModel, context, navController, pokemon, observePokemon)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Screen(
    viewModel: PokemonViewModel,
    context: Context,
    navController: NavHostController,
    pokemon: PokemonEntity,
    observePokemon: List<PokemonEntity>
) {
//    if(pokemonEntity.uuid.isEmpty()){
//        return
//    }

    val pokemonEntity = pokemon

    val baseStats = MapTypeConverter.stringToMapInt(pokemonEntity.baseStats)

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

    Surface() {
        Box(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            bg,
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
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
                        Column(
                            modifier = Modifier.padding(25.dp, 0.dp)
                        ) {
                            Text(
                                text = pokemonEntity.height.toString() + " M",
                                textAlign = TextAlign.Center
                            )
                            Text(text = "Height", textAlign = TextAlign.Center)
                        }
                        Column(
                            modifier = Modifier.padding(25.dp, 0.dp)
                        ) {
                            Text(
                                text = pokemonEntity.weight.toString() + " KG",
                                textAlign = TextAlign.Center
                            )
                            Text(text = "Weight", textAlign = TextAlign.Center)
                        }
                    }

                    MyTeamDropdown(pokemonEntity)

                    Text(text = "Evolutions", style = MaterialTheme.typography.h6)
                    Box(
                        contentAlignment = Center
                    ) {
                        if (pokemonEntity.nextEvolution.isNotEmpty()) {
//                        if(pokemonEntity != null || pokemonEntity.nextEvolution != null || pokemonEntity.nextEvolution.isNotEmpty()){

                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            ) {
                                val nextEvoList =
                                    ListTypeConverter.stringToList(pokemonEntity.nextEvolution!!)
                                if (nextEvoList.isNotEmpty() && nextEvoList.size != 1) {
                                    for (nextEvo in nextEvoList) {
                                        Card(
                                            onClick = {
                                                navController.navigate(
                                                    "pokemon/{name}"
                                                        .replace(
                                                            oldValue = "{name}",
                                                            newValue = nextEvo
                                                        )
                                                )
                                            },
                                            modifier = Modifier
                                                .background(Color.Transparent)
                                                .padding(4.dp)
                                                .wrapContentSize(Alignment.Center),

                                            ) {
                                            Column(
//                                            modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    val nextEvoPokemon = observePokemon.find {it.uuid == nextEvo}
                                                    if (nextEvoPokemon != null) {
                                                        if (nextEvoPokemon.uuid.isNotEmpty()) {
                                                            Thumbnail(
                                                                thumbnail = nextEvoPokemon.thumbnail,
                                                            )
                                                        }
                                                    }
                                                }
                                                Box(contentAlignment = Center) {
                                                    Text(text = nextEvo.replaceFirstChar {
                                                        if (it.isLowerCase()) it.titlecase(
                                                            Locale.ROOT
                                                        ) else it.toString()
                                                    }, textAlign = TextAlign.Center)
                                                }
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
                        }
                    }

                    Text(text = "Abilities", style = MaterialTheme.typography.h6)
                    Row() {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            for (ability in ListTypeConverter.stringToList(pokemonEntity.abilities)) {
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
                            text = "Speed: $it1", value = it1.toFloat() * 0.01f,
                            color = getTypeColor(text = type[0])
                        )
                    }
                    baseStats["defense"]?.let { it1 ->
                        StatsBar(
                            text = "Defense: $it1", value = it1.toFloat() * 0.01f,
                            color = getTypeColor(text = type[0])
                        )
                    }
                    baseStats["hp"]?.let { it1 ->
                        StatsBar(
                            text = "HP: $it1",
                            value = it1.toFloat() * 0.01f,
                            color = getTypeColor(text = type[0])
                        )
                    }
                    baseStats["special-attack"]?.let { it1 ->
                        StatsBar(
                            text = "Special Attack: $it1", value = it1.toFloat() * 0.01f,
                            color = getTypeColor(text = type[0])
                        )
                    }
                    baseStats["special-defense"]?.let { it1 ->
                        StatsBar(
                            text = "Special Defense: $it1", value = it1.toFloat() * 0.01f,
                            color = getTypeColor(text = type[0])
                        )
                    }
                    baseStats["attack"]?.let { it1 ->
                        StatsBar(
                            text = "Attack: $it1", value = it1.toFloat() * 0.01f,
                            color = getTypeColor(text = type[0])
                        )
                    }

                }
            }
        }
    }
}

//@Preview
@Composable
fun StatsBar(text: String, value: Float, color: Color) {
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
                    .height(5.dp), progress = value,
                color = color
            )
        }
//        color = me.ariy.mydex.data.pokemon.entity.species.Color.RED, //progress color
    }
}


@Composable
fun TypeBadge(text: String, color: androidx.compose.ui.graphics.Color) {
    val c: Color = getTypeColor(text)

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

@Composable
fun getTypeColor(text: String): Color {
    val c: Color
    when (text) {
        "fire" -> c = DeepOrange
        "grass" -> c = Green
        "normal" -> c = Gray
        "poison" -> c = Purple
        "flying" -> c = BlueGray
        "water" -> c = Cyan
        "electric" -> c = Yellow
        "bug" -> c = Lime
        "ground" -> c = Gray
        "fighting" -> c = Red
        "ghost" -> c = Purple200
        "rock" -> c = Color.Gray
        "psychic" -> c = Purple200
        "ice" -> c = Indigo
        "fairy" -> c = Color.Magenta
        else -> c = Color.Gray
    }
    return c
}


@Preview
@Composable
fun PreviewTypeBadge() {
    TypeBadge(text = "Fire", color = Color.Red)
}

@Preview
@Composable
fun PreviewStatsBar() {
    StatsBar(text = "Attack", value = 0.07f, getTypeColor(text = "fire"))
}

@Composable
fun PokemonImage(thumbnail: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    key(thumbnail) {
        Image(
            rememberAsyncImagePainter(
                remember(thumbnail) {
                    ImageRequest.Builder(context)
                        .data(thumbnail)
                        .diskCacheKey(thumbnail)
                        .memoryCacheKey(thumbnail)
                        .build()
                },
            ),
            contentDescription = "Pokemon Image",
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = modifier
                .size(256.dp)
                .padding(8.dp)
        )
    }
//    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current).data(thumbnail).crossfade(false)
//            .memoryCachePolicy(CachePolicy.ENABLED)
//            .diskCachePolicy(CachePolicy.ENABLED).build(),
//        contentDescription = "Pokemon Image",
//        contentScale = ContentScale.Fit,
//        alignment = Alignment.Center,
//        modifier = modifier
//            .size(256.dp)
//            .padding(8.dp)
////            .clip(RoundedCornerShape(50)),
//    )
}

@Composable
fun Thumbnail(thumbnail: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(thumbnail).crossfade(false)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED).build(),
        contentDescription = "me.ariy.mydex.data.pokemon.entity.species.Pokemon Image",
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
        modifier = modifier
            .size(100.dp)
            .padding(8.dp)
//            .clip(RoundedCornerShape(50)),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyTeamDropdown(pokemonEntity: PokemonEntity) {
    val uppercasePokemonName = pokemonEntity.uuid.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }
    var openDialogSuccess by remember { mutableStateOf(false) }
    var openDialogError by remember { mutableStateOf(false) }
    if (openDialogSuccess) {
        openDialogSuccess = false
        SweetSuccess(
            message = "Added ${uppercasePokemonName} to your team!",
            duration = Toast.LENGTH_SHORT,
            padding = PaddingValues(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        )
    }
    if (openDialogError) {
        openDialogError = false
        SweetError(
            message = "Looks like your team already have 6 Pokemon!",
            duration = Toast.LENGTH_SHORT,
            padding = PaddingValues(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var expanded by remember { mutableStateOf(false) }
    val items = remember { mutableStateListOf<String>() }
    val context = LocalContext.current

    val teamViewModel: MyTeamViewModel =
        viewModel(factory = MyTeamViewModelFactory(context.applicationContext as Application))
    val teams = teamViewModel.team.observeAsState(listOf()).value
    print(teams.size)
    if (items.size < teams.size) {
        for (i in teams.indices) {
            items.add(teams[i].name)
        }
    }

    var text by remember { mutableStateOf("") }

    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .bringIntoViewRequester(bringIntoViewRequester)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Green.copy(0.7f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
                Text(
                    "Add ${
                        pokemonEntity.uuid.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                    } To MyTeam"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colors.background
                )
        ) {
            DropdownMenuItem(
                onClick = { /*TODO*/ },
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusEvent { state ->
                            if (state.hasFocus || state.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(ContentAlpha.medium),
                            text = "Create new team",
                            color = Color.Black
                        )
                    },
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier.alpha(ContentAlpha.medium),
                            onClick = {
                                if (text.isEmpty()) {
                                    return@IconButton
                                }
                                items.add(text)
                                teamViewModel.addTeam(MyTeamEntity(name = text))
                                text = ""
                            })
                        {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
                        }
                    },
                    singleLine = true
                )

            }
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    val pokemon = ArrayList<PokemonEntity>()

                    var pokemons: List<String> = emptyList()
                    val team = teams.find { it.name == s }

                    if (team != null && team.pokemon.isNotEmpty()) {
                        pokemons = ListTypeConverter.stringToList(team.pokemon)
                        for (i in pokemons.indices) {
                            pokemon.add(PokemonTypeConverter.stringToPokemonEntity(pokemons[i]))
                        }
                    }

                    if (pokemons.size >= 6) {
                        openDialogError = true
                    } else {
                        var newPokemon = pokemonEntity
                        newPokemon.uid = UUID.randomUUID().toString()
                        pokemon.add(newPokemon)
                        val pokemonString = ArrayList<String>()

                        for (i in pokemon.indices) {
                            pokemonString.add(PokemonTypeConverter.pokemonEntityToString(pokemon[i]))
                        }
                        if (team != null) {
                            team.pokemon = ListTypeConverter.listToString(pokemonString)
                        }
                        if (team != null) {
                            teamViewModel.updateTeam(team)
                        }
                        openDialogSuccess = true
                    }


                }) {
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
            }
        }
    }
}