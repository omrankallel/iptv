package tn.iptv.nextplayer.dashboard.layout


//import tn.iptv.nextplayer.dashboard.component.CustomDrawer
import PreferencesHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import fetchString
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.component.NavigationItemView
import tn.iptv.nextplayer.dashboard.component.TopBarDashBoard
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
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
import tn.iptv.nextplayer.dashboard.customdrawer.model.isOpened
import tn.iptv.nextplayer.dashboard.screens.FavoriteScreen
import tn.iptv.nextplayer.dashboard.screens.SettingsScreen
import tn.iptv.nextplayer.dashboard.screens.detailmovies.MovieDetailScreen
import tn.iptv.nextplayer.dashboard.screens.home.HomeScreen
import tn.iptv.nextplayer.dashboard.screens.movies.MoviesScreen
import tn.iptv.nextplayer.dashboard.screens.serieDetails.SerieDetailsScreen
import tn.iptv.nextplayer.dashboard.screens.series.SeriesScreen
import tn.iptv.nextplayer.dashboard.screens.tvChannel.TVChannelScreen
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.episode.EpisodeItem
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.feature.player.model.grouped_media.GroupedMedia
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end2_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_end_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_application_start_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_series_center_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_series_end_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_series_start2_color
import tn.iptv.nextplayer.listchannels.ui.theme.back_series_start_color
import tn.iptv.nextplayer.login.LoginActivity
import tn.iptv.nextplayer.login.app
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("LogNotTimber", "SuspiciousIndentation", "UseOfNonLambdaOffsetOverload")
@Composable
fun DashBoardScreen(viewModel: DashBoardViewModel, favoriteViewModel: FavoriteViewModel) {

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    var drawerState by remember { mutableStateOf(CustomDrawerState.Opened) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    Log.d("DashBoardScreen", "screenWidth  ${screenWidth.value}")
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 8).dp } }
    Log.d("DashBoardScreen", "offsetValue  $offsetValue")
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 90.dp,
        label = "Animated Offset",
    )
    val searchValueInitial = remember { mutableStateOf("") }

    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }
    Box(
        modifier = Modifier
            .offset(x = animatedOffset)
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        back_application_start_color,
                        back_application_end_color,
                        back_application_end2_color,
                    ),
                ),
            ),
    ) {


        if (viewModel.bindingModel.selectedSerie.value.id.isNotEmpty()) {
            if (viewModel.bindingModel.selectedSerie.value.icon.isNotEmpty())
                Image(
                    painter = rememberAsyncImagePainter(viewModel.bindingModel.selectedSerie.value.icon), // Replace with your image resource
                    contentDescription = "Image with gradient background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop, // Adjust the image scaling
                )
        }
        if (viewModel.bindingModel.selectedMovie.value.id.isNotEmpty()) {
            if (viewModel.bindingModel.selectedMovie.value.icon.isNotEmpty())
                Image(
                    painter = rememberAsyncImagePainter(viewModel.bindingModel.selectedMovie.value.icon), // Replace with your image resource
                    contentDescription = "Image with gradient background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop, // Adjust the image scaling
                )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            back_series_start_color,
                            back_series_start2_color,
                            back_series_center_color,
                            back_series_end_color,
                        ),
                    ),
                ),
        )

        MainContent(
            viewModel = viewModel,
            favoriteViewModel = favoriteViewModel,
            searchValueInitial = searchValueInitial,
            selectedNavigationItem = viewModel.bindingModel.selectedNavigationItem.value,
            onDrawerClick = { drawerState = it },
            onShowScreenDetailSerie = { serieSelected ->
                viewModel.bindingModel.selectedNavigationItem.value = DetailSeries

                viewModel.bindingModel.selectedSerie.value = serieSelected
                viewModel.bindingModel.selectedEpisodeToWatch.value = EpisodeItem()
                channelManager.serieSelected.value = serieSelected

                channelManager.fetchSaisonBySerie()

            },
            onShowScreenDetailMovie = { movieSelected ->

                viewModel.bindingModel.selectedNavigationItem.value = DetailMovies
                viewModel.bindingModel.selectedMovie.value = movieSelected
                channelManager.movieSelected.value = movieSelected

            },
            onDrawerOpen = {
                drawerState = CustomDrawerState.Opened
            },
            onDrawerClose = {
                drawerState = CustomDrawerState.Closed
            },

            )
    }


}

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "LogNotTimber", "SuspiciousIndentation")
@Composable
fun MainContent(
    viewModel: DashBoardViewModel,
    favoriteViewModel: FavoriteViewModel,
    searchValueInitial: MutableState<String>,
    selectedNavigationItem: NavigationItem,
    onDrawerClick: (CustomDrawerState) -> Unit,
    onShowScreenDetailSerie: (MediaItem) -> Unit,
    onShowScreenDetailMovie: (MediaItem) -> Unit,
    onDrawerOpen: () -> Unit,
    onDrawerClose: () -> Unit,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = 0)
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }
    val selectedIndex = remember { mutableStateOf<Int?>(null) }  // Track the selected index

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(360.dp)) {
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = rememberAsyncImagePainter(channelManager.channelSelected.value!!.icon),
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally),
                    contentDescription = "Channel Icon",
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    NavigationItem.entries.filterNot {
                        it in listOf(DetailSeries, DetailMovies)
                    }.forEachIndexed { index, navigationItem ->
                        // Use forEachIndexed to get the index
                        NavigationItemView(
                            drawerState = drawerState,
                            navigationItem = navigationItem,
                            selected = navigationItem == viewModel.bindingModel.selectedNavigationItem.value,
                            onClick = {
                                selectedIndex.value = index  // Update the selected index when an item is clicked
                                viewModel.bindingModel.selectedNavigationItem.value = navigationItem
                                searchValueInitial.value = ""
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
                                coroutineScope.launch { drawerState.close() }
                                handleNavigationClick(navigationItem, viewModel, channelManager, favoriteViewModel)
                            },
                        )
                    }
                }
            }
        },
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier
                //.//background(Color.Transparent)
                .clickable(enabled = drawerState.isOpen) {
                    onDrawerClick(CustomDrawerState.Closed)
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
                    titlePage = titlePage, drawerState = drawerState, searchValue = searchValueInitial,
                    selectedNavigationItem = selectedNavigationItem,
                    onSearchValueChange = { searchVal ->
                        Log.d("dashBord", "onSearchValueChange $searchVal ")
                        channelManager.searchValue.value = searchVal

                        when (viewModel.bindingModel.selectedPage) {
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
                            HomeScreen(
                                viewModel,
                                onSelectPackage = { tvChannelSelected ->
                                    channelManager.homeSelected.value = tvChannelSelected
                                    channelManager.fetchPackages()

                                },
                                pagerState = pagerState,
                                onDrawerOpen = onDrawerOpen,
                                onDrawerClose = onDrawerClose,
                            )
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
                                    //  SerieDetailsScreen()
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
                                    if (it.type == "Series") {
                                        val serieSelected = MediaItem(it.cast, "", it.date, "", it.genre, it.icon, it.itemId, it.name, it.plot, it.url)
                                        onShowScreenDetailSerie(serieSelected)
                                    } else if (it.type == "Movies") {
                                        val movieSelected = MediaItem(it.cast, "", it.date, "", it.genre, it.icon, it.itemId, it.name, it.plot, it.url)
                                        onShowScreenDetailMovie(movieSelected)
                                    } else if (it.type == "Live TV") {
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
