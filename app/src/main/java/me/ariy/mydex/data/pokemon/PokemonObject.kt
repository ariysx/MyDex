package me.ariy.mydex.data.pokemon

import java.io.Serializable

data class PokemonObject (
    val name: String,
    val thumbnail: String,
    val type: String,
    val description: String,
) : Serializable