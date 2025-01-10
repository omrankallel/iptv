package tn.iptv.nextplayer.dashboard.screens.comingSoon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.domain.models.GroupedMedia
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import tn.iptv.nextplayer.dashboard.screens.tvChannel.ItemLiveChannel
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.feature.player.utils.AppHelper

@Composable
fun ItemGenreSeries(
    mediaType: MediaType,
    groupedMediaItem: GroupedMedia,
    onSelectMediaItem: (MediaItem) -> Unit,
    onShowAll: (GroupedMedia) -> Unit
) {
    val showOtherItems = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Text(
            text = AppHelper.cleanChannelName(groupedMediaItem.labelGenre),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(5.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(groupedMediaItem.listSeries.take(4)) { serie ->
                if (mediaType == MediaType.LIVE_TV) {
                    ItemLiveChannel(mediaType, serie, onSelectLiveChannel = { onSelectMediaItem(it) })
                } else {
                    ItemSeries(mediaType, serie, onSelectSerie = { onSelectMediaItem(it) })
                }
            }

            if (groupedMediaItem.listSeries.size > 4) {
                item {
                    ItemShowAll(mediaType, onClickToShowAll = { onShowAll(groupedMediaItem) })
                }
            }
        }
    }
}
