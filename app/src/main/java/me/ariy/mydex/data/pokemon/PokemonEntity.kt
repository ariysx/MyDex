package me.ariy.mydex.data.pokemon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID
import kotlin.random.Random

@Entity(tableName = "pokemons")
data class PokemonEntity(
    // UUID will hold primary key of pokemon name in String
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "baseStats") var baseStats: String,
    @ColumnInfo(name = "nextEvolution") val nextEvolution: String,
    @ColumnInfo(name = "abilities") val abilities: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "nickname") var nickname: String = "",
    @ColumnInfo(name = "uid") var uid: String = UUID.randomUUID().toString(),
//    @ColumnInfo(name = "apiResult") val apiResult: String?,

) : Serializable