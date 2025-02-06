package tn.iptv.nextplayer.dashboard

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import org.koin.java.KoinJavaComponent
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailMovies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailSeries
import tn.iptv.nextplayer.dashboard.layout.MainContent
import tn.iptv.nextplayer.domain.channelManager.ChannelManager
import tn.iptv.nextplayer.domain.models.episode.EpisodeItem


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("LogNotTimber", "SuspiciousIndentation", "UseOfNonLambdaOffsetOverload")
@Composable
fun DashBoardScreen(viewModel: DashBoardViewModel, favoriteViewModel: FavoriteViewModel) {

    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)

    val searchValueInitial = remember { mutableStateOf("") }

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

    MainContent(
        viewModel = viewModel,
        favoriteViewModel = favoriteViewModel,
        searchValueInitial = searchValueInitial,
        selectedNavigationItem = viewModel.bindingModel.selectedNavigationItem.value,
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
