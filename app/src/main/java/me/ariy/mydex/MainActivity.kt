package me.ariy.mydex

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.pokemon.PokemonRepository
import me.ariy.mydex.data.pokemon.PokemonViewModel
import me.ariy.mydex.screen.MainScreen
import me.ariy.mydex.ui.theme.MyDexTheme
import java.io.File
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db = AppDatabase.getInstance(applicationContext)
        println("Database exist? " + doesDatabaseExist(applicationContext, "mydex"))
        setContent {
            MyDexTheme {
                MainScreen()
            }
        }
    }
}

private fun doesDatabaseExist(context: Context, dbName: String): Boolean {
    val dbFile: File = context.getDatabasePath(dbName)
    return dbFile.exists()
}