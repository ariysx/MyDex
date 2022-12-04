package me.ariy.mydex.data.pokemon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import me.ariy.mydex.data.MapTypeConverter
import java.io.Serializable

@Entity(tableName = "pokemons")
data class PokemonEntity(
    // UUID will hold primary key of pokemon name in String
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "baseStats") val baseStats: String,
//    @ColumnInfo(name = "apiResult") val apiResult: String?,

) : Serializable