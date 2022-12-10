package me.ariy.mydex.data.pokemon

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.pokemon.entity.evolutions.EvolutionsData
import me.ariy.mydex.data.pokemon.entity.pokeAPI.PokeData
import me.ariy.mydex.data.pokemon.entity.pokemon.PokemonData
import me.ariy.mydex.data.pokemon.entity.species.SpeciesData
import me.ariy.mydex.data.retrofit.RetrofitAPI
import retrofit2.*
import java.io.IOException

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

    var pokemon: LiveData<List<PokemonEntity>>
    private val repository: PokemonRepository

    init {
        val pokemonDao = AppDatabase.getInstance(application).pokemonDao()
        repository = PokemonRepository(pokemonDao)
        pokemon = repository.pokemon
    }

    fun addPokemon(pokemonEntity: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            println("[SyncCloud] Adding ${pokemonEntity.uuid} ")
            repository.addPokemon(pokemonEntity)
        }
    }

    fun removePokemon(pokemonEntity: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removePokemon(pokemonEntity)
        }
    }

    fun updatePokemon(pokemonEntity: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePokemon(pokemonEntity)
        }
    }

    fun getPokemon(name: String): PokemonEntity {
        var pokemon = PokemonEntity()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getPokemon(name)
            pokemon = response
        }
        return pokemon
    }

    fun countPokemon(): Int {
        var count = 0
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.countPokemon()
            count = response
        }
        return count
    }

    fun loadLocal() {
        pokemon = repository.pokemon
    }

    fun dropAll() {
        viewModelScope.launch {
            repository.removeAllPokemon()
        }
    }

    fun searchByName(s: String) : List<PokemonEntity> {
        var pokemon: List<PokemonEntity> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.searchName(s)
            pokemon = response
        }
        return pokemon
    }

    fun searchByType(s: String) : List<PokemonEntity> {
        var pokemon: List<PokemonEntity> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.searchType(s)
            pokemon = response
        }
        return pokemon
    }

    fun getAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAll()
            pokemon = repository.pokemon
        }
    }

    fun searchLegendary() : List<PokemonEntity> {
        var pokemon: List<PokemonEntity> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.searchLegendary()
            pokemon = response
        }
        return pokemon
    }

    fun searchMythical() : List<PokemonEntity> {
        var pokemon: List<PokemonEntity> = emptyList()
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.searchMythical()
            pokemon = response
        }
        return pokemon
    }

    fun countByType(type: String) : Int {
        var count = 0
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.countByType(type)
            count = response
        }
        return count
    }

    /*
    Reference: https://www.geeksforgeeks.org/retrofit-with-kotlin-coroutine-in-android/
    https://www.howtodoandroid.com/retrofit-android-example-kotlin/
     */

    //    private val lock = Mutex()
    fun syncCloud() {
//        lock.withLock {
        println("Hello? Am I running?")
        viewModelScope.launch {
            try {
                val retrofitAPI = RetrofitAPI.create()
                val pokeData = retrofitAPI.getPokemonFromCloud()

                println("[CloudSync] Pokemon Count: " + pokeData.count)

                for (i in 0 until pokeData.results.size) {
//                    if(pokemon.value != null && pokeData.count == (pokemon.value?.size?.plus(1))){
//                        break
//                    }

                    if (getPokemon(pokeData.results[i].name) != null) {
                        continue
                    }

                    println("[CloudSync] Pokemon: ${pokeData.results[i].name} | URL: ${pokeData.results[i].url}")


                    val speciesData = retrofitAPI.getPokemonSpecies(pokeData.results[i].url)
                    val pokemonData =
                        retrofitAPI.getPokemon("https://pokeapi.co/api/v2/pokemon/${speciesData.id}")

                    var pokemonEntity: PokemonEntity = PokemonEntity()
                    pokemonEntity.uuid = pokeData.results[i].name
                    val description = speciesData.flavor_text_entries.find {
                        it.language.name == "en"
                    }
                    pokemonEntity.description = description?.flavor_text?.replace("\n", "").toString()
                    pokemonEntity.height = ((pokemonData.height.toDouble() / 10))
                    pokemonEntity.weight = ((pokemonData.weight.toDouble() / 10))
                    pokemonEntity.isLegendary = speciesData.is_legendary
                    pokemonEntity.isMythical = speciesData.is_mythical
                    val abilities = ArrayList<String>()
                    pokemonData.abilities.forEach {
                        abilities.add(it.ability.name)
                    }
                    pokemonEntity.abilities = ListTypeConverter.listToString(abilities)
                    pokemonEntity.thumbnail =
                        pokemonData.sprites.other.officialArtwork.front_default
                    val types = ArrayList<String>()
                    pokemonData.types.forEach {
                        types.add(it.type.name)
                    }
                    pokemonEntity.type = ListTypeConverter.listToString(types)
                    val baseStats = HashMap<String, Int>()
                    pokemonData.stats.forEach {
                        baseStats[it.stat.name] = it.baseStat
                    }
                    pokemonEntity.baseStats = MapTypeConverter.mapToString(baseStats)
                    pokemonEntity.pokedexID = speciesData.id

                    if(speciesData != null || speciesData.evolution_chain != null){
                        try {
                            val evolutionsData =
                                retrofitAPI.getPokemonEvolutions(speciesData.evolution_chain.url)

                            val evolutionTree = ArrayList<String>()
                            if (evolutionsData != null || evolutionsData.chain != null || evolutionsData.chain.evolves_to != null || evolutionsData.chain.evolves_to.size != 0) {
                                // First evolution
                                evolutionTree.add(evolutionsData.chain.species.name)
                                // Second evolution
                                evolutionsData.chain.evolves_to.forEach {
//                            println("[Evolution Tree] ${it.species.name}")
                                    evolutionTree.add(it.species.name)
                                    // Third evolution
                                    if(it.evolves_to != null){
                                        it.evolves_to.forEach {
                                            evolutionTree.add(it.species.name)
                                        }
                                    }
                                }
                            }
//                    println("[EvolutionTree] ${evolutionTree}")
                            pokemonEntity.nextEvolution = ListTypeConverter.listToString(evolutionTree)

                        } catch (e: java.lang.NullPointerException){
                            pokemonEntity.nextEvolution = ""
                        }
                    }
                    if( getPokemon(pokemonEntity.uuid) != null){
                        addPokemon(pokemonEntity)
                    }
                }
            } catch (e: IOException) {
                println(e.stackTraceToString())
            } catch (e: HttpException) {
                println(e.stackTraceToString())
            } catch (e: Exception){
                println(e.stackTraceToString())
            }
        }
    }
}