package tn.iptv.nextplayer.dashboard.screens.series

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.component.ButtonItem
import tn.iptv.nextplayer.component.OutlinedButtonIPTV
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
    val showAllState = rememberSaveable { mutableStateOf<GroupedMedia?>(null) }

    if (viewModel.bindingModel.showFilters.value)
        Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
        ) {

            if (channelManager.listOfFilterSeriesYear.value != null) {
                Text(
                    text = "Années",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                )
                FlowRow(
                    modifier = Modifier.padding(4.dp),
                ) {
                    channelManager.listOfFilterSeriesYear.value!!.forEachIndexed { index, year ->

                        if (viewModel.bindingModel.selectedFilteredYearIndex.value == index)

                            ButtonItem(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .wrapContentSize(),
                                paddingHorizontal = 10.dp,
                                sizeText = 13.sp,
                                shape = 5.dp,
                                label = year,
                                modifierText = Modifier.background(color = Color.Transparent),
                                onClick = {

                                },
                            )
                        else OutlinedButtonIPTV(
                            modifier = Modifier
                                .padding(4.dp)
                                .wrapContentSize(),
                            labelButton = year, modifierText = Modifier.background(color = Color.Transparent),
                            onClick = { viewModel.bindingModel.selectedFilteredYearIndex.value = index },
                        )


                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
            if (channelManager.listOfFilterSeriesGenre.value != null) {

                Text(
                    text = "Genres",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                )
                FlowRow(
                    modifier = Modifier.padding(4.dp),
                ) {
                    channelManager.listOfFilterSeriesGenre.value!!.forEachIndexed { index, genre ->

                        if (viewModel.bindingModel.selectedFilteredGenreIndex.value == index)

                            ButtonItem(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .wrapContentSize(),
                                paddingHorizontal = 10.dp,
                                sizeText = 13.sp,
                                shape = 5.dp,
                                label = genre,
                                modifierText = Modifier.background(color = Color.Transparent),
                                onClick = {

                                },
                            )
                        else OutlinedButtonIPTV(
                            modifier = Modifier
                                .padding(4.dp)
                                .wrapContentSize(),
                            labelButton = genre, modifierText = Modifier.background(color = Color.Transparent),
                            onClick = { viewModel.bindingModel.selectedFilteredGenreIndex.value = index },
                        )


                    }
                }
                Spacer(modifier = Modifier.height(10.dp))


            }
            if (channelManager.listOfFilterSeriesRating.value != null) {

                Text(
                    text = "Genres",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                )
                FlowRow(
                    modifier = Modifier.padding(4.dp),
                ) {
                    channelManager.listOfFilterSeriesRating.value!!.forEachIndexed { index, rating ->

                        if (viewModel.bindingModel.selectedFilteredRatingIndex.value == index)

                            ButtonItem(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .wrapContentSize(),
                                paddingHorizontal = 10.dp,
                                sizeText = 13.sp,
                                shape = 5.dp,
                                label = rating,
                                modifierText = Modifier.background(color = Color.Transparent),
                                onClick = {

                                },
                            )
                        else OutlinedButtonIPTV(
                            modifier = Modifier
                                .padding(4.dp)
                                .wrapContentSize(),
                            labelButton = rating, modifierText = Modifier.background(color = Color.Transparent),
                            onClick = { viewModel.bindingModel.selectedFilteredRatingIndex.value = index },
                        )


                    }
                }
                Spacer(modifier = Modifier.height(10.dp))


            }


        }


        ButtonItem(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
                .wrapContentSize(),
            paddingHorizontal = 10.dp,
            sizeText = 13.sp,
            shape = 5.dp,
            label = "OK",
            modifierText = Modifier.background(color = Color.Transparent),
            onClick = {
                viewModel.bindingModel.showFilters.value = false

            },
        )


    }
    else {
        if (showAllState.value != null) {
            AllItemsScreen(
                title = AppHelper.cleanChannelName(showAllState.value!!.labelGenre),
                mediaType = mediaType,
                items = showAllState.value!!.listSeries,
                onSelectMediaItem = onSelectSerie,
                onBack = { showAllState.value = null },
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (isLoading.value) CircularProgressIndicator(color = borderFrame)
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
                                mediaType = mediaType,
                                groupedMediaItem = groupedSer,
                                onSelectMediaItem = { onSelectSerie(it) },
                                onShowAll = { showAllState.value = it },
                            )
                        }
                    }
                }


            }
        }
    }


}