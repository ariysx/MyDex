package me.ariy.mydex.data.pokemon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID
import kotlin.random.Random

@Entity(tableName = "pokemon")
data class PokemonEntity(
    // UUID will hold primary key of pokemon name in String
    @PrimaryKey var uuid: String = "",
    @ColumnInfo(name = "thumbnail") var thumbnail: String = "",
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "baseStats") var baseStats: String = "",
    @ColumnInfo(name = "nextEvolution") var nextEvolution: String = "",
    @ColumnInfo(name = "abilities") var abilities: String = "",
    @ColumnInfo(name = "height") var height: Double = 0.0,
    @ColumnInfo(name = "weight") var weight: Double = 0.0,
    @ColumnInfo(name = "nickname") var nickname: String = "",
    @ColumnInfo(name = "uid") var uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "isLegendary") var isLegendary: Boolean = false,
    @ColumnInfo(name = "isMythical") var isMythical: Boolean = false,
    @ColumnInfo(name = "pokedexID") var pokedexID: Int = 0,
) : Serializable