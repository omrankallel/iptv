package tn.iptv.nextplayer.dashboard.screens.detailmovies

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.dashboard.DashBoardViewModel

@Composable
fun MovieDetailScreen(viewModel: DashBoardViewModel, favoriteViewModel: FavoriteViewModel, onBack: () -> Unit) {

    Log.d("MovieDetailScreen", "" + viewModel.bindingModel.selectedMovie.value.toString())

    BackHandler(onBack = { onBack() })

    Row(modifier = Modifier.fillMaxWidth()) {
        // Colonne 75%
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),

            ) {

            LayoutDetailsOfMovie(movieItem = viewModel.bindingModel.selectedMovie.value, favoriteViewModel = favoriteViewModel)

        }

        // Colonne 25%
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(10.dp),

            ) {

        }
    }


}