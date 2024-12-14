package tn.iptv.nextplayer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tn.iptv.nextplayer.core.database.dao.DirectoryDao
import tn.iptv.nextplayer.core.database.dao.MediumDao
import tn.iptv.nextplayer.core.database.entities.AudioStreamInfoEntity
import tn.iptv.nextplayer.core.database.entities.DirectoryEntity
import tn.iptv.nextplayer.core.database.entities.MediumEntity
import tn.iptv.nextplayer.core.database.entities.SubtitleStreamInfoEntity
import tn.iptv.nextplayer.core.database.entities.VideoStreamInfoEntity

@Database(
    entities = [
        DirectoryEntity::class,
        MediumEntity::class,
        VideoStreamInfoEntity::class,
        AudioStreamInfoEntity::class,
        SubtitleStreamInfoEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class MediaDatabase : RoomDatabase() {

    abstract fun mediumDao(): MediumDao

    abstract fun directoryDao(): DirectoryDao

    companion object {
        const val DATABASE_NAME = "media_db"
    }
}
