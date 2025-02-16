package tn.iptv.nextplayer.dashboard.screens

import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.component.ButtonItem
import tn.iptv.nextplayer.component.OutlinedButtonIPTV
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailMovies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.DetailSeries
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Favorite
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Home
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Movies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Series
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Settings
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.TVChannels
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.domain.models.packages.PackageMedia
import tn.iptv.nextplayer.feature.player.utils.AppHelper


@Composable
fun PackagesLayout(viewModel: DashBoardViewModel, selectedItem: MutableState<PackageMedia>, listItems: List<PackageMedia>, onSelectPackage: (PackageMedia) -> Unit) {


    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp), // Adds space between items
    ) {
        items(listItems) { item ->
            var isFocused by remember { mutableStateOf(false) }
            val focusManager = LocalFocusManager.current
            if (item.id == selectedItem.value.id)

                ButtonItem(
                    modifier = Modifier
                        .onKeyEvent { keyEvent: KeyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown) {
                                when (keyEvent.nativeKeyEvent.keyCode) {
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
                                    KEYCODE_DPAD_LEFT -> {
                                            viewModel.bindingModel.drawerState.value = CustomDrawerState.Opened
                                            focusManager.clearFocus()
                                            true


                                    }


                                    else -> false
                                }
                            } else {
                                false
                            }
                        }
                        .height(40.dp)
                        .width(150.dp),
                    paddingHorizontal = 10.dp,
                    sizeText = 13.sp,
                    shape = 5.dp,
                    label = AppHelper.cleanChannelName(item.name),
                    onClick = {

                    },
                )
            else
                OutlinedButtonIPTV(
                    label = AppHelper.cleanChannelName(item.name),
                    modifier = Modifier
                        .onFocusChanged { isFocused = it.isFocused }
                        .focusable()
                        .onKeyEvent { keyEvent: KeyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown) {
                                when (keyEvent.nativeKeyEvent.keyCode) {
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


                                    else -> false
                                }
                            } else {
                                false
                            }
                        }
                        .border(
                            width = 1.dp,
                            color = Color(0xFFB4A1FB),
                            shape = RoundedCornerShape(5.dp),
                        )
                        .height(40.dp),
                    paddingHorizontal = 10.dp,
                    sizeText = 13.sp,
                    shape = 5.dp,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isFocused) Color(0xFFB4A1FB) else Color.Transparent,
                    ),
                    onClick = { onSelectPackage(item) },
                )

        }
    }


}