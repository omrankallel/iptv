package tn.iptv.nextplayer.dashboard.screens.comingSoon


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.domain.models.series.MediaItem

@Composable
fun AllItemsScreen(
    viewModel: DashBoardViewModel,
    items: List<MediaItem>,
    onSelectMediaItem: (MediaItem) -> Unit,
    onBack: () -> Unit,
) {

    BackHandler(onBack = { onBack() })

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items.chunked(6)) { rowItems ->
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(rowItems.take(6)) { index, item ->
                    ItemSeries(viewModel, index, item, onSelectSerie = { onSelectMediaItem(it) })

                }

            }
        }
    }

}
