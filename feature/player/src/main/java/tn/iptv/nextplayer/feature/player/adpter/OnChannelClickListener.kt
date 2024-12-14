package tn.iptv.nextplayer.feature.player.adpter

import tn.iptv.nextplayer.feature.player.model.grouped_media.MediaItem


interface OnChannelClickListener {

    fun onChannelClick(position: Int, mediaItem: MediaItem)
}