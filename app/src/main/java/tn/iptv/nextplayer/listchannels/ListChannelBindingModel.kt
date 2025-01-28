package tn.iptv.nextplayer.listchannels


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import tn.iptv.nextplayer.domain.models.Channel


class ListChannelBindingModel {

    val listChannels: MutableState<List<Channel>> =
        mutableStateOf(listOf())

    val channelSelected: MutableState<Channel> =
        mutableStateOf(Channel())
}