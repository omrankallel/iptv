package tn.iptv.nextplayer.dashboard.screens.serieDetails

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.domain.channelManager.ChannelManager

@Composable
fun SerieDetailsScreen(viewModel: DashBoardViewModel, favoriteViewModel: FavoriteViewModel, onBack: () -> Unit) {

    BackHandler(onBack = { onBack() })

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)

    Log.d("SerieDetailsScreen", "" + viewModel.bindingModel.selectedSerie.value.toString())



    Row(modifier = Modifier.fillMaxWidth()) {
        // Colonne 75%
        Box(
            modifier = Modifier
                .weight(5f)
                .fillMaxHeight(),

            ) {
            LayoutDetailsOfSerie(serieItem = viewModel.bindingModel.selectedSerie.value, viewModel, favoriteViewModel)

        }

        // Colonne 25%
        Box(
            modifier = Modifier
                .weight(2.5f)
                .fillMaxHeight()
                .padding(10.dp),

            ) {

            LayoutSeasonOfSerie(listGroupedEpisodeBySeason = viewModel.bindingModel.listEpisodeGroupedBySeason.value, viewModel)

        }
    }


}