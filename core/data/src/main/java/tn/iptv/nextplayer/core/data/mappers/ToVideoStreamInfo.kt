package tn.iptv.nextplayer.core.data.mappers

import tn.iptv.nextplayer.core.database.entities.VideoStreamInfoEntity
import tn.iptv.nextplayer.core.model.VideoStreamInfo

fun VideoStreamInfoEntity.toVideoStreamInfo() = VideoStreamInfo(
    index = index,
    title = title,
    codecName = codecName,
    language = language,
    disposition = disposition,
    bitRate = bitRate,
    frameRate = frameRate,
    frameWidth = frameWidth,
    frameHeight = frameHeight,
)
