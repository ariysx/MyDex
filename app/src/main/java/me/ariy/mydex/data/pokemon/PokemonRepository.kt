package me.ariy.mydex.data.pokemon

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.ListTypeConverter
import me.ariy.mydex.data.MapTypeConverter
import me.ariy.mydex.data.RESTUtils
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object PokemonRepository {
    fun loadLocal(context: Context, viewModel: PokemonViewModel) {
        val db = AppDatabase.getInstance(context).pokemonDao()

        println("ViewModel: " + (viewModel.pokemons.size+1) + " ~~ " + db.countPokemons())
        if((viewModel.pokemons.size+1)!=db.countPokemons()){
            viewModel.clear()
            for (pokemon in db.getAll()) {
                viewModel.addPokemon(pokemon)
            }
        } else {
            viewModel.clear()
        }
    }

    fun syncCloud(context: Context, viewModel: PokemonViewModel) {
        val db = AppDatabase.getInstance(context).pokemonDao()

        if(db.getAll().isNotEmpty()){
            loadLocal(context, viewModel)
        }

        val url = "https://pokeapi.co/api/v2/pokemon-species?limit=151"
        val client = OkHttpClient()
        val response = RESTUtils().get(client, url)

        if (response.isSuccessful) {
            val api: JSONObject? = response.body?.string()?.let { JSONObject(it) }
            if (api != null) {
                println("[PokeAPI] Count pokemons: " + api.get("count").toString())
                println("[MyDex] Current Local Pokemons: " + (db.getAll().size))

                if (151 == (db.getAll().size)) {
//                if (api.get("count") == (db.getAll().size + 1)) {
                    println("[MyDex] No need to update pokemon to local database")
                    return
                }

                val pokemons = JSONArray(api.get("results").toString())
                for (i in 0 until pokemons.length()) {
                    // 5 < 20
                    if(i < db.getAll().size) {
                        println("Skipping Pokemon of ID: " + (i+1))
                        continue
                    }

                    val item = JSONObject(pokemons.get(i).toString())

                    var description: String = getDescription(i, client)

                    var baseStats: Map<String, String> = getBaseStats(i, client)

                    var types: List<String> = getTypes(i, client)


                    var evo: List<String> = getEvolution(i, client)

                    var height = getAttribute(i, client, "height")
                    var weight = getAttribute(i, client, "weight")

                    var ability = JSONArray(getAttribute(i, client, "abilities").toString())
                    var abilities = ArrayList<String>()
                    for(k in 0 until ability.length()){
                        abilities.add(ability.getJSONObject(k).getJSONObject("ability").get("name").toString().replace("-", " "))
                    }
                    println("[Abilities] $abilities")


                    val pokemon = PokemonEntity(
                        item.get("name").toString(),
//                        "https://raw.githubusercontent.com/ZeChrales/PogoAssets/master/pokemon_icons/pokemon_icon_" + (i + 1).toString().padStart(3, '0') + "_00.png",
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + (i + 1) + ".png",
//                        "https://projectpokemon.org/images/sprites-models/pgo-sprites/pm" + (i + 1) + ".icon.png",
                        ListTypeConverter.listToString(types),
                        description.replace("\n", " "),
                        MapTypeConverter.mapToString(baseStats),
                        ListTypeConverter.listToString(evo),
                        ListTypeConverter.listToString(abilities),
                        height as Int,
                        weight as Int,
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

    private fun getAttribute(i: Int, client: OkHttpClient, key: String): Any? {
        val url = "https://pokeapi.co/api/v2/pokemon/${i+1}"
        val response = RESTUtils().get(client, url)
        val value = response.body?.string()?.let { JSONObject(it) }
        if (value != null) {
            return value.get(key)
        }
        return null
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

    private fun getEvolution(i: Int, client: OkHttpClient): List<String> {
        val url = "https://pokeapi.co/api/v2/pokemon/${i+1}"
        val res = RESTUtils().get(client, url)
        val speciesURL = res.body?.string()?.let { JSONObject(it).getJSONObject("species").get("url") }

        val response = RESTUtils().get(client, speciesURL.toString())
        val res1 = response.body?.string()?.let { JSONObject(it) }
        var evolutions = ArrayList<String>()
        try {
            val evoURL = res1?.getJSONObject("evolution_chain")?.get("url")
            val response2 = RESTUtils().get(client, evoURL.toString())
            val res2 = response2.body?.string()?.let { JSONObject(it) }

            // chain.evolves_to[0].evolves_to[0].species.name
            val chain = JSONObject(res2?.get("chain").toString())

            // get first evo

            evolutions.add(JSONObject(chain.get("species").toString()).get("name").toString())

            try {
                try {
                    // Second Evo
                    val secondEvo = JSONObject(JSONArray(chain.get("evolves_to").toString()).getJSONObject(0).get("species").toString()).get("name")
                    evolutions.add(secondEvo.toString())
                    try {
                        val e1 = JSONArray(chain.get("evolves_to").toString()).getJSONObject(0).get("evolves_to")
                        val e2 = JSONArray(e1.toString()).getJSONObject(0).get("species")
                        val e3 = JSONObject(e2.toString()).get("name")
                        // Add third evo
                        evolutions.add(e3.toString())
                    } catch (e: JSONException){
                        print("No 3rd evo")
                    }
                } catch(e: JSONException){
                    print("No 2nd evo")
                }
                println("[Evolution] $evolutions")
            } catch(e: JSONException){
                print("No evo at all")
            }
        } catch(e: JSONException){
            print("Error... ???")
        }

        // obj result -> obj chain -> arr evolves_to ->
        // obj evolves_to[0] -> arr evolves_to -> obj evolves_to[0]

//        for(j in 0 until chain.length()){
//            val chainEvo = JSONObject(chain.getJSONObject(j).get("stat").toString()).get("name").toString()
//            val statValue = stats.getJSONObject(j).get("base_stat").toString()
//
//        }

        return evolutions
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