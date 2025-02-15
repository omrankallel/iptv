package tn.iptv.nextplayer.dashboard.component


import android.os.Build
import android.util.Log
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.dashboard.DashBoardViewModel
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
import tn.iptv.nextplayer.dashboard.util.Page
import tn.iptv.nextplayer.listchannels.ui.theme.backTextFiledLight
import tn.iptv.nextplayer.listchannels.ui.theme.gray
import tn.iptv.nextplayer.listchannels.ui.theme.white
import tn.iptv.nextplayer.login.layout.TextFieldItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarDashBoard(
    titlePage: String,
    drawerState: CustomDrawerState,
    selectedNavigationItem: NavigationItem,
    searchValue: MutableState<String>,
    onSearchValueChange: (String) -> Unit,
    onClickFilter: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickSettings: () -> Unit,
    viewModel: DashBoardViewModel,
) {
    var isFocusedTextBox by remember { mutableStateOf(false) }
    var isFocusedTextFiled by remember { mutableStateOf(false) }
    var isFocusedText by remember { mutableStateOf(false) }
    var isFocusedFavorite by remember { mutableStateOf(false) }
    var isFocusedSettings by remember { mutableStateOf(false) }
    var isFocusedFiltered by remember { mutableStateOf(false) }
    var isFocusedNotification by remember { mutableStateOf(false) }
    val focusTextBoxRequester = remember { FocusRequester() }
    val focusTextFiledRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)

            // .background(back_application_start_color)
            .padding(end = if (drawerState.isOpened()) 180.dp else 80.dp),
        /* colors = TopAppBarDefaults.topAppBarColors(
             containerColor = back_application_start_color
         ),)**/
        verticalAlignment = Alignment.CenterVertically,
    )
    {


        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .onFocusChanged { isFocusedText = it.isFocused }
                .focusable()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(if (isFocusedText) Color(0xFFB4A1FB) else Color.Transparent)
                .padding(8.dp)
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

                            KEYCODE_DPAD_DOWN -> {
                                when (viewModel.bindingModel.selectedNavigationItem.value) {
                                    Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                    TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                    Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                    DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                    Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                    DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                    Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                    Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                    Logout -> TODO()
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
                },
        )
        {
            Text(
                text = titlePage,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.W500,
                color = if (isFocusedText) Color.Black else White,
            )
        }

        Spacer(Modifier.width(20.dp))
        CustomSeparator()
        Spacer(Modifier.width(20.dp))
        DisplayDateTime(viewModel)

        Spacer(Modifier.weight(1f))

        Spacer(Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .onFocusChanged { isFocusedTextBox = it.isFocused }
                .focusable()
                .focusRequester(focusTextBoxRequester)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    width = 2.dp,
                    color = if (isFocusedTextBox) Color(0xFFB4A1FB) else Color.Transparent,
                    shape = RoundedCornerShape(8.dp),
                )
                .onKeyEvent { keyEvent: KeyEvent ->
                    Log.d("KeyEventBox", keyEvent.toString())
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

                            KEYCODE_DPAD_DOWN -> {
                                when (viewModel.bindingModel.selectedNavigationItem.value) {
                                    Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                    TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                    Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                    DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                    Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                    DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                    Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                    Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                    Logout -> TODO()
                                }
                                true
                            }

                            KEYCODE_DPAD_CENTER -> {
                                focusTextFiledRequester.requestFocus()
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                },
        ) {
            TextFieldItem(
                textValue = searchValue.value,
                colorBorder = backTextFiledLight,
                leadingIcon = R.drawable.ic_search,
                hint = "Search for Something",
                modifier = Modifier
                    .width(300.dp)
                    .height(54.dp)
                    .focusable()
                    .focusRequester(focusTextFiledRequester)
                    .onFocusChanged { isFocusedTextFiled = it.isFocused }
                    .border(
                        width = 2.dp,
                        color = if (isFocusedTextFiled) Color(0xFFB4A1FB) else Color.Gray,
                        shape = RoundedCornerShape(8.dp),
                    )
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

                                KEYCODE_DPAD_DOWN -> {
                                    when (viewModel.bindingModel.selectedNavigationItem.value) {
                                        Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                        TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                        Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                        DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                        Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                        DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                        Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                        Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                        Logout -> TODO()
                                    }
                                    true
                                }

                                KEYCODE_DPAD_CENTER -> {
                                    true
                                }

                                else -> false
                            }
                        } else {
                            false
                        }
                    },
                customKeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            ) {
                searchValue.value = it
                onSearchValueChange(it)
            }
        }

        Spacer(Modifier.width(10.dp))

        if (selectedNavigationItem == Series || selectedNavigationItem == Movies) {
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Filter Icon",
                    tint = White,
                    modifier = Modifier
                        .onFocusChanged { isFocusedFiltered = it.isFocused }
                        .focusable()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(if (isFocusedFiltered) Color(0xFFB4A1FB) else Color.Transparent)
                        .padding(8.dp)
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

                                    KEYCODE_DPAD_DOWN -> {
                                        when (viewModel.bindingModel.selectedNavigationItem.value) {
                                            Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                            TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                            Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                            DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                            Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                            DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                            Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                            Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                            Logout -> TODO()
                                        }
                                        true
                                    }

                                    KEYCODE_DPAD_CENTER -> {
                                        onClickFilter()
                                        true
                                    }

                                    else -> false
                                }
                            } else {
                                false
                            }
                        },
                )
            }
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .onFocusChanged { isFocusedFavorite = it.isFocused }
                .focusable()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(if (isFocusedFavorite) Color(0xFFB4A1FB) else Color.Transparent)
                .padding(8.dp)
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

                            KEYCODE_DPAD_DOWN -> {
                                when (viewModel.bindingModel.selectedNavigationItem.value) {
                                    Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                    TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                    Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                    DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                    Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                    DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                    Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                    Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                    Logout -> TODO()
                                }
                                true
                            }

                            KEYCODE_DPAD_CENTER -> {
                                onClickFavorite()
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                },
            contentAlignment = Alignment.Center,
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = "favorite Icon",
                tint = White,
            )

        }
        Box(
            modifier = Modifier
                .size(50.dp)
                .onFocusChanged { isFocusedSettings = it.isFocused }
                .focusable()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(if (isFocusedSettings) Color(0xFFB4A1FB) else Color.Transparent)
                .padding(8.dp)
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

                            KEYCODE_DPAD_DOWN -> {
                                when (viewModel.bindingModel.selectedNavigationItem.value) {
                                    Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                    TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                    Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                    DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                    Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                    DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                    Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                    Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                    Logout -> TODO()
                                }
                                true
                            }

                            KEYCODE_DPAD_CENTER -> {
                                onClickSettings()
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                },
            contentAlignment = Alignment.Center,
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "settings Icon",
                tint = White,
            )
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .onFocusChanged { isFocusedNotification = it.isFocused }
                .focusable()
                .clip(shape = RoundedCornerShape(8.dp))
                .background(if (isFocusedNotification) Color(0xFFB4A1FB) else Color.Transparent)
                .padding(8.dp)
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

                            KEYCODE_DPAD_DOWN -> {
                                when (viewModel.bindingModel.selectedNavigationItem.value) {
                                    Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                    TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                    Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                    DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                    Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                    DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                    Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                    Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                    Logout -> TODO()
                                }
                                true
                            }


                            else -> false
                        }
                    } else {
                        false
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "Notification Icon",
                tint = White,
            )
        }

        Spacer(Modifier.width(10.dp))

    }


}


@Composable
fun CustomSeparator() {
    Box(
        modifier = Modifier
            .fillMaxHeight()  // Full height
            .width(1.dp)    // 1.dp width
            .padding(vertical = 10.dp)
            .background(white), // Set color of the separator
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayDateTime(viewModel: DashBoardViewModel) {

    // Current date and time
    val current = LocalDateTime.now()

    // Formatter for time in 12-hour format with AM/PM
    val timeFormatter = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH)
    val time = current.format(timeFormatter)

    // Formatter for date with day, month (in French), and year
    val dateFormatter = DateTimeFormatter.ofPattern("d, MMMM yyyy", Locale.ENGLISH)
    val date = current.format(dateFormatter)
    var isFocused by remember { mutableStateOf(false) }


    Column(
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

                        KEYCODE_DPAD_DOWN -> {
                            when (viewModel.bindingModel.selectedNavigationItem.value) {
                                Home -> viewModel.bindingModel.boxFocusRequesterHome.value.requestFocus()
                                TVChannels -> viewModel.bindingModel.boxFocusRequesterLive.value.requestFocus()
                                Series -> viewModel.bindingModel.boxFocusRequesterSeries.value.requestFocus()
                                DetailSeries -> viewModel.bindingModel.boxFocusRequesterDetailSeries.value.requestFocus()
                                Movies -> viewModel.bindingModel.boxFocusRequesterMovies.value.requestFocus()
                                DetailMovies -> viewModel.bindingModel.boxFocusRequesterDetailMovies.value.requestFocus()
                                Favorite -> viewModel.bindingModel.boxFocusRequesterFavorite.value.requestFocus()
                                Settings -> viewModel.bindingModel.boxFocusRequesterSettings.value.requestFocus()
                                Logout -> TODO()
                            }
                            true
                        }

                        else -> false
                    }
                } else {
                    false
                }
            }
            .clip(shape = RoundedCornerShape(8.dp))
            .background(if (isFocused) Color(0xFFB4A1FB) else Color.Transparent)
            .padding(8.dp),

        ) {

        Text(
            modifier = Modifier.focusable(false),
            text = time,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = if (isFocused) Color.Black else White,
        )


        Text(
            modifier = Modifier.focusable(false),
            text = date,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = if (isFocused) Color.Black else gray,
        )
    }

}
