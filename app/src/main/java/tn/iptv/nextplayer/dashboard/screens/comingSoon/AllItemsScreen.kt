package tn.iptv.nextplayer.dashboard.screens.comingSoon


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.dashboard.screens.tvChannel.ItemLiveChannel
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType

@Composable
fun AllItemsScreen(
    title: String,
    mediaType: MediaType,
    items: List<MediaItem>,
    onSelectMediaItem: (MediaItem) -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Back Button
        Text(
            text = "Back",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { onBack() },
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items.chunked(3)) { rowItems ->
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(rowItems.take(3)) { serie ->
                        if (mediaType == MediaType.LIVE_TV) {
                            ItemLiveChannel(mediaType, serie, onSelectLiveChannel = { onSelectMediaItem(it) })
                        } else {
                            ItemSeries(mediaType, serie, onSelectSerie = { onSelectMediaItem(it) })
                        }
                    }

                }
            }
        }
    }
}
