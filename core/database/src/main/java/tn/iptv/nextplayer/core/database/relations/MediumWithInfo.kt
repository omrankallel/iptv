package tn.iptv.nextplayer.core.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import tn.iptv.nextplayer.core.database.entities.AudioStreamInfoEntity
import tn.iptv.nextplayer.core.database.entities.MediumEntity
import tn.iptv.nextplayer.core.database.entities.SubtitleStreamInfoEntity
import tn.iptv.nextplayer.core.database.entities.VideoStreamInfoEntity

data class MediumWithInfo(
    @Embedded val mediumEntity: MediumEntity,
    @Relation(
        parentColumn = "uri",
        entityColumn = "medium_uri",
    )
    val videoStreamInfo: VideoStreamInfoEntity?,
    @Relation(
        parentColumn = "uri",
        entityColumn = "medium_uri",
    )
    val audioStreamsInfo: List<AudioStreamInfoEntity>,
    @Relation(
        parentColumn = "uri",
        entityColumn = "medium_uri",
    )
    val subtitleStreamsInfo: List<SubtitleStreamInfoEntity>,
)
