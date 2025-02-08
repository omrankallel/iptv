package tn.iptv.nextplayer.dashboard.layout



import PreferencesHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import fetchString
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.component.TopBarDashBoard
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailMovies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailSeries
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Favorite
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Home
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Logout
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Movies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Series
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Settings
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.TVChannels
import tn.iptv.nextplayer.dashboard.screens.FavoriteScreen
import tn.iptv.nextplayer.dashboard.screens.SettingsScreen
import tn.iptv.nextplayer.dashboard.screens.detailmovies.MovieDetailScreen
import tn.iptv.nextplayer.dashboard.screens.movies.MoviesScreen
import tn.iptv.nextplayer.dashboard.screens.serieDetails.SerieDetailsScreen
import tn.iptv.nextplayer.dashboard.screens.series.SeriesScreen
import tn.iptv.nextplayer.dashboard.screens.tvChannel.TVChannelScreen
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.feature.player.model.grouped_media.GroupedMedia
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame
import tn.iptv.nextplayer.listchannels.ui.theme.colorBackSelectedElement
import tn.iptv.nextplayer.listchannels.ui.theme.colorTextButton
import tn.iptv.nextplayer.listchannels.ui.theme.redLogout
import tn.iptv.nextplayer.login.LoginActivity
import tn.iptv.nextplayer.login.app
import kotlin.math.absoluteValue


@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "LogNotTimber", "SuspiciousIndentation")
@Composable
fun MainContent(
    viewModel: DashBoardViewModel,
    favoriteViewModel: FavoriteViewModel,
    searchValueInitial: MutableState<String>,
    selectedNavigationItem: NavigationItem,
    onShowScreenDetailSerie: (MediaItem) -> Unit,
    onShowScreenDetailMovie: (MediaItem) -> Unit,

) {
    val context = LocalContext.current
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val coroutineScope = rememberCoroutineScope()
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val widthAnim by animateDpAsState(targetValue = if(isExpanded) 220.dp else 70.dp, label = "")

    BackHandler(enabled = isExpanded) {
        coroutineScope.launch { isExpanded !=isExpanded }
    }
    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }

    val drawerFocusRequester = remember { FocusRequester() }
    val focusedIndex = remember { mutableIntStateOf(0) }
    val focusRequesters = remember { List(NavigationItem.entries.size) { FocusRequester() } }
    val focusManager = LocalFocusManager.current // Focus manager
    val pagerState = rememberPagerState(initialPage = 0)

    LaunchedEffect(!isExpanded) {
        if (!isExpanded) {
            focusManager.clearFocus(force = true)
        }
    }

    val listOfPackages = channelManager.listOfPackages.value
    val isLoading = viewModel.bindingModel.isLoadingHome

    Scaffold() {
        Box() {
            Column(
                Modifier
                    .fillMaxSize().background(Color(0xFF262435)),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Scaffold(
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .padding(start = 70.dp)
                        .clickable(enabled = isExpanded) {
                           isExpanded = !isExpanded
                        },
                    topBar = {

                        val titlePage = when (selectedNavigationItem) {
                            Home -> fetchString(context, R.string.label_home)
                            TVChannels -> fetchString(context, R.string.label_tv_channel)
                            Series, DetailSeries -> fetchString(context, R.string.label_series)
                            Movies, DetailMovies -> fetchString(context, R.string.label_movies)
                            Favorite -> fetchString(context, R.string.label_favorites)
                            Settings -> fetchString(context, R.string.label_settings)
                            Logout -> fetchString(context, R.string.label_logout)

                        }
                        TopBarDashBoard(
                            titlePage = titlePage,  searchValue = searchValueInitial,
                            selectedNavigationItem = selectedNavigationItem,
                            onSearchValueChange = { searchVal ->
                                channelManager.searchValue.value = searchVal
                                when (viewModel.bindingModel.selectedPage) {
                                    Page.HOME->{

                                    }
                                    Page.NOTHING -> {}

                                    Page.TV_CHANNEL -> {
                                        channelManager.searchForCategoryLiveTVAndLiveTV(searchVal)
                                    }

                                    Page.SERIES -> {
                                        channelManager.searchCategorySeriesAndSeries(searchVal)
                                    }

                                    Page.MOVIES -> {
                                        channelManager.searchCategoryMoviesAndMovies(searchVal)
                                    }
                                }
                            },

                            onClickFilter = {
                                viewModel.bindingModel.showFilters.value = !viewModel.bindingModel.showFilters.value
                            },
                        )

                    },
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                        ) {

                            when (selectedNavigationItem) {
                                Home -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        if (isLoading.value) {
                                            CircularProgressIndicator(color = borderFrame)
                                        } else if (!listOfPackages.isNullOrEmpty()) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                HorizontalPager(
                                                    count = listOfPackages.size,
                                                    state = pagerState,
                                                    contentPadding = PaddingValues(horizontal = 270.dp),
                                                    modifier = Modifier.fillMaxWidth().weight(1f),
                                                ) { page ->
                                                    Box(
                                                        modifier = Modifier

                                                            .onKeyEvent { event ->
                                                                when (event.nativeKeyEvent.keyCode) {
                                                                    android.view.KeyEvent.KEYCODE_DPAD_LEFT -> {
                                                                        if (pagerState.currentPage > 0) {
                                                                            runBlocking {
                                                                                coroutineScope.launch {
                                                                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)

                                                                                }
                                                                            }
                                                                        }
                                                                        true
                                                                    }
                                                                    android.view.KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                                                        if (pagerState.currentPage < listOfPackages.size - 1) {
                                                                            runBlocking {
                                                                                coroutineScope.launch {
                                                                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                                                                }
                                                                            }
                                                                        }
                                                                        true
                                                                    }

                                                                    else -> {false}
                                                                }
                                                            }
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
                                                                val packageItem = listOfPackages[page]
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
                                                        val iconUrl = listOfPackages[page].icon
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                .data(iconUrl)
                                                                .crossfade(true)
                                                                .scale(Scale.FILL)
                                                                .listener(
                                                                    onError = { _, _ -> },
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
                                }
                                TVChannels -> {
                                    viewModel.bindingModel.selectedPage = Page.TV_CHANNEL

                                    TVChannelScreen(
                                        viewModel,
                                        onSelectTVChannel = { groupOFChannel, tvChannelSelected ->


                                            val indexOfChannel = groupOFChannel.listSeries.indexOf(tvChannelSelected)


                                            val gson = Gson()
                                            val jsonStringGroupOFChannel = gson.toJson(groupOFChannel)

                                            try {

                                                val intent = Intent(app, PlayerActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                intent.putExtra("GROUP_OF_CHANNEL", jsonStringGroupOFChannel)
                                                intent.putExtra("INDEX_OF_CHANNEL", indexOfChannel)
                                                intent.data = Uri.parse(tvChannelSelected.url)
                                                app.startActivity(intent)


                                            } catch (error: Exception) {
                                                Toast.makeText(app, "Error While loading your movie , Please try again", Toast.LENGTH_LONG).show()
                                            }

                                        },
                                    )
                                    viewModel.bindingModel.selectedSerie.value = MediaItem()
                                    viewModel.bindingModel.selectedMovie.value = MediaItem()
                                }

                                Series -> {
                                    viewModel.bindingModel.selectedPage = Page.SERIES
                                    viewModel.bindingModel.selectedFilteredYear.value = "Tous"
                                    viewModel.bindingModel.selectedFilteredGenre.value = "Tous"


                                    SeriesScreen(
                                        viewModel,
                                        onSelectSerie = { serieSelected ->
                                            onShowScreenDetailSerie(serieSelected)
                                        },
                                    )
                                    viewModel.bindingModel.selectedSerie.value = MediaItem()
                                    viewModel.bindingModel.selectedMovie.value = MediaItem()
                                }

                                DetailSeries -> {
                                    viewModel.bindingModel.selectedPage = Page.NOTHING
                                    SerieDetailsScreen(viewModel, favoriteViewModel)
                                }

                                Movies -> {
                                    viewModel.bindingModel.selectedPage = Page.MOVIES
                                    viewModel.bindingModel.selectedFilteredYear.value = "Tous"
                                    viewModel.bindingModel.selectedFilteredGenre.value = "Tous"


                                    MoviesScreen(
                                        viewModel,
                                        onSelectMovie = { movieSelected ->
                                            onShowScreenDetailMovie(movieSelected)
                                        },
                                    )

                                    viewModel.bindingModel.selectedSerie.value = MediaItem()
                                    viewModel.bindingModel.selectedMovie.value = MediaItem()
                                }

                                DetailMovies -> {
                                    viewModel.bindingModel.selectedPage = Page.NOTHING
                                    MovieDetailScreen(viewModel, favoriteViewModel)
                                }

                                Favorite -> {
                                    FavoriteScreen(
                                        viewModel,
                                        onSelectFavorite = {
                                            when (it.type) {
                                                "Series" -> {
                                                    val serieSelected = MediaItem(it.cast, "", it.date, "", it.genre, it.icon, it.itemId, it.name, it.plot, it.url)
                                                    onShowScreenDetailSerie(serieSelected)
                                                }
                                                "Movies" -> {
                                                    val movieSelected = MediaItem(it.cast, "", it.date, "", it.genre, it.icon, it.itemId, it.name, it.plot, it.url)
                                                    onShowScreenDetailMovie(movieSelected)
                                                }
                                                "Live TV" -> {
                                                    val mediaItem = MediaItem(it.cast, "", it.date, "", it.genre, it.icon, it.itemId, it.name, it.plot, it.url)
                                                    try {
                                                        val gson = Gson()
                                                        val groupOfChannel: GroupedMedia = gson.fromJson(it.groupOFChannel, GroupedMedia::class.java)
                                                        val indexOfChannel = groupOfChannel.listSeries.indexOfFirst { serie ->
                                                            serie.id == it.itemId
                                                        }

                                                        val intent = Intent(app, PlayerActivity::class.java)
                                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                        intent.putExtra("GROUP_OF_CHANNEL", it.groupOFChannel)
                                                        intent.putExtra("INDEX_OF_CHANNEL", indexOfChannel)
                                                        intent.data = Uri.parse(mediaItem.url)
                                                        app.startActivity(intent)


                                                    } catch (error: Exception) {
                                                        Toast.makeText(app, "Error While loading your movie , Please try again", Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                            }
                                        },
                                    )
                                }

                                Settings -> {
                                    viewModel.bindingModel.selectedPage = Page.NOTHING
                                    SettingsScreen()
                                    viewModel.bindingModel.selectedSerie.value = MediaItem()
                                    viewModel.bindingModel.selectedMovie.value = MediaItem()
                                }

                                Logout -> {

                                    PreferencesHelper.logoutUser(context)
                                    favoriteViewModel.deleteAllFavorite()
                                    viewModel.bindingModel.selectedPage = Page.NOTHING
                                    viewModel.bindingModel.selectedSerie.value = MediaItem()
                                    viewModel.bindingModel.selectedMovie.value = MediaItem()


                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                }


                            }
                        }

                    },
                )
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(widthAnim)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(colorTextButton)
                    .padding(horizontal = 4.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = rememberAsyncImagePainter(channelManager.channelSelected.value!!.icon),
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally),
                    contentDescription = "Channel Icon",
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .background(colorTextButton)
                        .fillMaxHeight()
                        .padding(12.dp)
                        .focusRequester(drawerFocusRequester)
                        .onFocusChanged { focusState ->
                            if (!focusState.hasFocus && isExpanded) {
                                drawerFocusRequester.requestFocus()
                            }
                        }
                        .onKeyEvent { keyEvent: KeyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown) {
                                when (keyEvent.nativeKeyEvent.keyCode) {
                                    KEYCODE_DPAD_UP -> {
                                        if (focusedIndex.intValue > 0) {
                                            focusedIndex.intValue -= 1
                                            focusRequesters[focusedIndex.intValue].requestFocus()
                                        }
                                        true
                                    }
                                    KEYCODE_DPAD_RIGHT -> {
                                            coroutineScope.launch {
                                                isExpanded = !isExpanded;
                                            }
                                        true
                                    }
                                    KEYCODE_DPAD_DOWN -> {
                                        if (focusedIndex.intValue < focusRequesters.size - 1) {
                                            focusedIndex.intValue += 1
                                            focusRequesters[focusedIndex.intValue].requestFocus()
                                        }
                                        true
                                    }
                                    else -> false
                                }
                            } else {
                                false
                            }
                        }
                        .verticalScroll(rememberScrollState())
                        .focusable(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    NavigationItem.entries.filterNot {
                        it in listOf(DetailSeries, DetailMovies)
                    }.forEachIndexed { index, navigationItem ->
                        Row(
                            modifier = Modifier
                                .focusRequester(focusRequesters[index])
                                .clip(shape = RoundedCornerShape(4.dp))
                                .background(
                                    when {
                                        navigationItem == viewModel.bindingModel.selectedNavigationItem.value -> colorBackSelectedElement // Keep selected color
                                        focusedIndex.intValue == index -> Color.Gray // Highlight focused item
                                        else -> Color.Transparent
                                    }
                                ).clickable {
                                        selectedIndex.intValue = index
                                        viewModel.bindingModel.selectedNavigationItem.value = navigationItem
                                        searchValueInitial.value = ""
                                    coroutineScope.launch {  isExpanded !=isExpanded }
                                        when (viewModel.bindingModel.selectedNavigationItem.value) {
                                            Home -> {}
                                            TVChannels -> {
                                                channelManager.listOfPackages.value?.forEach { packageItem ->
                                                    if (packageItem.screen == "1" && packageItem.name == "Live TV") {
                                                        channelManager.listOfPackagesOfLiveTV.value = packageItem.live.toMutableList()
                                                        channelManager.selectedPackageOfLiveTV.value = channelManager.listOfPackagesOfLiveTV.value!!.first()
                                                    }
                                                }
                                                channelManager.fetchCategoryLiveTVAndLiveTV("")
                                            }
                                            Series -> {
                                                channelManager.listOfPackages.value?.forEach { packageItem ->
                                                    if (packageItem.screen == "3" && packageItem.name == "Series") {
                                                        channelManager.listOfPackagesOfSeries.value = packageItem.series.toMutableList()
                                                        channelManager.selectedPackageOfSeries.value = channelManager.listOfPackagesOfSeries.value!!.first()
                                                    }
                                                }
                                                channelManager.fetchCategorySeriesAndSeries("")
                                            }
                                            DetailSeries -> {}
                                            Movies -> {
                                                channelManager.listOfPackages.value?.forEach { packageItem ->
                                                    if (packageItem.screen == "2" && packageItem.name == "Movies") {
                                                        channelManager.listOfPackagesOfMovies.value = packageItem.movies.toMutableList()
                                                        channelManager.selectedPackageOfMovies.value = channelManager.listOfPackagesOfMovies.value!!.first()
                                                    }
                                                }
                                                channelManager.fetchCategoryMoviesAndMovies("")
                                            }
                                            DetailMovies -> {}
                                            Favorite -> {
                                                channelManager.fetchFavorites(favoriteViewModel)
                                            }
                                            Settings -> {}
                                            Logout -> {
                                                PreferencesHelper.logoutUser(app)
                                                viewModel.bindingModel.selectedPage = Page.NOTHING
                                                viewModel.bindingModel.selectedSerie.value = MediaItem()
                                                viewModel.bindingModel.selectedMovie.value = MediaItem()

                                                val intent = Intent(app, LoginActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the activity stack
                                                app.startActivity(intent)
                                            }
                                        }

                                        handleNavigationClick(navigationItem, viewModel, channelManager, favoriteViewModel)
                                }
                                .focusable(),

                        ){
                            Icon(
                                painter = painterResource(id = navigationItem.icon,),
                                modifier = Modifier.padding(all = 4.dp),
                                contentDescription = "Navigation Item Icon",
                                tint = if (navigationItem.title != "Logout") {
                                    if (navigationItem == viewModel.bindingModel.selectedNavigationItem.value) Color.Black else Color.White
                                } else redLogout,
                            )
                            AnimatedContent(
                                targetState = isExpanded,
                                transitionSpec ={
                                    fadeIn(animationSpec = tween(150,150)) with fadeOut(
                                        tween(150)
                                    ) using SizeTransform { initialSize, targetSize ->
                                        keyframes {
                                            IntSize(targetSize.width,initialSize.height) at 150
                                            durationMillis = 300
                                        }

                                    }
                                },
                                label = ""
                            ) {
                                    targetState ->
                                if (targetState) {

                                    Row( Modifier
                                           .padding(all = 4.dp)
                                        .fillMaxWidth() ) {
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = navigationItem.title, color = Color.White,)
                                    }
                                }
                            }

                        }
                    }
                }

            }

        }

    }

}

fun handleNavigationClick(navigationItem: NavigationItem, viewModel: DashBoardViewModel, channelManager: ChannelManager, favoriteViewModel: FavoriteViewModel) {
    when (navigationItem) {
        Home -> {}
        TVChannels -> channelManager.fetchCategoryLiveTVAndLiveTV("")
        Series -> channelManager.fetchCategorySeriesAndSeries("")
        Movies -> channelManager.fetchCategoryMoviesAndMovies("")
        Favorite -> channelManager.fetchFavorites(favoriteViewModel)
        Settings -> {}
        Logout -> handleLogout(viewModel)
        else -> {}
    }
}

fun handleLogout(viewModel: DashBoardViewModel) {
    PreferencesHelper.logoutUser(app)
    viewModel.bindingModel.selectedPage = Page.NOTHING
    val intent = Intent(app, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    app.startActivity(intent)
}
