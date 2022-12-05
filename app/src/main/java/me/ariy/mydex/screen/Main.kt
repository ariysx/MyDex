package me.ariy.mydex.screen

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import me.ariy.mydex.BottomBarNavGraph
import me.ariy.mydex.BottomBarScreen
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.data.pokemon.PokemonRepository
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.ui.theme.*
import java.util.*
import kotlin.concurrent.thread

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyDexApp(
    viewModel: PokemonViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    thread {
        println("${Thread.currentThread()} has run.")
        PokemonRepository.syncCloud(context, viewModel)
    }

    Scaffold()
    {
        SearchBarApp(onCloseClicked = {
            PokemonRepository.loadLocal(context, viewModel)
        }, onSearchClicked = {
            var query = it.lowercase()
            var type: String = ""
            when(query) {
                "normal" -> type = "normal"
                "fire" -> type = "fire"
                "water" -> type = "water"
                "water" -> type = "water"
                "electric" -> type = "electric"
                "ice" -> type = "ice"
                "fighting" -> type = "fighting"
                "poison" -> type = "poison"
                "ground" -> type = "ground"
                "flying" -> type = "flying"
                "psychic" -> type = "psychic"
                "bug" -> type = "bug"
                "rock" -> type = "rock"
                "ghost" -> type = "ghost"
                "dark" -> type = "dark"
                "dragon" -> type = "dragon"
                "steel" -> type = "steel"
                "fairy" -> type = "fairy"
                "grass" -> type = "grass"

            }

            if(type.isEmpty()){
                println("[SearchBar] Searching for query")
                var filtered = viewModel.pokemons.filter { pokemon -> pokemon.uuid.contains(it) }
                viewModel.clear()
                for (i in filtered) {
                    viewModel.addPokemon(i)
                }
            } else {
                var filtered = viewModel.pokemons.filter { pokemon -> pokemon.type.contains(it) }
                viewModel.clear()
                for (i in filtered) {
                    viewModel.addPokemon(i)
                }
            }
        })
        PokemonList(viewModel.pokemons, navController)
    }
}

@Composable
fun PokemonList(pokemons: SnapshotStateList<PokemonEntity>, navController: NavHostController) {
    LazyVerticalGrid(modifier = Modifier.padding(top = 60.dp), columns = GridCells.Fixed(2)) {

        items(pokemons.size) { index ->
            PokemonItem(pokemons[index], navController)
        }
    }
}

//
//@Preview
//@Composable
//fun PrePokemonListItem() {
//    val context = LocalContext.current
//    val pokemons = remember {
//        AppDatabase.getInstance(context).pokemonDao().findByName("bulbasaur")
//    }
//    PokemonItem(pokemon = pokemons)
//}

@Composable
fun PokemonItem(pokemon: PokemonEntity, navController: NavHostController) {
    var context = LocalContext.current
    var bg = GetColorFromType(pokemon)

    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(5),
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                onClick = {
//                    Toast
//                        .makeText(context, pokemon.name, Toast.LENGTH_SHORT)
//                        .show()
//                    val intent = Intent(context, ViewPokemonActivity::class.java)
//                    intent.putExtra("pokemon", pokemon)
//                    startActivity(context, intent, null)

                    navController.navigate(
                        "pokemon/{name}"
                            .replace(
                                oldValue = "{name}",
                                newValue = pokemon.uuid
                            )
                    )

                }
            )
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .background(color = bg.copy(alpha = 0.3f), shape = RoundedCornerShape(5))

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                PokemonThumbnail(pokemon.thumbnail)
                PokemonInformation(pokemon.uuid.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }, pokemon.type)
                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun GetColorFromType(pokemon: PokemonEntity): Color {
    val type = ListTypeConverter.stringToList(pokemon.type)
    var bg = Color.Gray
    when (type[0]) {
        "fire" -> bg = DeepOrange
        "grass" -> bg = Green
        "normal" -> bg = Gray
        "poison" -> bg = Purple
        "flying" -> bg = BlueGray
        "water" -> bg = Cyan
        "electric" -> bg = Yellow
        "bug" -> bg = Lime
        "ground" -> bg = Gray
        "fighting" -> bg = Red
        "ghost" -> bg = Purple200
        "rock" -> bg = Color.Gray
        "psychic" -> bg = Purple200
        "ice" -> bg = Indigo
        "fairy" -> bg = Color.Magenta
        "dragon" -> bg = Teal
        else -> bg = Color.Gray
    }
    return bg
}

@Composable
fun PokemonWeaknesses(type: String) {

}

@Composable
fun PokemonItemButton(expanded: Boolean, onClick: () -> Unit) {

}

@Composable
fun PokemonInformation(name: String, type: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.h6,
            modifier = modifier.padding(top = 8.dp)
        )
        Row() {
            for (i in 0 until ListTypeConverter.stringToList(type).size) {
                TypeBadge(text = ListTypeConverter.stringToList(type)[i], color = Color.Gray)
            }
        }
    }
}

@Composable
fun PokemonThumbnail(thumbnail: String, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(thumbnail)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = "Pokemon Image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(96.dp)
            .padding(8.dp),
        loading = { CircularProgressIndicator() },
    )
//    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(thumbnail)
//            .crossfade(true)
//            .build(),
//        contentDescription = "Pokemon Image",
//        contentScale = ContentScale.Crop,
//        modifier = modifier
//            .size(96.dp)
//            .padding(8.dp)
//            .clip(RoundedCornerShape(50)),
//    )
}

@Composable
fun SearchBarApp(
    onSearch: (String) -> Unit = {},
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
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
                    text = "Search Pokemon",
                    color = Color.Black
                )
            },
//            textStyle =
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.medium), onClick = { /*TODO*/ })
                {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }
            },
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
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            )
        )
    }
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBarApp(onCloseClicked = {}, onSearchClicked = {})
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }) { innerPadding ->
        // Apply the padding globally to the whole BottomNavScreensController
        Box(modifier = Modifier.padding(innerPadding)) {
            BottomBarNavGraph(navController = navController)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.home,
        BottomBarScreen.team,
        BottomBarScreen.settings
    )
    val navBackstackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackstackEntry?.destination

    BottomNavigation() {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon",
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}