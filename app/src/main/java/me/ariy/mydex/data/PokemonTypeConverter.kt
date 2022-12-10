/**
 * Type Converter class for transforming Type to Type using Gson
 */

package me.ariy.mydex.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.ariy.mydex.data.pokemon.PokemonEntity

object PokemonTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToPokemonEntity(value: String): PokemonEntity {
        return Gson().fromJson(value,  object : TypeToken<PokemonEntity>() {}.type)
    }

    @TypeConverter
    @JvmStatic
    fun pokemonEntityToString(value: PokemonEntity?): String {
        return if(value == null) "" else Gson().toJson(value)
    }
}