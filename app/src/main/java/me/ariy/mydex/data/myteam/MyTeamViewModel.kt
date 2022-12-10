/**
 * ViewModel class to interact with Room using Repository as a constructor by default
 * Therefore the ViewModel will always be attached to the repository
 */

package me.ariy.mydex.data.myteam

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.ariy.mydex.data.AppDatabase

class MyTeamViewModel(application: Application) : AndroidViewModel(application) {
    var team: LiveData<List<MyTeamEntity>>
    private val repository: MyTeamRepository

    init {
        val myTeamDao = AppDatabase.getInstance(application).myteamDao()
        repository = MyTeamRepository(myTeamDao)
        team = repository.team
    }

    fun addTeam(myTeamEntity: MyTeamEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(myTeamEntity)
        }
    }

    fun removeTeam(myTeamEntity: MyTeamEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.remove(myTeamEntity)
        }
    }

    fun updateTeam(myTeamEntity: MyTeamEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(myTeamEntity)
        }
    }

    fun findByName(name: String) : MyTeamEntity {
        var myTeamEntity: MyTeamEntity = MyTeamEntity(name = "")
        viewModelScope.launch(Dispatchers.IO) {
            myTeamEntity = repository.findByName(name)
        }
        return myTeamEntity
    }

    fun findById(uuid: String) : MyTeamEntity {
        var myTeamEntity: MyTeamEntity = MyTeamEntity(name = "")
        viewModelScope.launch(Dispatchers.IO) {
            myTeamEntity = repository.findById(uuid)
        }
        return myTeamEntity
    }

    fun removeAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeAllTeam()
        }
    }
}