package me.ariy.mydex.data.pokemon

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY pokedexID ASC")
    fun getAll(): LiveData<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE uuid LIKE :name LIMIT 1")
    fun findByName(name: String): PokemonEntity

    @Query("SELECT COUNT(uuid) FROM pokemon")
    fun countPokemon(): Int

    @Insert
    suspend fun insert(pokemonEntity: PokemonEntity)

    @Update
    suspend fun update(pokemonEntity: PokemonEntity)

    @Delete
    suspend fun remove(pokemonEntity: PokemonEntity)

    @Query("DELETE FROM pokemon")
    suspend fun removeAll()

    @Query("SELECT * FROM pokemon WHERE type LIKE '%' || :type || '%'")
    fun searchType(type: String) : List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE uuid LIKE '%' || :name || '%'")
    fun searchName(name: String) : List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE isLegendary = 1")
    fun searchLegendary() : List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE isMythical = 1")
    fun searchMythical() : List<PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemon WHERE type LIKE '%' || :type || '%'")
    fun countByType(type: String) : Int


//    @Delete
//    fun delete(pokemon: me.ariy.mydex.data.pokemon.entity.species.Pokemon)
}