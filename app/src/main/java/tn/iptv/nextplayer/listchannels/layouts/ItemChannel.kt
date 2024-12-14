package tn.iptv.nextplayer.listchannels.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import tn.iptv.nextplayer.domain.models.Channel



@Composable
fun ItemChannel (channel: Channel , onSelectChannel : (Channel) -> Unit){

    Box(modifier = Modifier.size(250.dp).clickable {
            onSelectChannel(channel)

    },
        ){

        Image(
            painter = rememberAsyncImagePainter(channel.icon),
            modifier = Modifier
                .fillMaxSize(),
            contentDescription = "ChannelIcon"
        )

    }



}