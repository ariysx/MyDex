package me.ariy.mydex.data.myteam

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "myteam")
data class MyTeamEntity (
    @PrimaryKey val uuid : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "pokemon") var pokemon : String = "",
) : Serializable