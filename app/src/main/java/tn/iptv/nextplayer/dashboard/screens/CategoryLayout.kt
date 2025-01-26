package tn.iptv.nextplayer.dashboard.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.component.ButtonItem
import tn.iptv.nextplayer.component.OutlinedButtonIPTV
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.domain.models.packages.PackageMedia
import tn.iptv.nextplayer.feature.player.utils.AppHelper

@Composable
fun PackagesLayout(
    selectedItem: MutableState<PackageMedia>,
    listItems: List<PackageMedia>,
    onSelectPackage: (PackageMedia) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    val currentIndex = listItems.indexOfFirst { it.id == selectedItem.value.id }
                    if (currentIndex != -1) {
                        when (keyEvent.key) {
                            androidx.compose.ui.input.key.Key.DirectionLeft -> {
                                if (currentIndex > 0) {
                                    selectedItem.value = listItems[currentIndex - 1]
                                    onSelectPackage(selectedItem.value)
                                }
                                true
                            }
                            androidx.compose.ui.input.key.Key.DirectionRight -> {
                                if (currentIndex < listItems.size - 1) {
                                    selectedItem.value = listItems[currentIndex + 1]
                                    onSelectPackage(selectedItem.value)
                                }
                                true
                            }
                            else -> false
                        }
                    } else {
                        false
                    }
                } else {
                    false
                }
            },
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(listItems) { item ->
            if (item.id == selectedItem.value.id)
                ButtonItem(
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp),
                    paddingHorizontal = 10.dp,
                    sizeText = 13.sp,
                    shape = 5.dp,
                    label = AppHelper.cleanChannelName(item.name),
                    onClick = { onSelectPackage(item) }
                )
            else
                OutlinedButtonIPTV(
                    labelButton = AppHelper.cleanChannelName(item.name),
                    onClick = { onSelectPackage(item) }
                )
        }
    }
}
