//package tn.iptv.nextplayer.dashboard.component
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import coil.compose.rememberAsyncImagePainter
//import org.koin.java.KoinJavaComponent
//import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
//import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
//import tn.iptv.nextplayer.dashboard.customdrawer.model.isOpened
//import tn.iptv.nextplayer.domain.channelManager.ChannelManager
//
//@SuppressLint("SuspiciousIndentation")
//@Composable
//fun CustomDrawer(
//    drawerState: CustomDrawerState,
//    widthMenu: Dp = 90.dp,
//    selectedNavigationItem: NavigationItem,
//    onNavigationItemClick: (NavigationItem) -> Unit,
//
//    ) {
//
//    val channelManager: ChannelManager by KoinJavaComponent.inject(ChannelManager::class.java)
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxHeight()
//            .width(widthMenu)
//            .padding(horizontal = 12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//
//        Spacer(modifier = Modifier.height(15.dp))
//
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.Start,
//        ) {
//            Spacer(modifier = Modifier.width(10.dp))
//
//            val sizeOfLogo = if (drawerState.isOpened()) 100.dp else 50.dp
//
//            Image(
//                painter = rememberAsyncImagePainter(channelManager.channelSelected.value!!.icon),
//                modifier = Modifier.size(sizeOfLogo),
//                contentDescription = "ChannelIconSelected",
//            )
//
//        }
//
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Box(
//            modifier = Modifier
//                .weight(1f) // This takes up the rest of the space
//                .fillMaxWidth()
//                .verticalScroll(rememberScrollState()), // Make the area scrollable
//        ) {
//            Column {
//                NavigationItem.entries.toTypedArray().filter { (it !in listOf(NavigationItem.DetailSeries, NavigationItem.DetailMovies)) }.take(7).forEach { navigationItem ->
//
//
//                    NavigationItemView(
//                        drawerState = drawerState,
//                        navigationItem = navigationItem,
//                        selected = navigationItem == selectedNavigationItem,
//                        onClick = { onNavigationItemClick(navigationItem) },
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//
//
//                }
//            }
//        }
//
//
//        //last  one
//        Spacer(modifier = Modifier.height(5.dp))
//        NavigationItem.entries.toTypedArray().takeLast(1).forEach { navigationItem ->
//            NavigationItemView(
//                drawerState = drawerState,
//                navigationItem = navigationItem,
//                isLogoutItem = true,
//                selected = false,
//                onClick = {
//                    when (navigationItem) {
//                        NavigationItem.Settings -> {
//                            onNavigationItemClick(NavigationItem.Settings)
//                        }
//
//                        NavigationItem.Logout -> {
//                            onNavigationItemClick(NavigationItem.Logout)
//                        }
//
//                        else -> {}
//                    }
//                },
//            )
//        }
//        Spacer(modifier = Modifier.height(24.dp))
//    }
//
//
//}
//
//
