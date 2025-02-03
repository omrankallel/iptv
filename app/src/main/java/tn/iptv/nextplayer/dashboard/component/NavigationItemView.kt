package tn.iptv.nextplayer.dashboard.component


import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.listchannels.ui.theme.colorBackSelectedElement
import tn.iptv.nextplayer.listchannels.ui.theme.redLogout


@Composable
fun NavigationItemView(
    drawerState: DrawerState,
    navigationItem: NavigationItem,
    isLogoutItem: Boolean = false,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        label = { Text(text = navigationItem.title) },
        icon = {
            Icon(
                painter = painterResource(id = navigationItem.icon),
                contentDescription = "Navigation Item Icon",
                tint = if (!isLogoutItem) {
                    if (selected) Color.Black else Color.White
                } else redLogout,
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = colorBackSelectedElement,
            unselectedContainerColor = Color.Transparent,
            selectedTextColor = Color.Black,
            unselectedTextColor = Color.White
        )
    )
}
