package me.ariy.mydex.data.myteam

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "myteam")
data class MyTeamEntity (
    @PrimaryKey val uuid : String,
    @ColumnInfo(name = "pokemon") val pokemon : String,
) : Serializable