/**
 * Main class to initialise the application
 */
package me.ariy.mydex

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import me.ariy.mydex.screen.MainScreen
import me.ariy.mydex.ui.theme.MyDexTheme
import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        println("Database exist? " + doesDatabaseExist(applicationContext, "mydex.db"))
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