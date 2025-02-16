package tn.iptv.nextplayer.dashboard.layout


import PreferencesHelper
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import fetchString
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.component.CustomDrawer
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
import tn.iptv.nextplayer.listchannels.ui.theme.back_custom_drawer
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

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 8).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (viewModel.bindingModel.drawerState.value.isOpened()) offsetValue else 90.dp,
        label = "Animated Offset",
    )
    val searchValueInitial = remember { mutableStateOf("") }

    BackHandler(enabled = viewModel.bindingModel.drawerState.value.isOpened()) {
        viewModel.bindingModel.drawerState.value = CustomDrawerState.Closed
    }




    Box(
        modifier = Modifier
            .background(back_custom_drawer)
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
    ) {

        CustomDrawer(
            drawerState = viewModel.bindingModel.drawerState.value,
            Modifier
                .fillMaxHeight()
                .width(animatedOffset)
                .padding(horizontal = 12.dp)
                .focusRequester(viewModel.bindingModel.boxFocusRequesterDrawer.value)
                .focusable()
                .onKeyEvent { keyEvent: KeyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        when (keyEvent.nativeKeyEvent.keyCode) {
                            KEYCODE_DPAD_RIGHT -> {

                                if (viewModel.bindingModel.drawerState.value == CustomDrawerState.Opened) {
                                    viewModel.bindingModel.drawerState.value = CustomDrawerState.Closed
                                }

                                when (viewModel.bindingModel.selectedNavigationItem.value) {
                                    Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                    TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                    Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                    DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                    Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                    DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                    Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                    Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                    Logout -> TODO()
                                }

                                true
                            }

                            KEYCODE_DPAD_LEFT -> {
                                viewModel.bindingModel.drawerState.value = CustomDrawerState.Opened
                                true
                            }

                            KEYCODE_DPAD_UP -> {
                                if (viewModel.bindingModel.focusedIndex.intValue > 0) {
                                    viewModel.bindingModel.focusedIndex.intValue -= 1
                                } else {
                                    viewModel.bindingModel.focusedIndex.intValue = 6
                                }
                                true
                            }


                            KEYCODE_DPAD_DOWN -> {
                                if (viewModel.bindingModel.focusedIndex.intValue < 6) {
                                    viewModel.bindingModel.focusedIndex.intValue += 1
                                } else {
                                    viewModel.bindingModel.focusedIndex.intValue = 0
                                }
                                true
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

                            KEYCODE_ENTER, KEYCODE_DPAD_CENTER -> {
                                onNavigationItemClick(viewModel, searchValueInitial, channelManager, favoriteViewModel, getNavigationItemByIndex(viewModel.bindingModel.focusedIndex.intValue))
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                },
            selectedIndex = viewModel.bindingModel.focusedIndex.intValue,
            selectedNavigationItem = viewModel.bindingModel.selectedNavigationItem.value,
            onNavigationItemClick = {
                onNavigationItemClick(viewModel, searchValueInitial, channelManager, favoriteViewModel, it)
            },
        )

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
                onDrawerClick = { viewModel.bindingModel.drawerState.value = it },
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

                )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = animatedOffset - 15.dp)
                .size(35.dp)
                .clip(CircleShape)
                .background(back_custom_drawer)
                .focusable(false)
                .clickable {
                    viewModel.bindingModel.drawerState.value = if (viewModel.bindingModel.drawerState.value == CustomDrawerState.Closed) CustomDrawerState.Opened else CustomDrawerState.Closed
                },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = if (viewModel.bindingModel.drawerState.value == CustomDrawerState.Opened) R.drawable.ic_arrow_back else R.drawable.ic_arrow_front), // Replace with your arrow icon resource
                contentDescription = "Toggle Drawer",
                modifier = Modifier
                    .size(25.dp)
                    .focusable(false),
            )
        }


    }
}

fun getNavigationItemByIndex(index: Int): NavigationItem {
    return when (index) {
        0 -> Home
        1 -> TVChannels
        2 -> Series
        3 -> Movies
        4 -> Favorite
        5 -> Settings
        6, 7 -> Logout
        else -> Home
    }
}


fun onNavigationItemClick(viewModel: DashBoardViewModel, searchValueInitial: MutableState<String>, channelManager: ChannelManager, favoriteViewModel: FavoriteViewModel, it: NavigationItem) {
    viewModel.bindingModel.selectedSerie.value = MediaItem()
    viewModel.bindingModel.selectedMovie.value = MediaItem()
    viewModel.bindingModel.selectedNavigationItem.value = it
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
            channelManager.showAllStateSeries.value = null
            channelManager.showAllStateSeriesFiltered.value = null
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
            channelManager.showAllStateMovies.value = null
            channelManager.showAllStateMoviesFiltered.value = null
            channelManager.listOfPackages.value?.forEach { packageItem ->

                if (packageItem.screen == "2" && packageItem.name == "Movies") {

                    channelManager.listOfPackagesOfMovies.value = packageItem.movies.toMutableList()
                    channelManager.selectedPackageOfMovies.value = channelManager.listOfPackagesOfMovies.value!!.first()
                }

            }
            channelManager.fetchCategoryMoviesAndMovies("")

        }

        DetailMovies -> {
        }

        Favorite -> {
            channelManager.showAllStateFavorites.value = ArrayList()
            channelManager.showAllStateFavoritesFiltered.value = ArrayList()
            channelManager.fetchFavorites(favoriteViewModel, "")
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
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
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
) {
    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            //.//background(Color.Transparent)
            .clickable(enabled = viewModel.bindingModel.drawerState.value == CustomDrawerState.Opened) {
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
                titlePage = titlePage,
                drawerState = viewModel.bindingModel.drawerState.value, searchValue = searchValueInitial,
                selectedNavigationItem = selectedNavigationItem,
                viewModel = viewModel,
                onSearchValueChange = { searchVal ->
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

                        Page.FAVORITES -> {
                            channelManager.searchCategoryFavoritesAndFavorites(searchVal)
                        }
                    }


                },
                onClickFilter = {
                    viewModel.bindingModel.showFilters.value = !viewModel.bindingModel.showFilters.value
                },
                onClickFavorite = {
                    onNavigationItemClick(viewModel, searchValueInitial, channelManager, favoriteViewModel, Favorite)
                },
                onClickSettings = {
                    onNavigationItemClick(viewModel, searchValueInitial, channelManager, favoriteViewModel, Settings)
                },
            )

        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(end = if (viewModel.bindingModel.drawerState.value.isOpened()) 190.dp else 90.dp),
            ) {

                when (selectedNavigationItem) {

                    Home -> {
                        HomeScreen(
                            viewModel,
                            onSelectPackage = { tvChannelSelected ->
                                channelManager.homeSelected.value = tvChannelSelected
                                channelManager.fetchPackages()

                            },
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
                            searchValueInitial,
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
                                viewModel.bindingModel.selectedNavigationSeriesOrFavorite.value = Series
                                onShowScreenDetailSerie(serieSelected)
                            },
                            searchValueInitial,
                        )
                        viewModel.bindingModel.selectedSerie.value = MediaItem()
                        viewModel.bindingModel.selectedMovie.value = MediaItem()
                    }

                    DetailSeries -> {
                        viewModel.bindingModel.selectedPage = Page.NOTHING
                        SerieDetailsScreen(
                            viewModel, favoriteViewModel,
                            onBack = {
                                if (viewModel.bindingModel.selectedNavigationSeriesOrFavorite.value == Favorite) {
                                    viewModel.bindingModel.selectedNavigationItem.value = Favorite
                                    viewModel.bindingModel.selectedPage = Page.FAVORITES
                                } else {
                                    viewModel.bindingModel.selectedNavigationItem.value = Series
                                    viewModel.bindingModel.selectedPage = Page.SERIES
                                }

                            },
                        )
                    }

                    Movies -> {
                        viewModel.bindingModel.selectedPage = Page.MOVIES
                        viewModel.bindingModel.selectedFilteredYear.value = "Tous"
                        viewModel.bindingModel.selectedFilteredGenre.value = "Tous"


                        MoviesScreen(
                            viewModel,
                            onSelectMovie = { movieSelected ->
                                viewModel.bindingModel.selectedNavigationMoviesOrFavorite.value = Movies
                                onShowScreenDetailMovie(movieSelected)
                            },
                            searchValueInitial,
                        )

                        viewModel.bindingModel.selectedSerie.value = MediaItem()
                        viewModel.bindingModel.selectedMovie.value = MediaItem()
                    }

                    DetailMovies -> {
                        viewModel.bindingModel.selectedPage = Page.NOTHING
                        MovieDetailScreen(
                            viewModel, favoriteViewModel,
                            onBack = {
                                if (viewModel.bindingModel.selectedNavigationMoviesOrFavorite.value == Favorite) {
                                    viewModel.bindingModel.selectedNavigationItem.value = Favorite
                                    viewModel.bindingModel.selectedPage = Page.FAVORITES
                                } else {
                                    viewModel.bindingModel.selectedNavigationItem.value = Movies
                                    viewModel.bindingModel.selectedPage = Page.MOVIES
                                }
                            },
                        )
                    }

                    Favorite -> {
                        viewModel.bindingModel.selectedPage = Page.FAVORITES
                        FavoriteScreen(
                            viewModel,
                            onSelectFavorite = {
                                if (it.type == "Series") {
                                    viewModel.bindingModel.selectedNavigationSeriesOrFavorite.value = Favorite
                                    val serieSelected = MediaItem(it.cast, "", it.date, "", it.genre, it.icon, it.itemId, it.name, it.plot, it.url)
                                    onShowScreenDetailSerie(serieSelected)
                                } else if (it.type == "Movies") {
                                    viewModel.bindingModel.selectedNavigationMoviesOrFavorite.value = Favorite
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
                        SettingsScreen(viewModel)
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
