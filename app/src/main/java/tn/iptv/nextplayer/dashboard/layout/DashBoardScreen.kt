package tn.iptv.nextplayer.dashboard.layout


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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import coil.compose.rememberAsyncImagePainter
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
@SuppressLint("LogNotTimber", "SuspiciousIndentation")
@Composable
fun DashBoardScreen(viewModel: DashBoardViewModel, favoriteViewModel: FavoriteViewModel) {

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    Log.d("DashBoardScreen", "screenWidth  ${screenWidth.value}")
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 12).dp } }
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
            .background(back_custom_drawer)
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
    ) {


        CustomDrawer(
            drawerState = drawerState,
            widthMenu = animatedOffset,
            selectedNavigationItem = viewModel.bindingModel.selectedNavigationItem.value,
            onNavigationItemClick = {
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
                drawerState = drawerState,
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

                )
        }

        // Arrow button in the middle for separation between drawer and main content
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart) // Align in the middle
                .offset(x = animatedOffset - 15.dp) // Adjust the position if needed
                .size(35.dp) // Set size for the circular background
                .clip(CircleShape) // Make it a circle
                .background(back_custom_drawer)
                .clickable {
                    // Toggle drawer state
                    drawerState = if (drawerState == CustomDrawerState.Closed) CustomDrawerState.Opened else CustomDrawerState.Closed
                },
            contentAlignment = Alignment.Center, // Center the arrow inside the circle
        ) {
            Image(
                painter = painterResource(id = if (drawerState == CustomDrawerState.Opened) R.drawable.ic_arrow_back else R.drawable.ic_arrow_front), // Replace with your arrow icon resource
                contentDescription = "Toggle Drawer",
                modifier = Modifier.size(25.dp), // Set the size of the image
            )
        }


    }
}

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "LogNotTimber", "SuspiciousIndentation")
@Composable
fun MainContent(
    viewModel: DashBoardViewModel,
    favoriteViewModel: FavoriteViewModel,
    // modifier: Modifier = Modifier,
    searchValueInitial: MutableState<String>,
    drawerState: CustomDrawerState,
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
            .clickable(enabled = drawerState == CustomDrawerState.Opened) {
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
                    .padding(paddingValues)
                    .padding(end = if (drawerState.isOpened()) 190.dp else 90.dp),

                ) {

                if (viewModel.bindingModel.showFilters.value)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (channelManager.listOfFilterCategory.value != null)
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                            ) {
                                channelManager.listOfFilterCategory.value!!.forEachIndexed { index, category ->
                                    Button(
                                        onClick = { channelManager.selectedFilteredCategoryIndex.value = index },
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .background(color = Color(0xFFB4A1FB))
                                            .wrapContentSize(),
                                        colors =  ButtonColors(
                                            contentColor = Color(0xFFB4A1FB),
                                            containerColor = Color(0xFFB4A1FB),
                                            disabledContainerColor = Color(0xFFB4A1FB),
                                            disabledContentColor = Color(0xFFB4A1FB),
                                        )
                                    ) {
                                        Text(
                                            text = category,
                                            color = if (channelManager.selectedFilteredCategoryIndex.value == index) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSurface
                                            },
                                        )
                                    }
                                }
                            }

                    } else {
                    when (selectedNavigationItem) {

                        Home -> {
                            HomeScreen(
                                viewModel,
                                onSelectPackage = { tvChannelSelected ->
                                    Log.d("dashBoard", "tvChannelSelected --> ${tvChannelSelected.toString()}")
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

                                    Log.d("dashBoard_onSelTVCha", "GroupOFChannel --> ${groupOFChannel.toString()}")
                                    Log.d("dashBoard_onSelTVCha", "tvChannelSelected --> ${tvChannelSelected.toString()}")

                                    val indexOfChannel = groupOFChannel.listSeries.indexOf(tvChannelSelected)
                                    Log.d("dashBoard_onSelTVCha", "indexOfChannel --> $indexOfChannel")

                                    // openUrlWithVLC(app, tvChannelSelected.url)

                                    // Serialize GroupedMedia object to JSON string
                                    val gson = Gson()
                                    val jsonStringGroupOFChannel = gson.toJson(groupOFChannel)

                                    try {

                                        val intent = Intent(app, PlayerActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        intent.putExtra("GROUP_OF_CHANNEL", jsonStringGroupOFChannel)
                                        intent.putExtra("INDEX_OF_CHANNEL", indexOfChannel)
                                        intent.data = Uri.parse(tvChannelSelected.url)
                                        Log.d("PlayerActivity", "------ channel  ${tvChannelSelected.url}")
                                        app.startActivity(intent)


                                    } catch (error: Exception) {
                                        Toast.makeText(app, "Error While loading your movie , Please try again", Toast.LENGTH_LONG).show()
                                    }


                                    /*try {

                                        val intent = Intent(app, PlayerActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                                        intent.putExtra("GROUP_OF_CHANNEL",jsonStringGroupOFChannel)
                                        intent.data = Uri.parse(tvChannelSelected.url)
                                        Log.d("PlayerActivity", "------ channel  ${tvChannelSelected.url}")
                                        app.startActivity(intent)


                                    } catch (error: Exception) {
                                        Toast.makeText(app, "Error While loading your Channel TV , Please try again", Toast.LENGTH_LONG).show()
                                    }
                                    */

                                },
                            )
                            viewModel.bindingModel.selectedSerie.value = MediaItem()
                            viewModel.bindingModel.selectedMovie.value = MediaItem()
                        }

                        Series -> {
                            viewModel.bindingModel.selectedPage = Page.SERIES


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
                                        val indexOfChannel = MediaItem(it.cast, "", it.date, "", it.genre, it.icon, it.itemId, it.name, it.plot, it.url)
                                        try {

                                            val intent = Intent(app, PlayerActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            //intent.putExtra("GROUP_OF_CHANNEL", jsonStringGroupOFChannel)
                                            // intent.putExtra("INDEX_OF_CHANNEL", indexOfChannel)
                                            intent.data = Uri.parse(indexOfChannel.url)
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
            }

        },
    )


}