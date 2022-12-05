package me.ariy.mydex.data.myteam

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyTeamDao {
    @Query("SELECT * FROM myteam")
    fun getAll(): List<MyTeamEntity>

    @Insert
    fun insertOne(myTeamEntity: MyTeamEntity)

    @Delete
    fun deleteOne(myTeamEntity: MyTeamEntity)
//
    @Query("SELECT * FROM myteam WHERE uuid LIKE :name LIMIT 1")
    fun findByName(name: String) : MyTeamEntity
//
//    @Query("SELECT COUNT(uuid) FROM myteam")
//    fun countPokemons(): Int
//
//    @Insert
//    fun insertOne(name: String)
}