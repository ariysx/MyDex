package me.ariy.mydex.data.pokemon

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.RESTUtils
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject

object PokemonRepository {
    fun loadLocal(context: Context, viewModel: PokemonViewModel) {
        val db = AppDatabase.getInstance(context).pokemonDao()

        viewModel.clear()

        for (pokemon in db.getAll()) {
            viewModel.addPokemon(pokemon)
        }
    }

    fun syncCloud(context: Context, viewModel: PokemonViewModel) {
        val db = AppDatabase.getInstance(context).pokemonDao()

        val url = "https://pokeapi.co/api/v2/pokemon-species?limit=151"
        val client = OkHttpClient()
        val response = RESTUtils().get(client, url)

        if (response.isSuccessful) {
            val api: JSONObject? = response.body?.string()?.let { JSONObject(it) }
            if (api != null) {
                println("[PokeAPI] Count pokemons: " + api.get("count").toString())
                println("[MyDex] Current Local Pokemons: " + (db.getAll().size + 1))

                if (152 == (db.getAll().size + 1)) {
//                if (api.get("count") == (db.getAll().size + 1)) {
                    println("[MyDex] No need to update pokemon to local database")
                    return
                }

                val pokemons = JSONArray(api.get("results").toString())
                for (i in 0 until pokemons.length()) {
                    val item = JSONObject(pokemons.get(i).toString())

                    var description: String = getDescription(i, client)

                    var baseStats: Map<String, String> = getBaseStats(i, client)

                    var types: List<String> = getTypes(i, client)


                    val pokemon = PokemonEntity(
                        item.get("name").toString(),
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + (i + 1) + ".png",
                        ListTypeConverter.listToString(types),
                        description.replace("\n", " "),
                        MapTypeConverter.mapToString(baseStats)
                    )
                    try {
                        db.insertOne(pokemon)
                    } catch (e: SQLiteConstraintException) {
                        println("[SyncCloud] Pokemon already exists: " + pokemon.uuid)
                        continue
                    }
//                    loadLocal(context, viewModel)
                    viewModel.addPokemon(pokemon)
                    println("[SyncCloud] Added Pokemon: " + pokemon.uuid)
                }
            }
        }
    }

    private fun getTypes(i: Int, client: OkHttpClient): List<String> {
        val url = "https://pokeapi.co/api/v2/pokemon/${i+1}"
        val response = RESTUtils().get(client, url)

        val types = ArrayList<String>()
        val res1 = response.body?.string()?.let { JSONObject(it) }
        val stats = JSONArray(res1?.get("types").toString())
        for(j in 0 until stats.length()){
            val typeName = JSONObject(stats.getJSONObject(j).get("type").toString()).get("name").toString()

            types.add(typeName)
        }

        return types
    }

    private fun getBaseStats(i: Int, client: OkHttpClient): HashMap<String, String> {
        val url = "https://pokeapi.co/api/v2/pokemon/${i+1}"
        val response = RESTUtils().get(client, url)

        val baseStats = HashMap<String, String>()
        val res1 = response.body?.string()?.let { JSONObject(it) }
        val stats = JSONArray(res1?.get("stats").toString())
        for(j in 0 until stats.length()){
            val statName = JSONObject(stats.getJSONObject(j).get("stat").toString()).get("name").toString()
            val statValue = stats.getJSONObject(j).get("base_stat").toString()

            baseStats.put(statName, statValue)
        }

        return baseStats
    }

    private fun getDescription(i: Int, client: OkHttpClient): String {
        val url = "https://pokeapi.co/api/v2/pokemon-species/${i + 1}"
        val response = RESTUtils().get(client, url)

        val res1 = response.body?.string()?.let { JSONObject(it) }

        val res2 = JSONArray(res1?.get("flavor_text_entries").toString())
        var description: String = ""
        for (j in 0 until res2.length()) {
            val lang = JSONObject(res2.getJSONObject(j).get("language").toString()).get("name")
            if (lang.equals("en")) {
                description = res2.getJSONObject(j).get("flavor_text").toString()
            }
        }
        return description
    }
}