package tn.iptv.nextplayer.listchannels.layouts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.domain.models.Channel
import tn.iptv.nextplayer.listchannels.ListChannelViewModel
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame
import kotlin.math.absoluteValue


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ListChannelWithCarousel(viewModel: ListChannelViewModel, onSelectChannel: (Channel) -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0)
    val isLoading = viewModel.bindingModel.isLoadingChannel

    if (isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
            )
        { CircularProgressIndicator(color = borderFrame) }
    } else
        Column(modifier = Modifier.fillMaxSize()) {


            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                HorizontalPager(
                    count = viewModel.bindingModel.listChannels.value.size,
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 250.dp),
                    modifier = Modifier

                        .height(600.dp)
                        .weight(1f),

                    ) { page ->

                    Box(

                        // shape = RoundedCornerShape(10.dp),

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

                                if (pagerState.currentPage == page)
                                    onSelectChannel(viewModel.bindingModel.listChannels.value[page])

                            },


                        ) {

                        val iconUrl = viewModel.bindingModel.listChannels.value.map { it.icon }[page]
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


            /*
                Row (
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    repeat(viewModel.bindingModel.listChannels.value.size){indexItem ->
                         val color  = if (pagerState.currentPage ==  indexItem ) {
                             Color.DarkGray
                         }else
                             Color.LightGray
                        Box(modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .size(20.dp)
                            .background(color)
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(indexItem)
                                }
                            }){


                        }

                    }

                }*/


        }

}
