package me.ariy.mydex.data.pokemon.entity.species/*
Copyright (c) 2022 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class SpeciesData (

    @Transient val base_happiness : Int,
    @Transient val capture_rate : Int,
    @Transient val color : Color,
    @Transient val egg_groups : List<Egg_groups>,
    val evolution_chain : Evolution_chain,
    val evolves_from_species : Evolves_from_species,
    val flavor_text_entries : List<Flavor_text_entries>,
    @Transient val form_descriptions : List<String>,
    @Transient val forms_switchable : Boolean,
    @Transient val gender_rate : Int,
    @Transient val genera : List<Genera>,
    @Transient val generation : Generation,
    @Transient val growth_rate : Growth_rate,
    @Transient val habitat : Habitat,
    @Transient val has_gender_differences : Boolean,
    @Transient val hatch_counter : Int,
    val id : Int,
    @Transient val is_baby : Boolean,
    val is_legendary : Boolean,
    val is_mythical : Boolean,
    val name : String,
    val names : List<Names>,
    val order : Int,
    val pal_park_encounters : List<Pal_park_encounters>,
    val pokedex_numbers : List<Pokedex_numbers>,
    val shape : Shape,
    val varieties : List<Varieties>
)