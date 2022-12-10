/**
 * Data class that hold Pokemon as an object that can be used for
 * Room and Other operations
 */

package me.ariy.mydex.data.myteam

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

/*
Table name called myteam : Serializable so that it can be passed around easily in bundles
 */
@Entity(tableName = "myteam")
data class MyTeamEntity (
    @PrimaryKey val uuid : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "pokemon") var pokemon : String = "",
) : Serializable