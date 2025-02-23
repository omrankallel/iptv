package tn.iptv.nextplayer.dashboard.screens.tvChannel

import android.util.Log
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailMovies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailSeries
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Favorite
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Home
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Movies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Series
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Settings
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.TVChannels
import tn.iptv.nextplayer.dashboard.screens.serieDetails.Chip
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.feature.player.utils.AppHelper
import tn.iptv.nextplayer.listchannels.ui.theme.backCardMovie



@Composable
fun  ItemLiveChannel (viewModel: DashBoardViewModel, index:Int, seriesItem : MediaItem, onSelectLiveChannel : (MediaItem) -> Unit){
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

Box (
    modifier = Modifier
        .width(210.dp)
        .height(220.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(backCardMovie)
        .shadow(1.dp)
        .padding(2.dp)
        .onFocusChanged { isFocused = it.isFocused }
        .focusable()
        .onKeyEvent { keyEvent: KeyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                    when (keyEvent.nativeKeyEvent.keyCode) {


                        KEYCODE_DPAD_LEFT -> {
                            if (index == 0) {
                                viewModel.bindingModel.drawerState.value = CustomDrawerState.Opened
                                focusManager.clearFocus()
                                true
                            } else {
                                false
                            }

                        }

                        KEYCODE_BACK -> {
                            when (viewModel.bindingModel.selectedNavigationItem.value) {
                                DetailSeries -> {
                                    viewModel.bindingModel.selectedNavigationItem.value = Series
                                    viewModel.bindingModel.selectedPage = Page.SERIES
                                }

                                DetailMovies -> {
                                    viewModel.bindingModel.selectedNavigationItem.value = Movies
                                    viewModel.bindingModel.selectedPage = Page.MOVIES
                                }

                                TVChannels, Series, Movies, Favorite, Settings -> {
                                    viewModel.bindingModel.selectedNavigationItem.value = Home
                                    viewModel.bindingModel.selectedPage = Page.NOTHING
                                }

                                else -> {

                                }
                            }
                            true
                        }


                        else -> false
                    }
            } else {
                false
            }
        }
        .border(
            width = 2.dp,
            color = if (isFocused) Color(0xFFB4A1FB) else Color.Transparent,
            shape = RoundedCornerShape(8.dp),
        )
        .clickable {
            onSelectLiveChannel(seriesItem)
        }

)

{

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(6.dp))

    {
        Column(modifier = Modifier
            .fillMaxSize()) {

            Box  (  modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()){

                AsyncImage(
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(3.dp))
                        .background(backCardMovie),
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(seriesItem.icon)
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
                    contentScale = ContentScale.Crop)

                // Tags Row
                if (seriesItem.genre.isNotEmpty() ){
                    // Split the string by comma and trim spaces
                    val castList = seriesItem.genre.split(",").map { it.trim() }
                    Row(
                        modifier = Modifier.align(Alignment.BottomStart).padding(5.dp).horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        castList.take(2) .forEach { cast ->
                                Chip(cast)
                            }
                    }
                }

            }


            Spacer(modifier = Modifier.height(7 .dp))

            Row (Modifier.fillMaxSize()){

                Text(
                    text = AppHelper.cleanChannelName(seriesItem.name),
                    fontSize =10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    color = Color.White,
                    lineHeight = 15.sp)

                Spacer(modifier = Modifier.weight(1f))

            }

        }

    }

}


}