package tn.iptv.nextplayer.dashboard.screens.detailmovies

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.listchannels.ui.theme.backTag
import tn.iptv.nextplayer.login.app


@Composable
fun LayoutDetailsOfMovie(movieItem: MediaItem, favoriteViewModel: FavoriteViewModel) {
    var exist by remember { mutableStateOf(false) }
    LaunchedEffect(movieItem.id) {
        favoriteViewModel.isFavoriteExists(movieItem.id) { exists ->
            exist = exists
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, top = 20.dp, end = 16.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        Text(
            text = movieItem.name,
            style = MaterialTheme.typography.h4,
            color = Color.White,
        )

        // Tags Row
        if (movieItem.genre.isNotEmpty()) {

            // Split the string by comma and trim spaces
            val castList = movieItem.genre.split(",").map { it.trim() }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                castList.forEach { cast ->
                    Chip(cast)
                }
            }
        }


        // Rating, Year, Language, Seasons Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Example Image",
                modifier = Modifier.size(20.dp),
            )
            Text(text = "--.-/--", color = Color.White)
            Text(text = movieItem.date, color = Color.White)
            Text(text = "- EN", color = Color.White)

        }

        // Description
        Text(
            text = movieItem.plot,
            color = Color.White,
        )

        // Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(
                modifier = Modifier.size(width = 150.dp, height = 40.dp),
                onClick = {

                    try {

                        val intent = Intent(app, PlayerActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.data = Uri.parse(movieItem.url)
                        Log.d("PlayerActivity", "------movie  ${movieItem.url}")
                        app.startActivity(intent)


                    } catch (error: Exception) {
                        Toast.makeText(app, "Error While loading your movie , Please try again", Toast.LENGTH_LONG).show()
                    }

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = backTag),
            ) {
                Text("Play now", color = Color.White)
            }
            Button(
                modifier = Modifier.size(width = 150.dp, height = 40.dp),
                onClick = {
                    if (exist) {
                        favoriteViewModel.deleteFavoriteById(movieItem.id)
                    } else {
                        favoriteViewModel.addFavorite(movieItem.id, movieItem.name, movieItem.icon, movieItem.url, movieItem.plot, movieItem.cast, movieItem.genre, movieItem.date, "", "", "", "Movies","")
                    }
                    favoriteViewModel.isFavoriteExists(movieItem.id) { exists ->
                        exist = exists
                    }
                    Log.d("existssssss", exist.toString())

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            ) {
                if (exist)
                    Text(" - My Wishlist", color = Color.White)
                else
                    Text(" + My Wishlist", color = Color.White)
            }
        }
    }
}


@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .background(backTag, shape = RoundedCornerShape(2.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Text(text = text, color = Color.White)
    }
}