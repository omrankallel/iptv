import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.feature.player.ChannelItem
import tn.iptv.nextplayer.feature.player.model.grouped_media.GroupedMedia
import tn.iptv.nextplayer.feature.player.model.grouped_media.MediaItem
import tn.iptv.nextplayer.feature.player.utils.AppHelper

@SuppressLint("InvalidColorHexValue")
@Composable
fun SideMenu(
    favoriteViewModel: FavoriteViewModel,
    groupOfChannel: GroupedMedia,
    indexOfCurrentChannel: Int,
    width: Dp,
    dataOfLiveChannelJsonString: String,
    onPress: (Int, MediaItem) -> Unit,
    onPressBack: () -> Unit,

    ) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(width),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .padding(3.dp),
                shape = CircleShape,
                elevation = 4.dp,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(groupOfChannel.icon),
                    contentDescription = "Image with gradient background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop, // Adjust the image scaling
                )
            }

            androidx.compose.material3.Text(
                text = AppHelper.cleanChannelName(groupOfChannel.labelGenre),
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f),
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            color = Color.White,
            thickness = 2.dp,
        )
        LazyColumn(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        ) {
            itemsIndexed(groupOfChannel.listSeries) { index, channel ->
                ChannelItem(
                    favoriteViewModel = favoriteViewModel,
                    mediaItem = channel,
                    isSelected = indexOfCurrentChannel == index,
                    onPress = { onPress(index, channel) },
                    dataOfLiveChannelJsonString = dataOfLiveChannelJsonString,
                    onPressBack = {
                        onPressBack()
                    }
                )
            }
        }
    }
}
