package me.ariy.mydex.data.pokemon

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.ariy.mydex.data.myteam.MyTeamViewModel
import me.ariy.mydex.data.pokemon.PokemonViewModel

class MyTeamViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MyTeamViewModel::class.java)) {
            return MyTeamViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}