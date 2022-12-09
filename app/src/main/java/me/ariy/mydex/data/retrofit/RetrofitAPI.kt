package me.ariy.mydex.data.retrofit

import me.ariy.mydex.data.pokemon.entity.evolutions.EvolutionsData
import me.ariy.mydex.data.pokemon.entity.pokeAPI.PokeData
import me.ariy.mydex.data.pokemon.entity.pokemon.PokemonData
import me.ariy.mydex.data.pokemon.entity.species.SpeciesData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface RetrofitAPI {

    @GET
    suspend fun getPokemon(@Url url: String): PokemonData

    @GET
    suspend fun getPokemonSpecies(@Url url: String): SpeciesData

    @GET
    suspend fun getPokemonEvolutions(@Url url: String): EvolutionsData

    @GET("pokemon-species?limit=-1")
    suspend fun getPokemonFromCloud() : PokeData

    companion object {

        var BASE_URL = "https://pokeapi.co/api/v2/"

        fun create() : RetrofitAPI {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(RetrofitAPI::class.java)

        }
    }
}