package tn.iptv.nextplayer.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.dashboard.customdrawer.model.isOpened
import tn.iptv.nextplayer.listchannels.ui.theme.colorBackSelectedElement
import tn.iptv.nextplayer.listchannels.ui.theme.redLogout

@Composable
fun NavigationItemView(
    drawerState: CustomDrawerState,
    navigationItem: NavigationItem,
    isLogoutItem: Boolean = false,
    selected: Boolean,
    onClick: () -> Unit,
    isFocused:Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 8.dp))
            .clickable { onClick() }
            .background(
                color = if (selected) colorBackSelectedElement else if (isFocused) Color.Gray
                else Color.Unspecified,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (drawerState.isOpened()) Arrangement.Start else Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = navigationItem.icon),
            contentDescription = "Navigation Item Icon",
            tint = if (isLogoutItem.not()) {
                if (selected) Color.Black else Color.White
            } else redLogout,
        )
        if (drawerState.isOpened()) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = navigationItem.title,
                color = if (isLogoutItem.not()) {
                    if (selected) Color.Black else Color.White
                } else redLogout,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                lineHeight = 20.sp,
            )
        }
    }
}