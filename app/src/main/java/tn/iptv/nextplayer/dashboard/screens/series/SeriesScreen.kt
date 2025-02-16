package tn.iptv.nextplayer.dashboard.screens.series

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
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
@SuppressLint("LogNotTimber")
@Composable
fun SeriesScreen(viewModel: DashBoardViewModel, onSelectSerie: (MediaItem) -> Unit, searchValueInitial: MutableState<String>) {

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listPackagesSeries = channelManager.listOfPackagesOfSeries.value


    val listState = rememberLazyListState()
    val selectedPackage = viewModel.bindingModel.selectedPackageOfSeries
    val groupedSeriesFiltered = viewModel.bindingModel.listSeriesByCategoryFiltered
    val groupedSeries = viewModel.bindingModel.listSeriesByCategory
    val isLoading = viewModel.bindingModel.isLoadingSeries
    val selectedSort = rememberSaveable { mutableStateOf<Boolean>(false) }
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .focusRequester(viewModel.bindingModel.boxFocusRequesterSeries.value),
    ) {
        LaunchedEffect(!isLoading.value) {
            if (!isLoading.value) {
                if (viewModel.bindingModel.drawerState.value == CustomDrawerState.Closed) {
                    viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                }
            }
        }

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

                        channelManager.showAllStateSeriesFiltered.value = null
                        channelManager.showAllStateSeries.value = null
                        groupedSeriesFiltered.value = groupedSeries.value


                        val list: MutableList<GroupedMedia> = mutableListOf()
                        val year = viewModel.bindingModel.selectedFilteredYear.value
                        val genre = viewModel.bindingModel.selectedFilteredGenre.value

                        groupedSeriesFiltered.value.forEach { packageMovie ->
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



                        groupedSeriesFiltered.value = list
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


        if (channelManager.showAllStateSeries.collectAsState().value != null) {
            if (channelManager.showAllStateSeriesFiltered.collectAsState().value != null) {

                AllItemsScreen(
                    viewModel,
                    items = channelManager.showAllStateSeriesFiltered.collectAsState().value!!.listSeries,
                    onSelectMediaItem = onSelectSerie,
                    onBack = {
                        channelManager.showAllStateSeries.value = null
                        channelManager.showAllStateSeriesFiltered.value = null
                    },
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (isLoading.value) CircularProgressIndicator(color = borderFrame)
                else Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (selectedPackage.value != null) {
                        if (listPackagesSeries != null) {
                            PackagesLayout(
                                viewModel,
                                selectedPackage, listPackagesSeries,
                                onSelectPackage = { newCategorySelected ->
                                    channelManager.selectedPackageOfSeries.value = newCategorySelected
                                    searchValueInitial.value = ""
                                    channelManager.searchValue.value = ""
                                    channelManager.fetchCategorySeriesAndSeries("")


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
                        items(groupedSeriesFiltered.value) { groupedSer ->

                            Log.e("ItemGenreSeries", "too Affich  $groupedSer")

                            ItemGenreSeries(
                                viewModel,
                                mediaType = MediaType.SERIES,
                                groupedMediaItem = groupedSer,
                                onSelectMediaItem = { onSelectSerie(it) },
                                onShowAll = {
                                    channelManager.showAllStateSeriesFiltered.value = it
                                    channelManager.showAllStateSeries.value = it


                                },
                            )
                        }
                    }
                }


            }
        }


    }


}