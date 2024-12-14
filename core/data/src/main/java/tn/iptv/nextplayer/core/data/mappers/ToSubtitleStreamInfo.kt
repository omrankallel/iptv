package tn.iptv.nextplayer.core.data.mappers

import tn.iptv.nextplayer.core.database.entities.SubtitleStreamInfoEntity
import tn.iptv.nextplayer.core.model.SubtitleStreamInfo

fun SubtitleStreamInfoEntity.toSubtitleStreamInfo() = SubtitleStreamInfo(
    index = index,
    title = title,
    codecName = codecName,
    language = language,
    disposition = disposition,
)
