package tn.iptv.nextplayer.dashboard.screens.detailmovies

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.dashboard.DashBoardViewModel

@Composable
fun  MovieDetailScreen (viewModel: DashBoardViewModel){

    Log.d("MovieDetailScreen",""+  viewModel.bindingModel.selectedMovie.value.toString())


    Row(modifier = Modifier.fillMaxWidth()) {
        // Colonne 75%
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()

        ) {

            LayoutDetailsOfMovie (movieItem = viewModel.bindingModel.selectedMovie.value )

        }

        // Colonne 25%
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(10.dp)

        ) {

        }
    }


}