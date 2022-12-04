package me.ariy.mydex

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.pokemon.PokemonEntity
import me.ariy.mydex.data.pokemon.PokemonRepository
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.ui.theme.MyDexTheme
import java.io.File
import java.util.*
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db = AppDatabase.getInstance(applicationContext)
        println("Database exist? " + doesDatabaseExist(applicationContext, "mydex"))
        setContent {
            MyDexTheme {
                MyDexApp()
            }
        }
    }
}

private fun doesDatabaseExist(context: Context, dbName: String): Boolean {
    val dbFile: File = context.getDatabasePath(dbName)
    return dbFile.exists()
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyDexApp(viewModel: PokemonViewModel = viewModel()) {
    val context = LocalContext.current
    thread {
        println("${Thread.currentThread()} has run.")
        PokemonRepository.syncCloud(context, viewModel)
        PokemonRepository.loadLocal(context, viewModel)
    }

    Scaffold(
        topBar = {
            MyDexAppTopBar()
        }
    )
    {
        SearchBarApp()
        PokemonList(viewModel.pokemons)
    }
}

@Composable
fun PokemonList(pokemons: SnapshotStateList<PokemonEntity>) {
    LazyColumn(modifier = Modifier.padding(top = 60.dp)) {

        items(pokemons) { pokemon ->
            PokemonItem(pokemon)
        }
    }
}

@Preview
@Composable
fun PrePokemonListItem() {
    val pokemons = remember {
        PokemonEntity(
            "charizard",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",
            "fire",
            "hello",
            "hello"
        )
    }
    PokemonItem(pokemon = pokemons)
}

@Composable
fun PokemonItem(pokemon: PokemonEntity, modifier: Modifier = Modifier) {
    var context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Card(
        elevation = 4.dp,
        modifier = modifier
            .padding(8.dp)
            .clickable(
                onClick = {
//                    Toast
//                        .makeText(context, pokemon.name, Toast.LENGTH_SHORT)
//                        .show()
                    val intent = Intent(context, ViewPokemonActivity::class.java)
                    intent.putExtra("pokemon", pokemon)
                    startActivity(context, intent, null)
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
        ) {
            Row(
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
                PokemonItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                )
            }
            if (expanded) {
                PokemonWeaknesses(pokemon.type)
            }
        }
    }
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
        Row(){
            for (i in 0 until ListTypeConverter.stringToList(type).size){
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
            .padding(8.dp)
            .clip(RoundedCornerShape(50)),
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
fun MyDexAppTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = modifier
                .size(64.dp)
                .padding(8.dp),
            painter = painterResource(R.mipmap.ic_logo_icon),
            /*
             * Content Description is not needed here - image is decorative, and setting a null
             * content description allows accessibility services to skip this element during
             * navigation.
             */
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h5
        )
    }
}

@Preview
@Composable
fun SearchBarApp() {
    var text: String = ""
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.background,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = {
                text = it
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
                        }
                    })
                {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyDexTheme {
        MyDexApp()
    }
}