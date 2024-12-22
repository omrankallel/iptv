package tn.iptv.nextplayer.dashboard.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.packages.CategoryMedia
import tn.iptv.nextplayer.domain.models.packages.ResponsePackageItem
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame
import kotlin.math.absoluteValue

@SuppressLint("LogNotTimber")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(viewModel: DashBoardViewModel, onSelectPackage: (CategoryMedia) -> Unit) {

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listOfPackages = channelManager.listOfPackages.value
    val isLoading = viewModel.bindingModel.isLoadingHome
    val pagerState = rememberPagerState(initialPage = 1)


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        if (isLoading.value)
            CircularProgressIndicator(color = borderFrame)
        else Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            HorizontalPager(
                count = listOfPackages!!.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 270.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),

                ) { page ->

                Box(

                    modifier = Modifier
                        .background(Color.Transparent)
                        .graphicsLayer {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                            lerp(
                                start = 0.75f,
                                stop = 1f,
                                fraction = 1 - pageOffset.coerceIn(0f, 1f),
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale

                            }
                            alpha = lerp(
                                start = 0.50f,
                                stop = 1f,
                                fraction = 1 - pageOffset.coerceIn(0f, 1f),
                            )

                        }
                        .clickable {
                            val packageItem: ResponsePackageItem = listOfPackages.map { it }[page]

                            when (packageItem.screen) {
                                "1" -> {

                                    channelManager.listOfPackagesOfLiveTV.value = packageItem.live.toMutableList()
                                    channelManager.selectedPackageOfLiveTV.value = channelManager.listOfPackagesOfLiveTV.value!!.first()
                                    runBlocking {
                                        channelManager.searchValue.value?.let { channelManager.fetchCategoryLiveTVAndLiveTV(it) }
                                    }
                                    viewModel.bindingModel.selectedNavigationItem.value = NavigationItem.TVChannels

                                }

                                "2" -> {

                                    channelManager.listOfPackagesOfMovies.value = packageItem.movies.toMutableList()
                                    channelManager.selectedPackageOfMovies.value = channelManager.listOfPackagesOfMovies.value!!.first()
                                    runBlocking {
                                        channelManager.searchValue.value?.let { channelManager.fetchCategoryMoviesAndMovies(it) }
                                    }

                                    viewModel.bindingModel.selectedNavigationItem.value = NavigationItem.Movies

                                }

                                "3" -> {

                                    channelManager.listOfPackagesOfSeries.value = packageItem.series.toMutableList()
                                    channelManager.selectedPackageOfSeries.value = channelManager.listOfPackagesOfSeries.value!!.first()
                                    runBlocking {
                                        channelManager.searchValue.value?.let { channelManager.fetchCategorySeriesAndSeries(it) }
                                    }
                                    viewModel.bindingModel.selectedNavigationItem.value = NavigationItem.Series

                                }
                            }


                        },


                    ) {

                    val iconUrl = listOfPackages.map { it.icon }[page]
                    Log.d("ImageURL", iconUrl) // Check if the URL is correct

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(iconUrl)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .listener(
                                onError = { request, throwable ->
                                    Log.e("ImageLoadError", "Failed to load image: ${throwable.throwable.message}")
                                },
                            )
                            .build(),
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.placeholder_image),
                        error = painterResource(R.drawable.error_image),

                        )

                }

            }

        }


    }


}