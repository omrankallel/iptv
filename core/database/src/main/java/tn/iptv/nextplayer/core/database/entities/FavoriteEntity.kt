package tn.iptv.nextplayer.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite",
)
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "itemId") val itemId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "plot") val plot: String,
    @ColumnInfo(name = "cast") val cast: String,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "rate") val rate: String,
    @ColumnInfo(name = "age") val age: String,
    @ColumnInfo(name = "type") val type: String,
)
