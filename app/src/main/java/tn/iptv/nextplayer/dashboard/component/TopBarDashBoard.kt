package tn.iptv.nextplayer.dashboard.component



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import tn.iptv.nextplayer.R
import tn.iptv.nextplayer.dashboard.customdrawer.model.CustomDrawerState
import tn.iptv.nextplayer.dashboard.customdrawer.model.isOpened
import tn.iptv.nextplayer.listchannels.ui.theme.backTextFiledLight
import tn.iptv.nextplayer.listchannels.ui.theme.gray
import tn.iptv.nextplayer.listchannels.ui.theme.white
import tn.iptv.nextplayer.login.layout.TextFieldItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopBarDashBoard(titlePage: String,
                    drawerState: CustomDrawerState,
                    searchValue : MutableState<String>,
                    onSearchValueChange : (String) -> Unit,) {



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
        Text(
            text = titlePage,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.W500,
            color = white,
        )

        Spacer(Modifier.width(20.dp))
        CustomSeparator()
        Spacer(Modifier.width(20.dp))
        DisplayDateTime()

        Spacer(Modifier.weight(1f))

        Spacer(Modifier.width(10.dp))

        TextFieldItem(
            textValue = searchValue.value,
            colorBorder = backTextFiledLight,
            leadingIcon = R.drawable.ic_search,
            hint = "Search for Something",
            modifier = Modifier.width(300.dp).height(54.dp),
            customKeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        ) {
            searchValue.value = it
            onSearchValueChange ( it )
        }

        Spacer(Modifier.width(10.dp))

        Box(modifier = Modifier.size(50.dp),
             contentAlignment = Alignment.Center) {

            Icon(
                painter = painterResource(id = R.drawable.ic_favorite),
                contentDescription = "favorite Icon",
                tint = White,
            )

        }
        Box(modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center) {

            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "settings Icon",
                tint = White,
            )
        }

        Box(modifier = Modifier.size(50.dp),
            contentAlignment = Alignment.Center) {
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
fun DisplayDateTime() {

    // Current date and time
    val current = LocalDateTime.now()

    // Formatter for time in 12-hour format with AM/PM
    val timeFormatter = DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH)
    val time = current.format(timeFormatter)

    // Formatter for date with day, month (in French), and year
    val dateFormatter = DateTimeFormatter.ofPattern("d, MMMM yyyy", Locale.ENGLISH)
    val date = current.format(dateFormatter)


    Column {

        Text(
            text = time,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = white,
        )


        Text(
            text = date,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            color = gray,
        )
    }

}
