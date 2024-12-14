package tn.iptv.nextplayer.listchannels.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.domain.models.Channel
import tn.iptv.nextplayer.listchannels.ListChannelViewModel



@Composable
fun ListChannelScreen(viewModel: ListChannelViewModel,   onSelectChannel : (Channel) -> Unit) {

    Row  ( Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically){


        viewModel.bindingModel.listChannels

        LazyRow (  modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed (items =  viewModel.bindingModel.listChannels.value){ index, channel ->

                ItemChannel(channel, onSelectChannel = {channelSelected ->
                    onSelectChannel(channelSelected)
                })

            }


        }


    }

}