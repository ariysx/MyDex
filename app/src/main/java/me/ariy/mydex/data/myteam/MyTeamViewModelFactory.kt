/**
 * Factory model to return a new ViewModel instance of MyTeamViewModel
 * Constructor with application to get context
 * Reference: https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
 */

package me.ariy.mydex.data.myteam

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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