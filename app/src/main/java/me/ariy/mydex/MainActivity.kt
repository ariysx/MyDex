package me.ariy.mydex

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModelFactory
import me.ariy.mydex.screen.MainScreen
import me.ariy.mydex.ui.theme.MyDexTheme
import java.io.File
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var db = AppDatabase.getInstance(applicationContext)
        println("Database exist? " + doesDatabaseExist(applicationContext, "mydex.db"))
        setContent {
            MyDexTheme {
//                SyncCloud()
                MainScreen()
            }
        }
    }
}

//@Composable
//fun SyncCloud(){
//    val context = LocalContext.current
//    val viewModel: PokemonViewModel =
//        viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))
//
//    thread {
//        val count = AppDatabase.getInstance(context).pokemonDao().countPokemon()
//        if (count == 0) {
//            println("[CloudSync] Initialising...")
//            viewModel.syncCloud()
//        }
//    }
//}

private fun doesDatabaseExist(context: Context, dbName: String): Boolean {
    val dbFile: File = context.getDatabasePath(dbName)
    return dbFile.exists()
}