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
    @ColumnInfo(name = "type") val type: String,
)
