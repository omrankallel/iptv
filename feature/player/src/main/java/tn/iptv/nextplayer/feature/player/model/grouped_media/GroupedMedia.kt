package tn.iptv.nextplayer.feature.player.model.grouped_media




data class GroupedMedia(
    val labelGenre: String ="",
    val listSeries: ArrayList<MediaItem>  = ArrayList<MediaItem> (),
)