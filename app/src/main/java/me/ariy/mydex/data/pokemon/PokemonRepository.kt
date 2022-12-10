package me.ariy.mydex.data.pokemon

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.gson.Gson
import me.ariy.mydex.data.retrofit.RetrofitAPI


class PokemonRepository(private val pokemonDao: PokemonDao) {

    var pokemon : LiveData<List<PokemonEntity>> = pokemonDao.getAll()

    suspend fun getAll(){
        pokemon = pokemonDao.getAll()
    }

    suspend fun addPokemon(pokemonEntity: PokemonEntity){
        try {
            pokemonDao.insert(pokemonEntity)
            println("[CloudSync] Adding ${pokemonEntity.uuid} ")
        } catch (e: SQLiteConstraintException) {
//            println("Error adding Pokemon - ${pokemonEntity.uuid}")
        }
    }

    suspend fun updatePokemon(pokemonEntity: PokemonEntity){
        pokemonDao.update(pokemonEntity)
    }

    suspend fun removePokemon(pokemonEntity: PokemonEntity){
        pokemonDao.remove(pokemonEntity)
    }

     fun getPokemon(name: String): PokemonEntity {
        return pokemonDao.findByName(name)
    }

    suspend fun countPokemon(): Int {
        return pokemonDao.countPokemon()
    }

    suspend fun removeAllPokemon(){
        pokemonDao.removeAll()
    }

    suspend fun searchType(type: String): List<PokemonEntity> {
        return pokemonDao.searchType(type)
    }

    suspend fun searchName(name: String): List<PokemonEntity> {
        return pokemonDao.searchName(name)
    }

    suspend fun searchLegendary(): List<PokemonEntity> {
        return pokemonDao.searchLegendary()
    }

    suspend fun searchMythical(): List<PokemonEntity> {
        return pokemonDao.searchMythical()
    }

    fun countByType(type: String) : Int {
        return pokemonDao.countByType(type)
    }
}