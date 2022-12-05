package me.ariy.mydex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.ariy.mydex.data.myteam.MyTeamEntity
import me.ariy.mydex.data.pokemon.PokemonEntity

@Database(entities = [PokemonEntity::class, MyTeamEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): me.ariy.mydex.data.pokemon.PokemonDao
    abstract fun myteamDao(): me.ariy.mydex.data.myteam.MyTeamDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        private val lock = Any()

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {

            // When calling this instance concurrently from multiple threads we're in serious trouble:
            // So we use this method, 'synchronized' to lock the instance
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "mydex.db").allowMainThreadQueries().build()
                }
                return INSTANCE!!
            }
        }
    }
}
