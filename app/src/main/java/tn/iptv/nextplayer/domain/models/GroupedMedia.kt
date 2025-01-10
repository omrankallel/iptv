package tn.iptv.nextplayer.domain.models

import tn.iptv.nextplayer.domain.models.series.MediaItem


data class GroupedMedia(
    val labelGenre: String ="",
    val icon: String="",
    val listSeries: ArrayList<MediaItem>  = ArrayList<MediaItem> (),
)