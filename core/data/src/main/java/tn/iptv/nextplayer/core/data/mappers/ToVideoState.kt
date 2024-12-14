package tn.iptv.nextplayer.core.data.mappers

import tn.iptv.nextplayer.core.data.models.VideoState
import tn.iptv.nextplayer.core.database.converter.UriListConverter
import tn.iptv.nextplayer.core.database.entities.MediumEntity

fun MediumEntity.toVideoState(): VideoState {
    return VideoState(
        path = path,
        position = playbackPosition,
        audioTrackIndex = audioTrackIndex,
        subtitleTrackIndex = subtitleTrackIndex,
        playbackSpeed = playbackSpeed,
        externalSubs = UriListConverter.fromStringToList(externalSubs),
        videoScale = videoScale,
    )
}
