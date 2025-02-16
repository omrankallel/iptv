package tn.iptv.nextplayer.dashboard.screens.serieDetails


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Series
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.models.episode.EpisodeItem
import tn.iptv.nextplayer.domain.models.episode.GroupedEpisode
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.listchannels.ui.theme.borderFrame
import tn.iptv.nextplayer.login.app


@SuppressLint("NewApi")
@Composable
fun LayoutSeasonOfSerie(listGroupedEpisodeBySeason: List<GroupedEpisode>, viewModel: DashBoardViewModel) {


    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        // Background image with blur effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                )
                .blur(30.dp),
            //  .padding(10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(listGroupedEpisodeBySeason) { season ->
                ExpandableSeason(groupedEpisode = season, viewModel)
            }
        }


    }


}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ExpandableSeason(groupedEpisode: GroupedEpisode, viewModel: DashBoardViewModel) {
    var expanded by remember { mutableStateOf(false) }

    var isFocused by remember { mutableStateOf(false) }

    Column {
        // Season header with expand/collapse functionality
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .background(color = if (isFocused) Color(0xFFB4A1FB) else Color.Transparent, shape = RoundedCornerShape(5.dp))
                .focusable()
                .onKeyEvent { keyEvent: KeyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        when (keyEvent.nativeKeyEvent.keyCode) {
                            KEYCODE_BACK -> {
                                viewModel.bindingModel.selectedNavigationItem.value = Series
                                viewModel.bindingModel.selectedPage = Page.SERIES
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                }
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = groupedEpisode.labelSaison,
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Wrapping LazyColumn for episodes within AnimatedVisibility
        AnimatedVisibility(visible = expanded) {
            Column { // Use Column instead of LazyColumn here to avoid nested scrolling
                groupedEpisode.listEpisode.forEach { episode ->
                    EpisodeItemLayout(episode = episode, viewModel)
                }
            }
        }


    }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun EpisodeItemLayout(episode: EpisodeItem, viewModel: DashBoardViewModel) {

    val borderModifier = if (viewModel.bindingModel.selectedEpisodeToWatch.value == episode) {
        Modifier.border(BorderStroke(2.dp, borderFrame), RoundedCornerShape(5.dp))
    } else {
        Modifier
    }


    Column {
        var isFocused by remember { mutableStateOf(false) }
        var lastKeyPressTime by remember { mutableStateOf(0L) }
        val doubleClickThreshold = 300L

        Row(
            modifier = borderModifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .border(
                    width = 2.dp,
                    color = if (isFocused) Color(0xFFB4A1FB) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp),
                )
                .focusable()
                .onKeyEvent { keyEvent: KeyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        when (keyEvent.nativeKeyEvent.keyCode) {
                            KEYCODE_BACK -> {
                                viewModel.bindingModel.selectedNavigationItem.value = Series
                                viewModel.bindingModel.selectedPage = Page.SERIES
                                true
                            }

                            KEYCODE_ENTER, KEYCODE_DPAD_CENTER -> {
                                val currentTime = System.currentTimeMillis()

                                if (currentTime - lastKeyPressTime <= doubleClickThreshold) {
                                    try {
                                        if (viewModel.bindingModel.selectedEpisodeToWatch.value != EpisodeItem()) {
                                            val videoSelected = viewModel.bindingModel.selectedEpisodeToWatch.value
                                            val intent = Intent(app, PlayerActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            intent.data = Uri.parse(videoSelected.url)


                                            app.startActivity(intent)
                                        } else Toast.makeText(app, "Please select an episode as a first step ", Toast.LENGTH_LONG).show()
                                    } catch (error: Exception) {
                                        Toast.makeText(app, "Error While loading your episode , Please try again", Toast.LENGTH_LONG).show()
                                    }


                                }else{
                                    viewModel.bindingModel.selectedEpisodeToWatch.value = episode
                                }

                                lastKeyPressTime = currentTime
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                }
                .padding(2.dp)
                .clickable {
                    viewModel.bindingModel.selectedEpisodeToWatch.value = episode
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Image placeholder for episode thumbnail

            if (episode.icon.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(episode.icon),
                    contentDescription = "Image with gradient background",
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    //Modifier.size(80.dp),
                    contentScale = ContentScale.Crop, // Adjust the image scaling
                )
            } else
                if (viewModel.bindingModel.selectedSerie.value.icon.isNotEmpty())
                    Image(
                        painter = rememberAsyncImagePainter(viewModel.bindingModel.selectedSerie.value.icon), // Replace with your image resource
                        contentDescription = "Image with gradient background",
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        //Modifier.size(80.dp),
                        contentScale = ContentScale.Crop, // Adjust the image scaling
                    )



            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = episode.name,
                    color = Color.White,
                    fontSize = 12.sp,
                )
                Text(
                    text = "42 min ",
                    color = Color.LightGray,
                    fontSize = 10.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }

}


