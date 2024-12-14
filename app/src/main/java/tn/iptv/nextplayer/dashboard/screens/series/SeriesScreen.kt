package tn.iptv.nextplayer.dashboard.screens.series

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.screens.PackagesLayout
import tn.iptv.nextplayer.dashboard.screens.comingSoon.ItemGenreSeries
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame


@SuppressLint("LogNotTimber")
@Composable
fun SeriesScreen(viewModel: DashBoardViewModel, onSelectSerie: (MediaItem) -> Unit) {

    val mediaType = MediaType.SERIES
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listPackagesSeries = channelManager.listOfPackagesOfSeries.value


    val listState = rememberLazyListState()
    val selectedPackage = viewModel.bindingModel.selectedPackageOfSeries
    val groupedSeries = viewModel.bindingModel.listSeriesByCategory
    val isLoading = viewModel.bindingModel.isLoadingSeries



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading.value)
            CircularProgressIndicator(color = borderFrame)
        else Column {
            Spacer(modifier = Modifier.height(10.dp))
            if (selectedPackage.value != null) {
                if (listPackagesSeries != null) {
                    PackagesLayout(
                        selectedPackage, listPackagesSeries,
                        onSelectPackage = { newCategorySelected ->
                            channelManager.selectedPackageOfSeries.value = newCategorySelected
                            channelManager.searchValue.value?.let { channelManager.fetchCategorySeriesAndSeries(it) }


                        },
                    )
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp), // Adds space between items
            ) {
                items(groupedSeries.value) { groupedSer ->

                    Log.e("ItemGenreSeries", "too Affich  $groupedSer")

                    ItemGenreSeries(
                        mediaType, groupedSer,
                        onSelectMediaItem = {
                            onSelectSerie(it)
                        },
                    )
                }
            }
        }


    }


}