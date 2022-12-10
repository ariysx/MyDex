package me.ariy.mydex.data.myteam

import android.content.Context
import androidx.lifecycle.LiveData
import me.ariy.mydex.data.AppDatabase
import me.ariy.mydex.data.pokemon.PokemonEntity

class MyTeamRepository(private val myTeamDao: MyTeamDao) {

    val team: LiveData<List<MyTeamEntity>> = myTeamDao.getAll()

    suspend fun add(myTeamEntity: MyTeamEntity) {
        myTeamDao.insert(myTeamEntity)
    }

    suspend fun remove(myTeamEntity: MyTeamEntity) {
        myTeamDao.remove(myTeamEntity)
    }

    suspend fun update(myTeamEntity: MyTeamEntity){
        myTeamDao.update(myTeamEntity)
    }

    suspend fun findByName(id: String): MyTeamEntity {
        return myTeamDao.findByName(id)
    }

    suspend fun findById(uuid: String): MyTeamEntity {
        return myTeamDao.findById(uuid)
    }

    suspend fun removeAllTeam(){
        myTeamDao.removeAll()
    }
}