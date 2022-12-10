package me.ariy.mydex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import me.ariy.mydex.data.myteam.MyTeamEntity
import me.ariy.mydex.data.pokemon.PokemonEntity
import java.util.concurrent.Executors

@Database(entities = [PokemonEntity::class, MyTeamEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): me.ariy.mydex.data.pokemon.PokemonDao
    abstract fun myteamDao(): me.ariy.mydex.data.myteam.MyTeamDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "mydex.db"
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Executors.newSingleThreadExecutor().execute {
                                    instance?.let {
                                        it.myteamDao().insertOnCreate(MyTeamEntity(name = "MyTeam"))
                                    }
                                }
                            }
                        })
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
