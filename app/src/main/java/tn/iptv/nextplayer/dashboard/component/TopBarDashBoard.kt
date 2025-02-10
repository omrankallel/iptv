package tn.iptv.nextplayer.dashboard.component


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material3.DrawerState
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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

//        Box(
//            modifier = Modifier.clickable {  }
//        ){
//            Text(
//                text = titlePage,
//                fontSize = MaterialTheme.typography.titleMedium.fontSize,
//                fontWeight = FontWeight.W500,
//                color = white,
//            )
//        }

        FocusableBox(titlePage)
        Spacer(Modifier.width(30.dp))

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(32.dp)
                .background(White)
        )

        Spacer(Modifier.width(20.dp))

        DateTimeDisplay()

        Spacer(Modifier.width(20.dp))

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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimeDisplay() {
    val currentDateTime = remember { LocalDateTime.now() }
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = currentDateTime.format(timeFormatter),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = White
        )
        Text(
            text = currentDateTime.format(dateFormatter),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
    }
}

@Composable
fun FocusableBox(titlePage: String) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(if (isFocused) Color.Gray else Color.Transparent)
            .padding(8.dp)
    ) {
        BasicText(
            text = titlePage,
            style = MaterialTheme.typography.titleMedium.copy(
                color = if (isFocused) Color.Black else White
            )
        )
    }
}