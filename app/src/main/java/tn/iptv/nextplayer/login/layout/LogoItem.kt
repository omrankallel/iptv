package tn.iptv.nextplayer.login.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import tn.iptv.nextplayer.domain.models.Channel

@Composable
fun LogoItem(channel: Channel) {

    Box(modifier = Modifier.size(150.dp)) {
        Image(
            painter = rememberAsyncImagePainter(channel.icon),
            modifier = Modifier
                .fillMaxSize(),
            contentDescription = "ChannelIconSelected",
        )
    }
}