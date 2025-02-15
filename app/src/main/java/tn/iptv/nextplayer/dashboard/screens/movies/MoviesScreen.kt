package tn.iptv.nextplayer.dashboard.screens.movies


import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.component.LargeDropdownMenu
import tn.iptv.nextplayer.component.SortByRow
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.screens.PackagesLayout
import tn.iptv.nextplayer.dashboard.screens.comingSoon.AllItemsScreen
import tn.iptv.nextplayer.dashboard.screens.comingSoon.ItemGenreSeries
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.feature.player.utils.AppHelper
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoviesScreen(viewModel: DashBoardViewModel, onSelectMovie: (MediaItem) -> Unit) {

    val mediaType = MediaType.MOVIES
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    var listPackagesMovies = channelManager.listOfPackagesOfMovies.value

    val listState = rememberLazyListState()
    val selectedPackage = viewModel.bindingModel.selectedPackageOfMovies
    val groupedMoviesFiltered = viewModel.bindingModel.listMoviesByCategoryFiltered
    val groupedMovies = viewModel.bindingModel.listMoviesByCategory
    val isLoading = viewModel.bindingModel.isLoadingMovies
    var isFocused by remember { mutableStateOf(false) }


    val selectedSort = rememberSaveable { mutableStateOf<Boolean>(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .focusRequester(viewModel.bindingModel.boxFocusRequesterMovies.value),
    ) {
        if (viewModel.bindingModel.showFilters.value) AlertDialog(
            onDismissRequest = { viewModel.bindingModel.showFilters.value = false },
            backgroundColor = Color.DarkGray,
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Années", modifier = Modifier.padding(bottom = 4.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    LargeDropdownMenu(
                        selected = viewModel.bindingModel.selectedFilteredYear.value,
                        items = channelManager.listOfFilterYear.value!!,
                        onItemSelected = { it -> viewModel.bindingModel.selectedFilteredYear.value = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Text("Genres", modifier = Modifier.padding(bottom = 4.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    LargeDropdownMenu(
                        selected = viewModel.bindingModel.selectedFilteredGenre.value,
                        items = channelManager.listOfFilterGenre.value!!,
                        onItemSelected = { it -> viewModel.bindingModel.selectedFilteredGenre.value = it },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SortByRow(
                        isAscending = selectedSort.value,
                        onSortClick = {
                            selectedSort.value = !selectedSort.value
                        },
                    )


                }
            },
            confirmButton = {
                TextButton(
                    onClick = {

                        channelManager.showAllStateMoviesFiltered.value = null
                        channelManager.showAllStateMovies.value = null

                        groupedMoviesFiltered.value = groupedMovies.value


                        val list: MutableList<GroupedMedia> = mutableListOf()
                        val year = viewModel.bindingModel.selectedFilteredYear.value
                        val genre = viewModel.bindingModel.selectedFilteredGenre.value

                        groupedMoviesFiltered.value.forEach { packageMovie ->
                            packageMovie.listSeries.forEach { serie ->
                                val isFindYear = year == "Tous" || serie.date.contains(year)
                                val isFindGenre = genre == "Tous" || serie.genre.contains(genre)

                                if (isFindYear && isFindGenre) {
                                    val existingIndex = list.indexOfFirst { it.labelGenre == packageMovie.labelGenre }

                                    if (existingIndex >= 0) {
                                        list[existingIndex].listSeries.add(serie)
                                    } else {
                                        val newListSeries = ArrayList<MediaItem>()
                                        newListSeries.add(serie)
                                        val groupMedia = packageMovie.copy(listSeries = newListSeries)
                                        list.add(groupMedia)
                                    }
                                }
                                if (list.size > 0)
                                    if (selectedSort.value) {
                                        list.last().listSeries.sortBy { it.rate }
                                    } else {
                                        list.last().listSeries.sortByDescending { it.rate }
                                    }
                            }
                        }



                        groupedMoviesFiltered.value = list
                        viewModel.bindingModel.showFilters.value = false
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.bindingModel.showFilters.value = false }) {
                    Text("Annuler")
                }
            },
        )


        if (channelManager.showAllStateMovies.collectAsState().value != null) {
            if (channelManager.showAllStateMoviesFiltered.collectAsState().value != null) {

                AllItemsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    title = AppHelper.cleanChannelName(channelManager.showAllStateMoviesFiltered.collectAsState().value!!.labelGenre),
                    mediaType = mediaType,
                    items = channelManager.showAllStateMoviesFiltered.collectAsState().value!!.listSeries,
                    onSelectMediaItem = onSelectMovie,
                    onBack = {
                        channelManager.showAllStateMovies.value = null
                        channelManager.showAllStateMoviesFiltered.value = null
                    },
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (isLoading.value) {
                    CircularProgressIndicator(color = borderFrame)
                } else {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))

                        if (selectedPackage.value != null) {
                            listPackagesMovies?.let {
                                PackagesLayout(
                                    selectedPackage, it,
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
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(groupedMoviesFiltered.value) { groupedMov ->
                                ItemGenreSeries(
                                    mediaType = mediaType,
                                    groupedMediaItem = groupedMov,
                                    onSelectMediaItem = { onSelectMovie(it) },
                                    onShowAll = {
                                        channelManager.showAllStateMoviesFiltered.value = it
                                        channelManager.showAllStateMovies.value = it

                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

