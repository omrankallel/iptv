package tn.iptv.nextplayer.domain.models


data class ChannelResponse(
    val response: Boolean,
    val data: List<Channel>,
)