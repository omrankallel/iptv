package tn.iptv.nextplayer.dashboard.screens.movies

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


@Composable
fun MoviesScreen(viewModel: DashBoardViewModel, onSelectMovie: (MediaItem) -> Unit) {

    val mediaType = MediaType.MOVIES
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listPackagesMovies = channelManager.listOfPackagesOfMovies.value


    val listState = rememberLazyListState()
    val selectedPackage = viewModel.bindingModel.selectedPackageOfMovies
    val groupedMovies = viewModel.bindingModel.listMoviesByCategory
    val isLoading = viewModel.bindingModel.isLoadingMovies

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading.value)
            CircularProgressIndicator(color = borderFrame)
        else Column {
            Spacer(modifier = Modifier.height(10.dp))
            if (selectedPackage.value != null) {
                if (listPackagesMovies != null) {
                    PackagesLayout(
                        selectedPackage, listPackagesMovies,
                        onSelectPackage = { newCategorySelected ->

                            channelManager.selectedPackageOfMovies.value = newCategorySelected
                            channelManager.searchValue.value?.let { channelManager.fetchCategoryMoviesAndMovies(it) }

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
                items(groupedMovies.value) { groupedMov ->

                    Log.e("ItemGenreSeries", "too Show  $groupedMov")

                    ItemGenreSeries(
                        mediaType, groupedMov,
                        onSelectMediaItem = {
                            onSelectMovie(it)
                        },
                    )
                }
            }
        }

    }
}