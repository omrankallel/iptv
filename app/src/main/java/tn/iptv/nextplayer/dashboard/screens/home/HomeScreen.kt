package tn.iptv.nextplayer.dashboard.screens.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.packages.CategoryMedia
import tn.iptv.nextplayer.domain.models.packages.ResponsePackageItem
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    viewModel: DashBoardViewModel,
) {
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val listOfPackages = channelManager.listOfPackages.value
    val isLoading = viewModel.bindingModel.isLoadingHome
    val pagerState = rememberPagerState(initialPage = 0)
    val focusedIndex = remember { mutableIntStateOf(0) }

    // Initialize focus requesters safely
    val focusRequesters = remember {
        listOfPackages?.let { List(it.size) { FocusRequester() } } ?: emptyList()
    }

    val coroutineScope = rememberCoroutineScope() // Ensure a valid coroutine scope

    // Ensure focus when loading is complete
    LaunchedEffect(isLoading.value) {
        if (!isLoading.value && focusRequesters.isNotEmpty()) {
            focusRequesters[0].requestFocus() // Focus on first item
            focusedIndex.intValue = 0
        }
    }


}
