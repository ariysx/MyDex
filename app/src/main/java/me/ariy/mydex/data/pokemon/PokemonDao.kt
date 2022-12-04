package me.ariy.mydex.data.pokemon

import androidx.room.*

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemons")
    fun getAll(): List<PokemonEntity>

    @Query("SELECT * FROM pokemons WHERE uuid LIKE :name LIMIT 1")
    fun findByName(name: String): PokemonEntity

    @Query("SELECT COUNT(uuid) FROM pokemons")
    fun countPokemons(): Int

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(vararg pokemons: Pokemon)

    @Insert
    fun insertOne(pokemonEntity: PokemonEntity)

//    @Delete
//    fun delete(pokemon: Pokemon)
}