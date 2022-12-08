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
    @Query("SELECT * FROM myteam WHERE uuid LIKE :id LIMIT 1")
    fun findById(id: String) : MyTeamEntity

    @Query("UPDATE myteam SET pokemon = :pokemon WHERE uuid = :id")
    fun updatePokemon(id: String, pokemon: String)
//
//    @Query("SELECT COUNT(uuid) FROM myteam")
//    fun countPokemons(): Int
//
//    @Insert
//    fun insertOne(name: String)
}