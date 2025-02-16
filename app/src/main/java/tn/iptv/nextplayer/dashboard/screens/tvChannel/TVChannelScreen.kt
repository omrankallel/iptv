package tn.iptv.nextplayer.dashboard.screens.tvChannel

import android.annotation.SuppressLint
import androidx.compose.foundation.focusable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
import tn.iptv.nextplayer.dashboard.screens.PackagesLayout
import tn.iptv.nextplayer.dashboard.screens.comingSoon.ItemGenreLive
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame


@SuppressLint("LogNotTimber")
@Composable
fun TVChannelScreen(viewModel: DashBoardViewModel, onSelectTVChannel: (GroupedMedia, MediaItem) -> Unit, searchValueInitial: MutableState<String>) {

    val mediaType = MediaType.LIVE_TV
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listPackagesTVChannel = channelManager.listOfPackagesOfLiveTV.value


    val listState = rememberLazyListState()
    val selectedPackage = viewModel.bindingModel.selectedPackageOfLiveTV
    val groupedLiveTV = viewModel.bindingModel.listLiveByCategory
    val isLoading = viewModel.bindingModel.isLoadingLiveTV
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .focusRequester(viewModel.bindingModel.boxFocusRequesterLive.value),
        contentAlignment = Alignment.Center,
    ) {
        LaunchedEffect(!isLoading.value) {
            if (!isLoading.value) {
                if (viewModel.bindingModel.drawerState.value == CustomDrawerState.Closed) {
                    viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                }
            }
        }
        if (isLoading.value)
            CircularProgressIndicator(color = borderFrame)
        else Column {
            Spacer(modifier = Modifier.height(10.dp))
            if (listPackagesTVChannel != null) {
                PackagesLayout(
                    viewModel,
                    selectedPackage, listPackagesTVChannel,
                    onSelectPackage = { newPackageSelected ->
                        channelManager.selectedPackageOfLiveTV.value = newPackageSelected
                        searchValueInitial.value = ""
                        channelManager.searchValue.value = ""
                        channelManager.fetchCategoryLiveTVAndLiveTV("")

                    },
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(groupedLiveTV.value) { groupedLiveChannel ->
                    ItemGenreLive(
                        viewModel,
                        groupedLiveChannel,
                        onSelectMediaItem = {
                            onSelectTVChannel(groupedLiveChannel, it)
                        },
                    )
                }
            }
        }


    }
}

