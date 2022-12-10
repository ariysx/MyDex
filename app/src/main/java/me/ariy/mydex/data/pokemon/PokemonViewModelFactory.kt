/**
 * Factory model to return a new ViewModel instance of PokemonViewModel
 * Constructor with application to get context
 * Reference: https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
 */

package me.ariy.mydex.data.pokemon

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.ariy.mydex.data.pokemon.PokemonViewModel

class PokemonViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            return PokemonViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}