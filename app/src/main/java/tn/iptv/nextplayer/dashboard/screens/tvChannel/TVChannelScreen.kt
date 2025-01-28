package tn.iptv.nextplayer.dashboard.screens.tvChannel

import android.annotation.SuppressLint
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
import tn.iptv.nextplayer.dashboard.screens.comingSoon.ItemGenreLive
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame


@SuppressLint("LogNotTimber")
@Composable
fun TVChannelScreen(viewModel: DashBoardViewModel, onSelectTVChannel: (GroupedMedia, MediaItem) -> Unit) {

    val mediaType = MediaType.LIVE_TV
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listPackagesTVChannel = channelManager.listOfPackagesOfLiveTV.value


    val listState = rememberLazyListState()
    val selectedPackage = viewModel.bindingModel.selectedPackageOfLiveTV
    val groupedLiveTV = viewModel.bindingModel.listLiveByCategory
    val isLoading = viewModel.bindingModel.isLoadingLiveTV

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading.value)
            CircularProgressIndicator(color = borderFrame)
        else Column {
            Spacer(modifier = Modifier.height(10.dp))
            if (listPackagesTVChannel != null) {
                PackagesLayout(
                    selectedPackage, listPackagesTVChannel,
                    onSelectPackage = { newPackageSelected ->

                        channelManager.selectedPackageOfLiveTV.value = newPackageSelected
                        channelManager.searchValue.value?.let { channelManager.fetchCategoryLiveTVAndLiveTV(it) }

                    },
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
//                    .onKeyEvent { keyEvent ->
//                        if (keyEvent.type == KeyEventType.KeyDown) {
//                            when (keyEvent.key) {
//                                Key.DirectionDown -> {
//                                    // Ensure the index doesn't go out of bounds
//                                    coroutineScope.launch {
//                                        if (selectedCategoryIndex < groupedLiveTV.value.size - 1) {
//                                            selectedCategoryIndex++
//                                        }
//                                    }
//
//                                    true
//                                }
//                                else -> false
//                            }
//                        } else false
//                    }
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(groupedLiveTV.value) { groupedLiveChannel ->
                    ItemGenreLive(
                        mediaType, groupedLiveChannel,
                        onSelectMediaItem = {
                            onSelectTVChannel(groupedLiveChannel, it)
                        },
                    )
                }
            }
        }
    }
//    LaunchedEffect(selectedCategoryIndex) {
//        listState.animateScrollToItem(selectedCategoryIndex)
//    }
}



