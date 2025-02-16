package tn.iptv.nextplayer.dashboard.screens.comingSoon

import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailMovies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailSeries
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Favorite
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Home
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Movies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Series
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Settings
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.TVChannels
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.feature.player.utils.AppHelper

@Composable
fun ItemGenreSeries(
    viewModel: DashBoardViewModel,
    mediaType: MediaType,
    groupedMediaItem: GroupedMedia,
    onSelectMediaItem: (MediaItem) -> Unit,
    onShowAll: (GroupedMedia) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Text(
            modifier = Modifier.focusable(false),
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
            itemsIndexed(groupedMediaItem.listSeries.take(10)) { index, item ->
                ItemSeries(viewModel, index, item, onSelectSerie = { onSelectMediaItem(it) })
            }

            if (groupedMediaItem.listSeries.size > 10) {
                item {
                    ItemShowAll(mediaType, onClickToShowAll = { onShowAll(groupedMediaItem) })
                }
            }
        }
    }
}
