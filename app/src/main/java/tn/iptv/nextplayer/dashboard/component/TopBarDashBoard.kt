package tn.iptv.nextplayer.dashboard.component


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Movies
import tn.iptv.nextplayer.dashboard.customdrawer.model.NavigationItem.Series
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
    selectedNavigationItem: NavigationItem,
    searchValue: MutableState<String>,
    onSearchValueChange: (String) -> Unit,
    onClickFilter: () -> Unit,

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {


        Spacer(Modifier.width(10.dp))

        Box(
            modifier = Modifier.clickable {  }
        ){
            Text(
                text = titlePage,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.W500,
                color = white,
            )
        }

        Spacer(Modifier.width(20.dp))


        if (selectedNavigationItem != NavigationItem.Home)
            TextFieldItem(
                textValue = searchValue.value,
                colorBorder = backTextFiledLight,
                leadingIcon = R.drawable.ic_search,
                isReadOnly = true,
                hint = "Search for Something",
                modifier = Modifier
                    .width(300.dp)
                    .height(54.dp),
                customKeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            ) {
                searchValue.value = it
                onSearchValueChange(it)
            }

        Spacer(Modifier.width(10.dp))

        if (selectedNavigationItem == Series || selectedNavigationItem == Movies) {
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Filter Icon",
                    tint = White,
                    modifier = Modifier.clickable { onClickFilter() },
                )
            }
        }

        IconButtonItem(R.drawable.ic_favorite, "Favorite Icon")
        IconButtonItem(R.drawable.ic_settings, "Settings Icon")
        IconButtonItem(R.drawable.ic_notification, "Notification Icon")

        Spacer(Modifier.width(10.dp))
    }
}

@Composable
fun IconButtonItem(iconId: Int, contentDescription: String) {
    Box(
        modifier = Modifier.size(50.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            tint = White,
        )
    }
}
