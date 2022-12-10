/**
 * Data Access Object via Room for the MyTeam Table
 * Insert, Update, Delete and Queries
 */

package me.ariy.mydex.data.myteam

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyTeamDao {
    @Query("SELECT * FROM myteam")
    fun getAll(): LiveData<List<MyTeamEntity>>

    @Insert
    suspend fun insert(myTeamEntity: MyTeamEntity)

    @Delete
    suspend fun remove(myTeamEntity: MyTeamEntity)

    @Update
    suspend fun update(myTeamEntity: MyTeamEntity)

    @Query("SELECT * FROM myteam WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String) : MyTeamEntity

    @Query("SELECT * FROM myteam WHERE uuid LIKE :uuid LIMIT 1")
    suspend fun findById(uuid: String) : MyTeamEntity

    @Insert
    fun insertOnCreate(myTeamEntity: MyTeamEntity)

    @Query("DELETE FROM myteam")
    fun removeAll()

//
//    @Query("UPDATE myteam SET pokemon = :pokemon WHERE uuid = :id")
//    suspend fun updatePokemon(id: String, pokemon: String)
}