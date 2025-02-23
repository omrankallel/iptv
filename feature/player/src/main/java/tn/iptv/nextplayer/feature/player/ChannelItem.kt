package tn.iptv.nextplayer.feature.player

import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import tn.iptv.nextplayer.core.data.favorite.FavoriteViewModel
import tn.iptv.nextplayer.feature.player.model.grouped_media.MediaItem
import tn.iptv.nextplayer.feature.player.utils.AppHelper

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ChannelItem(
    favoriteViewModel: FavoriteViewModel,
    mediaItem: MediaItem,
    isSelected: Boolean,
    onPressBack: () -> Unit,
    onPress: (MediaItem) -> Unit,
    dataOfLiveChannelJsonString: String,
) {
    var isFocused by remember { mutableStateOf(false) }

    val backgroundColor = if (isSelected) Color(0xFF9B8AFF) else Color.Transparent
    val borderColor = if (isFocused) Color(0xFFB4A1FB) else Color.Transparent
    var isLongPressTriggered by remember { mutableStateOf(false) }
    var showFavorite by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var imgFavorite by remember { mutableStateOf(false) }
    var txtFavorite by remember { mutableStateOf("") }


    favoriteViewModel.isFavoriteExists(mediaItem.id) { exists ->
        isFavorite = exists

        if (isFavorite) {
            imgFavorite = true
            txtFavorite = "Supprimer des favoris"
        } else {
            imgFavorite = false
            txtFavorite = "Ajouter aux Favoris"
        }

    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .focusable()
                .border(2.dp, borderColor, RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .background(backgroundColor)
                .onKeyEvent { keyEvent ->
                    when (keyEvent.type) {
                        KeyEventType.KeyDown -> {
                            when (keyEvent.nativeKeyEvent.keyCode) {
                                KEYCODE_ENTER, KEYCODE_DPAD_CENTER -> {
                                    if (keyEvent.nativeKeyEvent.isLongPress) {
                                        isLongPressTriggered = true
                                        showFavorite = true
                                        ////long press
                                    }
                                    true
                                }
                                KEYCODE_BACK->{
                                    onPressBack()
                                    true
                                }

                                else -> false
                            }
                        }

                        KeyEventType.KeyUp -> {
                            when (keyEvent.nativeKeyEvent.keyCode) {
                                KEYCODE_ENTER, KEYCODE_DPAD_CENTER -> {
                                    if (!isLongPressTriggered) {
                                        if (showFavorite) {

                                            if (isFavorite) {
                                                imgFavorite = false
                                                favoriteViewModel.deleteFavoriteById(mediaItem.id)
                                                txtFavorite = "Ajouter aux Favoris"
                                            } else {
                                                imgFavorite = true
                                                favoriteViewModel.addFavorite(mediaItem.id, mediaItem.name, mediaItem.icon, mediaItem.url, "", "", "", "", "", "", "", "Live TV", dataOfLiveChannelJsonString)
                                                txtFavorite = "Supprimer des favoris"
                                            }
                                            isFavorite = !isFavorite
                                            showFavorite = false
                                        } else {
                                            onPress(mediaItem)
                                        }

                                    }
                                    isLongPressTriggered = false
                                    true
                                }

                                else -> false
                            }
                        }

                        else -> false
                    }
                }
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = mediaItem.icon,
                contentDescription = "Logo de ${mediaItem.name}",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = AppHelper.cleanChannelName(mediaItem.name),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                color = Color.White,
                lineHeight = 15.sp,
                modifier = Modifier.weight(0.8f),
            )
            if (imgFavorite)
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favori",
                    tint = Color.White,
                    modifier = Modifier.weight(0.2f),
                )
        }
        if (showFavorite)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .background(color = Color(0xFF808080)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = txtFavorite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    color = Color.White,
                    lineHeight = 15.sp,
                    modifier = Modifier
                        .weight(0.8f)
                        .align(Alignment.CenterVertically),
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favori",
                    tint = Color.White,
                    modifier = Modifier
                        .weight(0.2f)
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
                )
            }
    }
}