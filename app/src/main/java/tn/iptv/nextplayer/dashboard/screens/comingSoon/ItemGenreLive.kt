package tn.iptv.nextplayer.dashboard.screens.comingSoon

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import tn.iptv.nextplayer.dashboard.screens.tvChannel.ItemLiveChannel
import tn.iptv.nextplayer.domain.models.GroupedMedia
import tn.iptv.nextplayer.domain.models.series.MediaItem
import tn.iptv.nextplayer.domain.models.series.MediaType
import tn.iptv.nextplayer.feature.player.PlayerActivity
import tn.iptv.nextplayer.feature.player.utils.AppHelper
import tn.iptv.nextplayer.login.app

@Composable
fun ItemGenreLive(mediaType: MediaType, groupedMediaItem: GroupedMedia, onSelectMediaItem: (MediaItem) -> Unit) {

    val showOtherItems = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
    ) {
        Text(
            text = AppHelper.cleanChannelName(groupedMediaItem.labelGenre),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = Color.White,
        )

        Spacer(modifier = Modifier.height(5.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            // Show first 4 items
            items(groupedMediaItem.listSeries.take(10)) { serie ->

                ItemLiveChannel(
                    mediaType, serie,
                    onSelectLiveChannel = {
                        onSelectMediaItem(it)
                    },
                )


            }
            if (groupedMediaItem.listSeries.size > 10) {

                if (showOtherItems.value.not()) {
                    item {
                        ItemShowAll(
                            mediaType,
                            onClickToShowAll = {


                                // openUrlWithVLC(app, tvChannelSelected.url)

                                // Serialize GroupedMedia object to JSON string
                                val gson = Gson()
                                val jsonStringGroupOFChannel = gson.toJson(groupedMediaItem)

                                try {

                                    val intent = Intent(app, PlayerActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.putExtra("GROUP_OF_CHANNEL", jsonStringGroupOFChannel)
                                    intent.putExtra("INDEX_OF_CHANNEL", 0)
                                    intent.data = Uri.parse(groupedMediaItem.listSeries.first().url)
                                    app.startActivity(intent)


                                } catch (error: Exception) {
                                    Toast.makeText(app, "Error While loading your movie , Please try again", Toast.LENGTH_LONG).show()
                                }
                            },
                        )
                    }
                }

                if (showOtherItems.value) {
                    items(groupedMediaItem.listSeries.drop(10)) { serie ->
                        ItemLiveChannel(
                            mediaType, serie,
                            onSelectLiveChannel = {
                                onSelectMediaItem(it)
                            },
                        )

                    }

                }

            }

        }


    }


}